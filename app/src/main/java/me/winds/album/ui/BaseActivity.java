package me.winds.album.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.winds.album.R;

/**
 * Created by Administrator on 2016/12/7.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeLoad();
        setContentView(getLayoutResId());//把设置布局文件的操作交给继承的子类
        ButterKnife.bind(this);
        mActivity = this;
        initView(savedInstanceState);
    }

    protected void beforeLoad(){
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Toast toast;
    protected void showTips(String msg) {
        if(toast == null) {
            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    protected Dialog dialog;
    protected void showDialog(View view) {
        Display display = getWindowManager().getDefaultDisplay();

        dialog = new Dialog(mActivity, R.style.AlertDialogTheme);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        //调整Dialog大小
        Window window = dialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (display.getWidth() * 0.75);
        window.setAttributes(attributes);
        dialog.show();
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract int getLayoutResId();

}
