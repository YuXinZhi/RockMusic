package com.example.rockmusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		TextView splashText = (TextView) findViewById(R.id.splash_text);
		new CountDownTimer(1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}

		}.start();
	}

}
