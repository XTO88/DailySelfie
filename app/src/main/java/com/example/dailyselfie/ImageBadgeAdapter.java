package com.example.dailyselfie;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ImageBadgeAdapter extends BaseAdapter {

    private ArrayList<File> list = new ArrayList<File>();
    private static LayoutInflater inflater = null;
    private Context mContext;
    String photoPath;

    public ImageBadgeAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;
        ViewHolder holder;

        File curr = list.get(i);
        photoPath = curr.getAbsolutePath();

        if (null == view) {
            holder = new ViewHolder();
            newView = inflater
                    .inflate(R.layout.image_badge_view, viewGroup, false);
            holder.imageView = (ImageView) newView.findViewById(R.id.small_image);
            holder.date = (TextView) newView.findViewById(R.id.image_date);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

            holder.imageView.setImageBitmap(setPic(holder.imageView, photoPath));
            holder.date.setText(curr.getName().substring(6,25));
            holder.date.setTextColor(Color.BLACK);
            holder.date.setTextSize(22);


        return newView;
    }

    static class ViewHolder {

        ImageView imageView;
        TextView date;

    }
    private Bitmap setPic(ImageView imageView, String photoPath) {
        // Get the dimensions of the View
        int targetW = imageView.getMaxWidth();
        int targetH = imageView.getMaxHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        return bitmap;
    }

    public void add(File image) {
        list.add(image);
        notifyDataSetChanged();
    }

    public ArrayList<File> getList() {
        return list;
    }

    public void removeAllViews() {
        list.clear();
        this.notifyDataSetChanged();
    }
}
