package com.example.engahmed.imageview_animation;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {


    ImageSwitcher imageswitcher;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageswitcher=(ImageSwitcher)findViewById(R.id.imageSwitcher);
        imageswitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));

                return imageView;
            }
        });
        butnnext();
        butonprovious();
        Animation in= AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out= AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        imageswitcher.setInAnimation(in);
        imageswitcher.setOutAnimation(out);
    }


    public void butnnext(){

        Button buttonnext=(Button)findViewById(R.id.button2);

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageswitcher.setImageResource(R.drawable.mido);
                imageswitcher.setImageResource(R.drawable.mido3);

            }
        });

    }

    public void butonprovious(){

        Button buttonprovious=(Button)findViewById(R.id.button);
        buttonprovious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageswitcher.setImageResource(R.drawable.xx);
            }
        });


    }



}
