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
    private SeekBar mDense;
    private int mNum = 10;
    private int mDenseNum = 1;

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
                mNum = progress * 10 + 2;
                generateData(mNum, mDenseNum);
                //调整产生假数据的数量
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mDense.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //调整产生假数据的间隔
                mDenseNum = progress * 4 + 1;
                generateData(mNum, mDenseNum);
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
        generateData(mNum, mDenseNum);
    }

    private void generateData(int num, int dense) {
        ArrayList<Integer> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            list.add(random.nextInt(dense) + 400);
        }
        mPolylineView.setData(list);
    }

    private void initView() {
        mPolylineView = (PolylineView) findViewById(R.id.canvas_view);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mDense = (SeekBar) findViewById(R.id.seek_bar);
        mCallBack = (TextView) findViewById(R.id.call_back);
    }
}
