package me.winds.album.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.winds.album.GloableValue;
import me.winds.album.R;
import me.winds.album.adapter.PhotoGridAdapter;
import me.winds.album.event.OnItemClickListener;
import me.winds.album.event.OnItemLongClickListener;
import me.winds.album.tools.AndroidLifeCycleUtils;
import me.winds.album.tools.CommonTools;
import me.winds.album.tools.FileUtils;
import me.winds.album.view.TitleView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author by Winds on 2016/11/11 0011.
 * Email heardown@163.com.
 */

public class DetailsActivity extends BaseSwipeActivity implements OnItemClickListener, OnItemLongClickListener, Observer, View.OnClickListener {
    @BindView(R.id.rv_details)
    RecyclerView rv_details;
    @BindView(R.id.title)
    TitleView title;

    private int mode;               //当前模式 1 图片  2 上位机
    private String path;            //路径
    private boolean isChecked;    //判断当前是否进入全选状态
    private PhotoGridAdapter mAdapter;
    private RequestManager mGlideRequestManager;

    private Subscription subscribe;

    private final byte[] image = new byte[153600];

    private boolean isChange = false;
    private boolean isFirst = true;

    public static Intent getIntent(Context context, String path, int mode) {
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra("path", path);
        i.putExtra("mode", mode);
        return i;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_details;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initAdapter();

        mode = getIntent().getIntExtra("mode", 1);
        switch (mode) {
            case 1:     //普通图片查看界面
                path = getIntent().getStringExtra("path");
                title.setCenterText(path.substring(path.lastIndexOf("/") + 1));
                mAdapter.setDirPath(path);
                break;
            case 2:     //上位机模式
                title.setCenterText(getString(R.string.title_device_mode));
//                factory = new IrSurveyFactory();
//                path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + GloableValue.FOLDER_DIR + File.separator + GloableValue.FOLDER_TEMP;
//                receiver = TcpReceiveClient.getInstance(AppConfig.dpcpIP, 3334);
//                receiver.setReceiveAvailable(true);
//                new Thread(new TcpReqClient(AppConfig.dpcpIP, 3333, factory.setDeviceMode(true))).start();

//                if (receiver.isRunning()) {
//                    Toasts.showCustomVerticalImageToast(this, getString(R.string.tips_receiveing), R.drawable.ic_success);
//                } else {
//                    Toasts.showVerticalToastWithNoticeImage(this, getString(R.string.tips_can_not_receive));
//                }

                break;

            case 3:     //被动接收图片
                title.setCenterText(getString(R.string.receive_image));
//                receiver = TcpReceiveClient.getInstance(AppConfig.dpcpIP, 3334);
//                receiver.setReceiveAvailable(true);
//                path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + GloableValue.FOLDER_DIR
//                        + File.separator + GloableValue.FOLDER_IMAGE + File.separator
//                        + CommonTools.getPreference(DetailsActivity.this, GloableValue.SP_IMAGE_FOLDER, getString(R.string.default_image_folder));
//                //// TODO
//                if (receiver.isRunning()) {
//                    Toasts.showCustomVerticalImageToast(this, getString(R.string.tips_send_image), R.drawable.ic_success);
//                } else {
//                    Toasts.showVerticalToastWithNoticeImage(this, getString(R.string.tips_can_not_receive));
//                }
        }

        title.title_left_txt.setVisibility(View.GONE);
        title.title_right_txt.setVisibility(View.GONE);

        title.title_left_image.setOnClickListener(this);
        title.title_left_txt.setOnClickListener(this);
        title.title_right_txt.setOnClickListener(this);
        title.title_right_image.setOnClickListener(this);

    }

    @Override
    public void update(java.util.Observable o, Object arg) {
//        if (((Messager) arg).getSign() == Messager.SIGN_DATA_IMAGE) {
//            byte[] data = ((Messager) arg).getData();
//            LogUtils.info("--> " + data.length);
//            if (mode == 2 || mode == 3) {
//                System.arraycopy(data, 0, image, 0, 153600);
//                subscribe = IrSurveyTools.getOriginalBitmapPath(image, 240, 320, path)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<String>() {
//                            @Override
//                            public void call(String s) {
//                                LogUtils.info(s);
//                                mAdapter.addItem(s);
//                            }
//                        });
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode == 1) {
            getNormalImage(path);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (isChecked) {
            title.setLeftText("");
            title.setRightText("");
            title.title_left_image.setVisibility(View.VISIBLE);
            title.title_right_image.setVisibility(View.VISIBLE);
            isChecked = false;
            mAdapter.clearSelection();
            mAdapter.setCheckEnable(isChecked);
            return;
        }


        if (isChange || mode == 3 || mode == 2) {
            setResult(RESULT_OK);
        }

        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
        if (subscribe != null) {
            subscribe.unsubscribe();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                //返回
                onBackPressed();
                break;
            case R.id.title_left_txt:
                if (isChecked) {
                    title.setLeftText("");
                    title.setRightText("");
                    title.title_left_image.setVisibility(View.VISIBLE);
                    title.title_right_image.setVisibility(View.VISIBLE);
                    isChecked = false;
                    mAdapter.clearSelection();
                    mAdapter.setCheckEnable(isChecked);
                    return;
                }

                break;
            case R.id.title_right_image:

                break;

            case R.id.title_right_txt:
                int count = mAdapter.getSelectedItemCount();
                if (count > 0) {
                    if (mode == 1 || mode == 3) {
                        showDialog(getNormalDialog(1, getString(R.string.tip), getString(R.string.delete_selected_image)));
                    } else if (mode == 2) {
                        showDialog(getNormalDialog(2, getString(R.string.tip), getString(R.string.save_selected_image)));
                    }
                } else {
                    showTips(getString(R.string.tips_not_selected));
                }
                break;
        }

    }

