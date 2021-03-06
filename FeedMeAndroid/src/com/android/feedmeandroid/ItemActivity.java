package com.android.feedmeandroid;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("asdf", "oncreate");
		Food temp_food = null;
		try {
			Intent i = getIntent();
			temp_food = (Food) i.getSerializableExtra("food");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (temp_food == null) {
			finish();
			return;
		}
		final Food this_food = temp_food;
		setTitle(this_food.title);
		LinearLayout toplayout = new LinearLayout(this);
		toplayout.setOrientation(LinearLayout.VERTICAL);

		RelativeLayout rLayout = new RelativeLayout(this);
		LayoutParams rlParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		rLayout.setLayoutParams(rlParams);

		ImageView image = new ImageView(this);
		image.setImageBitmap(Cache.get(this_food.image_url));
		image.setLayoutParams(rlParams);

		RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		tParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		LinearLayout rating_layout = new LinearLayout(this);
		rating_layout.setOrientation(LinearLayout.VERTICAL);
		TextView text_p = new TextView(this);
		text_p.setText("+"+this_food.num_positive);
		text_p.setTextColor(Color.rgb(00,12*16,00));
		text_p.setTypeface(Typeface.DEFAULT_BOLD);
		text_p.setLayoutParams(tParams);
		text_p.setTextSize(20);
		text_p.setPadding(50, 0, 50, 0);
		TextView text_n = new TextView(this);
		text_n.setText("-"+this_food.num_negative);
		text_n.setTextColor(Color.RED);
		text_n.setTypeface(Typeface.DEFAULT_BOLD);
		text_n.setLayoutParams(tParams);
		text_n.setTextSize(20);
		text_n.setPadding(50, 0, 50, 0);
		rating_layout.addView(text_p);
		rating_layout.addView(text_n);
		rating_layout.setBackgroundResource(R.drawable.candidate_feedback_background);
		rating_layout.setLayoutParams(new android.view.ViewGroup.LayoutParams(
	            android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
	            android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		rLayout.addView(image);
		rLayout.addView(rating_layout);
		LayoutParams params = rating_layout.getLayoutParams();
		toplayout.addView(rLayout, new android.view.ViewGroup.LayoutParams(
	            android.view.ViewGroup.LayoutParams.FILL_PARENT,
	            android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		TextView description_view = new TextView(this);
		description_view.setText(this_food.description+"\n");
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    llp.setMargins(50, 0, 50, 0); // llp.setMargins(left, top, right, bottom);
		description_view.setLayoutParams(llp);
		description_view.setTextSize(20);
		toplayout.addView(description_view);

		
		TextView price_view = new TextView(this);
		final DecimalFormat rounding = new DecimalFormat("#0.00");

		price_view.setText("Price: $"+rounding.format(Double.parseDouble(this_food.price))+"\n\n");
		LinearLayout.LayoutParams llp2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    llp2.setMargins(50, 0, 50, 0); // llp.setMargins(left, top, right, bottom);
	    price_view.setLayoutParams(llp2);
	    price_view.setTextSize(20);
		toplayout.addView(price_view);

		
		Button add = new Button(this);
		add.setBackgroundResource(R.drawable.candidate_first_dark);
		add.setTextColor(Color.WHITE);
		add.setText("Add");
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Feed.order.addFood(this_food);
				Toast.makeText(getApplicationContext(), "Item added!", 0).show();

				new Thread(new Runnable(){
					public void run(){
						finish();
					}
				}).start();

			}
			
		});

		/*TextView comments_header = new TextView(this);
		comments_header.setText("Reviews:");
		comments_header.setTypeface(null, Typeface.BOLD);
		comments_header.setTextSize(22);
		layout.addView(comments_header);
		TextView comments_view[] = new TextView[this_food.comment.length];
		for (int i=0; i<this_food.comment.length; i++){
			comments_view[i] = new TextView(this);
			comments_view[i].setText(this_food.comment[i].trim());
			comments_view[i].setBackgroundResource(R.drawable.guide_click_botton_bg);
			layout.addView(comments_view[i]);
		}*/
		ScrollView scroll = new ScrollView(this);
		scroll.addView(toplayout);
		LinearLayout finallayout = new LinearLayout(this);
		finallayout.setOrientation(LinearLayout.VERTICAL);
		finallayout.addView(scroll,  Feed.width, 3*Feed.height/4);
		finallayout.addView(add, Feed.buttonParams);
		setContentView(finallayout);
	}
}
