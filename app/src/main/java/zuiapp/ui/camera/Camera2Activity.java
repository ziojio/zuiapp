package zuiapp.ui.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import timber.log.Timber;
import zuiapp.databinding.ActivityCamera2Binding;
import zuiapp.ui.activity.BaseActivity;

public class Camera2Activity extends BaseActivity implements SurfaceHolder.Callback, ImageReader.OnImageAvailableListener {
    private ActivityCamera2Binding binding;
    private CameraDevice mCamera;
    private CameraManager mCameraManager;
    private ImageReader mImageReader;
    private CameraCharacteristics mCharacteristics;
    private CameraCaptureSession mCameraCaptureSession;
    private HandlerThread mHandlerThread;
    private String FACING_BACK = "0";
    private String FACING_FRONT = "1";
    private String mFacing = FACING_FRONT;
    private Handler mHandler;
    private OrientationLiveData relativeOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        binding = ActivityCamera2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.setTitle("相机");
        binding.surfaceView.getHolder().addCallback(this);

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        mHandlerThread = new HandlerThread("Camera");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        binding.takePicture.setOnClickListener(v -> {
            if (mCameraCaptureSession != null) {
                takePicture();
            }
        });

        Rect rect = new Rect();
        DisplayMetrics metrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getDisplay().getRealMetrics(metrics);
            getDisplay().getRectSize(rect);
        }

        Timber.d("Camera2 " + rect);
        Timber.d("Camera2 " + metrics);
        mHandler.post(() -> {
            if (1 == 1) return;
            try {
                String[] ids = mCameraManager.getCameraIdList();
                Timber.d("Camera ------------------------------------------");
                for (String id : ids) {
                    Timber.d("Camera.Id " + id);
                    CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(id);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        Timber.d("CameraCharacteristics.INFO_VERSION " + characteristics.get(CameraCharacteristics.INFO_VERSION));
                    }
                    Timber.d("CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL " + characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL));
                    Timber.d("CameraCharacteristics.LENS_FACING " + characteristics.get(CameraCharacteristics.LENS_FACING));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Timber.d("CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE " + characteristics.get(CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE).toString());
                    }
                }
                Timber.d("Camera ------------------------------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCamera();
        mHandlerThread.quit();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Timber.i("surfaceCreated");
        openCamera();
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

    /**
     * 打开相机
     */
    @SuppressLint("MissingPermission")
    private void openCamera() {
        Timber.i("openCamera");
        try {
            mCameraManager.openCamera(mFacing, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    Timber.i("onOpened: " + camera);
                    initCamera2(camera);
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    Timber.i("onDisconnected: " + camera);
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    String s;
                    switch (error) {
                        case ERROR_CAMERA_DEVICE:
                            s = "Fatal (device)";
                            break;
                        case ERROR_CAMERA_DISABLED:
                            s = "Device policy";
                            break;
                        case ERROR_CAMERA_IN_USE:
                            s = "Camera in use";
                            break;
                        case ERROR_CAMERA_SERVICE:
                            s = "Fatal (service)";
                            break;
                        case ERROR_MAX_CAMERAS_IN_USE:
                            s = "Maximum cameras in use";
                            break;
                        default:
                            s = "Unknown";
                            break;
                    }
                    Timber.i("onError: " + camera + " error=" + s);
                }
            }, mHandler);
        } catch (CameraAccessException e) {
            Timber.e(e);
        }
    }

    private void initCamera2(CameraDevice camera) {
        Timber.i("initCamera2");
        mCamera = camera;
        try {
            mCharacteristics = mCameraManager.getCameraCharacteristics(camera.getId());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        // Used to rotate the output media to match device orientation
        runOnUiThread(() -> {
            if (relativeOrientation != null) {
                 relativeOrientation.removeObservers(this);
            }
            relativeOrientation = new OrientationLiveData(this, mCharacteristics);
            relativeOrientation.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    Timber.i("relativeOrientation onChanged: " + integer);
                }
            });
        });
        StreamConfigurationMap map = mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Timber.i("OutputFormats: " + Arrays.toString(map.getOutputFormats()));
        Size[] sizes = map.getOutputSizes(ImageFormat.JPEG);
        Timber.i("OutputSizes: " + Arrays.toString(sizes));
        Size size = CameraUtil.getPictureSize(sizes, 1280 * 960);
        Timber.i("PictureSize: " + size);
        mImageReader = ImageReader.newInstance(size.getWidth(), size.getHeight(), ImageFormat.JPEG, 2);

        List<Surface> targets = List.of(binding.surfaceView.getHolder().getSurface(), mImageReader.getSurface());

        try {
            mCamera.createCaptureSession(targets, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    Timber.i("onConfigured: session=" + session);
                    // This will keep sending the capture request as frequently as possible until the
                    // session is torn down or session.stopRepeating() is called
                    mCameraCaptureSession = session;
                    try {
                        CaptureRequest.Builder captureRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        captureRequest.addTarget(binding.surfaceView.getHolder().getSurface());
                        // captureRequest.addTarget(mImageReader.getSurface());
                        mCameraCaptureSession.setRepeatingRequest(captureRequest.build(), null, mHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭相机
     */
    private void closeCamera() {
        Timber.i("closeCamera " + mCamera);
        if (mCamera != null) {
            mCamera.close();
            mCamera = null;
        }
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        Timber.i("onImageAvailable: " + reader);
        try {
            Image image = reader.acquireLatestImage();
            Timber.i("Width: " + image.getWidth());
            Timber.i("Height: " + image.getHeight());
            Timber.i("Format: " + image.getFormat());
            File file = CameraUtil.createPictureFile(getCacheDir(), "IMG", "jpg");
            OutputStream output = new FileOutputStream(file);
            Timber.i("file: " + file.getAbsolutePath());
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            Timber.i("capacity: " + buffer.capacity());
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            image.close();

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
            Matrix matrix = new Matrix();
            matrix.postRotate(relativeOrientation.getValue());
            bitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            Timber.i("result: " + result);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ExifInterface exif = new ExifInterface(file);
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, "270");
                exif.saveAttributes();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes a picture.
     */
    private void takePicture() {
        try {
            CaptureRequest.Builder captureBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG);
            captureBuilder.addTarget(mImageReader.getSurface());

            int sensorOrientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            // Timber.i("SensorOrientation " + sensorOrientation);
            // Timber.i("DisplayRotation " + getRotationDegree(getDisplay().getRotation()));

            // captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, 0);

            mImageReader.setOnImageAvailableListener(this, mHandler);
            mCameraCaptureSession.capture(captureBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                    Timber.i("onCaptureStarted: timestamp=" + timestamp + " frameNumber=" + frameNumber);

                }

                @Override
                public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                    super.onCaptureFailed(session, request, failure);
                    Timber.i("onCaptureFailed: failure=" + failure);
                    Timber.i("onCaptureFailed: failure=" + failure.getReason());
                }

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Timber.i("onCaptureCompleted: result=" + result);
                    // mImageReader.setOnImageAvailableListener(null, null);
                }
            }, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
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

}