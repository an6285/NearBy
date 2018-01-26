package com.example.ashish.maps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ConnectionDetection cd;
    TextView tv_lat,tv_long;


    double latitude,longitude;
    String url;
    Button btn_rest,btn_map;
    Spinner sp_rad,sp_type;

    ListView lv1;

    String[] rad={"250","500","1000","1500","2000","2500"};
    String[] type={"airport","atm","bank","cafe","doctor","gym","hospital","library","movie_theater","police"
    ,"restaurant","school","shopping_mall","train_station"};

    ArrayAdapter<String> ad;

    JSONObject jsonobj;
    JSONArray resarray;

    public static final ArrayList<String> names=new ArrayList<String>();
    public static final ArrayList<String> todisplay=new ArrayList<String>();
    public static final ArrayList<Double> latitudes=new ArrayList<Double>();
    public static final ArrayList<Double> longitudes=new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tv_lat= (TextView) findViewById(R.id.tv_lat2);
        tv_long= (TextView) findViewById(R.id.tv_long2);

        sp_rad= (Spinner) findViewById(R.id.sp_rad);
        sp_type= (Spinner) findViewById(R.id.sp_type);

        btn_rest= (Button) findViewById(R.id.btn_rest);
        btn_map= (Button) findViewById(R.id.btn_map);

        lv1= (ListView) findViewById(R.id.lv1);

        cd=new ConnectionDetection(Main2Activity.this);

        String lat,longi;
        lat=getIntent().getStringExtra("latitude");
        longi=getIntent().getStringExtra("longitude");

        ArrayAdapter<String> rad1=new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_list_item_1,rad);
        sp_rad.setAdapter(rad1);

        ArrayAdapter<String> type1=new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_list_item_1,type);
        sp_type.setAdapter(type1);

        latitude=Double.parseDouble(lat);
        longitude=Double.parseDouble(longi);

        //url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=28.577380,77.314494&radius=500&types=restaurant&sensor=false&key=AIzaSyBYim7RY_8oUxXfBxnJCb0jjgPCDrFr790";


        tv_lat.setText(lat);
        tv_long.setText(longi);

        btn_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url=sbMethod(sp_rad.getSelectedItem().toString(),sp_type.getSelectedItem().toString());
                if(cd.isConnectedToInternet())
                {
                    new SyncData().execute(url);
                }
                else
                {
                    Toast.makeText(Main2Activity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cd.isConnectedToInternet())
                {
                    Intent intent1=new Intent(Main2Activity.this,MapsActivity.class);
                    intent1.putExtra("latitude",latitude);
                    intent1.putExtra("longitude",longitude);
                    startActivity(intent1);
                }
                else
                {
                    Toast.makeText(Main2Activity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class SyncData extends AsyncTask<String,Void,String> {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Main2Activity.this);
            pd.setTitle("Please Wait....");
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String requesturl=strings[0];
            String status="NOK";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpGet req=new HttpGet(requesturl);
                HttpResponse res=httpClient.execute(req);
                HttpEntity jsonentity=res.getEntity();
                InputStream in=jsonentity.getContent();
                jsonobj= new JSONObject(convertStreamToString(in));
                status="OK";
                return status;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s=="OK") {
                try {
                    resarray = jsonobj.getJSONArray("results");
                    int len = resarray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject place= (JSONObject) resarray.get(i);
                        String lat=place.getJSONObject("geometry").getJSONObject("location").getString("lat");
                        String lng=place.getJSONObject("geometry").getJSONObject("location").getString("lng");

                        latitudes.add(i,Double.parseDouble(lat));
                        longitudes.add(i,Double.parseDouble(lng));
                        names.add(i,resarray.getJSONObject(i).getString("name"));
                        todisplay.add(i,resarray.getJSONObject(i).getString("name")+" lat="+lat+" lng="+lng);

                    }
                    ad=new ArrayAdapter<>(Main2Activity.this,android.R.layout.simple_list_item_1,todisplay);
                    lv1.setAdapter(ad);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(Main2Activity.this,"Some Error Occured",Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    }

    public String sbMethod(String radius,String type) {

        //use your current location here
        String sb = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        sb+="location=" + latitude + "," + longitude;
        sb+="&radius="+radius;
        sb+="&types=" + type;
        sb+="&sensor=true";
        sb+="&key=AIzaSyBYim7RY_8oUxXfBxnJCb0jjgPCDrFr790";
        return sb;
    }

    private String convertStreamToString(InputStream in) {
        // TODO Auto-generated method stub
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        StringBuilder jsonstr=new StringBuilder();
        String line;
        try {
            while((line=br.readLine())!=null)
            {
                String t=line+"\n";
                jsonstr.append(t);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonstr.toString();
    }
}
