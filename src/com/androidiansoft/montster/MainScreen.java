package com.androidiansoft.montster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainScreen extends Activity {

	Button light;
	Button viewCam;
	SharedPreferences preferences;
	public static final String LIGHT_PREF = "light_pref";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
        viewCam = (Button) findViewById(R.id.viewCam);
        light = (Button) findViewById(R.id.light);
        
		preferences = PreferenceManager
				.getDefaultSharedPreferences(this
						.getApplicationContext());
		
		int lightPref = preferences.getInt(LIGHT_PREF, 0);
		
		if(lightPref == 0) {
			light.setText(R.string.light_off);
		} else if(lightPref == 1) {
			light.setText(R.string.light_on);
		} else {
			changeLights();
		}
        
        light.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				changeLights();
			}
		});
        
        viewCam.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Uri uri = Uri.parse("http://68.110.111.35:91");
				 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				 startActivity(intent);
				
			}
		});
    }
    
    private void changeLights() {
    	
    	AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {


			@Override
			protected String doInBackground(String... params) {
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost get = new HttpPost("http://68.110.111.35:8080");
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs
						.add(new BasicNameValuePair("$", "l"));

				String response = "";
				try {
					ResponseHandler<String> responseHandler=new BasicResponseHandler();
					get.setEntity(new UrlEncodedFormEntity(nameValuePairs));					
					response = httpClient.execute(get, responseHandler);
					System.out.println(response);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return response;
			}
			
			@Override
			protected void onPostExecute(String result) {
				SharedPreferences.Editor editor = preferences.edit();
				if(result.trim().equals("1")) {
					light.setText(R.string.light_on);
					editor.putInt(LIGHT_PREF, 1);
					editor.commit();
				} else if (result.trim().equals("0")) {
					light.setText(R.string.light_off);
					editor.putInt(LIGHT_PREF, 0); 
					editor.commit();
				}
			}
    		
    		
    		
		};
		task.execute("");
    }
    
}
