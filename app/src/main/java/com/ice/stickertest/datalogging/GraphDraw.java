package com.ice.stickertest.datalogging;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.ice.stickertest.R;

import java.util.ArrayList;
import java.util.List;

/*
  Chart Types
  GRAPH_LINE
  GRAPH_BAR
  GRAPH_POINTS


 */


public class GraphDraw extends View implements Graph {
    public static final String PREFS_NAME = "MyPrefsFile";

    List<Integer> dataPoints = new ArrayList();// graph drawing constants
    String[] xLabels = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String[] yLabels = new String[]{"", "10", "20", "30", "40", "50", "60", "70", "80"};

    Paint mAxisPaint = new Paint();
    Paint mMinorPaint = new Paint();
    Paint mDataPaint = new Paint();
    Paint mTextPaint = new Paint();

    private Point origin = new Point(150,400);

    private Point graphStop   = new Point (1800, 500);
    private Point spacing     = new Point (50, 50);

    public Path dataPath = new Path();

    //  Constructor for class
    public GraphDraw (Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public GraphDraw(Context context) {
        super(context);
    }

    public void setDP()  {
        invalidate();              // this redraws page
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // set up paints

        mAxisPaint.setColor(Color.BLUE);
        mAxisPaint.setStrokeWidth(8);

        mDataPaint.setColor(Color.RED);
        mDataPaint.setStrokeWidth(4);

        mMinorPaint.setColor(Color.LTGRAY);
        mMinorPaint.setStrokeWidth(3);
        mMinorPaint.setStyle(Style.STROKE);
        mMinorPaint.setPathEffect(new DashPathEffect(new float[] {10,10}, 15));

        mTextPaint.setColor ( Color.BLACK );
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setTextSize(40);

        // print title
        canvas.drawText("P/L", 300, 300, mTextPaint);

        // draw vertical axis
        canvas.drawLine(origin.x, 0, origin.x, origin.y, mAxisPaint);
        // draw horizontal axis
        canvas.drawLine(origin.x, origin.y, graphStop.x, origin.y, mAxisPaint);
        // draw center line
        canvas.drawLine(origin.x,  origin.y/2, graphStop.x,  origin.y/2, mMinorPaint);


        // set labels on axes
        setXLabels(canvas, mTextPaint);
        setYLabels(canvas, mTextPaint);


        // draw minor grid lines. Need to use Path instead of drawLine.
        //Bug in android wont draw dashed lines, but will draw dashed paths.

        int xval = origin.x + spacing.x;
        int yval = origin.y;
        Path vertline = new Path();
        vertline.moveTo(xval, yval);
        for (int i=0; i<32; i++) {
            vertline.moveTo(xval, yval);
            vertline.lineTo(xval, 0);
            canvas.drawPath(vertline, mMinorPaint);
            xval += spacing.x;
        }

        xval = origin.x;
        yval = origin.y;
        Path horizline = new Path();
        horizline.moveTo(xval, yval);
        for (int i=0; i<32; i++) {
            horizline.moveTo(xval, yval);
            horizline.lineTo(graphStop.x, yval);
            canvas.drawPath(horizline, mMinorPaint);
            yval -= spacing.y;
        }


        // draw data line
        drawDataLine(  dataPath, canvas, mDataPaint);

    }



    public void drawDataLine(Path path, Canvas c, Paint paint)  {
        paint.setStrokeWidth(8);
        paint.setStyle(Style.STROKE);
        c.drawPath(path, paint);
    }


    public void setDataPath(Path aPath)  {

        dataPath = aPath;
        invalidate();
    }


    public void setXLabels(Canvas c, Paint paint) {
        paint.setColor ( Color.BLACK );
        int xoffset = 115;
        int yoffset = origin.y + 50;


        for (int i = 0; i < xLabels.length; i++)   {
            c.drawText(xLabels[i], xoffset, yoffset, paint);
            xoffset += 100;
        }

    }

    public void setYLabels(Canvas c, Paint paint)  {
        paint.setColor ( Color.BLACK );
        int xoffset = 40;
        int yoffset = origin.y + 50;


        for (int i = 0; i < yLabels.length; i++)   {
            c.drawText(yLabels[i], xoffset, yoffset, paint);
            yoffset -= 100;
        }

    }

    public void setOrigin(int x, int y) {
        origin.x = x;
        origin.y = y;
    }


    // takes point from data set and puts it on path
    // need to be converted from integer to float
    // also need to do transform between data space and graph space
    public Path convertDataSetToPath( ArrayList<Point> dataSet) {
        Path newPath = new Path();

        newPath.moveTo((float)dataSet.get(0).x, (float)dataSet.get(0).y);
        for (Point point:  dataSet ) {
            newPath.lineTo((float)point.x, (float)point.y);
        }

        return newPath;
    }

    @Override
    public void setWidth() {

    }

    @Override
    public void setLength() {

    }

    @Override
    public void setXDivisions() {

    }

    @Override
    public void setYDivisions() {

    }
}
