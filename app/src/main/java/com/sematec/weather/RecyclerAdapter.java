package com.sematec.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {
    ArrayList<String> menuLst;

    public interface OnItemClickListener {
        void onItemClick(String item);
    }
    private final OnItemClickListener listener;


    RecyclerAdapter(ArrayList<String> lst,OnItemClickListener clickListener ){
        menuLst=lst;
        listener=clickListener;
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

        holder.bind(name,listener);


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

        public void bind(final String item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
