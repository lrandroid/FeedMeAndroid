package com.android.feedmeandroid;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.feedmeandroid.exception.StripeException;
import com.android.feedmeandroid.model.Charge;


public class Payment extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final LinearLayout item_layout = new LinearLayout(Payment.this);
		item_layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		layoutParams.height = 88;
		layoutParams.width = 90;
		layoutParams.setMargins(20, 15, 20, 20);
		double sum = 0;
		final TextView subtotal = new TextView(Payment.this);
		subtotal.setTextSize(24);
		final TextView tax = new TextView(Payment.this);
		tax.setTextSize(24);
		final TextView total = new TextView(Payment.this);
		total.setTextSize(24);

		final DecimalFormat rounding = new DecimalFormat("#0.00");

		for (int i = 0; i < Feed.order.size(); i++) {
			final Food food = Feed.order.get(i);
			final LinearLayout this_layout = new LinearLayout(Payment.this);
			this_layout.setOrientation(LinearLayout.HORIZONTAL);
			TextView item_description = new TextView(Payment.this);
			item_description.setText(food.title + "\n" + food.price);
			sum += Double.parseDouble(food.price);
			item_description.setTextSize(24);
			item_description.setMinHeight(layoutParams.height * 2
					+ layoutParams.bottomMargin * 2 + layoutParams.topMargin
					* 2);
			item_description.setGravity(Gravity.CENTER_VERTICAL);
			item_description.setLayoutParams(textParams);

			Button upvote = new Button(Payment.this);
			upvote.setBackgroundResource(R.drawable.thumbs_up);
			upvote.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

				}

			});
			Button downvote = new Button(Payment.this);
			downvote.setBackgroundResource(R.drawable.thumbs_down);
			downvote.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

				}

			});
			LinearLayout vote_layout = new LinearLayout(this);
			vote_layout.setOrientation(LinearLayout.HORIZONTAL);
			vote_layout.addView(upvote, layoutParams);
			vote_layout.addView(downvote, layoutParams);
			this_layout.addView(vote_layout);
			this_layout.addView(item_description);
			this_layout.setBackgroundResource(R.drawable.guide_click_botton_bg);
			item_layout.addView(this_layout);
		}
		subtotal.setText("Subtotal: $" + rounding.format(sum));
		double tax_cost = sum * .0725;
		double total_cost = sum + tax_cost;
		tax.setText("Tax: $" + rounding.format(tax_cost));
		total.setText("Total: $" + rounding.format(total_cost));

		item_layout.addView(subtotal);
		item_layout.addView(tax);
		item_layout.addView(total);
		ScrollView scroll = new ScrollView(Payment.this);
		scroll.addView(item_layout);
		LinearLayout full_layout = new LinearLayout(this);
		full_layout.setOrientation(LinearLayout.VERTICAL);
		full_layout.addView(scroll, Feed.width, 3 * Feed.height / 4);

		Button paynow = new Button(this);
		paynow.setText("Pay Now");
		paynow.setTextSize(28);
		paynow.setTextColor(Color.WHITE);
		paynow.setTypeface(null,Typeface.BOLD);
		paynow.setBackgroundResource(R.drawable.candidate_first_dark);
		full_layout.addView(paynow, Feed.buttonParams);
		setContentView(full_layout);
	}

	public static void makePayment() {
		Stripe.apiKey = "L8px8dWKTJmab3qzAuq7Vh4hwp3sXbK4";
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", 100);
		chargeMap.put("currency", "usd");
		Map<String, Object> cardMap = new HashMap<String, Object>();
		cardMap.put("number", "4242424242424242");
		cardMap.put("exp_month", 12);
		cardMap.put("exp_year", 2012);
		chargeMap.put("card", cardMap);
		try {
			Charge charge = Charge.create(chargeMap);
			System.out.println(charge);
		} catch (StripeException e) {
			e.printStackTrace();
		}
	}

    
    public static void submitReview(final int food_id, final boolean isThumpsUp) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {					
					JSONObject webRequest = new JSONObject();
					webRequest.put("user_id", Feed.fb_id);
					webRequest.put("dish_id", food_id);
					if(isThumpsUp) {
						webRequest.put("value", "1");
					}
					else {
						webRequest.put("value", "-1");
					}
					webRequest.put("comment","");
					
					Log.v("request", webRequest.toString());
					ArrayList<JSONObject> response = HTTPClient.SendHttpPost(
							Constants.WEB_CLIENT_REST_URL_RATINGS, webRequest);
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

    }
}
