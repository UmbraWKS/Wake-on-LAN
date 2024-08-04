package com.wakeonlan.app.utilities;

import android.view.MotionEvent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

public class GuidelineResizing {
    private static float initialTouchY;
    private static float initialGuidePercent;
    private static final float RESIZESENSITIVITY = 0.6f; // sensitivity of the resizable bar
    // more than .5 causes excessive ghosting

    public static boolean handleTouch(MotionEvent motionEvent, Guideline guideline, ConstraintLayout layout) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: // using ACTION_DOWN to record the initial data
                // recording initial touch position and current guideline percentage
                initialTouchY = motionEvent.getY(); // initial touch position
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
                initialGuidePercent = params.guidePercent; // initial guideline percentage
                return true;

            case MotionEvent.ACTION_MOVE: // using ACTION_MOVE to move the view
                // calculating how much the user has moved since the initial touch
                float newPercent = getNewPercent(motionEvent, layout);

                // ensure the percentage is within bounds
                // otherwise it goes out of the screen
                if (newPercent >= 0 && newPercent <= 1) {
                    // applying to the constraintLayout
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
                    layoutParams.guidePercent = newPercent;
                    guideline.setLayoutParams(layoutParams);
                    guideline.requestLayout(); // updating the layout
                }

                return true;

            default:
                return false;
        }
    }

    // the new percentage of the guideline
    private static float getNewPercent(MotionEvent motionEvent, ConstraintLayout layout) {
        float currentTouchY = motionEvent.getY(); // current vertical touch position
        float deltaY = currentTouchY - initialTouchY; // by scrolling up the view goes up
        int parentHeight = layout.getHeight(); // height of the parent layout
        float deltaPercent = (deltaY / parentHeight) * RESIZESENSITIVITY; // delta percent

        // calculating new guideline percentage
        return initialGuidePercent + deltaPercent;
    }
}
