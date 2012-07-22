package com.android.feedmeandroid;

import java.io.Serializable;

public class Food implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1329012229322458612L;
	public int id;
	public String title;
	public String description;
	public String image_url;
	public int num_positive;
	public int num_negative;
	public String price;

	public Food(int id, String title, String description, String image_url,
			int num_positive, int num_negative, String price) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.image_url = image_url;
		this.num_positive = num_positive;
		this.num_negative = num_negative;
		this.price = price;
	}
}
