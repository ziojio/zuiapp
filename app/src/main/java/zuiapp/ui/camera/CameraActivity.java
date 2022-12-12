package zuiapp.ui.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import timber.log.Timber;
import zuiapp.databinding.ActivityCameraBinding;
import zuiapp.ui.activity.BaseActivity;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private ActivityCameraBinding binding;
    private Camera mCamera;
    private long lastTime = 0;
    private Handler handler = new Handler();
    private Bitmap mBitmap = null;
    private PointF myMidPoint;
    /**
     * 中心点坐标
     */
    private Point centerPoint;
    /**
     * 半径
     */
    private int radius;
    private CircleCameraListener circleCameraListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.setTitle("相机");

        // 使用View的方式
        // CircleCameraPreview circleCameraPreview = binding.surfaceView;
        // TextView tvTips = binding.msg;
        // circleCameraPreview.setOnCircleCameraListener(state -> {
        //     switch (state) {
        //         case NONE:
        //             tvTips.setText("请将人脸移至中心位置");
        //             break;
        //
        //         case ONE:
        //             tvTips.setText("检测到人脸,请拍摄");
        //             break;
        //
        //         case MODE:
        //             tvTips.setText("请不要合拍");
        //             break;
        //         case TAKE:
        //             tvTips.setText("图片处理中，请稍候...");
        //             break;
        //     }
        // });

        init();
    }

    private void init() {
        SurfaceView surfaceView = binding.surfaceView;
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Timber.i("surfaceCreated");
        openCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Timber.i("surfaceChanged format=%d, width=%d, height=%d", format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Timber.i("surfaceDestroyed");
        closeCamera();
    }

    byte[] buffer = new byte[ImageFormat.getBitsPerPixel(ImageFormat.NV21) * 800 * 600 / 8];
    long x = 0;

    /**
     * 关闭相机
     */
    private void closeCamera() {
        Timber.i("closeCamera");
        synchronized (this) {
            if (mCamera == null) {
                return;
            }
            Timber.i("closeCamera");
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /*
     * 设置自动对焦
     */
    private void setFocusMode(Camera.Parameters parameters) {
        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        Timber.i(String.valueOf(supportedFocusModes));
        if (supportedFocusModes != null && supportedFocusModes.size() > 0) {
            if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
        }
    }

    /*
     * 设置方向
     */
    private void setCameraRotation(Camera camera, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        // 获取当前手机的旋转角度
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 获取最大支持 4:3 图像尺寸
     *
     * @param params 参数
     * @return 最大尺寸
     */
    private Camera.Size getMaxPictureSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPictureSizes();
        int maxArea = -1;
        Camera.Size maxSize = null;

        for (Camera.Size size : previewSizes) {
            Timber.i("Support size -> " + size.width + " x " + size.height);

            int gcd = gcd(size.width, size.height);
            int w = size.width / gcd;
            int h = size.height / gcd;
            if (size.width > 1000) {
                continue;
            }
            if (w == 4 && h == 3 && (size.width * size.height) > maxArea) {
                maxArea = size.width * size.height;
                maxSize = size;
            }
        }
        if (maxSize != null) {
            Timber.i("Max 4:3 -> " + maxSize.width + " x " + maxSize.height);
        }
        return maxSize;
    }

    /**
     * 计算最大公约数
     *
     * @return 最大公约数
     */
    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    /**
     * 打开相机
     */
    private void openCamera(SurfaceHolder holder) {
        Timber.i("openCamera");
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            Timber.e("相机开始失败");
            closeCamera();
            return;
        }
        Camera.Parameters params = mCamera.getParameters();
        // setFocusMode(params);
        // setCameraRotation(mCamera, Camera.CameraInfo.CAMERA_FACING_FRONT);

        // 获取支持的最大的 4:3 比例尺寸图片尺寸
        Camera.Size maxSize = getMaxPictureSize(params);
        // 设置预览尺寸为图像尺寸
        Timber.i("openCamera: w = " + maxSize.width + " h =  " + maxSize.height);
        params.setPreviewSize(maxSize.width, maxSize.height);
        params.setPictureSize(maxSize.width, maxSize.height);
        // 设置预览编码图像编码格式为 NV21
        params.setPreviewFormat(ImageFormat.NV21);
        mCamera.setParameters(params);
        mCamera.setPreviewCallbackWithBuffer(this);
        mCamera.addCallbackBuffer(buffer);
        mCamera.startPreview();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Timber.d("onPreviewFrame: " + data);
        if (data == null || data.length == 0 || System.currentTimeMillis() - lastTime <= 2000 || camera == null) {

        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    checkFace(data, camera);
                }
            });
            lastTime = System.currentTimeMillis();
        }
        mCamera.addCallbackBuffer(buffer);
    }

    private void checkFace(byte[] mData, Camera camera) {
        Timber.d("checkFace camera=" + camera);
        if (camera != null) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            ByteArrayOutputStream mBitmapOutput = new ByteArrayOutputStream();

            YuvImage yuvImage = new YuvImage(mData, ImageFormat.NV21, size.width, size.height, null);
            yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, mBitmapOutput);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;//必须设置为565，否则无法检测
            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, 1.0f);
            matrix.preRotate(270.0f);

            Bitmap bitmapDetect = BitmapFactory.decodeByteArray(mBitmapOutput.toByteArray(), 0, mBitmapOutput.toByteArray().length, options);
            if (bitmapDetect != null) {
                mBitmapOutput.reset();

                bitmapDetect = Bitmap.createBitmap(bitmapDetect, 0, 0, bitmapDetect.getWidth(), bitmapDetect.getHeight(), matrix, false);
                mBitmap = bitmapDetect;
                FaceDetector faceDetector = new FaceDetector(mBitmap.getWidth(), mBitmap.getHeight(), 2);
                FaceDetector.Face[] face = new FaceDetector.Face[2];
                int faces = faceDetector.findFaces(mBitmap, face);
                if (faces > 0) {
                    if (faces > 1) {
                        // circleCameraListener.onObserver(State.MODE);
                    } else {
                        faceInfo(face[0]);
                        // isInCircle(face[0]);
                    }
                }
            }
        }
    }

    private void startFaceDetection() {
        // Try starting Face Detection
        Camera.Parameters params = mCamera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0) {
            // camera supports face detection, so can start it:
            mCamera.startFaceDetection();
        }
    }

    private void faceInfo(FaceDetector.Face face1) {
        Timber.d("isInCircle face=" + face1);
        Timber.d("isInCircle confidence=" + face1.confidence());
        Timber.d("isInCircle eyesDistance=" + face1.eyesDistance());
        Timber.d("isInCircle EULER_X=" + face1.pose(FaceDetector.Face.EULER_X));
        Timber.d("isInCircle EULER_Y=" + face1.pose(FaceDetector.Face.EULER_Y));
        Timber.d("isInCircle EULER_Z=" + face1.pose(FaceDetector.Face.EULER_Z));
    }

    /**
     * 计算四个方位点是否在圆内
     */
    private void isInCircle(FaceDetector.Face face1) {
        face1.getMidPoint(myMidPoint);
        float myEyesDistance = face1.eyesDistance();   //得到人脸中心点和眼间距离参数
        //x1坐标与圆心的x坐标的距离
        int leftPoint = (int) (myMidPoint.x - myEyesDistance);
        //y1坐标与圆心的y坐标的距离
        int topPoint = (int) (myMidPoint.y - myEyesDistance);
        //x2坐标与圆心的x坐标的距离
        int rightPoint = (int) (myMidPoint.x + myEyesDistance);
        //y2坐标与圆心的x坐标的距离
        int bottomPoint = (int) (myMidPoint.y + 1.5 * myEyesDistance);

        int distanceX1 = Math.abs(centerPoint.x - leftPoint);
        int distanceY1 = Math.abs(centerPoint.y - topPoint);

        int distanceX2 = Math.abs(centerPoint.x - rightPoint);
        int distanceY2 = Math.abs(centerPoint.y - bottomPoint);

        int distanceZ1 = (int) Math.sqrt(Math.pow(distanceX1, 2) + Math.pow(distanceY1, 2));
        int distanceZ2 = (int) Math.sqrt(Math.pow(distanceX2, 2) + Math.pow(distanceY1, 2));
        int distanceZ3 = (int) Math.sqrt(Math.pow(distanceX1, 2) + Math.pow(distanceY2, 2));
        int distanceZ4 = (int) Math.sqrt(Math.pow(distanceX2, 2) + Math.pow(distanceY2, 2));

        if (distanceZ1 > radius || distanceZ2 > radius || distanceZ3 > radius || distanceZ4 > radius) {
            circleCameraListener.onObserver(State.NONE);
        } else {
            circleCameraListener.onObserver(State.ONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                String perm = permissions[i];
                if (perm.equals(Manifest.permission.CAMERA) && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showToast("摄像头未授权");
                    finish();
                }
                Timber.d(perm + " " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));
            }
        }
    }


    public enum State {
        NONE, ONE, MODE, TAKE
    }

    public interface CircleCameraListener {
        void onObserver(State state);
    }

    static class MyFaceDetectionListener implements Camera.FaceDetectionListener {

        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            if (faces.length > 0) {
                Log.d("FaceDetection", "face detected: " + faces.length +
                        " Face 1 Location X: " + faces[0].rect.centerX() +
                        "Y: " + faces[0].rect.centerY());
            }
        }
    }


}