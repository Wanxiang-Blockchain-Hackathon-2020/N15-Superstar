package com.example.chinacompetition.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageResizeUtils {

    /**
     * 이미지의 너비를 변경한다.
     * @param file
     * @param newFile
     * @param newWidth
     */
    public static void resizeFile(File file, File newFile, int newWidth,int newHeight, Boolean isCamera) {

        String TAG = "blackjin";

        Bitmap originalBm = null;
        Bitmap resizedBitmap = null;


        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inDither = true;

            originalBm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(file.getAbsolutePath());
                // try ~ cathch 안하면 IOException 발생함
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            int exifDegree = exifOrientationToDegrees(exifOrientation);


            if(isCamera && exifOrientation==1){
                Log.e("tag","exifOrientation"+exifOrientation);
            }else if(isCamera && exifOrientation==2){
                Log.e("tag","exifOrientation"+exifOrientation);
            }else if(isCamera && exifOrientation==3){
                Log.e("tag","exifOrientation"+exifOrientation);
            }else if(isCamera && exifOrientation==4){
                Log.e("tag","exifOrientation"+exifOrientation);
            }else if(isCamera && exifOrientation==5){
                Log.e("tag","exifOrientation"+exifOrientation);
            }else if(isCamera && exifOrientation==6){
                Log.e("tag","exifOrientation"+exifOrientation);
            }else if(isCamera && exifOrientation==7){
                Log.e("tag","exifOrientation"+exifOrientation);
            }else if(isCamera && exifOrientation==8){
                Log.e("tag","exifOrientation"+exifOrientation);
            }else if(isCamera && exifOrientation==0){
                Log.e("tag","exifOrientation"+exifOrientation);
            }

            if(isCamera && exifDegree==1){
                Log.e("tag","exifDegree"+exifDegree);
            }else if(isCamera && exifDegree==2){
                Log.e("tag","exifDegree"+exifDegree);
            }else if(isCamera && exifDegree==3){
                Log.e("tag","exifDegree"+exifDegree);
            }else if(isCamera && exifDegree==4){
                Log.e("tag","exifDegree"+exifDegree);
            }else if(isCamera && exifDegree==5){
                Log.e("tag","exifDegree"+exifDegree);
            }else if(isCamera && exifDegree==6){
                Log.e("tag","exifDegree"+exifDegree);
            }else if(isCamera && exifDegree==7){
                Log.e("tag","exifDegree"+exifDegree);
            }else if(isCamera && exifDegree==8){
                Log.e("tag","exifDegree"+exifDegree);
            }else if(isCamera && exifDegree==0){
                Log.e("tag","exifDegree"+exifDegree);
            }



            if(isCamera) {
                Matrix matrix = new Matrix();
                if(isCamera && exifOrientation==ExifInterface.ORIENTATION_NORMAL){
                    Log.e("tag","카메라ORIENTATION_NORMAL :"+ExifInterface.ORIENTATION_NORMAL);
                }else if(isCamera && exifOrientation==ExifInterface.ORIENTATION_FLIP_HORIZONTAL){
                    Log.e("tag","카메라ORIENTATION_FLIP_HORIZONTAL :"+ExifInterface.ORIENTATION_NORMAL);
                    matrix.setScale(-1, 1);
                }else if(isCamera && exifOrientation==ExifInterface.ORIENTATION_ROTATE_180){
                    Log.e("tag","카메라ExifInterface.ORIENTATION_ROTATE_180 :"+ExifInterface.ORIENTATION_ROTATE_180);
                    matrix.setRotate(180);
                }else if(isCamera && exifOrientation==ExifInterface.ORIENTATION_FLIP_VERTICAL){
                    Log.e("tag","카메라ExifInterface.ORIENTATION_FLIP_VERTICAL :"+ExifInterface.ORIENTATION_FLIP_VERTICAL);
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                }else if(isCamera && exifOrientation==ExifInterface.ORIENTATION_TRANSPOSE){
                    Log.e("tag","카메라ExifInterface.ORIENTATION_TRANSPOSE :"+ExifInterface.ORIENTATION_TRANSPOSE);
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                }else if(isCamera && exifOrientation==ExifInterface.ORIENTATION_ROTATE_90){
                    Log.e("tag","카메라ExifInterface.ORIENTATION_ROTATE_90 :"+ExifInterface.ORIENTATION_ROTATE_90);
                    matrix.setRotate(90);
                }else if(isCamera && exifOrientation==ExifInterface.ORIENTATION_TRANSVERSE){
                    Log.e("tag","카메라ExifInterface.ORIENTATION_TRANSVERSE :"+ExifInterface.ORIENTATION_TRANSVERSE);
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                }else if(isCamera && exifOrientation==ExifInterface.ORIENTATION_ROTATE_270){
                    Log.e("tag","카메라ExifInterface.ORIENTATION_ROTATE_270 :"+ExifInterface.ORIENTATION_ROTATE_270);
                    matrix.setRotate(-90);
                }else{
                    Log.e("tag","들어오지마");
                }

                if(isCamera && exifOrientation==1){
                    Log.e("tag","exifOrientation"+exifOrientation);
                }else if(isCamera && exifOrientation==2){
                    Log.e("tag","exifOrientation"+exifOrientation);
                }else if(isCamera && exifOrientation==3){
                    Log.e("tag","exifOrientation"+exifOrientation);
                }else if(isCamera && exifOrientation==4){
                    Log.e("tag","exifOrientation"+exifOrientation);
                }else if(isCamera && exifOrientation==5){
                    Log.e("tag","exifOrientation"+exifOrientation);
                }else if(isCamera && exifOrientation==6){
                    Log.e("tag","exifOrientation"+exifOrientation);
                }else if(isCamera && exifOrientation==7){
                    Log.e("tag","exifOrientation"+exifOrientation);
                }else if(isCamera && exifOrientation==8){
                    Log.e("tag","exifOrientation"+exifOrientation);
                }else if(isCamera && exifOrientation==0){
                    Log.e("tag","exifOrientation"+exifOrientation);
                }


                if(isCamera && exifDegree==1){
                    Log.e("tag","exifDegree"+exifDegree);
                }else if(isCamera && exifDegree==2){
                    Log.e("tag","exifDegree"+exifDegree);
                }else if(isCamera && exifDegree==3){
                    Log.e("tag","exifDegree"+exifDegree);
                }else if(isCamera && exifDegree==4){
                    Log.e("tag","exifDegree"+exifDegree);
                }else if(isCamera && exifDegree==5){
                    Log.e("tag","exifDegree"+exifDegree);
                }else if(isCamera && exifDegree==6){
                    Log.e("tag","exifDegree"+exifDegree);
                }else if(isCamera && exifDegree==7){
                    Log.e("tag","exifDegree"+exifDegree);
                }else if(isCamera && exifDegree==8){
                    Log.e("tag","exifDegree"+exifDegree);
                }else if(isCamera && exifDegree==0){
                    Log.e("tag","exifDegree"+exifDegree);
                }

                // 카메라인 경우 이미지를 상황에 맞게 회전시킨다
//                try {


//                   exif = new ExifInterface(file.getAbsolutePath());
//                      exifOrientation = exif.getAttributeInt(
//                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

//                    int exifDegree = exifOrientationToDegrees(exifOrientation);
//                    Log.d(TAG,"exifDegree : " + exifDegree);
//                    Log.e(TAG,"exifDegree : " + exifDegree);
                      Log.e(TAG,"exifOrientation : " + exifOrientation);
//                    Log.e(TAG,"exifDegree : " + exifDegree);
                    originalBm = rotate(originalBm, exifOrientation);

//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }

            if(originalBm == null) {
                Log.e(TAG,("파일 에러"));
                return;
            }

            // 실제 사진 넓이,높이
            int width = originalBm.getWidth();
            int height = originalBm.getHeight();
            Log.e("tag","사진 width :"+width);
            Log.e("tag","사진 height :"+height);
            float aspect, scaleWidth, scaleHeight;
            // *사진돌아가는문제1 (해상도라고 생각했는데 아니었다.전혀틀림)
            // 해상도가 1080x1920 같은경우는 제대로 나오는데
            // 해상도가 4030x3200 이런식이면 자꾸 돌아가는 문제가 발생함
            // 오히려 가로가 더 크게나와서
            // *로그로 앨범,카메라에서 가져온 실제 사진의 width,height을 갖고오니 둘다 숫자가 똑같이나옴

            if(width > height) {
                Log.e("tag","width > height: 넓이가 더 클때 들어왔니?");
                if (width <= newWidth) return;

                aspect = (float) width / height;

                scaleWidth = newWidth;
                scaleHeight = scaleWidth / aspect;
            } else {
                Log.e("tag","width <= height: 높이가 더 크거나 같을때 들어왔니?");
                if(height <= newWidth) return;

                Log.e("tag","newWidth: "+newWidth+"의 값보다 height: "+height+"이 더 클때 들어올 수 있음");
                aspect = (float) height / width;
                Log.e("tag","사진 aspect :"+aspect);
                scaleHeight = newWidth;
                Log.e("tag","사진 scaleHeight :"+scaleHeight);
                scaleWidth = scaleHeight / aspect;
                Log.e("tag","사진 scaleWidth :"+scaleWidth);
            }

//			if(width <= newWidth) return;
//			float aspect = (float) width / height;
//			float scaleWidth = newWidth;
//			float scaleHeight = scaleWidth / aspect;

            // create a matrix for the manipulation
            Matrix matrix = new Matrix();

            // resize the bitmap
            // postScale 에서 안나눠주면 OutOfMemory 발생하네
            matrix.postScale(scaleWidth / width, scaleHeight / height); // 확대, 축소

//            matrix.postRotate(90); // 이미지 회전
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    Log.e("tag","앨범ORIENTATION_NORMAL :"+ExifInterface.ORIENTATION_NORMAL);
                    return ;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    Log.e("tag","앨범ORIENTATION_FLIP_HORIZONTAL :"+ExifInterface.ORIENTATION_NORMAL);
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.e("tag","ExifInterface.ORIENTATION_ROTATE_180 :"+ExifInterface.ORIENTATION_ROTATE_180);
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    Log.e("tag","ExifInterface.ORIENTATION_FLIP_VERTICAL :"+ExifInterface.ORIENTATION_FLIP_VERTICAL);
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    Log.e("tag","ExifInterface.ORIENTATION_TRANSPOSE :"+ExifInterface.ORIENTATION_TRANSPOSE);
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.e("tag","ExifInterface.ORIENTATION_ROTATE_90 :"+ExifInterface.ORIENTATION_ROTATE_90);
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    Log.e("tag","ExifInterface.ORIENTATION_TRANSVERSE :"+ExifInterface.ORIENTATION_TRANSVERSE);
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.e("tag","ExifInterface.ORIENTATION_ROTATE_270 :"+ExifInterface.ORIENTATION_ROTATE_270);
                    matrix.setRotate(-90);
                    break;
                default:
                    matrix.setRotate(90);
                    return;
            }
            // 출처: https://90000e.tistory.com/13 [구만이의 어린 프로그래밍] Matrix 함수
            // recreate the new Bitmap
            resizedBitmap = Bitmap.createBitmap(originalBm, 0, 0, width, height, matrix, true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(newFile));
            } else {
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 80, new FileOutputStream(newFile));

            }


        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } finally {

            if(originalBm != null){
                originalBm.recycle();
            }

            if (resizedBitmap != null){
                resizedBitmap.recycle();
            }
        }

    }

    /**
     * EXIF 정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
    public static int exifOrientationToDegrees(int exifOrientation)
        {
            Matrix matrix = new Matrix();
            if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
                Log.e("tag","Rotate1");
                return 90;
            }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
                Log.e("tag","Rotate2");
                return 180;
            }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
                Log.e("tag","Rotate3");
                return 270;
            }else if(exifOrientation == ExifInterface.ORIENTATION_NORMAL){
                Log.e("tag","Rotate4");
                return 0;
            }else if(exifOrientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL){
                Log.e("tag","Rotate5");
                matrix.setScale(-1, 1);
                return 180;
            }else if(exifOrientation == ExifInterface.ORIENTATION_FLIP_VERTICAL){
                Log.e("tag","Rotate6");
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                return 180;
            }else if(exifOrientation == ExifInterface.ORIENTATION_TRANSPOSE){
                Log.e("tag","Rotate7");
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                return 90;
            }else if(exifOrientation == ExifInterface.ORIENTATION_TRANSVERSE){
                Log.e("tag","Rotate8");
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                return -90;
            }else{
                Log.e("tag","여기로 들어오니?");
            }
        return 0;
    }

    /**
     * 이미지를 회전시킵니다.
     *
     * @param bitmap 비트맵 이미지
     * @param degrees 회전 각도
     * @return 회전된 이미지
     */
    public static Bitmap rotate(Bitmap bitmap, int degrees) {


        Log.e("tag","degrees"+degrees);
        Matrix matrix = new Matrix();
        switch (degrees) {
            case ExifInterface.ORIENTATION_NORMAL:
                Log.e("tag","카메라ORIENTATION_NORMAL :"+ExifInterface.ORIENTATION_NORMAL);
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                Log.e("tag","카메라ExifInterface.ORIENTATION_FLIP_HORIZONTAL :"+ExifInterface.ORIENTATION_FLIP_HORIZONTAL);
                matrix.setScale(-1, 1);
                return bitmap;
            case ExifInterface.ORIENTATION_ROTATE_180:
                Log.e("tag","카메라ExifInterface.ORIENTATION_ROTATE_180 :"+ExifInterface.ORIENTATION_ROTATE_180);
                matrix.setRotate(180,(float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                Log.e("tag","카메라ExifInterface.ORIENTATION_FLIP_VERTICAL :"+ExifInterface.ORIENTATION_FLIP_VERTICAL);
                matrix.setRotate(180,(float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                matrix.postScale(-1, 1);
                return bitmap;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                Log.e("tag","카메라ExifInterface.ORIENTATION_TRANSPOSE :"+ExifInterface.ORIENTATION_TRANSPOSE);
                matrix.setRotate(90,(float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                matrix.postScale(-1, 1);
                return bitmap;
            case ExifInterface.ORIENTATION_ROTATE_90:
                Log.e("tag","카메라ExifInterface.ORIENTATION_ROTATE_90 :"+ExifInterface.ORIENTATION_ROTATE_90);
                matrix.setRotate(90,(float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                return bitmap;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                Log.e("tag","카메라ExifInterface.ORIENTATION_TRANSVERSE :"+ExifInterface.ORIENTATION_TRANSVERSE);
                matrix.setRotate(-90,(float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                matrix.postScale(-1, 1);
                return bitmap;
            case ExifInterface.ORIENTATION_ROTATE_270:
                Log.e("tag","카메라ExifInterface.ORIENTATION_ROTATE_270 :"+ExifInterface.ORIENTATION_ROTATE_270);
                matrix.setRotate(-90,(float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                return bitmap;
            default:
                Log.e("tag","카메라 default");
                break;
        }


        if(degrees != 0 && bitmap != null)
        {

            matrix.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex) {

                ex.printStackTrace();
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }
}
