package com.waytruck.cargo.slider.IndicatorView.animation;

import androidx.annotation.NonNull;

import com.waytruck.cargo.slider.IndicatorView.animation.controller.AnimationController;
import com.waytruck.cargo.slider.IndicatorView.animation.controller.ValueController;
import com.waytruck.cargo.slider.IndicatorView.draw.data.Indicator;


public class AnimationManager {

    private AnimationController animationController;

    public AnimationManager(@NonNull Indicator indicator, @NonNull ValueController.UpdateListener listener) {
        this.animationController = new AnimationController(indicator, listener);
    }

    public void basic() {
        if (animationController != null) {
            animationController.end();
            animationController.basic();
        }
    }

    public void interactive(float progress) {
        if (animationController != null) {
            animationController.interactive(progress);
        }
    }

    public void end() {
        if (animationController != null) {
            animationController.end();
        }
    }
}
