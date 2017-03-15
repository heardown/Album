package me.winds.album.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import butterknife.ButterKnife;
import me.winds.album.R;

/**
 * Created by Winds on 2016/10/12 0012.
 */
public abstract class BaseFragment extends Fragment {
    public Activity mActivity;
    protected Dialog dialog;
    protected Display display;

    private Toast toast;
    protected void showTips(String msg) {
        if(toast == null) {
            Toast.makeText(mActivity, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getViewResourceId(), null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public abstract int getViewResourceId();

    public abstract void initView(View view, Bundle savedInstanceState);

    protected void showDialog(View view) {
        display = mActivity.getWindowManager().getDefaultDisplay();

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


}
