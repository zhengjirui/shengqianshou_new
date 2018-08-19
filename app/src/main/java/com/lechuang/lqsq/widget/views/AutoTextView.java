package com.lechuang.lqsq.widget.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import com.lechuang.lqsq.R;

import java.util.ArrayList;

public class AutoTextView extends TextSwitcher implements
        ViewSwitcher.ViewFactory {
    protected ArrayList<String> mArrayList;
    private float mHeight;
    private Context mContext;
    // mInUp,mOutUp分别构成向下翻页的进出动画
    private Animation mInUp;
    private Animation mOutUp;
    onItemClickListener mClickListener;
    onSwitchListener mSwitchListener;
    // mInDown,mOutDown分别构成向下翻页的进出动画
    private Animation mInDown;
    private Animation mOutDown;
    private int mColor = 0xff6c6c6c;
    //翻页时间
    private int mGap = 7000;
    int current;

    public AutoTextView(Context context) {
        this(context, null);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.auto3d);
        mHeight = a.getDimension(R.styleable.auto3d_textSize, 12);
        a.recycle();
        mContext = context;
        init();
    }

    public void setTextAuto(ArrayList<String> arrayList) {
        current = -1;
        mArrayList = arrayList;
        setAutomaticsliding(true);
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        mClickListener = clickListener;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != -1) {
                    mClickListener.onItemClick(current);
                }
            }
        });
    }

    public void setOnSwitchListener(onSwitchListener switchListener) {
        mSwitchListener = switchListener;
    }

    private void init() {
        setFactory(this);
        mInUp = createAnim(0, 0, -90, 0);
        // createAnim(-90, 0 , true, true);
        mOutUp = createAnim(0, 0, 0, 90);
        // createAnim(0, 90, false, true);
        mInDown = createAnim(0, 0, 90, 0);
        // createAnim(90, 0, true, false);
        mOutDown = createAnim(0, 0, 0, -90);
        // createAnim(0, -90, false, false);
        // TextSwitcher主要用于文件切换，比如 从文字A 切换到 文字 B，
        // setInAnimation()后，A将执行inAnimation，
        // setOutAnimation()后，B将执行OutAnimation
        // setInAnimation(mInUp);
        // setOutAnimation(mOutUp);

        setInAnimation(mInDown);
        setOutAnimation(mOutDown);
    }

    private TranslateAnimation createAnim(float fromXDelta, float toXDelta,
                                          float fromYDelta, float toYDelta) {
        final TranslateAnimation rotation = new TranslateAnimation(fromXDelta,
                toXDelta, fromYDelta, toYDelta);
        rotation.setDuration(600);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }

    private Rotate3dAnimation createAnim(float start, float end,
                                         boolean turnIn, boolean turnUp) {
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
                turnIn, turnUp);
        rotation.setDuration(600);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }

    public void setTextSize(int i) {
        mHeight = i;
    }

    public void setTextColor(int color) {
        mColor = color;
    }

    public void setGap(int time) {
        mGap = time;
    }

    // 这里返回的TextView，就是我们看到的View
    @Override
    public View makeView() {
        TextView t = new TextView(mContext);
        t.setTextSize(mHeight);
        t.setTextColor(mColor);
        t.setMaxLines(2);
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        t.setLayoutParams(lp);
        return t;
    }

    // 定义动作，向下滚动翻页
    public void previous() {
        if (getInAnimation() != mInDown) {
            setInAnimation(mInDown);
        }
        if (getOutAnimation() != mOutDown) {
            setOutAnimation(mOutDown);
        }
    }

    // 定义动作，向上滚动翻页
    public void next() {
        if (getInAnimation() != mInUp) {
            setInAnimation(mInUp);
        }
        if (getOutAnimation() != mOutUp) {
            setOutAnimation(mOutUp);
        }
    }

    private Runnable mTicker;
    private Handler mHandler;
    /**
     * 运行启动自动翻页
     **/
    private boolean Automaticsliding = false;
    int position = 0;
    /**
     * 是否正在执行自动翻页
     **/
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
                            setNextText();
                            if (Automaticsliding) {
                                mHandler.postDelayed(mTicker, mGap);
                            } else {
                                OnStop();
                            }
                        }
                    };
                    mHandler.post(mTicker);
                } else {
                    /* 如果还在运行，则不重新开启 */
                    // Log.e("q", "无改变");
                }
            } else {
                /* 如果已经自动翻页，则不再开启自动翻页 */
            }
        } else {
            Automaticsliding = false;
        }

    }

    /**
     * 向下翻一页，如果为空或者长度为1，则不支持自动翻页，关闭自动翻页
     */
    public void setNextText() {
        if (mArrayList == null) {
            Automaticsliding = false;
            return;
        }
        if (mArrayList.size() <= 0) {
            Automaticsliding = false;
            return;
        }
        current = position;
        setText(Html.fromHtml(mArrayList.get(position)));
        position++;
        if (position >= mArrayList.size()) {
            position = 0;
        }
        if (mSwitchListener != null)
            mSwitchListener.onSwitch(position);
        // }
    }

    private void OnRun() {
        // Log.e("q", "开始运行");
        running = true;
    }

    private void OnStop() {
        // Log.e("q", "停止运行");
        running = false;

    }

    public boolean isRunning() {
        return running;
    }

    @Override
    protected void onDetachedFromWindow() {
        OnStop();
        super.onDetachedFromWindow();
    }

    class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(float fromDegrees, float toDegrees,
                                 boolean turnIn, boolean turnUp) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight() / 2;
            mCenterX = getWidth() / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees
                    + ((mToDegrees - fromDegrees) * interpolatedTime);

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY
                        * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY
                        * (interpolatedTime), 0.0f);
            }
            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public interface onSwitchListener {
        void onSwitch(int position);
    }

}
