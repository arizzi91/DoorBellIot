package com.example.angelo.doorbelliot;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.angelo.servicemqtt.Connection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 *Adapter for RecyclerView in the CronologiaFragment
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    /**
     * Debug Tag for use logging debug output to LogCat
     */
    private static final String TAG="MyAdapter";
    /**
     * ArrayList which contains values of link at the images
     */
    private ArrayList<String> myCrono;


    /**
     *
     * @param images
     */
    public MyAdapter(ArrayList<String> images) {

        myCrono=images;
        Log.d(TAG,String.valueOf(myCrono.size()));

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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Context context= holder.mImageView.getContext();
        holder.mTextView.setText(getTitle(myCrono.get(position)));

        /**
         *Load image from link using Picasso library
         */
        Picasso.with(context).load(Uri.parse(myCrono.get(position))).into(holder.mImageView);
        Log.d(TAG,"foto settata");

        final int pos=position;
        /**
         * Zoom the image
         */
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


    /**
     * Insert link into ArrayList and notify adapter
     * @param data
     */
    public void add(String  data){
        myCrono.add(data);
        this.notifyDataSetChanged();


    }

    /**
     * Extract title from the link
     * @param data
     * @return title of the image
     */
    public String getTitle(String data){
        String title = data.substring(data.lastIndexOf("/")+1);
        return title;
    }

    /**
     * Get size of ArrayList
     * @return size of ArrayList
     */
    @Override
    public int getItemCount() {
        return myCrono.size();
    }




}
