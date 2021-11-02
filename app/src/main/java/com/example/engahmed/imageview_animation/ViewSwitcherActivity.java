package com.example.engahmed.imageview_animation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ViewSwitcher;

/**
 * 参考： [aldryd/imageswitcher: Example of how to use an ViewSwitcher to switch between two ImageView objects](https://github.com/aldryd/imageswitcher)
 */
public class ViewSwitcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_switcher);


        // Let the ViewSwitcher do the animation listening for you
        ((ViewSwitcher) findViewById(R.id.switcher)).
                setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewSwitcher switcher = (ViewSwitcher) v;

//                如果当前显示的是第一个，那么点击的时候就会显示下一个
                if (switcher.getDisplayedChild() == 0) {
                    switcher.showNext();
                } else {
                    switcher.showPrevious();
                }
            }
        });
    }
}