/***
	Copyright (c) 2009 
	Author: Stefan Klumpp <stefan.klumpp@gmail.com>
	Web: http://stefanklumpp.com

	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package com.android.feedmeandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HTTPClient {
	private static final String TAG = "HttpClient";

	public static ArrayList<JSONObject> SendHttpPost(String URL,
			JSONObject jsonObjSend) {

		try {
			ArrayList<JSONObject> retArray = new ArrayList<JSONObject>();
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPostRequest = new HttpPost(URL);

			StringEntity se;
			se = new StringEntity(jsonObjSend.toString());

			// Set HTTP parameters
			httpPostRequest.setEntity(se);
			httpPostRequest.setHeader("Accepts", "application/json");
			httpPostRequest.setHeader("Content-Type", "application/json");
			// httpPostRequest.setHeader("Accept-Encoding", "gzip"); // only set
			// this parameter if you would like to use gzip compression

			long t = System.currentTimeMillis();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpPostRequest);
			Log.v(TAG,
					"HTTPResponse received in ["
							+ (System.currentTimeMillis() - t) + "ms]");

			// Get hold of the response entity (-> the data):
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}

				// convert content stream to a String
				String resultString = convertStreamToString(instream);
				Log.i(TAG, resultString);
				instream.close();
				resultString = resultString.substring(1,
						resultString.length() - 2); // remove wrapping "[" and
													// "]"
				String[] results = resultString.split("\\},\\{");
				for (int i = 0; i < results.length; i++) {
					JSONObject jsonObjRecv;
					String res = results[i];

					// Transform the String into a JSONObject
					if (i == 0 && results.length > 1) {
						jsonObjRecv = new JSONObject(res + "}");
					} else if (i == results.length - 1 && results.length > 1) {
						jsonObjRecv = new JSONObject("{" + res);
					} else {
						jsonObjRecv = new JSONObject(res);
					}
					retArray.add(jsonObjRecv);

					// Raw DEBUG output of our received JSON object:
					Log.i(TAG, "<JSONObject>\n" + jsonObjRecv.toString()
							+ "\n</JSONObject>");
				}

				return retArray;
			}

		} catch (Exception e) {
			// More about HTTP exception handling in another tutorial.
			// For now we just print the stack trace.
			e.printStackTrace();
		}
		return null;
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 * 
		 * (c) public domain:
		 * http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/
		 * 11/a-simple-restful-client-at-android/
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static Bitmap downloadFile(final String fileUrl) {
		final Bitmap[] bitmap = new Bitmap[1];
		Thread t = new Thread(new Runnable() {
			public void run() {
				URL myFileUrl = null;
				try {
					myFileUrl = new URL(fileUrl);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					HttpURLConnection conn = (HttpURLConnection) myFileUrl
							.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream is = conn.getInputStream();

					Bitmap bmImg = BitmapFactory.decodeStream(is);
					bitmap[0] = bmImg;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap[0];
	}

}