package com.android.feedmeandroid;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class Feed extends Activity {

	Facebook facebook = new Facebook("409981355705862");
	private SharedPreferences mPrefs;
	Order order;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.v("test", "test0");
		// set facebook access token
		mPrefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, 0);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		/*
		 * Only call authorize if the access_token has expired.
		 */
		if (!facebook.isSessionValid()) {
			Log.v("test", "test2");
			facebook.authorize(this, new String[] {}, new DialogListener() {

				public void onComplete(Bundle values) {
					Log.v("test", "test3");
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();
					Feed.this.showMenu();
				}

				public void onFacebookError(FacebookError error) {
				}

				public void onError(DialogError e) {
				}

				public void onCancel() {
				}
			});
		}
		// if already authorized fb user, then show menu
		else {
			Log.v("test", "test1");
			showMenu();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		super.onResume();
		facebook.extendAccessTokenIfNeeded(this, null);
		showMenu();
	}

	public void showMenu() {
		// query facebook for basic user info
		final String[] name = new String[2];
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {

					JSONObject json_user_info;
					String user_info = "";
					user_info = facebook.request("me");
					json_user_info = new JSONObject(user_info);

					// query web client for the following:
					// 1) construct user json object to pass to web client
					// 2) notify them that the user has checked into the
					// restaurant,
					// along with FB user information
					// 3) receive menu info for the restaurant from the site

					// 1) construct user json object to pass to web client
					String first_name = json_user_info.getString("first_name");
					String last_name = json_user_info.getString("last_name");
					String facebook_id = json_user_info.getString("id");
					JSONObject pass_user_info = new JSONObject();
					pass_user_info.put("first_name", first_name);
					pass_user_info.put("last_name", last_name);
					pass_user_info.put("facebook_id", facebook_id);

					JSONObject webRequest = new JSONObject();
					String res_id = Session.getRestaurant();
					webRequest.put("restaurant_id", "1");
					String table_id = Session.getTable();
					webRequest.put("table_id", "1");
					webRequest.put("user", pass_user_info);

					// 2) notify them that the user has checked into the
					// restaurant,
					// along with FB user information
					// 3) receive menu info for the restaurant from the site
					Log.v("request", webRequest.toString());
					JSONObject menu = HTTPClient.SendHttpPost(
							Constants.WEB_CLIENT_REST_URL, webRequest);
					name[0] = first_name;
					name[1] = last_name;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Use returned JSONObject to populate layout with food
		ArrayList<Food> menu = new ArrayList<Food>();
		String[] comments = new String[2];
		comments[0] = "comment1";
		comments[1] = "comment2";
		Food test1 = new Food(
				"Hamburger",
				"delecious1",
				"http://www.hdwallpapersarena.com/wp-content/uploads/2012/07/Fast-Food-HD-Wallpapers-and-Images-4.jpg",
				1, 1, comments, "1.00");
		for (int i = 0; i < 5; i++) {
			menu.add(test1);
		}

		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);
		TextView text = new TextView(this);
		text.setText("welcome: " + name[0] + " " + name[1]);
		linear.addView(text);

		for (final Food f : menu) {
			LinearLayout item = new LinearLayout(this);
			item.setOrientation(LinearLayout.VERTICAL);
			TextView title_and_price = new TextView(this);
			title_and_price.setText(f.title + " " + f.price);
			item.addView(title_and_price);
			ImageView image = new ImageView(this);
			Bitmap bitmap = Cache.get(f.image_url);
			if (bitmap == null) {
				bitmap = HTTPClient.downloadFile(f.image_url);
				Cache.put(f.image_url, bitmap);
			}
			image.setImageBitmap(bitmap);
			image.setCropToPadding(true);
			item.addView(image);
			// make each item clickable to take to the food page
			item.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					showFood(f);
				}

			});
			item.setBackgroundResource(R.drawable.guide_click_botton_bg);
			linear.addView(item);
		}

		ScrollView scroll = new ScrollView(this);
		scroll.addView(linear);
		setContentView(scroll);

	}

	public void showFood(Food food) {
		Intent myIntent = new Intent(Feed.this, ItemActivity.class);
		myIntent.putExtra("food", food);
		Feed.this.startActivity(myIntent);
	}

}
