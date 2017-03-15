package me.winds.album.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import me.winds.album.GloableValue;
import me.winds.album.R;
import me.winds.album.adapter.AlbumAdapter;
import me.winds.album.adapter.GalleryDecoration;
import me.winds.album.bean.Folder;
import me.winds.album.loader.LoaderUtils;
import me.winds.album.tools.CommonTools;
import me.winds.album.tools.FileUtils;
import me.winds.album.tools.PathUtils;
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
public class MainActivity extends BaseTransActivity implements AlbumAdapter.ItemClickListener, AlbumAdapter.ItemLongClickListener, AlbumAdapter.OnItemCheckListener, View.OnClickListener {

    @BindView(R.id.rv_gallery)
    RecyclerView rv_gallery;

    @BindView(R.id.title)
    TitleView titleView;

    private AlbumAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Subscription subscribe;
    PopupWindow popupWindow;

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitle();

        adapter = new AlbumAdapter(mActivity, Glide.with(this));
        mLayoutManager = new LinearLayoutManager(mActivity);
        rv_gallery.setLayoutManager(mLayoutManager);

        rv_gallery.setAdapter(adapter);
        rv_gallery.setItemAnimator(new DefaultItemAnimator());
        GalleryDecoration decoration = new GalleryDecoration(mActivity, GalleryDecoration.VERTICAL_LIST);
        rv_gallery.addItemDecoration(decoration);

        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        adapter.setOnItemCheckListener(this);
        load();
    }

    private void initTitle() {
        titleView.setVisibility(View.VISIBLE);
        titleView.title_left_txt.setVisibility(View.GONE);
        titleView.title_right_txt.setVisibility(View.GONE);
        titleView.title_right_minor_txt.setVisibility(View.GONE);

        titleView.title_right_image.setVisibility(View.VISIBLE);
        titleView.title_left_txt.setOnClickListener(this);
        titleView.title_right_minor_txt.setOnClickListener(this);
        titleView.title_right_txt.setOnClickListener(this);
        titleView.title_right_image.setOnClickListener(this);
        titleView.setCenterText(getString(R.string.main_gallery));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onItemClick(View v, int position) {
        Folder folder = adapter.getItemAtPosition(position);
        if (folder.getType() == 3) {
            //上位机
            startActivityForResult(DetailsActivity.getIntent(mActivity, null, 2), 1000);
        } else {
            startActivityForResult(DetailsActivity.getIntent(mActivity, folder.getDir(), 1), 1000);
        }
    }

    @Override
    public boolean onItemLongClick(View v, int position) {
        if (position != 0) {
            changeTitle();
        }
        return true;
    }

    @Override
    public boolean OnItemCheck(int position, Object path, boolean isCheck, int selectedItemCount) {
        if (isCheck) {
            if (selectedItemCount - 1 > 1) {
                titleView.title_right_minor_txt.setVisibility(View.GONE);
            } else {
                titleView.title_right_minor_txt.setVisibility(View.VISIBLE);
            }
        } else {
            if (selectedItemCount + 1 > 1) {
                titleView.title_right_minor_txt.setVisibility(View.GONE);
            } else {
                titleView.title_right_minor_txt.setVisibility(View.VISIBLE);
            }
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right_image:
                showPopwindow(titleView);
                break;

            case R.id.title_right_minor_txt:    //重命名
                if (adapter.getSelectedItemCount() == 0) {
                    showTips(getString(R.string.tips_check_rename_item));
                } else {
                    Folder folder = adapter.getSelectedList().get(0);
                    int itemPosition = adapter.getItemPosition(adapter.getSelectedList().get(0));
                    showDialog(getInputDialog(2, getString(R.string.tips_rename), folder.getName(), itemPosition));

                }
                break;
            case R.id.title_right_txt:          //删除
                if (adapter.getSelectedItemCount() == 0) {
                    showTips(getString(R.string.tips_check_delete_item));
                } else {
                    List<Folder> selectedList = adapter.getSelectedList();
                    showDialog(getNormalDialog(getString(R.string.tip), getString(R.string.tips_confirm_delete), selectedList));
                }
                break;
            case R.id.title_left_txt:           //取消
                adapter.clearSelection();
                recoverTitle();

                break;
        }
    }

    @Override
    public void onDestroy() {
        titleView.title_left_txt.setVisibility(View.GONE);
        titleView.title_right_minor_txt.setVisibility(View.GONE);
        titleView.title_right_txt.setVisibility(View.GONE);

        if (adapter != null) {
            adapter.clearSelection();
        }

        if (subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            load();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (adapter.getCurrentCheckEnable()) {
            adapter.clearSelection();
            recoverTitle();

        } else {
            super.onBackPressed();
        }
    }

    /**
     * 恢复TitleView的初始状态
     */
    private void recoverTitle() {
        adapter.setCheckEnable(false);
        titleView.title_left_txt.setVisibility(View.GONE);
        titleView.title_right_minor_txt.setVisibility(View.GONE);
        titleView.title_right_txt.setVisibility(View.GONE);
        titleView.title_right_image.setVisibility(View.VISIBLE);
    }


    private void deleteFiles(final List<Folder> list) {
        final KProgressHUD hud = KProgressHUD.create(mActivity).setLabel(getString(R.string.tips_deleteing)).show();
        Observable.from(list)
                .map(new Func1<Folder, String>() {
                    @Override
                    public String call(Folder folder) {
                        return folder.getDir();
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

                        adapter.deleteSelectedItem(list);   // 删除选中条目
                        adapter.clearSelection();           // 清空临时选中的列表

                        if (hud != null && hud.isShowing()) {
                            hud.dismiss();
                        }
                        recoverTitle();
                    }

                    @Override
                    public void onError(Throwable e) {
                        load();
                        if (hud != null && hud.isShowing()) {
                            hud.dismiss();
                        }
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }


    private void deleteFile(final String path, final int position) {
        final KProgressHUD hud = KProgressHUD.create(mActivity).setLabel(getString(R.string.tips_deleteing)).show();
        Observable.just(path)
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
                        adapter.deleteItem(position);
                        if (hud != null && hud.isShowing()) {
                            hud.dismiss();
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

    private void changeTitle() {
        adapter.clearSelection();
        adapter.setCheckEnable(true);
        titleView.title_left_txt.setVisibility(View.VISIBLE);
        titleView.title_right_minor_txt.setVisibility(View.VISIBLE);
        titleView.title_right_txt.setVisibility(View.VISIBLE);

        titleView.title_right_minor_txt.setText(getString(R.string.rename));
        titleView.title_right_txt.setText(getString(R.string.delete));
        titleView.title_left_txt.setText(getString(R.string.cancel));
        titleView.title_right_image.setVisibility(View.GONE);

    }

    private void load() {
        adapter.clear();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + GloableValue.FOLDER_DIR
                + File.separator + GloableValue.FOLDER_IMAGE;
        subscribe = LoaderUtils.getImageFolder(path).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Folder>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Folder folder) {
                        adapter.addItem(folder);
                    }
                });

    }

    private void showPopwindow(View v) {
        View view = View.inflate(mActivity, R.layout.pop_image_pager, null);
        view.setBackgroundResource(R.drawable.ic_bg_pop_dark);
        ListView lv_setting = (ListView) view.findViewById(R.id.lv_setting);
        lv_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                switch (position) {
                    case 0:
                        showDialog(getInputDialog(1, getString(R.string.create_file_folder), getString(R.string.input_file_folder_name), 0));
                        break;
                    case 1:
                        changeTitle();
                        break;

                    case 2:
                        startActivityForResult(DetailsActivity.getIntent(mActivity, null, 3), 1000);
                        break;


                }
            }
        });

        String[] item = getResources().getStringArray(R.array.popwindow_gallery);
        lv_setting.setAdapter(new ArrayAdapter<String>(mActivity, R.layout.item_list_gallery, item));

        popupWindow = new PopupWindow(view, CommonTools.dip2px(mActivity, 115), WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popupWindow.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, CommonTools.dip2px(mActivity, 20),
                CommonTools.dip2px(mActivity, 73));

    }


    private View getInputDialog(final int sign, String titleMsg, String hintMsg, final Object params) {
        View view = View.inflate(mActivity, R.layout.dialog_input, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        final EditText message = (EditText) view.findViewById(R.id.message);
        final Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);

        title.setText(titleMsg);
        switch (sign) {
            case 1:
                message.setHint(hintMsg);
                break;
            case 2:
                message.setText(hintMsg);
                break;
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = message.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showTips(getString(R.string.enter_is_null));
                    return;
                }
                switch (sign) {
                    case 1: //新增文件夹
                        CommonTools.savePreference(mActivity, GloableValue.SP_IMAGE_FOLDER, content);
                        //TODO   添加空文件
                        Folder f = new Folder();
                        f.setSize(0);
                        f.setType(1);
                        String s = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + GloableValue.FOLDER_DIR
                                + File.separator + GloableValue.FOLDER_IMAGE + File.separator + content;
                        f.setDir(s);
                        PathUtils.buildDir(s);
                        int i = adapter.addItem(f);
                        //todo 添加后需要滚动到指定定位置？
                        rv_gallery.scrollToPosition(i);
                        showTips(getString(R.string.tips_add_success));

                        break;

                    case 2: //修改文件夹名称

                        Folder folder = adapter.getItemAtPosition(((int) params));
                        String name = folder.getName();

                        String dir = folder.getDir();
                        File file = new File(dir);
                        String parent = file.getParent();
                        File new_file = new File(parent + File.separator + content);
                        if (file.renameTo(new_file)) {   //修改成功
                            if (name.equals(CommonTools.getPreference(mActivity, GloableValue.SP_IMAGE_FOLDER,   //修改的文件夹名是新建的文件夹
                                    getString(R.string.default_image_folder)))) {
                                CommonTools.savePreference(mActivity, GloableValue.SP_IMAGE_FOLDER, content);
                            }

                            folder.setDir(parent + File.separator + content);
                            adapter.notifyItemChanged((int) params);
                            showTips(getString(R.string.tips_modify_success));

                        } else {    //修改失败
//                            Toasts.showCustomVerticalImageToast(mActivity, getString(R.string.tips_modify_failed), R.drawable.ic_notice);
                            showTips(getString(R.string.tips_modify_failed));
                        }

                        adapter.clearSelection();
                        recoverTitle();

                        break;

                }


                dialog.dismiss();
            }
        });
        return view;
    }


    private View getNormalDialog(String titleMsg, String contentMsg, final List<Folder> list) {
        View view = View.inflate(mActivity, R.layout.dialog_normal, null);
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
                //TODO 删除文件夹

                deleteFiles(list);
                dialog.dismiss();
            }
        });
        return view;
    }
}
