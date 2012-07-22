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
		TextView text = new TextView(this);
		text.setText(this_food.num_positive + "+ " + this_food.num_negative
				+ "-");
		text.setTextColor(Color.WHITE);
		text.setTypeface(Typeface.DEFAULT_BOLD);
		text.setLayoutParams(tParams);

		rLayout.addView(image);
		rLayout.addView(text);
		layout.addView(rLayout);
		TextView description_view = new TextView(this);
		description_view.setText(this_food.description+"\n\n");
		layout.addView(description_view);
		TextView comments_header = new TextView(this);
		comments_header.setText("Reviews");
		comments_header.setTextScaleX(2);
		layout.addView(comments_header);
		TextView comments_view = new TextView(this);
		StringBuilder comments_builder = new StringBuilder();
		for (int i=0; i<this_food.comment.length; i++){
			comments_builder.append(":"+this_food.comment[i].trim()+"\n\n");
		}
		comments_view.setText(comments_builder);
		layout.addView(comments_view);
		ScrollView scroll = new ScrollView(this);
		scroll.addView(layout);
		setContentView(scroll);
	}
}
