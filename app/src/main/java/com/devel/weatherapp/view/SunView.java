package com.devel.weatherapp.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.devel.weatherapp.R;


public class SunView extends View {

    Paint mPathPaint;
    private int mWidth;
    private int mHeight;
    int mainColor;
    int trackColor;
    private Path mPathPath;
    private Paint mMotionPaint;
    private Path mMotionPath;
    int controlX, controlY;
    float startX, startY;
    float endX, endY;
    private double rX;
    private double rY;
    private int[] mSunrise = new int[2];
    private int[] mSunset = new int[2];
    private Paint mSunPaint;
    private ValueAnimator valueAnimator;
    private float mProgress;
    private Paint mShadePaint;
    private Shader mPathShader;
    private float mCurrentProgress;
    private boolean isDraw = false;
    private DashPathEffect mDashPathEffect;
    private Paint mTextPaint;
    private LinearGradient mBackgroundShader;
    private int sunColor;
    private Paint mSunStrokePaint;
    private float svSunSize;
    private float svTextSize;
    private float textOffset;
    private float svPadding;
    private float svTrackWidth;

    public SunView(Context context) {
        super(context);
        init(null);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        //Initialize properties
        final Context context = getContext();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SunView);
        // In this place, if the xml attribute does not give a value, you cannot get the default value
        mainColor = array.getColor(R.styleable.SunView_svMainColor, 0x67B2FD);
        trackColor = array.getColor(R.styleable.SunView_svTrackColor, 0x67B2FD);
        sunColor = array.getColor(R.styleable.SunView_svSunColor, 0x00D3FE);
        svSunSize = array.getDimension(R.styleable.SunView_svSunRadius, 10);
        svTextSize = array.getDimension(R.styleable.SunView_svTextSize, 18);
        textOffset = array.getDimension(R.styleable.SunView_svTextOffset, 10);
        svPadding = array.getDimension(R.styleable.SunView_svPadding, 10);
        svTrackWidth = array.getDimension(R.styleable.SunView_svTrackWidth, 3);
        array.recycle();

        // Brush with gradient path
        Paint pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(mainColor);
        pathPaint.setStyle(Paint.Style.FILL);
        mPathPaint = pathPaint;
        // gradient path
        mPathPath = new Path();
        // Brush with gradient mask
        Paint shadePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadePaint.setColor(Color.parseColor("#00000000"));
        shadePaint.setStyle(Paint.Style.FILL);
        mShadePaint = shadePaint;
        // Motion track brush
        Paint motionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        motionPaint.setColor(trackColor);
        motionPaint.setStrokeCap(Paint.Cap.ROUND);
        motionPaint.setStrokeWidth(svTrackWidth);
        motionPaint.setStyle(Paint.Style.STROKE);
        mMotionPaint = motionPaint;
        // Motion track
        mMotionPath = new Path();
        // sun brush
        Paint sunPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sunPaint.setColor(sunColor);
        sunPaint.setStyle(Paint.Style.FILL);
        mSunPaint = sunPaint;
        // sun border brush
        Paint sunStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sunStrokePaint.setColor(Color.WHITE);
        sunStrokePaint.setStyle(Paint.Style.FILL);
        mSunStrokePaint = sunStrokePaint;
        // Sunrise and sunset time brush
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(trackColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(svTextSize);
        mTextPaint = textPaint;
        mDashPathEffect = new DashPathEffect(new float[]{6, 12}, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if(!isDraw){
            mWidth = getWidth();
            mHeight = getHeight();
            controlX = mWidth/2;
            controlY = 0-mHeight/2;
            startX = svPadding;
            startY = mHeight-svPadding;
            endX = mWidth-svPadding;
            endY = mHeight-svPadding;
            rX = svPadding;
            rY = mHeight-svPadding;
            // gradient path
            mPathShader = new LinearGradient(mWidth/2, svPadding, mWidth/2, endY,
                    mainColor, Color.WHITE, Shader.TileMode.CLAMP);
            mPathPaint.setShader(mPathShader);
            mPathPath.moveTo(startX, startY);
            mPathPath.quadTo(controlX, controlY, endX, endY);
            // Motion track
            mMotionPath.moveTo(startX, startY);
            mMotionPath.quadTo(controlX, controlY, endX, endY);
            isDraw = true;
        }

        // Draw according to the occlusion relationship
        // draw gradient
        canvas.drawPath(mPathPath, mPathPaint);
        // Draw a track that has moved in the past
        mMotionPaint.setStyle(Paint.Style.STROKE);
        mMotionPaint.setPathEffect(null);
        canvas.drawPath(mMotionPath, mMotionPaint);
        // Draw a rectangle to cover the gradient and track that have not been moved
        mShadePaint.setShader(mBackgroundShader);
        canvas.drawRect((float) rX, 0, mWidth, mHeight, mShadePaint);
        // Draw a dotted line to indicate the unmoved track
        mMotionPaint.setPathEffect(mDashPathEffect);
        canvas.drawPath(mMotionPath, mMotionPaint);

        // Draw sunrise and sunset text
        if (mSunrise.length != 0||mSunset.length != 0){
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Sunrise"+(mSunrise[0]<10? "0"+mSunrise[0]: mSunrise[0])
                    +":"+(mSunrise[1]<10? "0"+mSunrise[1]: mSunrise[1]), startX+textOffset, startY, mTextPaint);
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("Sunset"+(mSunset[0]<10? "0"+mSunset[0]: mSunset[0])
                    +":"+(mSunset[1]<10? "0"+mSunset[1]: mSunset[1]), endX-textOffset, endY, mTextPaint);
        }

        // draw endpoint
        mMotionPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(startX, startY, svTrackWidth*2, mMotionPaint);
        canvas.drawCircle(endX, endY, svTrackWidth*2, mMotionPaint);
        // draw the sun
        canvas.drawCircle((float) rX, (float)rY, svSunSize*6/5, mSunStrokePaint);
        canvas.drawCircle((float) rX, (float)rY, svSunSize, mSunPaint);

        canvas.restore();
    }

