package com.ice.stickertest.datalogging;

import android.app.Activity;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ice.stickertest.R;

import java.util.ArrayList;

public class GraphTest extends Activity {
    Path path1 = new Path();
    Path path2 = new Path();
    ArrayList<Point> dataSet = new ArrayList<Point>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_test);

        final GraphDraw graph = (GraphDraw) findViewById(R.id.graph_1);
        Button mRedraw = (Button) findViewById(R.id.redraw);
        graph.setOrigin(200, 800);


        dataSet.add(new Point(200, 800));           // sets beginning of curve
        dataSet.add(new Point(300, 350));
        dataSet.add(new Point(400, 400));
        dataSet.add(new Point(500, 500));
        dataSet.add(new Point(600, 600));
        dataSet.add(new Point(700, 500));
        dataSet.add(new Point(800, 450));
        dataSet.add(new Point(900, 400));
        dataSet.add(new Point(1000, 350));
        dataSet.add(new Point(1100, 320));
        dataSet.add(new Point(1200, 410));
        dataSet.add(new Point(1300, 480));
        dataSet.add(new Point(1400, 400));
        dataSet.add(new Point(1500, 180));
        dataSet.add(new Point(1600, 150));
        dataSet.add(new Point(1700, 120));
        dataSet.add(new Point(1800, 0));
        dataSet.add(new Point(1900, -100));

        path1 = graph.convertDataSetToPath( dataSet);
        graph.setDataPath ( path1 );

        mRedraw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                graph.setDP();
            }
        });
    }
}
