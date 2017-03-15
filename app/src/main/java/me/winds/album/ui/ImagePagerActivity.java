package me.winds.album.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

import me.winds.album.R;

/**
 * Author by Winds on 2016/11/14 0014.
 * Email heardown@163.com.
 */

public class ImagePagerActivity extends BaseActivity {
    private static final String TYPE = "type";
    private static final String DATA = "data";
    private static final String DIR = "dir";
    private static final String POSITION = "position";

    public static Intent getImageIntent(Context context, ArrayList<String> list, String dir, int type, int position) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(DIR, dir);
        intent.putExtra(POSITION, position);
        intent.putExtra(DATA, (Serializable)list);
        return  intent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_pager;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int type = intent.getIntExtra(TYPE, 1);

        int index = intent.getIntExtra(POSITION, 0);
        ArrayList<String> list = (ArrayList<String>) intent.getSerializableExtra(DATA);
        switch (type) {
            case 1:
            case 2:
                String dir = intent.getStringExtra(DIR);
                ImagePagerFragment fragment = ImagePagerFragment.newInstance(list, dir, index);
                addImagePagerFragment(fragment);

                break;

            case 3:

                break;
        }

    }

    public void addImagePagerFragment(ImagePagerFragment imagePagerFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, imagePagerFragment)
                .commit();
    }

    public void addImagePagerFragment(Fragment imagePagerFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, imagePagerFragment)
                .addToBackStack(null)
                .commit();
    }
}
