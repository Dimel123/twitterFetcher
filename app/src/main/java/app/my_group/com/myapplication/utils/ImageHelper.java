package app.my_group.com.myapplication.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.my_group.com.MyApplication;

/**
 * Created by MyUser on 19.03.2018.
 */

public class ImageHelper {

    public static void loadAvatar(String url, ImageView imageView){
        Picasso.with(MyApplication.getAppComponent().getApp()).load(url).into(imageView);
    }

}
