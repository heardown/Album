package me.winds.album.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import butterknife.BindView;
import butterknife.ButterKnife;
import me.winds.album.R;

/**
 * 看起来渣渣的标题栏
 * 渣渣的自由 渣渣的爱
 * Created by Winds on 2016/10/12 0012.
 */

public class TitleView extends RelativeLayout {
    @BindView(R.id.layout_left)
    public LinearLayout layout_left;

    @BindView(R.id.layout_right)
    public LinearLayout layout_right;

    @BindView(R.id.title_left_image)
    public ImageView title_left_image;

    @BindView(R.id.title_right_image)
    public ImageView title_right_image;

    @BindView(R.id.title_left_txt)
    public TextView title_left_txt;

    @BindView(R.id.title_right_txt)
    public TextView title_right_txt;

    @BindView(R.id.title_main_txt)
    public TextView title_main_txt;

    @BindView(R.id.title_right_minor_txt)
    public TextView title_right_minor_txt;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.view_title_bar, this);
        ButterKnife.bind(this, view);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.title_view);
        CharSequence left_text = a.getText(R.styleable.title_view_left_text);
        CharSequence right_text = a.getText(R.styleable.title_view_right_text);
        CharSequence center_text = a.getText(R.styleable.title_view_center_text);
        Drawable left_drawable = a.getDrawable(R.styleable.title_view_left_image);
        Drawable right_drawable = a.getDrawable(R.styleable.title_view_right_image);

        a.recycle();

        if(left_text != null) {
            title_left_txt.setText(left_text);
        }
        if(right_text != null) {
            title_right_txt.setText(right_text);
        }
        if(center_text != null) {
            title_main_txt.setText(center_text);
        }

        if(left_drawable != null) {
            title_left_image.setImageDrawable(left_drawable);
        } else {
            setVisiblity(title_left_image, false);
        }

        if(right_drawable != null) {
            title_right_image.setImageDrawable(right_drawable);
        } else {
            setVisiblity(title_right_image, false);
        }
    }

    public void setCenterText(String msg) {
        title_main_txt.setText(msg);
    }

    public void setLeftText(String msg) {
        title_left_txt.setVisibility(View.VISIBLE);
        title_left_txt.setText(msg);
    }

    public CharSequence getLeftText() {
        return title_left_txt.getText();
    }

    public void setLeftText(String msg, float size) {
        title_left_txt.setText(msg);
        title_left_txt.setTextSize(size);
    }

    public void setLeftTextColor(int color) {
        title_left_txt.setTextColor(color);
    }

    public void setRightText(String msg) {
        title_right_txt.setVisibility(View.VISIBLE);
        title_right_txt.setText(msg);
    }

    public void setRightText(String msg, float size) {
        title_right_txt.setText(msg);
        title_right_txt.setTextSize(size);
    }

    public void setRightTextColor(int color) {
        title_right_txt.setTextColor(color);
    }

    public void setVisiblity(View view, boolean bool) {
        view.setVisibility(bool ? View.VISIBLE : View.GONE);
    }

}
