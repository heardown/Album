package me.winds.album.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import me.winds.album.GloableValue;
import me.winds.album.R;
import me.winds.album.adapter.ImagePagerAdapter;
import me.winds.album.event.OnItemClickListener;
import me.winds.album.tools.CommonTools;
import me.winds.album.tools.FileUtils;
import me.winds.album.tools.IrSurveyTools;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ImagePagerFragment extends Fragment implements OnItemClickListener, View.OnClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.vp_photos)
    ViewPager mViewPager;

    @BindView(R.id.trabecula)
    View trabecula;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.iv_temp)
    ImageView iv_temp;

    @BindView(R.id.iv_more)
    ImageView iv_more;

    @BindView(R.id.tv_name)
    TextView tv_name;

    private Activity mActivity;
    private static final String TYPE = "type";
    private static final String DATA = "data";
    private static final String POSITION = "position";
    private static final String DIR = "dir";
    private static final String MODE = "mode";

    private int type;
    private String dir;
    private ArrayList<String> list;
    private ImagePagerAdapter mPagerAdapter;
    private int mPosition = 0;

    private String mCurrentPath;

    public static ImagePagerFragment newInstance(List<String> paths, String dir, int position) {
        ImagePagerFragment f = new ImagePagerFragment();
        Bundle args = new Bundle();
        args.putStringArray(DATA, paths.toArray(new String[paths.size()]));
        args.putInt(POSITION, position);
        args.putString(DIR, dir);
        f.setArguments(args);
        return f;
    }


    public static ImagePagerFragment newInstance(List<String> paths, String dir, int mode, int position) {
        ImagePagerFragment f = new ImagePagerFragment();
        Bundle args = new Bundle();
        args.putStringArray(DATA, paths.toArray(new String[paths.size()]));
        args.putInt(POSITION, position);
        args.putString(DIR, dir);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String[] data = bundle.getStringArray(DATA);

            if (data != null) {
                list = new ArrayList<>(Arrays.asList(data));
            }
            mPosition = bundle.getInt(POSITION);
            dir = bundle.getString(DIR);
            type = TextUtils.isEmpty(dir) ? 2 : 1; //1 有根路径 2 无根路径
        }
        mActivity = getActivity();


//        ShareSDK.initSDK(mActivity);
        mPagerAdapter = new ImagePagerAdapter(Glide.with(this), list, dir);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_image_pager, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_temp.setImageResource(type == 1 ? R.drawable.ic_delete : R.drawable.ic_save);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOffscreenPageLimit(3);
        mPagerAdapter.setOnItemClickListener(this);
        iv_back.setOnClickListener(this);
        iv_temp.setOnClickListener(this);
        iv_more.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                trabecula.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onDestroy() {
        list.clear();
        list = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //todo 返回上级列表
                mActivity.finish();
                break;
            case R.id.iv_temp:
                if (type == 1) {
                    String path = dir + File.separator + mPagerAdapter.getItem(mPosition);
                    int size = mPagerAdapter.remove(mPosition);
                    FileUtils.deleteFile(path);
                    if (size == 0) {
                        //list为空的情况下退出当前全局查看模式
                        mActivity.finish();
                    }

                } else {
                    //TODO 上位机模式
                    final String item = mPagerAdapter.getItem(mPosition);
                    final String s = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + GloableValue.FOLDER_DIR
                            + File.separator + GloableValue.FOLDER_IMAGE + File.separator
                            + CommonTools.getPreference(mActivity, GloableValue.SP_IMAGE_FOLDER, getString(R.string.default_image_folder))
                            + File.separator + System.currentTimeMillis() + ".jpeg";
                    IrSurveyTools.copyFile(item, s)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(String s) {
                                    if(TextUtils.isEmpty(s)) {
                                        showTips(getString(R.string.tips_save_failed));
                                    } else  {
                                        showTips(getString(R.string.tips_save_success));
                                    }
                                }
                            });

                }
                break;
            case R.id.iv_more:
                //todo  弹出PopWindow
                showPopwindow(trabecula);
                break;
        }
    }

    private Toast toast;
    protected void showTips(String msg) {
        if(toast == null) {
            Toast.makeText(mActivity, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    //todo popwindow列表点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentPath = type == 2 ? mPagerAdapter.getItem(mPosition) : dir + File.separator + mPagerAdapter.getItem(mPosition);
        popupWindow.dismiss();
        switch (position) {
            case 0:

                break;
            case 1:
//                mActivity.startActivity(ImageDetailsActivity.getIntent(mActivity, mCurrentPath));
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        //响应点击  横幅隐藏或显示
        trabecula.setVisibility(trabecula.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        if(trabecula.getVisibility() == View.VISIBLE) {
            mCurrentPath = type == 2 ? mPagerAdapter.getItem(position) : dir + File.separator + mPagerAdapter.getItem(position);
            //设置当前名称
            tv_name.setText(mCurrentPath.substring(mCurrentPath.lastIndexOf("/") + 1));
        }
    }


    PopupWindow popupWindow;
    private void showPopwindow(View v) {
        View view = View.inflate(mActivity, R.layout.pop_image_pager, null);
        ListView lv_setting = (ListView) view.findViewById(R.id.lv_setting);
        lv_setting.setOnItemClickListener(this);

        if (type == 1) {
            String[] item = getResources().getStringArray(R.array.popwindow_image_pager_double);
            lv_setting.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, item));
        } else {
            String[] item =  getResources().getStringArray(R.array.popwindow_image_pager_single);
            lv_setting.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, item));
        }
        popupWindow = new PopupWindow(view, CommonTools.dip2px(mActivity, 115), WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popupWindow.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, CommonTools.dip2px(mActivity, 20),
                CommonTools.dip2px(mActivity, 80));

    }

}
