package com.sematec.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class forecastRecyclerAdapter extends RecyclerView.Adapter<forecastRecyclerAdapter.forecastHolder> {

    ArrayList<myWeatherClass> forecastLst;
    forecastRecyclerAdapter(ArrayList<myWeatherClass> lst){
        forecastLst=lst;

    }

    @NonNull
    @Override
    public forecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_recycler_item,parent,false);
        forecastRecyclerAdapter.forecastHolder holder=new forecastHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull forecastHolder holder, int position) {
        myWeatherClass forecast= forecastLst.get(position+1);
        holder.txtDay.setText(forecast.getDay());
        holder.txtMinTemp.setText(convertNumber(forecast.getTempMin()));
        holder.txtMaxTemp.setText(convertNumber(forecast.getTempMax()));

    }

    @Override
    public int getItemCount() {
        return (forecastLst.size()-1);
    }

    class forecastHolder extends RecyclerView.ViewHolder{
        TextView txtDay;
        TextView txtMinTemp;
        TextView txtMaxTemp;
        ImageView imgStatus;

        public forecastHolder(@NonNull View itemView) {
            super(itemView);
            txtDay=itemView.findViewById(R.id.txtday);
            txtMinTemp=itemView.findViewById(R.id.txtmin);
            txtMaxTemp=itemView.findViewById(R.id.txtmax);
            imgStatus=itemView.findViewById(R.id.imgStatus);
        }
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

}
