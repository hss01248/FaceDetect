package com.hss01248.facedetect.net;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.hss01248.facedetect.constant.Global;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class FaceNet {

    public static void recogenition(final File file){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequests httpRequests = new HttpRequests(Global.FACEPP_KEY, Global.FACEPP_SECRET);
                PostParameters postParameters = new PostParameters().setImg(file).setAttribute("all");

                try {
                   // postParameters.getMultiPart().writeTo(System.out);

                    JSONObject result =   httpRequests.detectionDetect(postParameters);

                    System.out.println(result.toString());



                } catch (FaceppParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
