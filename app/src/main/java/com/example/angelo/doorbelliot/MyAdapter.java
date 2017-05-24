package com.example.angelo.doorbelliot;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by angelo on 09/05/17.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{



    private ArrayList<String> myCrono;




    public MyAdapter(ArrayList<String> images) {

        myCrono=images;


    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.info_image);
            mTextView = (TextView)v.findViewById(R.id.title_image);

        }

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Context context= holder.mImageView.getContext();
        holder.mTextView.setText(getTitle(myCrono.get(position)));
        Picasso.with(context).load(Uri.parse(myCrono.get(position))).into(holder.mImageView);


    }

    public void add(String  data){

        myCrono.add(data);

        this.notifyDataSetChanged();


    }

    public String getTitle(String data){
        String title = data.substring(data.lastIndexOf("/")+1);
        return title;
    }

    @Override
    public int getItemCount() {
        return myCrono.size();
    }




}
