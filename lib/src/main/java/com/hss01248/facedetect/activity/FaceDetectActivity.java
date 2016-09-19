package com.hss01248.facedetect.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.hss01248.facedetect.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SuppressWarnings("All")
public class FaceDetectActivity extends BaseActivity implements Callback, PreviewCallback {


    //@Bind(R.id.sv_preview_detect)
    SurfaceView svPreview;
   // @Bind(R.id.iv_image_detect)
    ImageView ivImage;

    private Camera mCamera;

    private int width = 640;
    private int height = 480;

    private boolean flag = true;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap rotateBitmap = getRotateBitmap(bitmap, -90);

            ivImage.setImageBitmap(rotateBitmap);
            camera.startPreview();
            dealWithFaceDetect(bitmap2Bytes(rotateBitmap));
        }
    };

    private byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void dealWithFaceDetect(final byte[] data) {
      /*  new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpRequests httpRequests = new HttpRequests(Global.FACEPP_KEY, Global.FACEPP_SECRET, true, true);

                    PostParameters postParameters = new PostParameters();
                    postParameters.setImg(data);

                    if (!flag) {
                        //------ 检测给定图片(Image)中的所有人脸(Face)的位置和相应的面部属性 ------//
                        JSONObject detectionJson = httpRequests.detectionDetect(postParameters);
                        Log.e("---> ", "detectionDetectJson: " + detectionJson.toString());

                        JSONArray faces = detectionJson.getJSONArray("face");
                        String face_id = faces.optJSONObject(0).getString("face_id");
                        String img_id = detectionJson.getString("img_id");
                        String session_id = detectionJson.getString("session_id");

                        Log.e("---> ", "face_id: " + face_id);
                        Log.e("---> ", "img_id: " + img_id);
                        Log.e("---> ", "session_id: " + session_id);


                        //------ 获取session相关状态和结果 ------//
                        JSONObject sessionJson = httpRequests.getSessionSync(session_id);
                        Log.e("----> ", "status: " + sessionJson.getString("status"));

                        postParameters.setFaceId(face_id);
                        postParameters.setPersonId(getFacePreferences().personId());
                        JSONObject recognitionJson = httpRequests.recognitionVerify(postParameters);
                        Log.e("---> ", "recognitionJson: " + recognitionJson.toString());
                        final boolean isSamePerson = recognitionJson.getBoolean("is_same_person");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isSamePerson) {
                                    Toast.makeText(FaceDetectActivity.this, "验证成功：相同", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(FaceDetectActivity.this, "验证成功：不相同", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        //------ 检测给定图片(Image)中的所有人脸(Face)的位置和相应的面部属性 ------//
                        JSONObject detectionJson = httpRequests.detectionDetect(postParameters);
                        Log.e("---> ", "detectionDetectJson: " + detectionJson.toString());

                        JSONArray faces = detectionJson.getJSONArray("face");
                        String face_id = faces.optJSONObject(0).getString("face_id");
                        String img_id = detectionJson.getString("img_id");
                        String session_id = detectionJson.getString("session_id");

                        Log.e("---> ", "face_id: " + face_id);
                        Log.e("---> ", "img_id: " + img_id);
                        Log.e("---> ", "session_id: " + session_id);


                        //------ 获取session相关状态和结果 ------//
                        JSONObject sessionJson = httpRequests.getSessionSync(session_id);
                        Log.e("----> ", "status: " + sessionJson.getString("status"));


                        //------ 创建一个Person ------//
                        PostParameters paramater1 = new PostParameters();
                        paramater1.setPersonName("Person name (" + face_id + ")");
                        paramater1.setTag("Person tag (" + face_id + ")");
                        paramater1.setFaceId(face_id);
                        JSONObject createJson = httpRequests.personCreate(paramater1);
                        Log.e("----> ", "createJson: " + createJson.toString());

                        String person_id = createJson.getString("person_id");
                        Log.e("----> ", "person_id: " + person_id);

                        //------ 针对verify功能对一个person进行训练 ------//
                        PostParameters paramater2 = new PostParameters();
                        paramater2.setPersonId(person_id);
                        JSONObject trainJson = httpRequests.trainVerify(paramater2);
                        Log.e("----> ", "trainJson: " + trainJson.toString());

                        //------ 获取session相关状态和结果 ------//
                        JSONObject sessionJson1 = httpRequests.getSessionSync(trainJson.getString("session_id"));
                        Log.e("----> ", "status: " + sessionJson1.getString("status"));
                        final String status = sessionJson1.getString("status");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status.equals("SUCC")) {
                                    Toast.makeText(FaceDetectActivity.this, "扫描人脸成功", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(FaceDetectActivity.this, "扫描人脸失败", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        getFacePreferences().faceId(face_id);
                        getFacePreferences().imgId(img_id);
                        getFacePreferences().personId(person_id);
                    }
                    flag = !flag;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);
        svPreview = (SurfaceView) findViewById(R.id.sv_preview_detect);
        ivImage = (ImageView) findViewById(R.id.iv_image_detect);
       // ButterKnife.bind(this);

      //  initToolBar(true, toolbar, R.string.title_detect);
        initData();
    }

    private void initData() {
        svPreview.getHolder().addCallback(this);
        svPreview.setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open(1);
        Camera.Parameters para = mCamera.getParameters();
        para.setPreviewSize(width, height);
        mCamera.setParameters(para);
        mCamera.setDisplayOrientation(90);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, pictureCallback);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onPreviewFrame(final byte[] data, final Camera camera) {
        Bitmap bitmap = null;
        if (data != null) {

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static Bitmap getRotateBitmap(Bitmap bitmap, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

}
