package lixiaoqian.bwie.com.watchdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

/**
 * @类的用途：
 * @author: 李晓倩
 * @date: 2017/5/5
 */

public class ClockView extends View {

    /**
     * 绘制表盘的画笔
     */
    private Paint circlePaint;

    /**
     * 绘制表盘数字
     */
    private Paint numPaint;
    /**
     * 绘制表心
     */
    private Paint dotPaint;
    /**
     * 时针
     */
    private Paint hourPaint;
    /**
     * 分针
     */
    private Paint minutePaint;
    /**
     * 秒针
     */
    private Paint secondPaint;
    /**
     * View宽度，默认256dp
     */
    private int width;
    /**
     * View高度，默认256dp
     */
    private int height;
    /**
     * 日历类，用来获取当前时间
     */
    private Calendar calendar;
    /**
     * 当前时针颜色
     */
    private int hourColor;
    /**
     * 当前分针颜色
     */
    private int minuteColor;
    /**
     * 当前秒针颜色
     */
    private int secondColor;
    /**
     * 时针宽度
     */
    private int hourWidth;
    /**
     * 分针宽度
     */
    private int minuteWidth;
    /**
     * 秒针宽度
     */
    private int secondWidth;

    //在代码中创建控件时调用
    public ClockView(Context context) {
        this(context, null);
    }

    //在布局文件中创建View时调用
    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //获取当前时间的实例
        calendar = Calendar.getInstance();
        //时钟默认宽高
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 256, getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 256, getResources().getDisplayMetrics());
        //初始化表针的颜色
        hourColor = Color.RED;
        minuteColor = Color.GREEN;
        secondColor = Color.BLUE;
        //初始化表针的宽度
        hourWidth = 8;
        minuteWidth = 5;
        secondWidth = 2;
        //初始化各种画笔
        circlePaint = new Paint();
        //去锯齿
        circlePaint.setAntiAlias(true);
        //设置画笔颜色
        circlePaint.setColor(Color.GREEN);
        //设置画笔style为描边
        circlePaint.setStyle(Paint.Style.STROKE);
        //设置描边的宽度
        circlePaint.setStrokeWidth(6);
        dotPaint = new Paint();
        dotPaint.setAntiAlias(true);
        dotPaint.setColor(Color.RED);
        dotPaint.setStyle(Paint.Style.FILL);
        numPaint = new Paint();
        numPaint.setColor(Color.RED);
        numPaint.setAntiAlias(true);
        //文本对齐方式
        numPaint.setTextAlign(Paint.Align.CENTER);
        hourPaint = new Paint();

        hourPaint.setColor(hourColor);
        hourPaint.setStyle(Paint.Style.FILL);
        hourPaint.setStrokeWidth(hourWidth);
        minutePaint = new Paint();
        minutePaint.setColor(minuteColor);
        minutePaint.setStyle(Paint.Style.FILL);
        minutePaint.setStrokeWidth(minuteWidth);
        secondPaint = new Paint();
        secondPaint.setColor(secondColor);
        secondPaint.setStyle(Paint.Style.FILL);
        secondPaint.setStrokeWidth(secondWidth);
    }

    //绘制View
    @Override
    protected void onDraw(Canvas canvas) {
        calendar = Calendar.getInstance();
        //1.圆心X轴坐标，2.圆心Y轴坐标，3.半径，4.画笔
        int radius = width / 2 - 10;
        //画表盘
        canvas.drawCircle(width / 2, height / 2, radius, circlePaint);

        canvas.drawCircle(width / 2, height / 2, 15, dotPaint);
        for (int i = 1; i < 13; i++) {
            //在旋转之前保存画布状态
            canvas.save();
            canvas.rotate(i * 30, width / 2, height / 2);
            //1.2表示起点坐标，3.4表示终点坐标，5.画笔
            canvas.drawLine(width / 2, height / 2 - radius, width / 2, height / 2 - radius + 10, circlePaint);
            //画表盘数字1.要绘制的文本，2.文本x轴坐标，3.文本基线，4.文本画笔
            canvas.drawText(i + "", width / 2, height / 2 - radius + 22, numPaint);
            //恢复画布状态
            canvas.restore();
        }
        //获得当前小时
        int hour = calendar.get(Calendar.HOUR);
        canvas.save();
        //旋转屏幕
        canvas.rotate(hour * 30, width / 2, height / 2);
        //画时针
        canvas.drawLine(width / 2, height / 2 + 20, width / 2, height / 2 - 90, hourPaint);
        canvas.restore();

        int minute = calendar.get(Calendar.MINUTE);
        canvas.save();
        canvas.rotate(minute * 6, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 + 30, width / 2, height / 2 - 110, minutePaint);
        canvas.restore();
        int second = calendar.get(Calendar.SECOND);
        canvas.save();
        canvas.rotate(second * 6, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 + 40, width / 2, height / 2 - 130, secondPaint);
        canvas.restore();
        //每隔1秒重绘View,重绘会调用onDraw()方法
        postInvalidateDelayed(1000);
    }
}

