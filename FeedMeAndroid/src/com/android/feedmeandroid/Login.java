package com.android.feedmeandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class Login extends Activity {

    Facebook facebook = new Facebook("409981355705862");
    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.v("login","start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*
         * Get existing access_token if any
         */
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {
        	Log.v("login","test");
            facebook.authorize(this, new String[] {}, new DialogListener() {

                public void onComplete(Bundle values) {
                	Log.v("login","complete");
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                    
                    //send web client user data
                    
                    //launch "feed"
                    Intent myIntent = new Intent(Login.this, Feed.class);
                    Login.this.startActivity(myIntent);
                }
    
               
                public void onFacebookError(FacebookError error) {}
    
              
                public void onError(DialogError e) {}
    
           
                public void onCancel() {}
            });
        }
        
        else {
        	Log.v("login","already logged in");
            //send web client user data
            
            //launch "feed"
            Intent myIntent = new Intent(Login.this, Feed.class);
            Login.this.startActivity(myIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    @Override
    public void onResume() {    
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }
}