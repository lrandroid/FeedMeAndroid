package com.android.feedmeandroid;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Food implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1329012229322458612L;
	public String title;
	public String description;
	public Bitmap image;
	public String[] comment;
	public int num_positive;
	public int num_negative;
	public String price;

	public Food(String title, String description, String image_url,
			int num_positive, int num_negative, String[] comment, String price) {
		this.title = title;
		this.description = description;
		try {
			this.image = loadBitmap(image_url);
		} catch (Exception e) {
		}
		this.num_positive = num_positive;
		this.num_negative = num_negative;
		this.comment = comment.clone();
		this.price = price;
	}
}
