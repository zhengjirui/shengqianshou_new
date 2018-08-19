package com.lechuang.lqsq.widget.views;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;

import java.util.ArrayList;


public class BigPicMap extends FrameLayout implements OnPageChangeListener {
	protected Context mContext;
	/**
	 * 装点点的ImageView数组
	 */
	private ViewGroup group;
	private ViewPagerFix advPager = null;
	protected LayoutInflater mInflater;
	protected ArrayList<String> mArrayList;
	private ImageView[] imageViews = null;

	public BigPicMap(Context context) {
		super(context);
		init(context);

	}

	public BigPicMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ViewPager getPagerView() {
		return advPager;

	}

	private void init(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.adv_map, null);
		advPager = (ViewPagerFix) view.findViewById(R.id.adv_pager);
		group = (ViewGroup) view.findViewById(R.id.viewGroup);
		advPager.addOnPageChangeListener(this);
		addView(view);
	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}

	private Runnable mTicker;
	private Handler mHandler;
	/** 运行启动自动翻页 **/
	private boolean Automaticsliding = false;
	int nowpage;
	/** 是否正在执行自动翻页 **/
	private boolean running = false;

	public void setAutomaticsliding(boolean b) {
		if (b) {

			if (!Automaticsliding) {
				Automaticsliding = true;
				if (!isRunning()) {
					OnRun();
					mHandler = new Handler();
					mTicker = new Runnable() {
						public void run() {
							setNextItem();
							if (Automaticsliding) {
								mHandler.postDelayed(mTicker, 4000);
							} else {
								OnStop();
							}
						}
					};
					mHandler.postDelayed(mTicker, 4000);
				} else {
					/* 如果还在运行，则不重新开启 */
//					Log.e("q", "无改变");
				}
			} else {
				/* 如果已经自动翻页，则不再开启自动翻页 */
			}
		} else {
			Automaticsliding = false;
		}

	}

	private void OnRun() {
//		Log.e("q", "开始运行");
		running = true;
	}

	private void OnStop() {
//		Log.e("q", "停止运行");
		running = false;

	}

	public boolean isRunning() {
		return running;
	}

	/**
	 * 向下翻一页，如果为空或者长度为1，则不支持自动翻页，关闭自动翻页
	 */
	public void setNextItem() {
		if (mArrayList == null) {
			Automaticsliding = false;
			return;
		}
		if (mArrayList.size() <= 1) {
			Automaticsliding = false;
			return;
		}
		if (nowpage != advPager.getCurrentItem()) {
			nowpage = advPager.getCurrentItem();
			return;
		}
		nowpage++;
		if (nowpage > mArrayList.size() - 1) {
			nowpage = 0;
		}
		advPager.setCurrentItem(nowpage, true);
		// }
	}

	public void setCurrentItem(int position) {
		advPager.setCurrentItem(position);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		if (mChangeListener != null)
			mChangeListener.onPageSelected(position);
		int len=imageViews.length;
		for (int i = 0; i < len; i++) {
			imageViews[position]
					.setBackgroundResource(R.drawable.adv_focus_radius);
			if (position != i) {
				imageViews[i].setBackgroundResource(R.drawable.adv_focus_radius1);
			}
		}
	}

	public int getCount() {
		return imageViews.length;

	}

	public void setAdapter(ArrayList<String> arrayList) {
		imageViews = new ImageView[arrayList.size()];
		group.removeAllViews();
		if (arrayList.size() > 1) {
			for (int i = 0; i < arrayList.size(); i++) {
				imageViews[i] = new ImageView(mContext);
				imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);
				if (i == 0) {
					// 默认选中第一张图片
					imageViews[i]
							.setBackgroundResource(R.drawable.adv_focus_radius);
				} else {
					imageViews[i].setBackgroundResource(R.drawable.adv_focus_radius1);
				}
				LinearLayout.LayoutParams wmParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				wmParams.setMargins(8, 0, 8, 5);
				wmParams.width = 15;
				wmParams.height = 15;
				group.addView(imageViews[i], wmParams);
			}
		}
		AdvAdapter adapter = new AdvAdapter(mContext, arrayList);
		mArrayList = arrayList;
		advPager.setAdapter(adapter);
	}

	private class AdvAdapter extends PagerAdapter {
		private ArrayList<String> mArrayList = null;
		private Context mContext;

		public AdvAdapter(Context context, ArrayList<String> list) {
			mContext = context;
			if (list != null) {
				mArrayList = list;
			} else {
				mArrayList = new ArrayList<String>();
			}

		}

		@Override
		public int getCount() {
			return mArrayList.size();

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			PhotoView imageView = new PhotoView(mContext);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//			switch (position) {
//			case 0:
//				imageView.setBackgroundColor(0XFFEC6D30);
//				break;
//			case 1:
//				imageView.setBackgroundColor(0XFF64A6EF);
//				break;
//			case 2:
//				imageView.setBackgroundColor(0XFFFA5254);
//				break;
//			default:
//				break;
//			}
			//修改图片加载的框架
			//ImageLoader.getInstance().displayImage(mArrayList.get(position), imageView);
			Glide.with(MyApplication.getContext()).load(mArrayList.get(position)).into(imageView);
			imageView.setOnPhotoTapListener(new  OnPhotoTapListener() {

				@Override
				public void onPhotoTap(ImageView view, float x, float y) {
					if (mOnScrollListener != null) {
						mOnScrollListener.onItemClick(position,
								mArrayList.get(position));
					}
				}
				/*
				@Override
				public void onPhotoTap(View view, float x, float y) {

				}*/
			});
			((ViewPager) container).addView(imageView, 0);

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
			/*if (object instanceof PhotoView) {
				PhotoView s = (PhotoView)object;
				GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable) s.getDrawable();
				if (bitmapDrawable != null) {
					Bitmap bm = bitmapDrawable.getBitmap();
					if (bm!=null && !bm.isRecycled()) {
						s.setImageResource(0);
						bm.recycle();
					}
				}
			}*/
		}
	}

	OnItemClickListener mOnScrollListener;
	OnPageChangeListener mChangeListener;

	public interface OnItemClickListener {
		public void onItemClick(int position, Object object);
	}

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnScrollListener = l;
	}

	public interface OnPageChangeListener {
		public void onPageSelected(int position);
	}

	public void setOnPageChangeListener(OnPageChangeListener l) {
		mChangeListener = l;
	}

}
