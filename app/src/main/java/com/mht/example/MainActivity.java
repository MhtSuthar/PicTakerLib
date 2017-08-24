package com.mht.example;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mht.pic.PicTake;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView mImagePreview;
    private static final String TAG = "MainActivity";
    private PicTake mPicTake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImagePreview = (ImageView) findViewById(R.id.img_picker);
    }

    public void onPicCall(View view){
        mPicTake = new PicTake(this);
        mPicTake.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult() called with: requestCode = [" + requestCode
                + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        String mPath = mPicTake.getImagePathFromResult(this, requestCode, resultCode, data);
        Log.e(TAG, "Image path "+mPath);
        if(mPath != null && mPath != "") {
            File file = new File(mPath);
            Uri imageUri = Uri.fromFile(file);
            Log.e(TAG, "exist  " + file.exists());
            Glide.with(this)
                    .load(imageUri)
                    .into(mImagePreview);
        }
    }
}
