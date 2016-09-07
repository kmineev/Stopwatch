package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.views.TouchImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.spaceotechnologies.training.stopwatch.applications.MyApplication.getAppContext;

/**
 * Created by Kostez on 05.09.2016.
 */
public class PictureFragment extends Fragment {

    public static String PICTURE_EXTRA = "picture_extra";
    private static final String BACKGROUND_BITMAP = "background_bitmap";

    private Toolbar toolbar;
    private Drawable drawable;
    private Bitmap bitmap;
    private ImageButton imageButtonSetBackground;

    public PictureFragment(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imageButtonSetBackground = (ImageButton) getActivity().findViewById(R.id.button_set_background);

        imageButtonSetBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File f = new File(getAppContext().getCacheDir(), BACKGROUND_BITMAP);
                FileOutputStream fos = null;
                try {
                    f.createNewFile();
                    bitmap = ((BitmapDrawable) drawable).getBitmap();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    fos = new FileOutputStream(f);
                    fos.write(bitmapdata);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bitmap = ((BitmapDrawable) drawable).getBitmap();

                Intent intent = new Intent();
                intent.putExtra(PICTURE_EXTRA, getAppContext().getCacheDir() + "/" + BACKGROUND_BITMAP);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        TouchImageView touchImageView = (TouchImageView) getActivity().findViewById(R.id.picture_fragment_iv);
        touchImageView.setImageDrawable(drawable);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        setFullscreen();
    }

    public void setFullscreen() {
        setFullscreen(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.setVisibility(View.VISIBLE);
        exitFullscreen(getActivity());
    }

    public void setFullscreen(Activity activity) {

        if (Build.VERSION.SDK_INT > 10) {
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;

            if (isImmersiveAvailable()) {
                flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        } else {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void exitFullscreen(Activity activity) {
        if (Build.VERSION.SDK_INT > 10) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    public static boolean isImmersiveAvailable() {
        return android.os.Build.VERSION.SDK_INT >= 19;
    }
}