    /**
     * Set the current progress and update the position of the sun center
     * @param t range: [0~1]
     */
    private void setProgress(float t){
        mProgress = t;
        rX = startX * Math.pow(1 - t, 2) + 2 * controlX * t * (1 - t) + endX * Math.pow(t, 2);
        rY = startY * Math.pow(1 - t, 2) + 2 * controlY * t * (1 - t) + endY * Math.pow(t, 2);
        // Only update the area to be painted
        invalidate((int)rX, 0, (int)(mWidth-svPadding), (int)(mHeight-svPadding));
    }

    /**
     * Set the current time (please set the sunrise and sunset time first)
     */
    public void setCurrentTime(int hour, int minute){
        if (mSunrise.length != 0||mSunset.length != 0){
            float p0 = mSunrise[0]*60+mSunrise[1];//Starting minutes
            float p1 = hour*60+minute-p0;//Total minutes in current time
            float p2 = mSunset[0]*60+mSunset[1]-p0;//Total number of minutes from sunset to sunrise
            float progress = p1/p2;
            mProgress = progress;
            motionAnimation();
        }
    }

    /**
     * Set sunrise time
     */
    public void setSunrise(int hour, int minute){
        mSunrise[0] = hour;
        mSunrise[1] = minute;
    }

    /**
     * Set sunset time
     */
    public void setSunset(int hour, int minute){
        mSunset[0] = hour;
        mSunset[1] = minute;
    }

    /**
     * Sun track animation
     */
    public void motionAnimation(){
        if (valueAnimator == null){
            mCurrentProgress = 0f;
            // Make sure the sun will not go out of bounds
            if (mProgress<0){
                mProgress=0;
            }
            if (mProgress>1){
                mProgress=1;
            }
            final ValueAnimator animator = ValueAnimator.ofFloat(mCurrentProgress, mProgress);
            animator.setDuration((long) (2500*(mProgress-mCurrentProgress)));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Object val = animator.getAnimatedValue();
                    if (val instanceof Float){
                        setProgress((Float) val);
                    }
                }
            });
            valueAnimator = animator;
        } else {
            valueAnimator.cancel();
            valueAnimator.setFloatValues(mCurrentProgress, mProgress);
        }
        valueAnimator.start();
        // Save the current progress, the next call to setCurrentTime() can move from the last progress to the current progress (Xiaomi effect)
        mCurrentProgress = mProgress;
    }
}