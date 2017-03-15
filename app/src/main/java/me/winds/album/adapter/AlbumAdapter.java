package me.winds.album.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;


import java.util.ArrayList;
import java.util.List;

import me.winds.album.R;
import me.winds.album.bean.Folder;

/**
 * Created by Winds on 2016/11/30.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> {
    private Context context;
    private RequestManager mGlide;
    private AlbumHolder holder;
    public boolean checkEnable = false;

    protected List<Folder> list;
    protected List<Folder> selectedList;

    private OnItemCheckListener onItemCheckListener = null;
    private ItemClickListener itemClickListener = null;
    private ItemLongClickListener itemLongClickListener = null;

    public AlbumAdapter(Context context, RequestManager requestManager) {
        this.context = context;
        this.mGlide = requestManager;

        list = new ArrayList<>();
        selectedList = new ArrayList<>();
        addDeviceModeType(list);
    }

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder = new AlbumHolder(View.inflate(context, R.layout.item_album_normal, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final AlbumHolder holder, int position) {
        holder.iv_selected.setVisibility(checkEnable ? View.VISIBLE : View.GONE);
        final Folder folder = list.get(position);
        int type = folder.getType();
        switch (type) {
            case 1:
                holder.tv_main.setText(folder.getName());
                holder.tv_minor.setText(folder.getSize() + context.getString(R.string.unit));
                String cover = (String) folder.getCover();
                if (cover == null) { //封面为空
                    holder.iv_image.setImageResource(R.drawable.pictures_no);
                } else {

                    mGlide
                            .load(cover)
                            .centerCrop()
                            .dontAnimate()
                            .thumbnail(0.5f)
                            .placeholder(R.drawable.pictures_no)
                            .error(R.drawable.pictures_no)
                            .into(holder.iv_image);
                }
                break;

            case 2:
                holder.tv_main.setText(folder.getName());
                holder.tv_minor.setText(folder.getSize());
                holder.iv_image.setImageBitmap((Bitmap) folder.getCover());
                break;
            case 3:
                holder.tv_main.setText(context.getString(R.string.title_device_mode));
                holder.iv_image.setImageResource(R.drawable.ic_device_mode);
                holder.tv_minor.setText("");

                holder.iv_selected.setVisibility(View.GONE);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    int pos = holder.getAdapterPosition();
                    if (checkEnable) {
                        holder.iv_selected.performClick();
                    } else {
                        itemClickListener.onItemClick(v, pos);
                    }
                }
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemLongClickListener != null) {
                    return itemLongClickListener.onItemLongClick(v, holder.getAdapterPosition());
                }
                return false;
            }
        });


        final boolean isChecked = isSelected(folder);
        holder.iv_selected.setSelected(isChecked);
        holder.iv_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                boolean isEnable = true;
                if (onItemCheckListener != null) {
                    isEnable = onItemCheckListener.OnItemCheck(pos, folder, isChecked,
                            selectedList.size());
                }

                if (isEnable) {
                    toggleSelection(folder);
                    notifyItemChanged(pos);
                }

            }
        });


    }

    @Override
    public void onViewRecycled(AlbumHolder holder) {
        Glide.clear(holder.iv_image);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addList(List<Folder> mList) {
        list.addAll(mList);
        notifyDataSetChanged();
    }

    public void setList(List<Folder> mList) {
        list.clear();
        list.addAll(mList);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Folder t, int position) {
        list.add(position, t);
        notifyItemInserted(position);
    }

    public int addItem(Folder t) {
        int position = list.size();
        list.add(position, t);
        notifyItemInserted(position);
        return position;
    }

    public void clear() {
        list.clear();
        addDeviceModeType(list);
        notifyDataSetChanged();
    }

    public int getItemPosition(Folder folder) {
        return list.indexOf(folder);
    }

    public void addDeviceModeType(List<Folder> list) {
        Folder f = new Folder();
        f.setType(3);
        list.add(f);
    }

    public boolean isSelected(Folder folder) {
        return selectedList.contains(folder);
    }

    public void toggleSelection(Folder folder) {
        if (selectedList.contains(folder)) {
            selectedList.remove(folder);
        } else {
            selectedList.add(folder);
        }
    }

    public Folder getItemAtPosition(int position) {
        return list.get(position);
    }

    public void clearSelection() {
        selectedList.clear();
    }

    public int getSelectedItemCount() {
        return selectedList.size();
    }

    public List<Folder> getSelectedList() {
        return selectedList;
    }

    /**
     * 删除选中的条目
     *
     * @param mList
     */
    public void deleteSelectedItem(List<Folder> mList) {
        list.removeAll(mList);
        notifyDataSetChanged();
    }

    public boolean getCurrentCheckEnable() {
        return checkEnable;
    }

    public void setCheckEnable(boolean bool) {
        this.checkEnable = bool;
        notifyDataSetChanged();
    }

    public class AlbumHolder extends RecyclerView.ViewHolder {
        View iv_selected;
        ImageView iv_image;
        TextView tv_main;
        TextView tv_minor;

        public AlbumHolder(View itemView) {
            super(itemView);
            iv_selected = itemView.findViewById(R.id.iv_selected);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_main = (TextView) itemView.findViewById(R.id.tv_main);
            tv_minor = (TextView) itemView.findViewById(R.id.tv_minor);
        }
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

    public interface ItemLongClickListener {
        boolean onItemLongClick(View v, int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    public interface OnItemCheckListener {

        /***
         *
         * @param position 所选图片的位置
         * @param path     所选的图片
         * @param isCheck   当前状态
         * @param selectedItemCount  已选数量
         * @return enable check
         */
        boolean OnItemCheck(int position, Object path, boolean isCheck, int selectedItemCount);

    }
}