    @Override
    public void onItemClick(View v, int position) {
        ArrayList<String> list = new ArrayList<>(mAdapter.getList());

        Intent intent;
        if (mode == 1) {
            intent = ImagePagerActivity.getImageIntent(DetailsActivity.this, list, path, mode, position);
            startActivity(intent);
        } else if (mode == 2) {
            intent = ImagePagerActivity.getImageIntent(DetailsActivity.this, list, null, mode, position);
            startActivity(intent);
        } else {
//            intent = ImagePagerActivity.getImageIntent(DetailsActivity.this, list, null, mode, position);
        }

    }

    @Override
    public boolean onItemLongClick(View v, int position) {
        isChecked = true;
        title.title_left_image.setVisibility(View.GONE);
        title.title_right_image.setVisibility(View.GONE);
        title.setLeftText(getString(R.string.cancel));
        if (mode == 1 || mode == 3) {
            title.setRightText(getString(R.string.delete));
        } else {
            title.setRightText(getString(R.string.save));
        }
        mAdapter.setCheckEnable(isChecked);
        return true;
    }


    private void initAdapter() {
        mGlideRequestManager = Glide.with(this);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rv_details.setLayoutManager(mLayoutManager);
        rv_details.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PhotoGridAdapter(this, mGlideRequestManager);
        rv_details.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        rv_details.addOnScrollListener(new OnScrollMonitor());
    }



    private void getNormalImage(String dir) {
        subscribe = Observable.just(dir)
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {

                        return Arrays.asList(new File(s).list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                if (name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".jpg"))
                                    return true;
                                return false;
                            }
                        }));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> list) {
                        if (isFirst) {
                            isFirst = false;
                        } else {
                            if (list.size() != mAdapter.getItemCount()) {
                                isChange = true;
                            }
                        }
                        mAdapter.setList(list);
                    }
                });
    }


    private View getNormalDialog(final int mode, String titleMsg, String contentMsg) {
        View view = View.inflate(this, R.layout.dialog_normal, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView message = (TextView) view.findViewById(R.id.message);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        title.setText(titleMsg);
        message.setText(contentMsg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = mAdapter.getSelectedList();

                if (mode == 1) {
                    //TODO 删除文件夹
                    deleteFile(list);
                } else {
                    final String s = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + GloableValue.FOLDER_DIR
                            + File.separator + GloableValue.FOLDER_IMAGE + File.separator
                            + CommonTools.getPreference(DetailsActivity.this, GloableValue.SP_IMAGE_FOLDER, getString(R.string.default_image_folder));
                    copySelectedFile(list, s);

                }
                dialog.dismiss();


            }
        });
        return view;
    }


    //    private Display display;
    private Dialog dialog;


    protected void showDialog(View view) {
        Display display = getWindowManager().getDefaultDisplay();

        dialog = new Dialog(this, R.style.AlertDialogTheme);
        dialog.setContentView(view);
        //调整Dialog大小
        Window window = dialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (display.getWidth() * 0.75);
        window.setAttributes(attributes);
        dialog.show();
    }


    private void copySelectedFile(List<String> list, final String dir) {
        isChange = true;
        final KProgressHUD hud = KProgressHUD.create(this).setLabel(getString(R.string.tips_saveing)).show();
        Observable.from(list)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return FileUtils.copyFile(s, dir + File.separator + System.currentTimeMillis() + ".jpeg");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        hud.dismiss();
                        mAdapter.deleteSelectedList();
                        if (isChecked) {
                            title.setLeftText("");
                            title.setRightText("");
                            title.title_left_image.setVisibility(View.VISIBLE);
                            title.title_right_image.setVisibility(View.VISIBLE);
                            isChecked = false;
                            mAdapter.clearSelection();
                            mAdapter.setCheckEnable(isChecked);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String aBoolean) {
                        if (!TextUtils.isEmpty(aBoolean)) {
                        }
                    }
                });
    }


    private void deleteFile(List<String> list) {
        isChange = true;
        final KProgressHUD hud = KProgressHUD.create(this).setLabel(getString(R.string.tips_deleteing)).show();
        Observable.from(list)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return path + File.separator + s;
                    }
                })
                .map(new Func1<String, Void>() {
                    @Override
                    public Void call(String s) {
                        FileUtils.deleteFile(s);
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        hud.dismiss();
                        mAdapter.deleteSelectedList();
                        if (isChecked) {
                            title.setLeftText("");
                            title.setRightText("");
                            title.title_left_image.setVisibility(View.VISIBLE);
                            title.title_right_image.setVisibility(View.VISIBLE);
                            isChecked = false;
                            mAdapter.clearSelection();
                            mAdapter.setCheckEnable(isChecked);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }


    private void resumeRequestsIfNotDestroyed() {
        if (!AndroidLifeCycleUtils.canLoadImage(this)) {
            return;
        }
        mGlideRequestManager.resumeRequests();
    }


    class OnScrollMonitor extends RecyclerView.OnScrollListener {
        private int SCROLL_THRESHOLD = 30;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (Math.abs(dy) > SCROLL_THRESHOLD) {
                mGlideRequestManager.pauseRequests();
            } else {
                resumeRequestsIfNotDestroyed();
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                resumeRequestsIfNotDestroyed();
            }
        }
    }

}
