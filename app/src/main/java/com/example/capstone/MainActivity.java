package com.example.capstone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    static final String IMAGE_RECEIVED = "CAPSTONE_IMAGE_RECEIVED";
    static final String HIDE_MESSAGE = "CAPSTONE_HIDE_MESSAGE";
    static final String DECODE_IMAGE = "CAPSTONE_DECODE_IMAGE";
    private ImageViewer imageViewer;
    private Bitmap image;

    private BroadcastReceiver dropDownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null)
                switch (action) {
                    case (IMAGE_RECEIVED):
                        imageViewer = new ImageViewer(image,context);
                        setContentView(imageViewer.getView());
                        break;
                    case (HIDE_MESSAGE):
                        setContentView(imageViewer.getComparisonView());
                        break;
                    case(DECODE_IMAGE):
                        setContentView(imageViewer.getDecodedMessageView());
                        break;
                }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chooseImageBtn = findViewById(R.id.select_image_btn);
        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(IMAGE_RECEIVED);
        filter.addAction(HIDE_MESSAGE);
        filter.addAction(DECODE_IMAGE);
        registerReceiver(dropDownReceiver,filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CAPSTONE", "request code is" + requestCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    if (data != null) {
                        try {
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            image = BitmapFactory.decodeStream(imageStream);
                            Intent intent = new Intent(IMAGE_RECEIVED);
                            sendBroadcast(intent);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

}
