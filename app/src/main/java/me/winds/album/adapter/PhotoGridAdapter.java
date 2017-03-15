package me.winds.album.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.winds.album.R;
import me.winds.album.event.OnItemCheckListener;
import me.winds.album.event.OnItemClickListener;
import me.winds.album.event.OnItemLongClickListener;
import me.winds.album.tools.AndroidLifeCycleUtils;

/**
 * Author by Winds on 2016/11/19 0019.
 * Email heardown@163.com.
 */

public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder> {

    private String dir;
    private int imageSize;
    private int columnNumber = 3;
    private RequestManager mGlide;
    private PhotoViewHolder holder;
    private LayoutInflater inflater;
    public boolean checkEnable = false;
    private OnItemCheckListener onItemCheckListener = null;
    private OnItemClickListener onItemClickListener = null;
    private OnItemLongClickListener onItemLongClickListener = null;

    public PhotoGridAdapter(Context context, RequestManager requestManager) {
        super();
        this.mGlide = requestManager;
        inflater = LayoutInflater.from(context);
        setColumnNumber(context, columnNumber);
    }

    public PhotoGridAdapter(Context context, RequestManager requestManager, String dir) {
        this(context, requestManager);
        this.mGlide = requestManager;
        this.dir = dir;
        inflater = LayoutInflater.from(context);
        setColumnNumber(context, columnNumber);
    }

    public PhotoGridAdapter(Context context, RequestManager requestManager, List<String> list) {
        this.list = list;
        this.mGlide = requestManager;
        inflater = LayoutInflater.from(context);
        setColumnNumber(context, columnNumber);
    }

    private PhotoGridAdapter(Context context, RequestManager requestManager, List<String> list, ArrayList<String> selectedList, int colNum) {
        this(context, requestManager, list);
        setColumnNumber(context, colNum);
        selectedList = new ArrayList<>();
        if (selectedList != null) selectedList.addAll(selectedList);
    }

    private void setColumnNumber(Context context, int columnNumber) {
        this.columnNumber = columnNumber;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNumber;
    }

    public void setDirPath(String dir) {
        this.dir = dir;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        holder = new PhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
        final String name = list.get(position);
        final String path = TextUtils.isEmpty(dir) ? name : dir + File.separator + name;
        holder.vSelected.setVisibility(checkEnable ? View.VISIBLE : View.GONE);
        boolean canLoadImage = AndroidLifeCycleUtils.canLoadImage(holder.ivPhoto.getContext());
        if (canLoadImage) {

            mGlide
                .load(path)
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f)
                .override(imageSize, imageSize)
                .placeholder(R.drawable.pictures_no)
                .error(R.drawable.pictures_no)
                .into(holder.ivPhoto);
    }

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    int pos = holder.getAdapterPosition();
                    if (checkEnable) {
                        holder.vSelected.performClick();
                    } else {
                        onItemClickListener.onItemClick(view, pos);
                    }
                }
            }
        });

        holder.ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    return onItemLongClickListener.onItemLongClick(v, holder.getAdapterPosition());
                }

                return false;
            }
        });

        final boolean isChecked = isSelected(name);
        holder.vSelected.setSelected(isChecked);

        holder.vSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                boolean isEnable = true;

                if (onItemCheckListener != null) {
                    isEnable = onItemCheckListener.OnItemCheck(pos, name, isChecked,
                            getSelectedList().size());
                }
                if (isEnable) {
                    toggleSelection(name);
                    notifyItemChanged(pos);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onViewRecycled(PhotoViewHolder holder) {
        Glide.clear(holder.ivPhoto);
        super.onViewRecycled(holder);
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
        }
    }


    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    private ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(getSelectedItemCount());
        for (String photo : selectedList) {
            selectedPhotoPaths.add(photo);
        }
        return selectedPhotoPaths;
    }

    public void setCheckEnable(boolean bool) {
        this.checkEnable = bool;
        notifyDataSetChanged();
    }


}
