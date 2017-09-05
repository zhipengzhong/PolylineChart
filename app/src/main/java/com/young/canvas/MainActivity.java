package com.young.canvas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.young.canvas.widget.PolylineView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private PolylineView mPolylineView;
    private SeekBar mSeekBar;
    private TextView mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();

    }

    private void setListener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress);
                generateData(progress * 10 + 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mPolylineView.setOnSelectedDotListener(new PolylineView.OnSelectedDotListener() {
            @Override
            public void OnSelectedDot(float value) {
                mCallBack.setText("回调数值:" + value);
            }
        });
    }

    private void initData() {
        generateData(30);
    }

    private void generateData(int num) {
        ArrayList<Integer> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            list.add(random.nextInt(40) + 40);
        }
        mPolylineView.setData(list);
    }

    private void initView() {
        mPolylineView = (PolylineView) findViewById(R.id.canvas_view);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mCallBack = (TextView) findViewById(R.id.call_back);
    }
}
