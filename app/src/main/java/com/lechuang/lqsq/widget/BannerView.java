package com.lechuang.lqsq.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.lechuang.lqsq.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by YST on 2016/8/3.
 */
public final class BannerView extends RelativeLayout implements View.OnTouchListener {
    private ViewPager viewPager;
    private CirclePagerIndicator circlePagerIndicator;
    private List<String> imgs;
    private BannerViewPagerAdapter adapter;
    private int mBannerPosition = 0;
    private int face_banner_size;
    private int banner_size;
    private boolean mIsUserTouched = false;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private int time = 0;
    private OnClickListener clickListener;

    public BannerView(Context context) {
        super(context);
        initView(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void onDestroy() {
        timer.cancel();
    }

    /**
     * 绑定数据
     *
     * @param data 数据
     * @param time 自动滚动到下一个的延迟时间
     */

    public void bindData(List<String> data, int time) {
        if (data == null || data.isEmpty()) {
            return;
        }
        if (imgs != null && !imgs.isEmpty()) {
            imgs = data;
            return;
        }
        this.time = time;
        imgs = data;
        banner_size = imgs.size();
//        banner_size = 4;
        if (banner_size > 1) {
            face_banner_size = banner_size * 3;


            timerTask = new TimerTask() {
                @Override
                public void run() {

                    if (!mIsUserTouched) {
                        mBannerPosition = (mBannerPosition + 1) % face_banner_size;
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mBannerPosition == face_banner_size - 1) {
                                    viewPager.setCurrentItem(banner_size - 1, false);
                                } else {
                                    viewPager.setCurrentItem(mBannerPosition);
                                }
                            }
                        });

                    }
                }

            };
            timer.schedule(timerTask, time, time);
        } else {
            face_banner_size = 1;
        }
        adapter = new BannerViewPagerAdapter();
        viewPager.setAdapter(adapter);

        circlePagerIndicator.setViewPagerRealSize(banner_size);
        circlePagerIndicator.bindViewPager(viewPager);
        circlePagerIndicator.setOnPageChangeListener(adapter);
        if (banner_size == 1) {
            circlePagerIndicator.setVisibility(INVISIBLE);
        }

    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.banners, this);
        viewPager = (ViewPager) findViewById(R.id.banner);
        viewPager.setOnTouchListener(this);
        circlePagerIndicator = (CirclePagerIndicator) findViewById(R.id.indicator);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_MOVE) {
            mIsUserTouched = true;

        } else if (action == MotionEvent.ACTION_UP) {
            mIsUserTouched = false;
        }
        return false;
    }


    private class BannerViewPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
        LayoutInflater inflater;

        public BannerViewPagerAdapter() {
            inflater = LayoutInflater.from(getContext());
        }

        @Override
        public int getCount() {
            return face_banner_size;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= banner_size;
            View view = inflater.inflate(R.layout.banners_item, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            String imgUrl = imgs.get(position);
            Glide.with(getContext()).load(imgUrl).placeholder(R.drawable.rvbaner).into(imageView);
            imageView.setTag(position);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onClick(v);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mBannerPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void finishUpdate(ViewGroup container) {
            if (face_banner_size == 1) {
                return;
            }
            int position = viewPager.getCurrentItem();
            if (position == 0) {
                position = banner_size;
                viewPager.setCurrentItem(position, false);
            } else if (position == face_banner_size - 1) {
                position = banner_size - 1;
                viewPager.setCurrentItem(position, false);
            }
        }
    }


}

