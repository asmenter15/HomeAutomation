package com.androidiansoft.montster;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainScreen extends Activity {

	Button viewList;
	Button viewCam;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        viewList = (Button) findViewById(R.id.viewDates);
        viewCam = (Button) findViewById(R.id.viewCam);
        viewList.setOnClickListener(new OnClickListener() {
		
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(), DatesList.class);
				startActivity(in);
			}
		});
        
        viewCam.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Uri uri = Uri.parse("http://192.168.0.128:91");
				 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				 startActivity(intent);
				
			}
		});
    }
}
