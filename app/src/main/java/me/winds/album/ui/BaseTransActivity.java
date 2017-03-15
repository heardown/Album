package me.winds.album.ui;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by Winds on 2016/12/12.
 */

public abstract class BaseTransActivity extends BaseActivity {
    @Override
    protected void beforeLoad() {
        super.beforeLoad();
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
    }
}
