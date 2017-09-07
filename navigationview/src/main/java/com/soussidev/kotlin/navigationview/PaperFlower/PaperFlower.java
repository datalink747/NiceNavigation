package com.soussidev.kotlin.navigationview.PaperFlower;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Soussi on 06/09/2017.
 */

public class PaperFlower extends View {

    int[] colors = {0xFFDF4288, 0xFFCD8BF8, 0XFF2B9DF2, 0XFFA4EEB4, 0XFFE097CA, 0XFFCAACC6, 0XFFC5A5FC, 0XFFF5BC16, 0XFFF2DFC8, 0XFFE1BE8E, 0XFFC8C79D, 0XFFC0C11D};
    List<Dot> dotList = new ArrayList<>();
    private long ANIMATE_DURATION = 1000;
    private float MAX_RADIUS = 150;
    private float MAX_CIRCLE_RADIUS = 100;
    private float progress;
    private Paint circlePaint;
    private float RING_WIDTH = 15;
    private float P1 = 0.20f;
    private float P2 = 0.28f;
    private float P3 = 0.30f;
    private int DOT_NUMBER = 20;
    private float DOT_BIG_RADIUS = 10;
    private float DOT_SMALL_RADIUS = 7;
    private int[] mExpandInset = new int[2];
    private PaperFlowerListener mListener;
    private int centerY;
    private int centerX;


    public PaperFlower(Context context) {
        super(context);
        init(null, 0);
    }

    public PaperFlower(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PaperFlower(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PaperFlower(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    public static PaperFlower attach2Window(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        PaperFlower smallBang = new PaperFlower(activity);
        rootView.addView(smallBang, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return smallBang;
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.BLACK);
    }

    /**
     * listener for animation change time
     *
     * @param listener
     */
    public void setmListener(PaperFlowerListener listener) {
        this.mListener = listener;
    }

    /**
     * set different colors for last dots
     *
     * @param newColors
     */
    public void setColors(int[] newColors) {
        this.colors = Arrays.copyOf(newColors, newColors.length);
    }

    /**
     * set small dot number
     * @param dotNumber
     */
    public void setDotNumber(int dotNumber){
        DOT_NUMBER = dotNumber;
    }

    public void paperflower(final View view, PaperFlowerListener listener) {
        paperflower(view, -1, listener);
    }

    public void paperflower(final View view, float radius, PaperFlowerListener listener) {

        // set listener
        if (listener != null) {
            setmListener(listener);
            this.mListener.onAnimationStart();
        }

        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        int[] location = new int[2];
        getLocationOnScreen(location);
        r.offset(-location[0], -location[1]);
        r.inset(-mExpandInset[0], -mExpandInset[1]);

        centerX = r.left + r.width() / 2;
        centerY = r.top + r.height() / 2;

        if (radius != -1) {
            initRadius(radius);
        } else {
            initRadius(Math.max(r.width(),r.height()));
        }

        view.setScaleX(0.1f);
        view.setScaleY(0.1f);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration((long) (ANIMATE_DURATION * 0.5f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                view.setScaleX(0.1f + animatedFraction * 0.9f);
                view.setScaleY(0.1f + animatedFraction * 0.9f);
            }
        });
        animator.setInterpolator(new OvershootInterpolator(2));
        animator.setStartDelay((long) (ANIMATE_DURATION * P3));

        animator.start();
        paperflower();

    }

    private void initRadius(float max_circle_radius) {
        MAX_CIRCLE_RADIUS = max_circle_radius;
        MAX_RADIUS = MAX_CIRCLE_RADIUS * 1.4f;
        DOT_BIG_RADIUS = MAX_CIRCLE_RADIUS * 0.1f;
        DOT_SMALL_RADIUS = DOT_BIG_RADIUS * 0.1f;
    }

    public void paperflower(final View view) {
        paperflower(view, null);
    }

    private void paperflower() {
        ValueAnimator valueAnimator = new ValueAnimator().ofFloat(0, 1).setDuration(ANIMATE_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimationEnd();
                }
            }
        });
        initDots();
    }

    private void initDots() {

        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < DOT_NUMBER * 2; i++) {
            Dot dot = new Dot();
            dot.startColor = colors[random.nextInt(99999) % colors.length];
            dot.endColor = colors[random.nextInt(99999) % colors.length];
            dotList.add(dot);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {



        if (progress >= 0 && progress <= P1) {
            float progress1 = 1f / P1 * progress;
            if (progress1 > 1) progress1 = 1;
            int startColor = colors[0];
            int endColor = colors[1];
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setColor(evaluateColor(startColor, endColor, progress1));
            canvas.drawCircle(centerX, centerY, MAX_CIRCLE_RADIUS * progress1, circlePaint);
        } else if (progress > P1) {

            if (progress > P1 && progress <= P3) {
                float progress2 = (progress - P1) / (P3 - P1);
                if (progress2 < 0) progress2 = 0;
                if (progress2 > 1) progress2 = 1;


                circlePaint.setStyle(Paint.Style.STROKE);
                float strokeWidth = (MAX_CIRCLE_RADIUS) * (1 - progress2);
                circlePaint.setStrokeWidth(strokeWidth);

                canvas.drawCircle(centerX, centerY, (MAX_CIRCLE_RADIUS) * progress2 + strokeWidth / 2, circlePaint);
            }
            if (progress >= P2) {
                circlePaint.setStyle(Paint.Style.FILL);
                float progress3 = (progress - P2) / (1 - P2);
                float r = MAX_CIRCLE_RADIUS + progress3 * (MAX_RADIUS - MAX_CIRCLE_RADIUS);


                for (int i = 0; i < dotList.size(); i += 2) {
                    Dot dot = dotList.get(i);
                    circlePaint.setColor(evaluateColor(dot.startColor, dot.endColor, progress3));

                    float x = (float) (r * Math.cos(i * 2 * Math.PI / DOT_NUMBER)) + centerX;
                    float y = (float) (r * Math.sin(i * 2 * Math.PI / DOT_NUMBER)) + centerY;
                    canvas.drawCircle(x, y, DOT_BIG_RADIUS * (1 - progress3), circlePaint);

                    Dot dot2 = dotList.get(i + 1);

                    circlePaint.setColor(evaluateColor(dot2.startColor, dot2.endColor, progress3));
                    float x2 = (float) (r * Math.cos(i * 2 * Math.PI / DOT_NUMBER + 0.2)) + centerX;
                    float y2 = (float) (r * Math.sin(i * 2 * Math.PI / DOT_NUMBER + 0.2)) + centerY;
                    canvas.drawCircle(x2, y2, DOT_SMALL_RADIUS * (1 - progress3), circlePaint);

                }
            }
        }
    }

    private int evaluateColor(int startValue, int endValue, float fraction) {
        if (fraction <= 0) {
            return startValue;
        }
        if (fraction >= 1) {
            return endValue;
        }
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) | ((startG + (int) (fraction * (endG - startG))) << 8) | ((startB + (int) (fraction * (endB - startB))));
    }

    class Dot {
        int startColor;
        int endColor;
    }
}
