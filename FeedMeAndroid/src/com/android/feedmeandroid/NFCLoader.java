package com.android.feedmeandroid;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Window;

public class NFCLoader extends Activity {
	PendingIntent mPendingIntent;
	String[][] mTechLists;
	IntentFilter[] mFilters;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] { ndef, };

		// Setup a tech list for all NfcF tags
		mTechLists = new String[][] { new String[] { MifareClassic.class
				.getName() } };

		Intent intent = getIntent();
		resolveIntent(intent);
	}

	void resolveIntent(Intent intent) {
		// 1) Parse the intent and get the action that triggered this intent
		String action = intent.getAction();
		// 2) Check if it was triggered by a tag discovered interruption.
		if (action.equalsIgnoreCase("android.nfc.action.NDEF_DISCOVERED")) {
			// 3) Get an instance of the TAG from the NfcAdapter
			// 4) Get an instance of the Mifare classic card from this TAG
			// intent
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
				if (msgs.length>0) {
					msgs[0] = (NdefMessage) rawMsgs[0];
					String data_raw = new String(msgs[0].toByteArray());
					String data = data_raw.substring(data_raw.indexOf(Constants.FEED_ME_ID)
							+ Constants.FEED_ME_ID.length());
					String[] data_split = data.split(",");
					Session.set(data_split[0], data_split[1]);
					Intent myIntent = new Intent(NFCLoader.this, Feed.class);
					
					//clear order
					Feed.order.clear();
					
					NFCLoader.this.startActivity(myIntent);
				}
			}
		}// End of method
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
		resolveIntent(intent);
	}
}
