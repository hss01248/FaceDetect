package com.hss01248.facedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hss01248.facedetect.activity.FaceTrackActivity;
import com.hss01248.facedetect.bean.FaceInfo;


public class MainActivity extends Activity {

    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
    }

    private void initView() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FaceTrackActivity.class);
                context.startActivityForResult(intent,9);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (data != null){
                String info = data.getStringExtra("info");
                Log.e("info","info:"+info);
               // Toast.makeText(context,info,Toast.LENGTH_LONG).show();
                FaceInfo faceInfo = JSON.parseObject(info,FaceInfo.class);
                if (faceInfo != null && faceInfo.face.size()>0){
                FaceInfo.FaceBean.AttributeBean faceBean =    faceInfo.face.get(0).attribute;
                   String str = "性别: "+ faceBean.gender.value +" ,年龄: "+faceBean.age.value
                           +"(±"+faceBean.age.range+"),\n人种: "+faceBean.race.value+", 笑值: "+faceBean.smiling.value;

                    Toast.makeText(context,str,Toast.LENGTH_LONG).show();
                }


            }

        }else if (resultCode == RESULT_CANCELED){
            Toast.makeText(context,"识别出错",Toast.LENGTH_LONG).show();
        }
    }
}
