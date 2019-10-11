package com.sematec.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        final RecyclerView recycler=findViewById(R.id.recycler);
        final EditText edtCity=findViewById(R.id.edtSearch);
        final TextView txtError=findViewById(R.id.txtError);
        final DatabaseHandler dbHandler=new DatabaseHandler(MainActivity.this,"WeatherDB",null,1);
        final Button btnMenu=findViewById(R.id.btnMenu);

        //dbHandler.ClearCityHistory();


        txtError.setText("");

        ArrayList lstCity= new ArrayList<String>();
        lstCity=dbHandler.GetCityList();

        RecyclerAdapter adapter=new RecyclerAdapter(lstCity);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));

        FillWeatherData("تهران");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myDrawer.openDrawer(GravityCompat.END);
                }
                catch (Exception e){
                   Log.d("myweather",e.getMessage()) ;
                }


            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String city= edtCity.getText().toString().trim();

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


                txtError.setText("");
                if (city==""){
                    txtError.setText("نام شهر وارد نشده است");
                }
                else
                {
                    FillWeatherData(city);
                    myDrawer.closeDrawer(GravityCompat.END);
                }


            }
        });


    }

    public void FillWeatherData(final String cityName)
    {

        Log.d("jsontest","We are in FillWeatherData method");
        final ArrayList<myWeatherClass> lstWeather=new ArrayList<myWeatherClass>();
        final TextView txtCity=findViewById(R.id.txtLocation);

        if(isInternetAvailable()) {


            AsyncHttpClient client = new AsyncHttpClient();

            String url = "http://api.openweathermap.org/data/2.5/forecast?lang=fa&units=metric&q=" + cityName + "&APPID=cb86e30c50a504dc52083199743e8646";

            try {

                client.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        txtCity.setText(cityName);
                        Log.d("jsontest", "We are in onSuccess method");

//                    Gson gson = new Gson();
//                    WeatherClass weatherClass = gson.fromJson(response.toString(), WeatherClass.class);

                        Log.d("jsontest", response.toString());

                        //index: in Json there is a forecast for  every 3 hour
                        //today
                        lstWeather.add(getDataFromJson(0, response.toString(), 0));
                        //tomorrow
                        lstWeather.add(getDataFromJson(8, response.toString(), 1));
                        //day2
                        lstWeather.add(getDataFromJson(16, response.toString(), 2));
                        //day3
                        lstWeather.add(getDataFromJson(24, response.toString(), 3));
                        //day4
                        lstWeather.add(getDataFromJson(32, response.toString(), 4));
                        //day5
                        lstWeather.add(getDataFromJson(39, response.toString(), 5));


                        TextView txtTodayTemp = findViewById(R.id.txtTodayTemp);
                        txtTodayTemp.setText(convertNumber(lstWeather.get(0).getTemp())+"º");


                        TextView txtTodayStatus = findViewById(R.id.txtTodayStatus);
                        txtTodayStatus.setText(lstWeather.get(0).getDescription());

                        RecyclerView forecastRecycler = findViewById(R.id.recyclerForecast);
                        forecastRecyclerAdapter forecastAdapter = new forecastRecyclerAdapter(lstWeather);
                        forecastRecycler.setAdapter(forecastAdapter);
                        forecastRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));

                        LinearLayout layout=findViewById(R.id.layoutTop);

                        Log.d("myweather","set weather picture");
                        String pic=lstWeather.get(0).getIcon();
                        Log.d("myweather",pic);
                        if((pic.equals("01d"))||(pic.equals("01n")))
                        {
                            Log.d("myweather","clear");
                            layout.setBackgroundResource(R.drawable.t01d);
                        }else if((pic.equals("02d"))||(pic.equals("03d"))||(pic.equals("04d"))||(pic.equals("02d"))||(pic.equals("03d"))||(pic.equals("04d")))
                        {
                            Log.d("myweather","cloudy");
                            layout.setBackgroundResource(R.drawable.cloudy);
                        }else if((pic.equals("09d"))||(pic.equals("10d"))||(pic.equals("09n"))||(pic.equals("10n")))
                        {
                            Log.d("myweather","rainy");
                            layout.setBackgroundResource(R.drawable.rainy);
                        } else if((pic.equals("13d"))||(pic.equals("13n"))){
                            Log.d("myweather","snowy");
                            layout.setBackgroundResource(R.drawable.snowy);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("jsontest", "FillWeather Data onFailure is called");
                        Log.d("jsontest", throwable.getMessage());

                        if (throwable.getMessage().equals("Not Found")) {
                            Log.d("jsontest", "we are in not found");
                            Toast.makeText(MainActivity.this, "این شهر وجود ندارد", Toast.LENGTH_LONG).show();
                        }


                    }
                });

            } catch (Exception e) {
                Log.d("jsontest", "We are in catch");
                Log.d("jsontest", e.getMessage());

            }
        }
        else {
            Toast.makeText(MainActivity.this, "اتصال اینترنت برقرار نیست", Toast.LENGTH_LONG).show();
        }



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
            Log.d("jsontest",GetIntPart(item0_mintemp));
        myWeather.setTempMin(GetIntPart(item0_mintemp));

        String item0_maxtemp=item0_mainObj.getString("temp_max");// max tempreture
        Log.d("jsontest",GetIntPart(item0_maxtemp));
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
    //
    private boolean isInternetAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
