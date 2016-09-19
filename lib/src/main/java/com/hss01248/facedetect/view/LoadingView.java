package com.hss01248.facedetect.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.hss01248.facedetect.R;


/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class LoadingView extends FrameLayout {
    View rootView;
    SpinKitView spinKitView;
    TextView textView;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData(context,attrs,defStyleAttr);
        initEvent(context);
        addView(rootView);
    }

    private void initView(Context context) {
       rootView = View.inflate(context, R.layout.view_loading,null);
        spinKitView = (SpinKitView) rootView.findViewById(R.id.loading);
        textView = (TextView) rootView.findViewById(R.id.tv_msg);

    }

    private void initData(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    private void initEvent(Context context) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setMsg(String msg){
        if (msg == null){
            msg= "";
        }
        textView.setText(msg);

    }
}
