package zuiapp.ui.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioRecordingConfiguration;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidz.util.ThreadUtil;
import timber.log.Timber;
import zuiapp.databinding.ActivityAudioBinding;
import zuiapp.ui.activity.BaseActivity;

public class AudioActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {
    private ActivityAudioBinding binding;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private AudioManager mAudioManager;
    private AudioRecord audioRecord;
    private int sampleRateHz = 48000;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    private int bufferSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        binding = ActivityAudioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.setTitle("录音");

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mHandlerThread = new HandlerThread("Audio");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), this);

        bufferSize = AudioRecord.getMinBufferSize(sampleRateHz, channelConfig, audioFormat);

        binding.play.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandler = null;
        }
        if (audioRecord != null) {
            audioRecord.release();
        }
    }

    @Override
    public void onClick(View v) {
        if (audioRecord == null || audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
            record();
            binding.time.setBase(SystemClock.elapsedRealtime());
            binding.time.start();
        } else {
            stop();
            binding.time.stop();
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void record() {
        Timber.i("record");
        if (audioRecord == null) {
            audioRecord = new AudioRecord.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(audioFormat)
                            .setSampleRate(sampleRateHz)
                            .setChannelMask(channelConfig)
                            .build())
                    .setBufferSizeInBytes(bufferSize * 2)
                    .build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                audioRecord.registerAudioRecordingCallback(ThreadUtil.getExecutor(), new AudioManager.AudioRecordingCallback() {
                    @Override
                    public void onRecordingConfigChanged(List<AudioRecordingConfiguration> configs) {
                        super.onRecordingConfigChanged(configs);
                        Timber.i("AudioRecord onRecordingConfigChanged ");
                    }
                });
            }
        }
        if (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
            audioRecord.startRecording();
            readAudio();
        }
    }

    private void stop() {
        Timber.i("stop ");
        if (audioRecord != null && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            audioRecord.stop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void readAudio() {
        File pcmFile = createPcmFile();
        if (pcmFile.exists()) {
            pcmFile.delete();
        }
        final byte[] buffer = new byte[bufferSize];
        audioRecord.startRecording();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(pcmFile);
                    while (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                        int readStatus = audioRecord.read(buffer, 0, bufferSize);
                        Timber.d("readStatus=" + readStatus);
                        if (readStatus > 0) {
                            stream.write(buffer, 0, readStatus);
                        }
                    }
                } catch (IOException e) {
                    Timber.e(e);
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }

    private File createPcmFile() {
        File pcmFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "raw.pcm");
        Timber.d("pcm file=" + pcmFile);
        return pcmFile;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                String perm = permissions[i];
                if (perm.equals(Manifest.permission.RECORD_AUDIO) && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showToast("录音未授权");
                    finish();
                }
                Timber.d(perm + " " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));
            }
        }
    }

}