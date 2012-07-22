package com.android.feedmeandroid;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ItemActivity extends Activity {
	public void OnCreate(Bundle bundle) {
		super.onCreate(bundle);
		Food this_food = null;
		try {
			this_food = (Food) bundle.get("food");
		} catch (Exception e) {
		}
		if (this_food == null) {
			finish();
			return;
		}
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		TextView title_view = new TextView(this);
		title_view.setText(this_food.title);
		layout.addView(title_view);

		RelativeLayout rLayout = new RelativeLayout(this);
		LayoutParams rlParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		rLayout.setLayoutParams(rlParams);

		ImageView image = new ImageView(this);
		image.setImageBitmap(this_food.image);
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
		description_view.setText(this_food.description);
		layout.addView(description_view);
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
