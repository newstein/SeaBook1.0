/*
 * Copyright (C) 2009-2011 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import org.geometerplus.zlibrary.core.filesystem.ZLFile;

import org.geometerplus.zlibrary.text.hyphenation.ZLTextHyphenator;

import com.sean.android.ebookmain.R;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidActivity;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;

import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.fbreader.bookmodel.BookModel;
import org.geometerplus.fbreader.library.Book;

import org.geometerplus.android.fbreader.library.KillerCallback;

import org.geometerplus.android.util.UIUtil;
import android.util.Log;
//sean_0517
import java.io.FileInputStream;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.view.Window;
//import com.sean.android.ebookmain.CoverFlow;

//sean_0517
public final class FBReader extends ZLAndroidActivity implements View.OnClickListener, ActionBar.TabListener     {
	public static final String BOOK_PATH_KEY = "BookPath";
        private static final String TAG = "FBReader";
	final static int REPAINT_CODE = 1;
	final static int CANCEL_CODE = 2;

	private int myFullScreenFlag;

	private static TextSearchButtonPanel ourTextSearchPanel;
	private static NavigationButtonPanel ourNavigatePanel;



//    private FBReaderApp fbReader ;

	@Override
	protected ZLFile fileFromIntent(Intent intent) {
		String filePath = intent.getStringExtra(BOOK_PATH_KEY);
		if (filePath == null) {
			final Uri data = intent.getData();
			if (data != null) {
				filePath = data.getPath();
			}
		}
		return filePath != null ? ZLFile.createFileByPath(filePath) : null;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
              Log.v(TAG, "SEAN_LOG  onCreate " ); 
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		myFullScreenFlag =
			application.ShowStatusBarOption.getValue() ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
		  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, myFullScreenFlag);

		final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
		//fbReader = (FBReaderApp)FBReaderApp.Instance();
		if (ourTextSearchPanel == null) {
			ourTextSearchPanel = new TextSearchButtonPanel(fbReader);
		}
		if (ourNavigatePanel == null) {
			ourNavigatePanel = new NavigationButtonPanel(fbReader);
		}

		fbReader.addAction(ActionCode.SHOW_LIBRARY, new ShowLibraryAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_PREFERENCES, new ShowPreferencesAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_BOOK_INFO, new ShowBookInfoAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_TOC, new ShowTOCAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_BOOKMARKS, new ShowBookmarksAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_NETWORK_LIBRARY, new ShowNetworkLibraryAction(this, fbReader));
		
		fbReader.addAction(ActionCode.SHOW_MENU, new ShowMenuAction(this, fbReader));
		fbReader.addAction(ActionCode.SHOW_NAVIGATION, new ShowNavigationAction(this, fbReader));
		fbReader.addAction(ActionCode.SEARCH, new SearchAction(this, fbReader));

		fbReader.addAction(ActionCode.PROCESS_HYPERLINK, new ProcessHyperlinkAction(this, fbReader));

		fbReader.addAction(ActionCode.SHOW_CANCEL_MENU, new ShowCancelMenuAction(this, fbReader));
        




        
	}

 	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	         Log.v(TAG, "SEAN_LOG  onPrepareOptionsMenu " ); 
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		if (!application.ShowStatusBarOption.getValue() &&
			application.ShowStatusBarWhenMenuIsActiveOption.getValue()) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
    public void invalidateOptionsMenu() {
        // TODO Auto-generated method stub
        Log.v(TAG, "SEAN_LOG  invalidateOptionsMenu " ); 
        final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
        if (!application.ShowStatusBarOption.getValue() &&
            application.ShowStatusBarWhenMenuIsActiveOption.getValue()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }	    
        super.invalidateOptionsMenu();
    }

    @Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		if (!application.ShowStatusBarOption.getValue() &&
			application.ShowStatusBarWhenMenuIsActiveOption.getValue()) {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
	Log.v(TAG, "SEAN_LOG  onNewIntent " ); 
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			final String pattern = intent.getStringExtra(SearchManager.QUERY);
			final Handler successHandler = new Handler() {
				public void handleMessage(Message message) {
					ourTextSearchPanel.show(true);
				}
			};
			final Handler failureHandler = new Handler() {
				public void handleMessage(Message message) {
					UIUtil.showErrorMessage(FBReader.this, "textNotFound");
					ourTextSearchPanel.StartPosition = null;
				}
			};
			final Runnable runnable = new Runnable() {
				public void run() {
					ourTextSearchPanel.initPosition();
					final FBReaderApp fbReader = (FBReaderApp)FBReaderApp.Instance();
					fbReader.TextSearchPatternOption.setValue(pattern);
					if (fbReader.getTextView().search(pattern, true, false, false, false) != 0) {
						successHandler.sendEmptyMessage(0);
					} else {
						failureHandler.sendEmptyMessage(0);
					}
				}
			};
			UIUtil.wait("search", runnable, this);
			startActivity(new Intent(this, getClass()));
		} else {
			super.onNewIntent(intent);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
              Log.v(TAG, "SEAN_LOG  onStart " ); 
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();

		final int fullScreenFlag =
			application.ShowStatusBarOption.getValue() ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
		if (fullScreenFlag != myFullScreenFlag) {
			finish();
			startActivity(new Intent(this, this.getClass()));
		}

		final RelativeLayout root = (RelativeLayout)findViewById(R.id.root_view);
		if (!ourTextSearchPanel.hasControlPanel()) {
			ourTextSearchPanel.createControlPanel(this, root);
		}
		if (!ourNavigatePanel.hasControlPanel()) {
			ourNavigatePanel.createControlPanel(this, root);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			sendBroadcast(new Intent(getApplicationContext(), KillerCallback.class));
		} catch (Throwable t) {
		}
		ControlButtonPanel.restoreVisibilities(FBReaderApp.Instance());
	}

	@Override
	public void onPause() {
		ControlButtonPanel.saveVisibilities(FBReaderApp.Instance());
		super.onPause();
	}

	@Override
	public void onStop() {
		ControlButtonPanel.removeControlPanels(FBReaderApp.Instance());
		super.onStop();
	}

	@Override
	protected FBReaderApp createApplication(ZLFile file) {
	Log.v(TAG, "SEAN_LOG  createApplication " ); 
		if (SQLiteBooksDatabase.Instance() == null) {
			new SQLiteBooksDatabase(this, "READER");
		}
		return new FBReaderApp(file != null ? file.getPath() : null);
	}

	@Override
	public boolean onSearchRequested() {
		final FBReaderApp fbreader = (FBReaderApp)FBReaderApp.Instance();
		ControlButtonPanel.saveVisibilities(fbreader);
		ControlButtonPanel.hideAllPendingNotify(fbreader);
		final SearchManager manager = (SearchManager)getSystemService(SEARCH_SERVICE);
		manager.setOnCancelListener(new SearchManager.OnCancelListener() {
			public void onCancel() {
				ControlButtonPanel.restoreVisibilities(fbreader);
				manager.setOnCancelListener(null);
			}
		});
		startSearch(fbreader.TextSearchPatternOption.getValue(), true, null, false);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		final FBReaderApp fbreader = (FBReaderApp)FBReaderApp.Instance();
		switch (requestCode) {
			case REPAINT_CODE:
			{
				final BookModel model = fbreader.Model;
				if (model != null) {
					final Book book = model.Book;
					if (book != null) {
						book.reloadInfoFromDatabase();
						ZLTextHyphenator.Instance().load(book.getLanguage());
					}
				}
				fbreader.clearTextCaches();
				fbreader.getViewWidget().repaint();
				break;
			}
			case CANCEL_CODE:
				fbreader.runCancelAction(resultCode);
				break;
		}
	}

	public void navigate() {
		ourNavigatePanel.runNavigation();
	}

	private void addMenuItem(Menu menu, String actionId, int iconId) {
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		application.myMainWindow.addMenuItem(menu, actionId, iconId);
	}

	private void addMenuItem(Menu menu, String actionId) {
		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		application.myMainWindow.addMenuItem(menu, actionId, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		addMenuItem(menu, ActionCode.SHOW_LIBRARY, R.drawable.ic_menu_library);
		addMenuItem(menu, ActionCode.SHOW_NETWORK_LIBRARY, R.drawable.ic_menu_networklibrary);
		addMenuItem(menu, ActionCode.SHOW_TOC, R.drawable.ic_menu_toc);
		addMenuItem(menu, ActionCode.SHOW_BOOKMARKS, R.drawable.ic_menu_bookmarks);
		addMenuItem(menu, ActionCode.SWITCH_TO_NIGHT_PROFILE, R.drawable.ic_menu_night);
		addMenuItem(menu, ActionCode.SWITCH_TO_DAY_PROFILE, R.drawable.ic_menu_day);
		addMenuItem(menu, ActionCode.SEARCH, R.drawable.ic_menu_search);
//sean_0517		addMenuItem(menu, ActionCode.SHOW_PREFERENCES);
        addMenuItem(menu, ActionCode.SHOW_PREFERENCES, R.drawable.ic_popup_settings);
        addMenuItem(menu, ActionCode.SHOW_BOOK_INFO);
        addMenuItem(menu, ActionCode.ROTATE, R.drawable.ic_popup_orientation);
//sean_0517              
		addMenuItem(menu, ActionCode.INCREASE_FONT,R.drawable.quickaction_arrow_up);
		addMenuItem(menu, ActionCode.DECREASE_FONT,R.drawable.quickaction_arrow_down);
		addMenuItem(menu, ActionCode.SHOW_NAVIGATION,R.drawable.ic_tab_selected_recent);

		final ZLAndroidApplication application = (ZLAndroidApplication)getApplication();
		application.myMainWindow.refreshMenu();

		return true;
	}




    
}
