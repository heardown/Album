package me.winds.album.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.winds.album.R;
import me.winds.album.event.OnItemClickListener;
import me.winds.album.tools.AndroidLifeCycleUtils;

/**
 * Author by Winds on 2016/11/14 0014.
 * Email heardown@163.com.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private List<String> list = new ArrayList<>();
    private RequestManager mGlide;
    private String dir;

    public ImagePagerAdapter(RequestManager mGlide, List<String> list) {
        this.list = list;
        this.mGlide = mGlide;
    }

    public ImagePagerAdapter(RequestManager mGlide, List<String> list, String dir) {
        this.list = list;
        this.mGlide = mGlide;
        this.dir = dir;
    }

    public int remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
        return list.size();
    }

    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final Context context = container.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_image_pager, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.ttv_image);

        final String path = TextUtils.isEmpty(dir) ? list.get(position) : dir + File.separator + list.get(position);

        boolean canLoadImage = AndroidLifeCycleUtils.canLoadImage(context);

        if (canLoadImage) {
            mGlide.load(path)
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .dontTransform()
                    .override(800, 800)
                    .placeholder(R.drawable.pictures_no)
                    .error(R.drawable.pictures_no)
                    .into(imageView);
        }

        container.addView(itemView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, position);
                }
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Glide.clear((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
