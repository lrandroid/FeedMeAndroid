package com.android.feedmeandroid;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class Feed extends Activity {

	Facebook facebook = new Facebook("409981355705862");
	private SharedPreferences mPrefs;
	static Order order;
	static String fb_id = "";
	static int width;
	static int height;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		width = display.getWidth();
		height = display.getHeight();
		if(order == null) {
			order = new Order();
		}
		

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
		// if already authorized fb user, then show menu
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
		showMenu();
	}

	public void showMenu() {
		// query facebook for basic user info
		final String[] name = new String[2];
		final ArrayList<JSONObject> menus = new ArrayList<JSONObject>();
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
					fb_id = json_user_info.getString("id");
					JSONObject pass_user_info = new JSONObject();
					pass_user_info.put("first_name", first_name);
					pass_user_info.put("last_name", last_name);
					pass_user_info.put("facebook_id", fb_id);

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
					ArrayList<JSONObject> tempMenu = HTTPClient.SendHttpPost(
							Constants.WEB_CLIENT_REST_URL_USER, webRequest);
					menus.addAll(tempMenu);
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

		Log.v("foodstart", Integer.toString(menus.size()));
		for (JSONObject m : menus) {
			try {
				// need to grab comments with JSON request
				final ArrayList<JSONObject> ratings = new ArrayList<JSONObject>();
				Log.v("foodloop", Integer.toString(menus.size()));
				Thread thread2 = new Thread(new Runnable() {
					public void run() {
						try {

							// query web client for comments based on restaurant
							// id
							JSONObject webRequest = new JSONObject();
							String res_id = Session.getRestaurant();
							webRequest.put("restaurant_id", "1");
							Log.v("request", webRequest.toString());
							ArrayList<JSONObject> tempComments = HTTPClient
									.SendHttpPost(
											Constants.WEB_CLIENT_REST_URL_COMMENTS,
											webRequest);
							ratings.addAll(tempComments);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				thread2.start();
				try {
					thread2.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String[] comments = new String[ratings.size()];
				for (int i = 0; i < ratings.size(); i++) {
					JSONObject rating = ratings.get(i);
					comments[i] = (String) rating.get("comment");
				}
				Food food = new Food(
						Integer.toString((Integer) m.get("id")),
						(String) m.get("name"),
						(String) m.get("description"),
						"http://i-cdn.apartmenttherapy.com/uimages/kitchen/2008_04_15-PlaneFood.jpg",
						(Integer) m.get("upvotes"), (Integer) m
								.get("downvotes"), comments, (String) m
								.get("price"));
				menu.add(food);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// put together full menu plus submit order button
		LinearLayout fullMenu = new LinearLayout(this);
		fullMenu.setOrientation(LinearLayout.VERTICAL);

		LinearLayout items = new LinearLayout(this);
		items.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(30, 20, 30, 0);

		for (final Food f : menu) {
			LinearLayout item = new LinearLayout(this);
			item.setOrientation(LinearLayout.VERTICAL);
			TextView title_and_price = new TextView(this);
			title_and_price.setText(f.title + " " + f.price);
			title_and_price
					.setBackgroundResource(R.drawable.guide_click_botton_bg);
			item.addView(title_and_price);
			ImageView image = new ImageView(this);
			Bitmap bitmap = Cache.get(f.image_url);
			if (bitmap == null) {
				bitmap = HTTPClient.downloadFile(f.image_url);
				Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
						bitmap.getHeight(), Config.ARGB_8888);
				Canvas canvas = new Canvas(output);

				final int color = 0xff424242;
				final Paint paint = new Paint();
				final Rect rect = new Rect(0, 0, bitmap.getWidth(),
						bitmap.getHeight());
				final RectF rectF = new RectF(rect);
				final float roundPx = 30;

				paint.setAntiAlias(true);
				canvas.drawARGB(0, 0, 0, 0);
				paint.setColor(color);
				canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
				canvas.drawBitmap(bitmap, rect, rect, paint);
				bitmap = output;
				Cache.put(f.image_url, bitmap);
			}
			BitmapDrawable bg = new BitmapDrawable(getResources(), bitmap);
			item.setBackgroundDrawable(bg);

			// make each item clickable to take to the food page
			item.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					showFood(f);
				}

			});

			items.addView(item, layoutParams);
		}

		items.setBackgroundColor(Color.WHITE);
		ScrollView scroll = new ScrollView(this);
		scroll.addView(items);

		// add submit order button
		Button submitOrder = new Button(this);
		submitOrder.setText("View Order");
		submitOrder.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		submitOrder.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AlertDialog.Builder done = new AlertDialog.Builder(Feed.this);
				final LinearLayout item_layout = new LinearLayout(Feed.this);
				item_layout.setOrientation(LinearLayout.VERTICAL);
				for (int i=0; i<order.size(); i++){
					final Food food = order.get(i);
					final LinearLayout this_layout = new LinearLayout(Feed.this);
					this_layout.setOrientation(LinearLayout.HORIZONTAL);
					TextView item_description = new TextView(Feed.this);
					item_description.setText(food.title);
					item_description.setTextSize(22);
					Button delete = new Button(Feed.this);
					delete.setBackgroundResource(R.drawable.x);
					delete.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							order.remove(food);
							item_layout.removeView(this_layout);
						}
						
					});
					this_layout.addView(delete);
					this_layout.addView(item_description);
					item_layout.addView(this_layout);
				}
				done.setView(item_layout);
				done.setTitle("View Order");
				done.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						order.submitOrder();
						dialog.cancel();
					}

				});
				done.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}

				});
				done.setCancelable(false);
				done.show();
			}

		});

		// add both elements to full menu
		
		fullMenu.addView(scroll, width, 4*height/5);
		fullMenu.addView(submitOrder);
		setContentView(fullMenu);

	}

	public void showFood(Food food) {
		Intent myIntent = new Intent(Feed.this, ItemActivity.class);
		myIntent.putExtra("food", food);
		Feed.this.startActivity(myIntent);
	}

}
