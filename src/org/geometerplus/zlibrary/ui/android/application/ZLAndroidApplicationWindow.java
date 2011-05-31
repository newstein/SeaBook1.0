/*
 * Copyright (C) 2007-2011 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.zlibrary.ui.android.application;

import java.util.*;

import android.view.Menu;
import android.view.MenuItem;

import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.core.application.ZLApplicationWindow;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.core.view.ZLView;
import org.geometerplus.zlibrary.core.view.ZLViewWidget;

import org.geometerplus.zlibrary.ui.android.view.ZLAndroidWidget;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidLibrary;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;

public final class ZLAndroidApplicationWindow extends ZLApplicationWindow {
	private final HashMap<MenuItem,String> myMenuItemMap = new HashMap<MenuItem,String>();

	private final MenuItem.OnMenuItemClickListener myMenuListener =
		new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				getApplication().doAction(myMenuItemMap.get(item));
				return true;
			}
		};
//sean_0517
         final int SHOW_AS_ACTION_ALWAYS=2;
         final int SHOW_AS_ACTION_IF_ROOM=1;
         final int SHOW_AS_ACTION_NEVER=0;
         final int SHOW_AS_ACTION_WITH_TEXT=4;



	public ZLAndroidApplicationWindow(ZLApplication application) {
		super(application);
	}

	public void addMenuItem(Menu menu, String actionId, Integer iconId) {
		final ZLResource resource = ZLResource.resource("menu");
		final MenuItem menuItem = menu.add(resource.getResource(actionId).getValue());
		if (iconId != null) {
			menuItem.setIcon(iconId);
  
                     menuItem.setShowAsAction(SHOW_AS_ACTION_ALWAYS); //always
    
		}
           //sean_0517
            
              
        
		menuItem.setOnMenuItemClickListener(myMenuListener);
		myMenuItemMap.put(menuItem, actionId);
	}

	@Override
	public void refreshMenu() {
		for (Map.Entry<MenuItem,String> entry : myMenuItemMap.entrySet()) {
			final String actionId = entry.getValue();
			final ZLApplication application = getApplication();
			entry.getKey().setVisible(application.isActionVisible(actionId) && application.isActionEnabled(actionId));
		}
	}

	protected ZLViewWidget getViewWidget() {
		return ((ZLAndroidLibrary)ZLAndroidLibrary.Instance()).getWidget();
	}

	@Override
	public void rotate() {
		((ZLAndroidLibrary)ZLAndroidLibrary.Instance()).rotateScreen();
	}

	public boolean canRotate() {
		return !ZLAndroidApplication.Instance().AutoOrientationOption.getValue();
	}

	public void close() {
		((ZLAndroidLibrary)ZLAndroidLibrary.Instance()).finish();
	}

	private int myBatteryLevel;
	protected int getBatteryLevel() {
		return myBatteryLevel;
	}
	public void setBatteryLevel(int percent) {
		myBatteryLevel = percent;
	}
}
