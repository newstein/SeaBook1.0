package com.sean.bookcase;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sean.android.ebookmain.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;

public class BookCaseList extends Activity {
    private static final String TAG = "BookCaseList";
	private Intent myListIntent;
	private List<ResolveInfo> myBookApps;
	private GridView myGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		// ����â
		ImageView infoImage = (ImageView) findViewById(R.id.titleInfoImage);
		infoImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder myDialog = new AlertDialog.Builder(BookCaseList.this);
				myDialog.setIcon(R.drawable.info);
				myDialog.setTitle(R.string.app_name);
				myDialog.setMessage(R.string.list_title);
				myDialog.setPositiveButton(R.string.info_ok, null);
				myDialog.show();
			}
		});

		// ����Ʈ�� ����� ī�װ? �߰��ϱ�.
		myListIntent = new Intent(Intent.ACTION_MAIN, null);
		myListIntent.addCategory("com.sean.intent.LAUNCHER"); // �̷�𿡼� ���� ���� ��������
		//listIntent.addCategory(Intent.CATEGORY_LAUNCHER); // �ȵ���̵� �޴��� ���� ��������

		// �׸���� �����
		myGridView = (GridView) findViewById(R.id.listGrid);
		myGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        // �ϳ��� �׸���޴��� ���������� ���õ� ���ø����̼� ����.
        /*
		myGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position < myBookApps.size()) {
					ResolveInfo info = myBookApps.get(position);
					Intent i = new Intent();
					i.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
					startActivity(i);
				}
			}
		});
		*/
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// myBookApps�� å����Ÿ ��������.
		if (myBookApps != null) {
			myBookApps.clear();
		}
		myBookApps = getPackageManager().queryIntentActivities(myListIntent, 0);

		// myBookApps�� ����ִ� ����Ÿ �����ϱ�.
		final Comparator<ResolveInfo> myBookAppsComparator = new Comparator<ResolveInfo>() {
			private final Collator myCollator = Collator.getInstance();

			public int compare(ResolveInfo info1, ResolveInfo info2) { // �ϴ� �̸����� ����.
				String str1 = info1.activityInfo.loadLabel(getPackageManager()).toString();
				String str2 = info2.activityInfo.loadLabel(getPackageManager()).toString();
				return myCollator.compare(str1, str2);
			}
		};
		Collections.sort(myBookApps, myBookAppsComparator);

		// myBookApps�� ����ִ� ����Ÿ�� myGridView�� ������.
		myGridView.setAdapter(new AppsAdapter());
	}

	public class AppsAdapter extends BaseAdapter {
		public AppsAdapter() {

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View myView;


			final DisplayMetrics myMetrics = getResources().getDisplayMetrics();
			Log.v(TAG, "SEAN_LOG position=" + position ); 
//			int layoutParamsX = (96*myMetrics.widthPixels/480);
//			int layoutParamsY = (130*myMetrics.heightPixels/320);
            int layoutParamsX = getCountX();
            int layoutParamsY = getCountY();
            Log.v(TAG, "SEAN_LOG X=" + layoutParamsX +"Y="+layoutParamsY); 
			// å ���ø����̼� ���� ��������.
			if(position < myBookApps.size()) {
				if (convertView == null) {
					LayoutInflater myInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					myView = myInflater.inflate(R.layout.item, null);
					myView.setLayoutParams(new GridView.LayoutParams(layoutParamsX, layoutParamsY));
				}
				else {
					myView = convertView;
				}

				// å����.
				final ResolveInfo info = myBookApps.get(position);

				// �̹��� �����ְ� Ŭ���̺�Ʈ ó���ϱ�.
				ImageView itemImage = (ImageView) myView.findViewById(R.id.itemImage);
				itemImage.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
				itemImage.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// ���õ� ���ø����̼� ����.
						Intent myIntent = new Intent();
						myIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
						startActivity(myIntent);
					}
				});
				itemImage.setOnLongClickListener(new OnLongClickListener() {
					public boolean onLongClick(View v) {
						// ���õ� ���ø����̼� ����.
						Uri uri = Uri.fromParts("package", info.activityInfo.packageName, null);
						Intent delelteIntent = new Intent(Intent.ACTION_DELETE, uri);
						startActivity(delelteIntent);
						return true;
					}
				});

				// å���� �����ֱ�.
				/*
				TextView itemText = (TextView) myView.findViewById(R.id.itemName);
				itemText.setText(info.activityInfo.loadLabel(getPackageManager()).toString());
				*/
			}
			else { // ����� å�常 �׸���
				LayoutInflater myInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				myView = myInflater.inflate(R.layout.item, null);
				myView.setLayoutParams(new GridView.LayoutParams(layoutParamsX, layoutParamsY));
			}

			return myView;
		}
        public  int getCountX() {
            final DisplayMetrics myMetrics = getResources().getDisplayMetrics();
          int layoutParamsX = 0;
 
        
          if (myMetrics.widthPixels == 480) {
              layoutParamsX = 96;
          }
          if (myMetrics.widthPixels == 320) {
              layoutParamsX = 80;
          }
          if (myMetrics.widthPixels > 480) {
              
              layoutParamsX = 80;    
          }
             
          return layoutParamsX;
        }
        public int getCountY() {
            final DisplayMetrics myMetrics = getResources().getDisplayMetrics();
          int layoutParamsY = 0;
          
          if (myMetrics.heightPixels == 480) {
              layoutParamsY = 96;
          }
          if (myMetrics.heightPixels == 320) {
              layoutParamsY = 130;
          }         
          if(myMetrics.heightPixels==1280)
          {
              layoutParamsY = 128;
                  
          }
          if(myMetrics.heightPixels==768)
          {
              layoutParamsY = 128;
                  
          }          

          
          return layoutParamsY;
        }		
		
		
		public final int getCount() {
		    final DisplayMetrics myMetrics = getResources().getDisplayMetrics();
          int layoutParamsX = (myMetrics.widthPixels/96);
          int layoutParamsY = (myMetrics.heightPixels/130);
          
          int Xcount=getCountX();
          int Ycount=getCountY();
          return Xcount*Ycount;
//			int bookNum =  myBookApps.size();
//			int line = (bookNum/5)+1;
//			if (line == 1)
//				return 10;
//			else
//				return line*5;
		}

		public final Object getItem(int position) {
			if(position < myBookApps.size())
				return myBookApps.get(position);
			else
				return null;
		}

		public final long getItemId(int position) {
			return position;
		}
	}

}
