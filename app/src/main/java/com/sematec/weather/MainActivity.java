package com.sematec.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sematec.weather.forecast.WeatherClass;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnSearch=findViewById(R.id.btnSearch);
        final DrawerLayout myDrawer=findViewById(R.id.drawer);
        final DatabaseHandler dbHandler=new DatabaseHandler(MainActivity.this,"WeatherDB",null,1);
        final RecyclerView recycler=findViewById(R.id.recycler);
        final EditText edtCity=findViewById(R.id.edtSearch);

        //dbHandler.ClearCityHistory();

        ArrayList lstCity= new ArrayList<String>();
        lstCity=dbHandler.GetCityList();
        Log.d("DBtest","Get list Completed");
        int size=lstCity.size();
        Log.d("DBtest","list size is "+size);


        RecyclerAdapter adapter=new RecyclerAdapter(lstCity);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String city= edtCity.getText().toString();
                Log.d("DBtest",city);

                boolean f=dbHandler.isExistCity(city);
                if(f){
                    boolean d=dbHandler.updateCity(city);
                    if(d){
                        Log.d("DBtest",city+" is updated");
                    }
                }
                else {

                    boolean flag = dbHandler.insertCity(city);
                    if (flag) {
                        Log.d("DBtest", city + " is added");
                    }
                }

                ArrayList lstCity= new ArrayList<String>();
                lstCity=dbHandler.GetCityList();

                RecyclerAdapter adapter=new RecyclerAdapter(lstCity);
                recycler.setAdapter(adapter);
                recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));

               // ArrayList<myWeatherClass> lstWeather=new ArrayList<myWeatherClass>();
                //lstWeather= FillWeatherData();

               // Log.d("jsontest","weather list size is "+ lstWeather.size());

                FillWeatherData();




                //TextView txtTodayTemp=findViewById(R.id.txtTodayTemp);
                //txtTodayTemp.setText(convertNumber(lstWeather.get(0).getTemp()));

                //ImageView imgTodayStatus=findViewById(R.id.imgTodayStatus);
               // Picasso.get().load("http://openweathermap.org/img/wn/" + lstWeather.get(0).getIcon() + "@2x.png").into(imgTodayStatus);




                myDrawer.closeDrawer(GravityCompat.END);




                /*int seq=dbHandler.getSequence("Shiraz");
                Log.d("DBtest","Shiraz sequence is "+seq);*/





            }
        });


    }

    public void FillWeatherData()
    {
        Log.d("jsontest","We are in FillWeatherData method");
        final ArrayList<myWeatherClass> lstWeather=new ArrayList<myWeatherClass>();

        EditText edtCity=findViewById(R.id.edtSearch);
        AsyncHttpClient client = new AsyncHttpClient();
        //String strCity=edtCity.getText().toString();
        String url="http://api.openweathermap.org/data/2.5/forecast?lang=fa&units=metric&q=Tehran&APPID=cb86e30c50a504dc52083199743e8646";

        try {


            client.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("jsontest","We are in onSuccess method");

//                    Gson gson = new Gson();
//                    WeatherClass weatherClass = gson.fromJson(response.toString(), WeatherClass.class);

                    Log.d("jsontest",response.toString());

                    //index: in Json there is a forecast for  every 3 hour
                    //today
                   lstWeather.add(getDataFromJson(0,response.toString(),0));
                    Log.d("jsontest","list size is "+ lstWeather.size());
                    //tomorrow
                   lstWeather.add(getDataFromJson(8,response.toString(),1));
                    Log.d("jsontest","list size is "+ lstWeather.size());
                    //day2
                   lstWeather.add(getDataFromJson(16,response.toString(),2));
                    Log.d("jsontest","list size is "+ lstWeather.size());
                   //day3
                   lstWeather.add(getDataFromJson(24,response.toString(),3));
                   //day4
                   lstWeather.add(getDataFromJson(32,response.toString(),4));
                   //day5
                   lstWeather.add(getDataFromJson(39,response.toString(),5));
                    Log.d("jsontest","list size is "+ lstWeather.size());

                    TextView txtTodayTemp=findViewById(R.id.txtTodayTemp);
                    txtTodayTemp.setText(convertNumber(lstWeather.get(0).getTemp()));

                    ImageView imgTodayStatus=findViewById(R.id.imgTodayStatus);
                    String url="http://openweathermap.org/img/wn/" + lstWeather.get(0).getIcon() + "@2x.png";
                    Log.d("jsontest",url);
                     Picasso.get().load(url).into(imgTodayStatus);

                    TextView txtTodayStatus=findViewById(R.id.txtTodayStatus);
                    txtTodayStatus.setText(lstWeather.get(0).getDescription());

                    RecyclerView forecastRecycler=findViewById(R.id.recyclerForecast);
                    forecastRecyclerAdapter forecastAdapter=new forecastRecyclerAdapter(lstWeather);
                    forecastRecycler.setAdapter(forecastAdapter);
                    forecastRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));




                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("jsontest", "FillWeather Data onFailure is called");
                    Log.d("jsontest", throwable.getMessage());
                }
            });

        }
        catch (Exception e){
            Log.d("jsontest","We are in catch");
            Log.d("jsontest", e.getMessage());

        }

        //return lstWeather;
    }

    //
    public myWeatherClass getDataFromJson(int index,String response, int dayFromToday)
    {
        myWeatherClass myWeather=new myWeatherClass();
        Log.d("jsontest",GetIntPart("Item"+index+" is started"));

        //Today
        JSONObject obj= null;
        try {
            obj = new JSONObject(response);

        String list=obj.getString("list");
        //Log.d("jsontest",list.toString());

        JSONArray list2=new JSONArray(list);
        JSONObject item0=list2.getJSONObject(index);
        String item0_main=item0.getString("main");

        JSONObject item0_mainObj=new JSONObject(item0_main);
        String item0_temp=item0_mainObj.getString("temp");// tempreture
        //Double.parseDouble(item0_main)
        myWeather.setTemp(GetIntPart(item0_temp));

        String item0_mintemp=item0_mainObj.getString("temp_min");// min tempreture
        myWeather.setTempMin(GetIntPart(item0_mintemp));

        String item0_maxtemp=item0_mainObj.getString("temp_max");// max tempreture
        //Log.d("jsontest",GetIntPart(item0_maxtemp));
        myWeather.setTempMax(GetIntPart(item0_maxtemp));

        String item0_humidity=item0_mainObj.getString("humidity"); // humidity
        myWeather.setHumidity(GetIntPart(item0_humidity));

        String weather0=item0.getString("weather");
        //Log.d("jsontest",weather0);
        JSONArray weather0Item=new JSONArray(weather0);
        JSONObject todayWeatherobj=weather0Item.getJSONObject(0);
        String todayWeather=todayWeatherobj.getString("description");// weather description
        myWeather.setDescription(todayWeather);

        String item0_icon=todayWeatherobj.getString("icon");// weather icon
        //Log.d("jsontest",item0_icon);
        myWeather.setIcon(item0_icon);

        String item0_wind=item0.getString("wind");
        JSONObject item0_windSpeed=new JSONObject(item0_wind);
        String item0_speed=item0_windSpeed.getString("speed");// wind speed
        myWeather.setwindSpeed(item0_speed);

        } catch (JSONException e) {
            Log.d("jsontest","Error in fetch info of Item"+index+" from JSON");
            e.printStackTrace();
        }
        myWeather.setDay(getDayofWeek(dayFromToday));
        return myWeather;
    }

    //convert English num to Farsi
    public String convertNumber(String faNumbers) {
        String[][] mChars = new String[][]{
                {"0", "۰"},
                {"1", "۱"},
                {"2", "۲"},
                {"3", "۳"},
                {"4", "۴"},
                {"5", "۵"},
                {"6", "۶"},
                {"7", "۷"},
                {"8", "۸"},
                {"9", "۹"}
        };

        for (String[] num : mChars) {

            faNumbers = faNumbers.replace(num[0], num[1]);

        }

        return faNumbers;
    }

    //
    public String GetIntPart(String str){
        String[] fn = str.split("\\.");
        return fn[0];
    }
    //
    public String getDayofWeek(int numberOfDayFromToday)
    {
        //Fetch base on time in day
        Calendar calendar=Calendar.getInstance();
        int today=calendar.get(Calendar.DAY_OF_WEEK);
        int day=(today+numberOfDayFromToday)%7;
        switch (day) {
            case 0:
                return "شنبه";
            case Calendar.SUNDAY:
                return "یکشنبه";
            case Calendar.MONDAY:
                return "دوشنبه";
            case Calendar.TUESDAY:
                return "سه شنبه";
            case Calendar.WEDNESDAY:
                return "چهارشنبه";
            case Calendar.THURSDAY:
                return "پنجشنبه";
            default:
                return "جمعه";

        }

    }


}
