package com.sean.bookcase;

import com.sean.android.ebookmain.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class BookCaseAllBook extends Activity {
	WebView browser;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allbook);
		browser = (WebView) findViewById(R.id.webkit);
		
		browser.loadUrl("http://192.168.0.25");
	}
}
