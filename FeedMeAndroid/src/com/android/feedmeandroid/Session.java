package com.android.feedmeandroid;

public class Session {
	private static String id = null;
	private static String table =null;
	public static void set(String restaurant_id, String table_id){
		id = restaurant_id;
		table = table_id;
	}
	public static String getRestaurant(){
		return id;
	}
	public static String getTable(){
		return table;
	}
}
