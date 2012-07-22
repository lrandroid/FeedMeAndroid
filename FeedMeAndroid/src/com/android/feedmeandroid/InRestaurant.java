package com.android.feedmeandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class InRestaurant extends Activity {
	static boolean isDoneEating = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isDoneEating) {
			Intent myIntent = new Intent(InRestaurant.this, Payment.class);
			InRestaurant.this.startActivity(myIntent);
		}
		setTitle("In Restaurant");
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		Button get_assistance = new Button(this);
		get_assistance.setText("Touch to Request Waiter");
		get_assistance.setTextColor(Color.WHITE);
		get_assistance.setTextSize(40);
		Resources r = getResources();
		Drawable[] layers = new Drawable[2];
		layers[0] = r.getDrawable(R.drawable.waitress_state);
		layers[1] = r.getDrawable(R.drawable.guide_click_botton_bg);
		LayerDrawable layerDrawable = new LayerDrawable(layers);
		get_assistance.setBackgroundDrawable(layerDrawable);
		get_assistance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder done = new AlertDialog.Builder(
						InRestaurant.this);
				done.setTitle("Request waiter?");
				done.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								callWaiter();
								dialog.cancel();
							}

						});
				done.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}

						});
				done.show();
			}

		});
		layout.addView(get_assistance, Feed.width, 3 * Feed.height / 4);
		Button gotopay = new Button(this);
		gotopay.setText("Done? Pay now...");
		gotopay.setTextColor(Color.WHITE);
		gotopay.setBackgroundResource(R.drawable.candidate_first_dark);
		gotopay.setLayoutParams(Feed.buttonParams);
		gotopay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isDoneEating = true;
				Intent myIntent = new Intent(InRestaurant.this, Payment.class);
				InRestaurant.this.startActivity(myIntent);

			}

		});
		layout.addView(gotopay);
		setContentView(layout);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isDoneEating) {
			Intent myIntent = new Intent(InRestaurant.this, Payment.class);
			InRestaurant.this.startActivity(myIntent);
		}
	}

	public static void callWaiter() {

	}
}
