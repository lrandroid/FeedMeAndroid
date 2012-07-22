package com.android.feedmeandroid;

import java.util.HashMap;

import android.graphics.Bitmap;

public class Cache {
	private static HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();

	public static void clear() {
		cache.clear();
	}

	public static Bitmap get(String key) {
		return cache.get(key);
	}

	public static void put(String key, Bitmap image) {
		cache.put(key, image);
	}
}
