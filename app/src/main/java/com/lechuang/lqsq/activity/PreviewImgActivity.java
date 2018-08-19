package com.lechuang.lqsq.activity;

import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.widget.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PreviewImgActivity extends BaseActivity {

    @BindView(R.id.vp)
    HackyViewPager vp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_img;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        vp.setAdapter(new SamplePagerAdapter(getIntent().getStringArrayListExtra("urls")));
        vp.setOffscreenPageLimit(1);
        vp.setCurrentItem(getIntent().getIntExtra("position",0),false);
    }

    class SamplePagerAdapter extends PagerAdapter {

        private List<String> urls;

        public SamplePagerAdapter(List<String> urls) {
            this.urls = urls;
        }

        @Override
        public int getCount() {
            int size = urls.size();
            return size;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(MyApplication.getContext()).load(urls.get(position)).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    photoView.setImageDrawable(resource);
                    hideWaitDialog();
                }

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    showWaitDialog("");
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    hideWaitDialog();
                }
            });


            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
