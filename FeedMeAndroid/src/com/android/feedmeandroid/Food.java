package com.android.feedmeandroid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

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
		this.image_url = image_url;
		this.num_positive = num_positive;
		this.num_negative = num_negative;
		this.comment = comment.clone();
		this.price = price;
	}

	public Bitmap loadBitmap(String url) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is, 8192);
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}
}
