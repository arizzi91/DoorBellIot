package com.example.angelo.doorbelliot;

import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by angelo on 09/05/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private static final String TAG="MyAdapter";
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

        final Context context= holder.mImageView.getContext();
        holder.mTextView.setText(getTitle(myCrono.get(position)));
        Picasso.with(context).load(Uri.parse(myCrono.get(position))).into(holder.mImageView);
        final int pos=position;

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                final Dialog dialog = new Dialog(context, R.style.FullScreenDialogTheme);
                dialog.setContentView(R.layout.dialog);
                ImageView im = (ImageView) dialog.findViewById(R.id.imageView);
                Picasso.with(context).load(Uri.parse(myCrono.get(pos))).into(im);

                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            }
        });

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
