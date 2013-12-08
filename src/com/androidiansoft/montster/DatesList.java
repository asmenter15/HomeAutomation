package com.androidiansoft.montster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DatesList extends Activity {

	ListView lv;

	ArrayList<String> pathArr = new ArrayList<String>();

	private static final String URL = "http://192.168.0.3:8080/Montsterr/rest/reads/findReads";
	private static final String DATE = "date";
	private static final String PIC_PATH = "picturePath";
	private static final String INF_READ = "infraredread";
	ProgressDialog pdia;

	AsyncTask<String, Void, ArrayList<Date>> asyncTask;
	StringBuffer strBuf1 = null;
	StringBuffer strBuf = null;
	int parentId = 0;
	ArrayList<Date> dates;
	ArrayList<Date> finalArr = new ArrayList<Date>();
	int pos;
	String leafNode;
	int deptId;
	int parentId2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dates_list);
		pdia = new ProgressDialog(this);
		lv = (ListView) findViewById(R.id.datesList);
		initializeListeners();
		getDatesList();
	}

	private void initializeListeners() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				pos = position;
				Intent in = new Intent(getApplicationContext(),
						ViewPicture.class);
				in.putExtra("path", pathArr.get(pos));
				startActivity(in);
			}
		});

	}

	public void getDatesList() {

		asyncTask = new AsyncTask<String, Void, ArrayList<Date>>() {

			@Override
			protected void onPreExecute() {
				pdia.setMessage("Retrieving Dates...");
				pdia.show();
				super.onPreExecute();
			}

			@Override
			protected ArrayList<Date> doInBackground(String... params) {

				dates = new ArrayList<Date>();

				XMLParser parser1 = new XMLParser();
				String xml = parser1.getXmlFromUrlGet(URL);
				Document document = parser1.getDomElement(xml);
				NodeList nl = document.getElementsByTagName(INF_READ);

				// Looping through all Dates
				for (int i = 0; i < nl.getLength(); i++) {

					Element e = (Element) nl.item(i);

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM/dd/yyyy hh:mm:ss");
					Date convertedDate = new Date();

					try {
						convertedDate = dateFormat.parse(parser1.getValue(e,
								DATE));
					} catch (java.text.ParseException e1) {
						e1.printStackTrace();
					}
					pathArr.add(parser1.getValue(e, PIC_PATH));

					dates.add(convertedDate);
				}
				Collections.reverse(dates);
				Collections.reverse(pathArr);
				return dates;
			}

			protected void onPostExecute(ArrayList<Date> result) {
				super.onPostExecute(result);
				pdia.dismiss();
				ListAdapter adapter = new ArrayAdapter<Date>(
						getApplicationContext(),
						android.R.layout.simple_list_item_1, result);
				lv.setAdapter(adapter);
			}
		};
		asyncTask.execute(URL);
	}
}
