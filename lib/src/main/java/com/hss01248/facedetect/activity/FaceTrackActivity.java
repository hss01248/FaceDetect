package com.hss01248.facedetect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.faceplusplus.api.FaceDetecter;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.hss01248.facedetect.MyBitmapUtils;
import com.hss01248.facedetect.MyImageFileUtils;
import com.hss01248.facedetect.R;
import com.hss01248.facedetect.constant.Global;
import com.hss01248.facedetect.view.FaceMask;
import com.hss01248.facedetect.view.LoadingView;
import com.hss01248.titlebarlib.Titlebar;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FaceTrackActivity extends BaseActivity implements Callback, PreviewCallback {


   // @Bind(R.id.sv_preview_track)
    SurfaceView svPreview;
   // @Bind(R.id.fm_mask_track)
    FaceMask fmMask;

    LoadingView loadingView;

    RelativeLayout root;
    
    private Camera mCamera;
    private HandlerThread handleThread;
    private Handler detectHandler;
    
    private int width = 640;
    private int height = 480;
    private FaceDetecter facedetecter;

    Activity activity;
    Titlebar titlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_track);
        svPreview = (SurfaceView) findViewById(R.id.sv_preview_track);
        fmMask = (FaceMask) findViewById(R.id.fm_mask_track);
        root = (RelativeLayout) findViewById(R.id.root);
        titlebar = (Titlebar) findViewById(R.id.titlebar);

        titlebar.init(this);
        titlebar.setTitle("人脸检测");
       // titlebar.getRootView().setBackgroundColor(Color.TRANSPARENT);
        titlebar.getTvRight().setText("拍照");
        titlebar.getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePic();
            }
        });
        //loadingView = (LoadingView) findViewById(R.id.loadingView);
        activity = this;
       // ButterKnife.bind(this);

      //  initToolBar(true, toolbar, R.string.title_track);
        initData();
    }



    private void initData() {
        handleThread = new HandlerThread("dt");
        handleThread.start();
        detectHandler = new Handler(handleThread.getLooper());
        svPreview.getHolder().addCallback(this);
        svPreview.setKeepScreenOn(true);

        facedetecter = new FaceDetecter();
        facedetecter.init(this, Global.FACEPP_KEY);
        facedetecter.setTrackingMode(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open(1);
        mCamera.setDisplayOrientation(90);
        Camera.Parameters para = mCamera.getParameters();

     /* List<Camera.Size> sizes =   para.getSupportedPreviewSizes();
        int count = sizes.size();
        for (int i = 0; i< count; i++){
            Camera.Size size = sizes.get(i);
            if (size.width > 800 && size.width < 1000){
                width = size.width;
                height = size.height;
                break;
            }
            Log.e("size","size w:"+size.width + "--h:"+size.height);

        }


*/
        Point showingArea = new Point();
        WindowManager wm = (WindowManager) getSystemService(
                Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
       int screenWidth = display.getWidth();
       int  screenHeight = display.getHeight();

       int topHeight = dip2px(activity,25+44);//状态栏+titlebar
        showingArea.set(screenWidth,screenHeight-topHeight);


        Point point = getBestCameraResolution(para,showingArea);

        width = point.x;
        height= point.y;
        //todo 预览变形的问题
        Log.e("size","size w:"+width + "--h:"+height);
        /*:1920--h:1080
09-19 15:30:11.068 19768-19768/com.hss01248.facedemo E/size: size w:1440--h:1080
09-19 15:30:11.068 19768-19768/com.hss01248.facedemo E/size: size w:1280--h:720
09-19 15:30:11.068 19768-19768/com.hss01248.facedemo E/size: size w:1056--h:864
09-19 15:30:11.068 19768-19768/com.hss01248.facedemo E/size: size w:960--h:720
09-19 15:30:11.068 19768-19768/com.hss01248.facedemo E/size: size w:720--h:480
09-19 15:30:11.068 19768-19768/com.hss01248.facedemo E/size: size w:640--h:480
09-19 15:30:11.068 19768-19768/com.hss01248.facedemo E/size: size w:352--h:288
09-19 15:30:11.068 19768-19768/com.hss01248.facedemo E/size: size w:320--h:240*/

        para.setPreviewSize(width, height);
        mCamera.setParameters(para);
    }

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    private Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution){
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float)screenResolution.x / (float)screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for(Camera.Size s : supportedPreviewSizes){
            tmp = Math.abs(((float)s.height/(float)s.width)-x_d_y);
            if(tmp<mindiff){
                mindiff = tmp;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
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
        camera.setPreviewCallback(null);
        if (mCamera == null) return;
        detectHandler.post(new Runnable() {
            @Override
            public void run() {
                int is = 0;
                final byte[] ori = new byte[width * height];
                for (int x = width - 1; x >= 0; x--) {
                    for (int y = height - 1; y >= 0; y--) {
                        ori[is++] = data[y * width + x];
                    }
                }
                final FaceDetecter.Face[] faceinfo = facedetecter.findFaces(ori, height, width);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fmMask.setFaceInfo(faceinfo);


                    }
                });


                try {
                    camera.setPreviewCallback(FaceTrackActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

/*
                if (!consumeFaceInfo(ori,faceinfo)){
                    try {
                        camera.setPreviewCallback(FaceTrackActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                  //  Toast.makeText(FaceTrackActivity.this,"consumed",Toast.LENGTH_SHORT).show();

                }*/

            }
        });
    }

   /* private boolean consumeFaceInfo(byte[] ori, final FaceDetecter.Face[] faceinfo) {
        if (faceinfo != null && faceinfo.length == 1){
            mCamera.takePicture(null, null,null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {



                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            loadingView = new LoadingView(activity);
                            loadingView.setMsg("正在识别,请骚等");
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300,300);
                            params.addRule(RelativeLayout.CENTER_IN_PARENT);
                            loadingView.setLayoutParams(params);
                            root.addView(loadingView);

                           // loadingView.setVisibility(View.VISIBLE);
                        }
                    });

                    Bitmap bitmap =  MyBitmapUtils.byteToBitmap(data,480,640);
                    //然后将bitmap旋转270度
                    Bitmap bitmap1 = MyBitmapUtils.rotateBitmap(bitmap,270,true);
                    bitmap.recycle();



                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    final File file = new File(dir,System.currentTimeMillis()+".jpg");



                    if (!file.exists()){
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    FileOutputStream outSteam=null;
                    try{
                        outSteam=new FileOutputStream(file);
                        bitmap1.compress(Bitmap.CompressFormat.JPEG,80,outSteam);
                        bitmap1.recycle();
                        //int degree = readPictureDegree(file.getAbsolutePath());
                       // setPictureDegreeZero(file.getAbsolutePath());

                        MyImageFileUtils.saveImageSendBroadcast(FaceTrackActivity.this,file.getAbsolutePath());//让系统可以扫描到这张图片
                       // Toast.makeText(FaceTrackActivity.this,"onPictureTaken successs,degree:"+degree,Toast.LENGTH_SHORT).show();





                        //图片文件上传到face++并拿到人脸信息
                       //注意,旋转的图识别不了,一定要正面的图,并且exif里旋转角度为0



                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HttpRequests httpRequests = new HttpRequests(Global.FACEPP_KEY, Global.FACEPP_SECRET);
                                PostParameters postParameters = new PostParameters().setImg(file).setAttribute("all");

                                try {
                                    // postParameters.getMultiPart().writeTo(System.out);
                                    //获取人脸信息
                                    JSONObject result =   httpRequests.detectionDetect(postParameters);

                                   // httpRequests.recognitionVerify(postParameters);

                                    String resonseStr = result.toString();
                                    System.out.println(resonseStr);

                                    Intent intent = new Intent();
                                    intent.putExtra("info",resonseStr);
                                    setResult(RESULT_OK,intent);
                                    finish();

                                } catch (FaceppParseException e) {
                                    e.printStackTrace();
                                    setResult(RESULT_CANCELED,null);
                                    finish();
                                }
                            }
                        }).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if (outSteam != null){
                            try {
                                outSteam.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
            });
           // mCamera.stopPreview();

            return true;
        }
        return false;
    }*/


    private void takePic() {
        mCamera.takePicture(null, null,null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {



                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        loadingView = new LoadingView(activity);
                        loadingView.setMsg("正在识别,请骚等");
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300,300);
                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
                        loadingView.setLayoutParams(params);
                        root.addView(loadingView);

                        // loadingView.setVisibility(View.VISIBLE);
                    }
                });

                Bitmap bitmap =  MyBitmapUtils.byteToBitmap(data,480,640);
                //然后将bitmap旋转270度
                Bitmap bitmap1 = MyBitmapUtils.rotateBitmap(bitmap,270,true);
                bitmap.recycle();



                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                final File file = new File(dir,System.currentTimeMillis()+".jpg");



                if (!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                FileOutputStream outSteam=null;
                try{
                    outSteam=new FileOutputStream(file);
                    bitmap1.compress(Bitmap.CompressFormat.JPEG,80,outSteam);
                    bitmap1.recycle();
                    //int degree = readPictureDegree(file.getAbsolutePath());
                    // setPictureDegreeZero(file.getAbsolutePath());

                    MyImageFileUtils.saveImageSendBroadcast(FaceTrackActivity.this,file.getAbsolutePath());//让系统可以扫描到这张图片
                    // Toast.makeText(FaceTrackActivity.this,"onPictureTaken successs,degree:"+degree,Toast.LENGTH_SHORT).show();





                    //图片文件上传到face++并拿到人脸信息
                    //注意,旋转的图识别不了,一定要正面的图,并且exif里旋转角度为0



                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpRequests httpRequests = new HttpRequests(Global.FACEPP_KEY, Global.FACEPP_SECRET);
                            PostParameters postParameters = new PostParameters().setImg(file).setAttribute("all");

                            try {
                                // postParameters.getMultiPart().writeTo(System.out);
                                //获取人脸信息
                                JSONObject result =   httpRequests.detectionDetect(postParameters);

                                // httpRequests.recognitionVerify(postParameters);

                                String resonseStr = result.toString();
                                System.out.println(resonseStr);

                                Intent intent = new Intent();
                                intent.putExtra("info",resonseStr);
                                setResult(RESULT_OK,intent);
                                finish();

                            } catch (FaceppParseException e) {
                                e.printStackTrace();
                                setResult(RESULT_CANCELED,null);
                                finish();
                            }
                        }
                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (outSteam != null){
                        try {
                            outSteam.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        });
    }


    /**
     * 将图片的旋转角度置为0
     * @Title: setPictureDegreeZero
     * @param path
     * @return void
     * @date 2012-12-10 上午10:54:46
     */
    private void setPictureDegreeZero(String path){
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            //修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            //例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
           // exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_270+"");
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        facedetecter.release(this);
        handleThread.quit();
        detectHandler=null;
    }

    /**
     * 获取图片的旋转角度
     * @Title: readPictureDegree
     * @param path
     * @return
     * @return int
     * @date 2012-12-4 上午9:22:33
     */
    private  int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
