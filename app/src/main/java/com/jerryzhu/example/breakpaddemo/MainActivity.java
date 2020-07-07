package com.jerryzhu.example.breakpaddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.jerryzhu.lib.breakpad.Breakpad;
import java.io.File;

/**
 * @Auther: JerryZhu
 * @datetime: 2020/7/7
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "breakpad";

    private static final int REQUEST_CODE = 100;
    private File mCrashDumpDir;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public native String doCrash();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        } else {
            initBreakpad();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                initBreakpad();
            }else{
                Log.d(TAG,"no write_external_storage permission !");
                Toast.makeText(this,"no write_external_storage permission !",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initBreakpad() {

        if(mCrashDumpDir == null){
             mCrashDumpDir = new File(getExternalCacheDir(), "breakpad/crash_dump");
        }

        if(!mCrashDumpDir.exists()){
            mCrashDumpDir.mkdirs();
        }

        Breakpad.init(mCrashDumpDir.getAbsolutePath());

    }

    public void doCrash(View view) {
        doCrash();
    }
}
