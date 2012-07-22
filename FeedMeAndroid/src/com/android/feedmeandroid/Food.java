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
	public String image_url;
	public String[] comment;
	public int num_positive;
	public int num_negative;
	public String price;

	public Food(String title, String description, String image_url,
			int num_positive, int num_negative, String[] comment, String price) {
		this.title = title;
		this.description = description;
<<<<<<< HEAD
		try {
			this.image = HTTPClient.downloadFile(image_url);
		} catch (Exception e) {
		}
=======
		this.image_url = image_url;
>>>>>>> 43a17cab9874e140be9c43299793220a23091e83
		this.num_positive = num_positive;
		this.num_negative = num_negative;
		this.comment = comment.clone();
		this.price = price;
	}
}
