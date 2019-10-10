package com.sematec.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {
    ArrayList<String> menuLst;
    RecyclerAdapter(ArrayList<String> lst){
        menuLst=lst;

    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        Holder holder=new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String name= menuLst.get(position);
        holder.txtCity.setText(name);
    }

    @Override
    public int getItemCount() {
        return menuLst.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView txtCity;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtCity=itemView.findViewById(R.id.txtRecyclerItem);
        }
    }
}
