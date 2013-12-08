package com.androidiansoft.montster;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

public class ViewPicture extends Activity {

	String path;
	AsyncTask<String, Void, String> asyncTask;
	File pathSpec;
	StringBuffer sb = new StringBuffer();
	String fileName;
	ProgressDialog pdia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		path = getIntent().getStringExtra("path");
		setContentView(R.layout.pic_view);
		pdia = new ProgressDialog(this);

		// Find folder on sdcard
		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/InfraredReads");
		if (!folder.exists()) {
			folder.mkdir();
		}

		pathSpec = new File(folder, path);
		try {
			pathSpec.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callFTP();

	}

	private void callFTP() {

		asyncTask = new AsyncTask<String, Void, String>() {

			@Override
			protected void onPreExecute() {
				pdia.setMessage("Retrieving Image...");
				pdia.show();
				super.onPreExecute();
			}

			@Override
			protected String doInBackground(String... params) {

				FTPClient client = new FTPClient();
				BufferedOutputStream fos = null;
				try {
					client.connect(InetAddress.getByName("192.168.0.3"), 21);
					client.login("test", ";lkjasdf");
					client.enterLocalPassiveMode();
					client.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
					fos = new BufferedOutputStream(new FileOutputStream(
							pathSpec));
					client.retrieveFile("/home/test/ftp/" + path, fos);
				}// try
				catch (IOException e) {
					e.printStackTrace();
				}// catch
				finally {
					try {
						if (fos != null)
							fos.close();
						client.disconnect();
					}// try
					catch (IOException e) {
						e.printStackTrace();
					}// catch
				}// finally
				return null;
			}// getfileFTP

			protected void onPostExecute(String result) {
				super.onPostExecute(fileName);
				pdia.dismiss();

				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(pathSpec), "image/*");
				startActivity(intent);
			}

		};

		asyncTask.execute();
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}
