package com.example.absin.firebasebus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by absin on 2019-05-27.
 */

public class Alarm_busActivity extends AppCompatActivity {

    String keyword;
    public String dataKey = "vgOxwLDnBL1K%2B0EV%2FG7Yi%2Bge%2BwfXMB66UwEnnmJEUuoej7Zg75Z85lE7wOcYZcysMUq5Sa2VGKzNsczJqzgg9A%3D%3D";
    private String requestUrl;
    ArrayList<Item> list = null;
    Item bus = null;
    RecyclerView recyclerView;
    EditText et1;

    LinearLayout imgDefault;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_bus);


        et1 = (EditText) findViewById(R.id.et1);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);


        et1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때

                recyclerView.removeAllViewsInLayout();
                keyword = et1.getText().toString();
                //  recyclerView.setAdapter(adapter);
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }



            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }

        });

    }



    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        public String getKeyword() {
            return keyword;
        }
        @Override
        protected String doInBackground(String... strings) {

            requestUrl = "http://openapi.gbis.go.kr/ws/rest/busrouteservice?serviceKey=" +dataKey+ "&keyword=" + getKeyword();
            try {
                boolean b_routeName = false;
                boolean b_districtCd =false;
                boolean b_routeTypeName = false;
                boolean b_regionName = false;
                boolean b_routeId = false;

                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));


                int eventType = parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            list = new ArrayList<Item>();
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("busRouteList") && bus != null) {
                                list.add(bus);
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if(parser.getName().equals("busRouteList")){
                                bus = new Item();
                            }
                            if (parser.getName().equals("districtCd")) b_districtCd = true;
                            if(parser.getName().equals("regionName")) b_regionName = true;
                            if(parser.getName().equals("routeId")) b_routeId = true;
                            if (parser.getName().equals("routeName")) b_routeName = true;
                            if (parser.getName().equals("routeTypeName")) b_routeTypeName = true;

                            break;
                        case XmlPullParser.TEXT:
                            if(b_districtCd){
                                bus.setDistrictCd(parser.getText());
                                b_districtCd = false;
                            }else if(b_regionName) {
                                bus.setRegionName(parser.getText());
                                b_regionName = false;
                            }else if(b_routeId) {
                                bus.setRouteId(parser.getText());
                                b_routeId = false;
                            } else if(b_routeName) {
                                bus.setRouteName(parser.getText());
                                b_routeName = false;
                            } else if (b_routeTypeName) {
                                bus.setRouteTypeName(parser.getText());
                                b_routeTypeName = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //   @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            //어답터 연결
            Alarm_bus_Adapter adapter = new Alarm_bus_Adapter(Alarm_busActivity.this, getApplicationContext(), list);
            recyclerView.setAdapter(adapter);

            imgDefault = (LinearLayout) findViewById(R.id.imgDefault);
            if (adapter.getItemCount() > 0) {
                imgDefault.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            else {
                imgDefault.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

        }
    }

    }
