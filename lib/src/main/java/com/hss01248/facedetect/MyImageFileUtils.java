package com.hss01248.facedetect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class MyImageFileUtils {

    /**
     * 获取图片类型
     */
    public String getType(byte[] data) {
        // Png test:
        if (data[1] == 'P' && data[2] == 'N' && data[3] == 'G') {
            return ".png";
        }
        // Gif test:
        if (data[0] == 'G' && data[1] == 'I' && data[2] == 'F') {
            return ".gif";
        }
        // JPG test:
        if (data[6] == 'J' && data[7] == 'F' && data[8] == 'I'
                && data[9] == 'F') {
            return ".jpg";
        }
        return ".jpg";
    }







    /**
     * 创建缩略图
     *
     * @param context
     * @param largeImagePath
     *            原始大图路径
     * @param thumbfilePath
     *            输出缩略图路径
     * @param square_size
     *            输出图片宽度
     * @param quality
     *            输出图片质量
     * @throws IOException
     */
    public static void createImageThumbnail(Context context,
                                            String largeImagePath, String thumbfilePath, int square_size,
                                            int quality) throws IOException {


        // 原始图片bitmap
        Bitmap cur_bitmap =getSmallBitmap(largeImagePath, square_size,square_size);
        saveBitmap(cur_bitmap,thumbfilePath);
    }


    /**
     * 质量压缩图片
     * @param imageFile
     * @param qualityCompress
     * @param maxSize
     * @return
     */
    public static Bitmap compressBitmap(String imageFile, boolean qualityCompress, long maxSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile, options);
        int targetWidth = options.outWidth / 2;
        int targeHeight = options.outHeight / 2;
        return MyBitmapUtils.compressBitmap(imageFile, qualityCompress, maxSize, targetWidth, targeHeight);
    }

    /**
     * 质量压缩图片-压缩在maxSize以内
     * @param imageFile
     * @param maxSize
     */
    public static void compressImage(String imageFile, long maxSize) {
        compressBitmap(imageFile, true, maxSize);
    }

    /**
     * 质量压缩图片-压缩在maxSize以内
     * @param imageFile
     * @param maxSize
     * @return
     */
    public static Bitmap compressBimap(String imageFile, long maxSize) {
        return compressBitmap(imageFile, true, maxSize);
    }

    /**
     * 质量压缩图片-压缩在1M以内
     * @param imageFile
     */
    public static void compressImage(String imageFile) {
        compressBitmap(imageFile, true, (long)(1 * 1024*1024));
    }

    /**
     * 质量压缩图片-压缩在1M以内
     * @param imageFile
     * @return
     */
    public static Bitmap compressBitmap(String imageFile) {
        return compressBitmap(imageFile, true, (long)(1 * 1024*1024));
    }


    public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    public byte[] compressBitmapToBytes(String filePath, int reqWidth, int reqHeight, int quality) {
        Bitmap bitmap = getSmallBitmap(filePath, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        bitmap.recycle();

        return bytes;
    }

    public byte[] compressBitmapSmallTo(String filePath, int reqWidth, int reqHeight, int maxLenth) {
        int quality = 100;
        byte[] bytes = compressBitmapToBytes(filePath, reqWidth, reqHeight, quality);
        while (bytes.length > maxLenth && quality > 0) {
            quality = quality / 2;
            bytes = compressBitmapToBytes(filePath, reqWidth, reqHeight, quality);
        }
        return bytes;
    }

    public byte[] compressBitmapQuikly(String filePath) {
        return compressBitmapToBytes(filePath, 480, 800, 50);
    }

    public byte[] compressBitmapQuiklySmallTo(String filePath, int maxLenth) {
        return compressBitmapSmallTo(filePath, 480, 800, maxLenth);
    }


    public static boolean saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null)
            return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean saveBitmap(Bitmap bitmap, String absPath) {
        return saveBitmap(bitmap, new File(absPath));
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int h = options.outHeight;
        int w = options.outWidth;
        int inSampleSize = 0;
        if (h > reqHeight || w > reqWidth) {
            float ratioW = (float) w / reqWidth;
            float ratioH = (float) h / reqHeight;
            inSampleSize = (int) Math.min(ratioH, ratioW);
        }
        inSampleSize = Math.max(1, inSampleSize);
        return inSampleSize;
    }

    public static  void  savePicRefreshGallery(Activity activity, String filePath){
        if (Build.VERSION.SDK_INT>19){
            saveImageSendScanner(activity,new MyMediaScannerConnectionClient(filePath));
        }else {
            File file  = new File(filePath);
            try {
                MediaStore.Images.Media.insertImage(activity.getContentResolver(),file.getAbsolutePath(), file.getName(), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            saveImageSendBroadcast(activity,filePath);
        }
    }
    /**
     * 保存后用广播扫描，Android4.4以下使用这个方法
     * @author YOLANDA
     */
    public static void saveImageSendBroadcast(Activity activity,String filePath){
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
        //  com.hss01248.lib.activity. sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory()+ filePath)));
    }

    /**
     * 保存后用MediaScanner扫描，通用的方法
     *
     */
    public static void saveImageSendScanner (Activity activity,   MyMediaScannerConnectionClient scannerClient) {

        final MediaScannerConnection scanner = new MediaScannerConnection(activity, scannerClient);
        scannerClient.setScanner(scanner);
        scanner.connect();
    }
    public   static class MyMediaScannerConnectionClient implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mScanner;

        private String mScanPath;

        public MyMediaScannerConnectionClient(String scanPath) {
            mScanPath = scanPath;
        }

        public void setScanner(MediaScannerConnection con) {
            mScanner = con;
        }

        @Override
        public void onMediaScannerConnected() {
            mScanner.scanFile(mScanPath, "image/*");
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mScanner.disconnect();
        }
    }

}
