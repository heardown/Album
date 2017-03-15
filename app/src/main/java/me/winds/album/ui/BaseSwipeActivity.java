package me.winds.album.ui;

import android.os.Bundle;

import com.jude.swipbackhelper.SwipeBackHelper;

import me.winds.album.R;


/**
 * Auther by winds on 2016/12/15
 * Email heardown@163.com
 */
public abstract class BaseSwipeActivity extends BaseTransActivity {

    @Override
    protected void beforeLoad() {
        super.beforeLoad();
        SwipeBackHelper.onCreate(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    /**
     * 退出当前页面
     */
    protected void goOut() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


}
