package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;



public class ImageViewer {
    private final Context context;
    private View view;
    private LayoutInflater inflater;
    private Bitmap originalBitmap;
    private Bitmap editedBitmap;
    private Button submitButton;
    private EditText enterMessage;
    private ImageView chosenImage;
    private int position = 0;

    public ImageViewer(final Bitmap bitmap,final Context context){
        this.context = context;
        this.originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,false);
        this.editedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.image_view,null);
        submitButton = view.findViewById(R.id.submit_button);
        enterMessage = view.findViewById(R.id.enter_message);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             final String message = enterMessage.getText().toString();
             editedBitmap = encodeMessage(getMessageBits(message));
             chosenImage.setImageBitmap(editedBitmap);
             Intent intent = new Intent(MainActivity.HIDE_MESSAGE);
             context.sendBroadcast(intent);
            }
        });
        chosenImage = view.findViewById(R.id.chosen_image);
        chosenImage.setImageBitmap(bitmap);

    }

     View getView(){
        return view;
    }

    private int[] getMessageBits(final String message){
        int[] bits = new int[message.length()*8];
        int x = 0;
        for(int i = 0; i < message.length(); i++){
            int y = message.charAt(i);
            String xString = Integer.toBinaryString(y);
            while(xString.length()!=8){
                xString = '0' + xString;
            }
            for(int j = 0; j < 8; j++){
                bits[x] = Integer.parseInt(String.valueOf(xString.charAt(j)));
                x++;
            }
        }
        return bits;
    }

    private Bitmap encodeMessage(int[] bits) {

        int bitLength = bits.length / 8;
        int[] bitMsg = new int[8];

        String bitString = Integer.toBinaryString(bitLength);

        while (bitString.length() != 8) {
            bitString = '0' + bitString;
        }

        for (int i = 0; i < 8; i++) {
            bitMsg[i] = Integer.parseInt(String.valueOf(bitString.charAt(i)));
        }

        int j = 0;
        int b = 0;
        int currentBit = 8;
        for (int x = 0; x < editedBitmap.getWidth(); x++) {
            for (int y = 0; y < editedBitmap.getHeight(); y++) {
                if (x == 0 && y < 8 ) {
                    int currentPixel = editedBitmap.getPixel(x, y);
                    int red = Color.red(currentPixel);
                    int green = Color.green(currentPixel);
                    int blue = Color.blue(currentPixel);

                    String blueString = Integer.toBinaryString(blue);
                    String blueSubString = blueString.substring(0, blueString.length() - 1);
                    blueSubString = blueSubString + bitMsg[b];

                    int temp = Integer.parseInt(blueSubString, 2);
                    int bluePixel = Integer.parseInt(blueSubString, 2);
                    int a = 255;
                    int rgb = Color.argb(a, red, green, bluePixel);

                    editedBitmap.setPixel(x, y, rgb);
                    b++;
                } else if (currentBit < bits.length + 8) {
                    int currentPixel = editedBitmap.getPixel(x, y);
                    int red = Color.red(currentPixel);
                    int green = Color.green(currentPixel);
                    int blue = Color.blue(currentPixel);

                    String blueString = Integer.toBinaryString(blue);
                    String blueSubString = blueString.substring(0, blueString.length() - 1);
                    int f = j % 8;
                    blueSubString = blueSubString + bitMsg[f];
                    j++;

                    int temp = Integer.parseInt(blueSubString, 2);
                    int bluePixel = Integer.parseInt(blueSubString, 2);
                    int a = 255;
                    int rgb = Color.argb(a, red, green, bluePixel);

                    editedBitmap.setPixel(x, y, rgb);
                    currentBit++;
                }

            }
        }
        return editedBitmap;
    }
    private String decodeImage(Bitmap encodedBitmap){
        String decodedMessage = "";
        int messageLength = 0;
        int j = 0;
        int currentBit = 0;
        String msgString = "";
        for(int x = 0; x<encodedBitmap.getWidth();x++){
            for(int y = 0; y<encodedBitmap.getHeight();y++){
                if(x==0 && y < 8){
                    int currentPixel = encodedBitmap.getPixel(x, y);
                    int blue = Color.blue(currentPixel);
                    String blueString = Integer.toBinaryString(blue);
                    msgString +=blueString.charAt(blueString.length()-1);
                    messageLength = Integer.parseInt(msgString,2);
                }
                else if( currentBit < messageLength * 8){
                    int currentPixel = encodedBitmap.getPixel(x, y);
                    int blue = Color.blue(currentPixel);
                    String blueString=Integer.toBinaryString(blue);
                    decodedMessage += blueString.charAt(blueString.length()-1);
                    currentBit++;
                }
            }
        }
        String convertedString="";
        for(int i=0;i<messageLength*8;i=i+8){

            String subString= decodedMessage.substring(i, i+8);

            int m=Integer.parseInt(subString, 2);
            char c=(char) m;
            convertedString+=c;
        }
        return convertedString;

    }

    public View getComparisonView(){
        View view = inflater.inflate(R.layout.compare_image_view,null);
        final Drawable[] images = { new BitmapDrawable(context.getResources(),originalBitmap),
                new BitmapDrawable(context.getResources(),editedBitmap)};
        final TextView imageTitle = view.findViewById(R.id.switcher_title);
        final ImageSwitcher switcher = view.findViewById(R.id.image_switcher);
        final Button nextButton = view.findViewById(R.id.next_image_btn);
        final Button decodeButton = view.findViewById(R.id.decode_message_button);

        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);

        switcher.setInAnimation(in);
        switcher.setOutAnimation(out);
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(context);
                FrameLayout.LayoutParams layoutParams =
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(layoutParams);
                imageView.setImageDrawable(images[position]);
                return imageView;
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.setImageDrawable(images[position]);
                if(position==0){
                    position = 1;
                    imageTitle.setText(R.string.edited_image);
                    nextButton.setText(R.string.see_original_image);
                }
                else{
                    position = 0;
                    imageTitle.setText(R.string.original_image);
                    nextButton.setText(R.string.see_edited_image);
                }
            }
        });
        decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.DECODE_IMAGE);
                context.sendBroadcast(intent);
            }
        });

        return view;
    }

    public View getDecodedMessageView(){
        View view = inflater.inflate(R.layout.result_view,null);
        TextView decodedTextView = view.findViewById(R.id.decoded_message_view);
        String decodedMessage = decodeImage(editedBitmap);
        decodedTextView.setText(decodedMessage);
        return view;
    }
}
