# FaceDetect
base on face++



## 解决了的问题:

 camera预览变形

拍照后图片不正

## 待解决

与face++ api配合的多样化的动作.(目前只有扫脸获取人脸信息,后续需加上建组,人脸登录等)



# Usage

## gradle

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

```

Step 2. Add the dependency

```
dependencies {
        compile 'com.github.hss01248:FaceDetect:1.0.0'
}
```



## manifest

```
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
...

 
<activity android:name="com.hss01248.facedetect.activity.FaceTrackActivity"/>
```

## code

startActivity:

```
Intent intent = new Intent(context, FaceTrackActivity.class);
context.startActivityForResult(intent,9);
```

onActivityResult:

```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK){
        if (data != null){
            String info = data.getStringExtra("info");
            Log.e("info","info:"+info);
            Toast.makeText(context,info,Toast.LENGTH_LONG).show();
        }

    }else if (resultCode == RESULT_CANCELED){
        Toast.makeText(context,"识别出错",Toast.LENGTH_LONG).show();
    }
}
```

## 拿到的脸部信息:

{"face":[{"position":{"mouth_right":{"y":69.440208,"x":57.177778},"mouth_left":{"y":69.0975,"x":40.728519},"center":{"y":64.0625,"x":49.814815},"height":19.791667,"width":35.185185,"nose":{"y":64.674375,"x":50.537778},"eye_left":{"y":59.557083,"x":42.082963},"eye_right":{"y":60.006458,"x":57.913704}},"attribute":{"race":{"value":"White","confidence":93.4608},"gender":{"value":"Male","confidence":76.8741},"smiling":{"value":53.9131},"age":{"value":19,"range":5}},"tag":"","face_id":"4a7b9360f279c8bd2f48c982d1526f76"}],"session_id":"d83c80b7d13e4c96afdf635110add267","img_height":480,"img_width":270,"img_id":"289889896bc471fe935355eec718890a","url":null,"response_code":200}

# 示例图

 ![demo](demo.jpg)



# blog

[基于face++的人脸识别库的封装](http://www.jianshu.com/p/b55920dfdd14)



#  Thanks

[FaceDetection](https://github.com/sfsheng0322/FaceDetection)

[Android-SpinKit](https://github.com/ybq/Android-SpinKit)