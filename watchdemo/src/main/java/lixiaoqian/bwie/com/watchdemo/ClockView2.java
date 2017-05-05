package lixiaoqian.bwie.com.watchdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @类的用途：
 * @author: 李晓倩
 * @date: 2017/5/5
 */

public class ClockView2 extends View {
    private int mWidth;
    private int mHeight;
    private float mRatio;

    private int mHour;
    private int mMinute;
    private int mSecond;
    private float mHourR;
    private float mMinuteR;
    private float mSecondR;
    int mDiameter;
    private Context mContext;
    private static final int H = 0;
    private static final int M = 1;
    private static final int S = 2;


    private static final int COLOR_DEFAULT = Color.WHITE;
    private static final int COLOR_BG = Color.BLACK;

    private int mBgColor;
    private int mRingColor;
    private int mCenterPointColor;
    private int mTextColor;
    private int mHourColor;
    private int mMinuteColor;
    private int mSecondColor;
    private int mLongScaleColor;
    private int mShortScaleColor;

    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    /**
     * 绘制时控制文本绘制的范围
     */


    public ClockView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initAttrs(context, attrs);
        initData(context);
        startClock();
    }

    public ClockView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView2(Context context) {
        this(context, null);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        mBgColor = ta.getColor(R.styleable.ClockView_bgColor, COLOR_BG);
        mRingColor = ta.getColor(R.styleable.ClockView_ringColor, COLOR_DEFAULT);
        mCenterPointColor = ta.getColor(R.styleable.ClockView_centerPointColor, COLOR_DEFAULT);
        mTextColor = ta.getColor(R.styleable.ClockView_textColor, COLOR_DEFAULT);
        mHourColor = ta.getColor(R.styleable.ClockView_hourColor, COLOR_DEFAULT);
        mMinuteColor = ta.getColor(R.styleable.ClockView_minuteColor, COLOR_DEFAULT);
        mSecondColor = ta.getColor(R.styleable.ClockView_secondColor, COLOR_DEFAULT);
        mLongScaleColor = ta.getColor(R.styleable.ClockView_longScaleColor, COLOR_DEFAULT);
        mShortScaleColor = ta.getColor(R.styleable.ClockView_shortScaleColor, COLOR_DEFAULT);
        ta.recycle();
    }
    private void initData(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dMetrics);
        mRatio = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dMetrics);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measuredWidth(widthMeasureSpec);
        mHeight = measuredHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        // 钟表的外圆直径（除去 padding ）
        mDiameter = Math.min(mWidth - getPaddingLeft() - getPaddingRight(), mHeight - getPaddingTop() - getPaddingBottom());
        // 时针半径外环半径的1/3
        mHourR = mDiameter/2f/3;
        // 分针半径为外环半径的1/2
        mMinuteR = mDiameter/2f/2;
        // 秒针半径为外环半径的1/1.5
        mSecondR = mDiameter/2f/1.5f;
    }
    private int measuredWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        } else {
            result = 500;
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measuredHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        } else {
            result = 500;
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBgColor);
        drawCircle(canvas);
        drawScale(canvas);
        drawIndicator(canvas);
        postInvalidateDelayed(1000);
    }

    private void setHour(int hour){
        mHour = hour;
    }
    private void setMinute(int minute){
        mMinute = minute;
    }
    private void setSecond(int second){
        mSecond = second;
    }

    private float getLeftBy(int indicator){
        float r=0f;
        float digit = 0;

        switch(indicator){
            case H:
                r = mHourR;
                // 根据分钟进行补充,每5分钟进一小格
                digit = ((mHour%12/12f*60 + mMinute/60f*5)) % 60;
                break;
            case M:
                r = mMinuteR;
                digit = mMinute;
                break;
            case S:
                r = mSecondR;
                digit = mSecond+1;
                break;
        }

        float left = (float) Math.sin(digit/60f * Math.PI*2) * r;
        if(digit<=30){
            return Math.abs(left);
        } else {
            return -Math.abs(left);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP: {
                int x = (int) event.getX();
                int y = (int) event.getY();

                if (x > mDiameter/4 && x < mDiameter/2-25 && y > mDiameter/4 && y < mDiameter/3-10) {
                    Toast.makeText(mContext, "今天是"+StringData(), Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }
        this.invalidate();
        return true;
    }


    private float getTopBy(int indicator){
        float r=0f;
        float digit = 0;

        switch(indicator){
            case H:
                r = mHourR;
                // 根据分钟进行补充,每5分钟进一小格
                digit = ((mHour%12/12f*60 + mMinute/60f*5)) % 60;
                break;
            case M:
                r = mMinuteR;
                digit = mMinute;
                break;
            case S:
                r = mSecondR;
                digit = mSecond+1;
                break;
        }

        float left = (float) Math.cos(digit/60f * Math.PI*2) * r;
        if(15<=digit && digit<=45){
            return Math.abs(left);
        } else {
            return -Math.abs(left);
        }
    }

    /**
     * 画指针：时针，分针，秒针
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {
        // 保存图层
        canvas.save();
        canvas.translate(mWidth/2f, mHeight/2f);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(getDigit(3));
        paint.setColor(mHourColor);
        canvas.drawLine(0, 0, getLeftBy(H), getTopBy(H), paint);

        paint.setStrokeWidth(getDigit(2));
        paint.setColor(mMinuteColor);
        canvas.drawLine(0, 0, getLeftBy(M), getTopBy(M), paint);

        paint.setStrokeWidth(getDigit(1));
        paint.setColor(mSecondColor);
        canvas.drawLine(0, 0, getLeftBy(S), getTopBy(S), paint);

        // 合并图层
        canvas.restore();
    }

    /**
     * 根据每个屏幕的不同像素，获取同宽度的值
     * @param data
     * @return
     */
    private float getDigit(float data){
        return data*mRatio;
    }

    /**
     * 画刻度
     * @param canvas
     */
    private void drawScale(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(getDigit(1));
        paint.setTextSize(getDigit(10));
        paint.setAntiAlias(true);
        for(int i=0; i<60; i++){

            if(i%5 == 0){
                paint.setStrokeWidth(getDigit(2));
                paint.setColor(mLongScaleColor);
                paint.setTextSize(getDigit(15));
                String timeText = "" + i/5;
                if(i==0){
                    timeText = "12";
                }
                canvas.drawLine(mWidth/2, mHeight/2 - mDiameter/2, mWidth/2, mHeight/2 - mDiameter/2 + 40, paint);
                paint.setColor(mTextColor);
                canvas.drawText(timeText, mWidth/2-paint.measureText(timeText)/2, mHeight/2-mDiameter/2+80, paint);
            } else {
                paint.setStrokeWidth(getDigit(1));
                paint.setTextSize(getDigit(15));
                paint.setColor(mShortScaleColor);
                canvas.drawLine(mWidth/2, mHeight/2 - mDiameter/2, mWidth/2, mHeight/2 - mDiameter/2 + 20, paint);
            }
            // 旋转画布，每次旋转6度
            canvas.rotate(6,mWidth/2, mHeight/2);
        }
    }

    /**
     * 画外圆和中心实心圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mRingColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(getDigit(2));
        canvas.drawCircle(mWidth/2, mHeight/2, mDiameter/2, paint);
        paint.setStyle(Paint.Style.STROKE);//实心矩形框
        paint.setColor(Color.GRAY);  //颜色为红色
        canvas.drawRect(new RectF(mDiameter/4, mDiameter/4, mDiameter/2-25, mDiameter/3-10), paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(20);
        paint.setColor(Color.WHITE);
        canvas.drawText(StringData(),mDiameter/3-20,mDiameter/3-20,paint);
        // 画中心的实心圆
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mCenterPointColor);
        canvas.drawCircle(mWidth/2, mHeight/2, getDigit(4), paint);
    }

    public  String StringData(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return "星期"+mWay;
    }


    /**
     * 启动钟表
     */
    private void startClock(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    int hour = calendar.get(Calendar.HOUR);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);
                    setHour(hour);
                    setMinute(minute);
                    setSecond(second);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

