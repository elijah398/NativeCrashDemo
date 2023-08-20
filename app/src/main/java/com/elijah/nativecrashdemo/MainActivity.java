package com.elijah.nativecrashdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.elijah.nativecrashdemo.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'nativecrashdemo' library on application startup.
    static {
        System.loadLibrary("nativecrashdemo");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init(this);

        // Example of a lead to a native crash
        Button bt = binding.crashButton;

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nativeCrash();
            }
        });
    }

    public static void init(Context context){
        Context applicationContext = context.getApplicationContext();
        File file = new File(applicationContext.getExternalCacheDir(),"native_crash");
        if(!file.exists()){
            file.mkdirs();
        }
        initNative(file.getAbsolutePath());
    }

    /**
     * 模拟崩溃
     */
    public static native void nativeCrash();

    /**
     * 初始化 breakpad
     * @param path
     */
    private static native void initNative(String path);
}