package com.android.feedmeandroid;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

public class Feed extends Activity {

	Facebook facebook = new Facebook("409981355705862");
	private SharedPreferences mPrefs;
	Order order;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
			facebook.authorize(this, new String[] {}, new DialogListener() {

				public void onComplete(Bundle values) {
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
		//if already authorized fb user, then show menu
		else {
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
    }
    
	public void showMenu() {
		// query facebook for basic user info
		String user_info = "";
		JSONObject json_user_info;
		try {
			user_info = facebook.request("me");
			json_user_info = new JSONObject(user_info);

			// query web client for the following:
			// 1) construct user json object to pass to web client
			// 2) notify them that the user has checked into the restaurant,
			// along with FB user information
			// 3) receive menu info for the restaurant from the site

			// 1) construct user json object to pass to web client
			String first_name = json_user_info.getString("first_name");
			String last_name = json_user_info.getString("last_name");
			String facebook_id = json_user_info.getString("id");
			JSONObject pass_user_info = new JSONObject();
			pass_user_info.put("first_name", first_name);
			pass_user_info.put("last_name", last_name);
			// pass_user_info.put("facebook_id",facebook_id);

			JSONObject webRequest = new JSONObject();
			String res_id = Session.getRestaurant();
			webRequest.put("restaurant_id", res_id);
			String table_id = Session.getTable();
			webRequest.put("table_id", table_id);
			webRequest.put("user", pass_user_info);

			// 2) notify them that the user has checked into the restaurant,
			// along with FB user information
			// 3) receive menu info for the restaurant from the site
			Log.v("request", webRequest.toString());
			JSONObject menu = HTTPClient.SendHttpPost(
					Constants.WEB_CLIENT_REST_URL, webRequest);
			// Log.v("tested request",menu.toString());

			// Use returned JSONObject to populate layout with food
			LinearLayout linear = new LinearLayout(this);
			linear.setOrientation(LinearLayout.VERTICAL);
			TextView text = new TextView(this);
			text.setText("welcome: " + first_name + " " + last_name);
			linear.addView(text);
			setContentView(linear);
		}
		// gotta catch em all
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showFood(Food food) {
		Intent myIntent = new Intent(Feed.this, ItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("food", food);
		Feed.this.startActivity(myIntent);
	}
}
