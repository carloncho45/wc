package com.waytruck.cargo.slider.Transformations;

import android.view.View;

import com.waytruck.cargo.slider.SliderPager;

public class CubeOutDepthTransformation implements SliderPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {

        if (position < -1){    // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        }
        else if (position <= 0) {    // [-1,0]
            page.setAlpha(1);
            page.setPivotX(page.getWidth());
            page.setRotationY(-90 * Math.abs(position));

        }
        else if (position <= 1){    // (0,1]
            page.setAlpha(1);
            page.setPivotX(0);
            page.setRotationY(90 * Math.abs(position));

        }
        else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);

        }



        if (Math.abs(position) <= 0.5){
            page.setScaleY(Math.max(0.4f,1-Math.abs(position)));
        }
        else if (Math.abs(position) <= 1){
            page.setScaleY(Math.max(0.4f,1-Math.abs(position)));
        }


    }
}