package com.android.feedmeandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ItemActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("asdf", "oncreate");
		Food this_food = null;
		try {
			Intent i = getIntent();
			this_food = (Food) i.getSerializableExtra("food");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this_food == null) {
			finish();
			return;
		}
		setTitle(this_food.title);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

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
		rating_layout.setOrientation(LinearLayout.HORIZONTAL);
		TextView text_p = new TextView(this);
		text_p.setText(this_food.num_positive + "+");
		text_p.setTextColor(Color.GREEN);
		text_p.setTypeface(Typeface.DEFAULT_BOLD);
		text_p.setLayoutParams(tParams);
		text_p.setHeight(50);
		text_p.setPadding(30, 0, 50, 0);
		TextView text_n = new TextView(this);
		text_n.setText(this_food.num_negative + "-");
		text_n.setTextColor(Color.RED);
		text_n.setTypeface(Typeface.DEFAULT_BOLD);
		text_n.setLayoutParams(tParams);
		text_n.setHeight(50);
		rating_layout.addView(text_p);
		rating_layout.addView(text_n);
		rating_layout.setBackgroundResource(R.drawable.accent_bg);
		rating_layout.setLayoutParams(new android.view.ViewGroup.LayoutParams(
	            android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
	            android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		rLayout.addView(image);
		rLayout.addView(rating_layout);
		LayoutParams params = rating_layout.getLayoutParams();
		params.height = 65;
		layout.addView(rLayout);
		TextView description_view = new TextView(this);
		description_view.setText(this_food.description+"\n\n");
		layout.addView(description_view);
		TextView comments_header = new TextView(this);
		comments_header.setText("Reviews:");
		comments_header.setTypeface(null, Typeface.BOLD);
		comments_header.setTextSize(22);
		layout.addView(comments_header);
		/*TextView comments_view[] = new TextView[this_food.comment.length];
		for (int i=0; i<this_food.comment.length; i++){
			comments_view[i] = new TextView(this);
			comments_view[i].setText(this_food.comment[i].trim());
			layout.addView(comments_view[i]);
		}*/
		ScrollView scroll = new ScrollView(this);
		scroll.addView(layout);
		setContentView(scroll);
	}
}
