package com.sean.bookcase;

import java.util.Timer;
import java.util.TimerTask;

import com.sean.android.ebookmain.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BookCaseIntro extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);

		TimerTask myTask = new TimerTask() {
			public void run() {
				Intent intent = new Intent (BookCaseIntro.this, BookCaseList.class);
				startActivity(intent);
				finish();
			}
		};

		// 1���� myTask ����.
		Timer myTimer = new Timer();
		myTimer.schedule(myTask, 1000);
	}

}
