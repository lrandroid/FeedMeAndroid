package com.android.feedmeandroid;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.Facebook;

public class Feed extends Activity {

	Facebook facebook = new Facebook("409981355705862");
	private SharedPreferences mPrefs;
	Order order;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);

		// set facebook access token
		mPrefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, 0);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		Log.v("test", access_token);
		Log.v("test", Long.toBinaryString(expires));

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		// query facebook for basic user info
		String user_info = "";
		JSONObject json_user_info;
		try {
			user_info = facebook.request("me");
			json_user_info = new JSONObject(user_info);

		}
		// gotta catch em all
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// query web client for the following:
		// 1) notify them that the user has checked into the restaurant,
		// along with user information
		// 2)

	}

	public void showFood(Food food) {
		Intent myIntent = new Intent(Feed.this, ItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("value", food);
		Feed.this.startActivity(myIntent);
	}
}
