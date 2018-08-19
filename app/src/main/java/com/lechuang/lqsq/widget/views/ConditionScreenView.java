package com.lechuang.lqsq.widget.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lechuang.lqsq.R;


/**
 * @author: LGH
 * @since: 2018/5/21
 * @describe:
 */

public class ConditionScreenView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private View mConditionScreenLayout;
    private LinearLayout mLlZonehe, mLlQuanHouJia, mLlXiaoLiang, mLlLayout;//综合、券后价、销量、布局切换
    private LinearLayout mLlItem0, mLlItem1, mLlItem2, mLlItem3;//综合选项的子列表
    private ImageView mIvZongHe, mIvQuanHouJia, mIvXiaoLiang, mIvLayout, mIvItem0, mIvItem1, mIvItem2, mIvItem3;
    private TextView mTvZongHe, mTvQuanHouJia, mTvXiaoLiang, mTvItem0, mTvItem1, mTvItem2, mTvItem3;
    private View vZongHe, vQuanHouJia, vXiaoLiang;
    private WiperSwitch mWiperSitch;


    //更改一下的值就能更改默认方式
    private int mTypeZongHe = 0;//0综合排序，1优惠券面试从低到高，2优惠券面试从高到低，3预估佣金从高到低
    private int mTypeQuanHouJia = 0;//0从高到低，1从低到高
    private int mTypeXiaoLiang = 0;//0从高到低，1从低到高

    private boolean mCurrentSelectZoneHe = true;//默认选中综合选项
    private boolean mCurrentSelectQuanHouJia = false;//选中券后价
    private boolean mCurrentSelectXiaoLiang = false;//选中销量

    private boolean mSingeLine = false;//单行展示，否则多列展示
    private boolean mCheckState = true;//仅显示优惠券商品，默认选中

    private IConditionScreenLisenter mIConditionScreenLisenter;
    private View mLChildContent;
    private View mTransparentLayout;


    public ConditionScreenView(Context context) {
        this(context, null);
    }

    public ConditionScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ConditionScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initView();

        initData();
    }

    private void initView() {
        mConditionScreenLayout = LayoutInflater.from(mContext).inflate(R.layout.condition_screen_layout, this,true);

        mLChildContent = mConditionScreenLayout.findViewById(R.id.ll_child_content);
        mTransparentLayout = mConditionScreenLayout.findViewById(R.id.transparent_layout);


        mLlZonehe = mConditionScreenLayout.findViewById(R.id.ll_zonghe);
        mLlQuanHouJia = mConditionScreenLayout.findViewById(R.id.ll_quanhoujia);
        mLlXiaoLiang = mConditionScreenLayout.findViewById(R.id.ll_xiaoliang);
        mLlLayout = mConditionScreenLayout.findViewById(R.id.ll_layout);

        mTvZongHe = mConditionScreenLayout.findViewById(R.id.tv_zonghe);
        mTvQuanHouJia = mConditionScreenLayout.findViewById(R.id.tv_quanhoujia);
        mTvXiaoLiang = mConditionScreenLayout.findViewById(R.id.tv_xiaoliang);

        mTvItem0 = mConditionScreenLayout.findViewById(R.id.tv_item0);
        mTvItem1 = mConditionScreenLayout.findViewById(R.id.tv_item1);
        mTvItem2 = mConditionScreenLayout.findViewById(R.id.tv_item2);
        mTvItem3 = mConditionScreenLayout.findViewById(R.id.tv_item3);

        vZongHe = mConditionScreenLayout.findViewById(R.id.v_zonghe);
        vQuanHouJia = mConditionScreenLayout.findViewById(R.id.v_quanhoujia);
        vXiaoLiang = mConditionScreenLayout.findViewById(R.id.v_xiaoliang);

        mIvZongHe = mConditionScreenLayout.findViewById(R.id.iv_zonghe);
        mIvQuanHouJia = mConditionScreenLayout.findViewById(R.id.iv_quanhoujia);
        mIvXiaoLiang = mConditionScreenLayout.findViewById(R.id.iv_xiaoliang);
        mIvLayout = mConditionScreenLayout.findViewById(R.id.img_layout);
        mIvItem0 = mConditionScreenLayout.findViewById(R.id.img_item0);
        mIvItem1 = mConditionScreenLayout.findViewById(R.id.img_item1);
        mIvItem2 = mConditionScreenLayout.findViewById(R.id.img_item2);
        mIvItem3 = mConditionScreenLayout.findViewById(R.id.img_item3);

        mWiperSitch = mConditionScreenLayout.findViewById(R.id.wiperSwitch);

        mLlItem0 = mConditionScreenLayout.findViewById(R.id.ll_item0);
        mLlItem1 = mConditionScreenLayout.findViewById(R.id.ll_item1);
        mLlItem2 = mConditionScreenLayout.findViewById(R.id.ll_item2);
        mLlItem3 = mConditionScreenLayout.findViewById(R.id.ll_item3);

        mWiperSitch.setOnChangedListener(new WiperSwitch.OnChangedListener() {
            @Override
            public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
                mCheckState = checkState;
                mIConditionScreenLisenter.wiperSitchChecked(checkState);
            }
        });

        mTransparentLayout.setOnClickListener(this);
        mLlZonehe.setOnClickListener(this);
        mLlQuanHouJia.setOnClickListener(this);
        mLlXiaoLiang.setOnClickListener(this);
        mLlLayout.setOnClickListener(this);
        mLlItem0.setOnClickListener(this);
        mLlItem1.setOnClickListener(this);
        mLlItem2.setOnClickListener(this);
        mLlItem3.setOnClickListener(this);
    }

    private void initData() {

        //设置综合的图片
        changeZongHeImg();

        //根据成员变量进行初始化，
//        if (mTypeQuanHouJia == 0) {
//            mIvQuanHouJia.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_shang));
//        } else {
//            mIvQuanHouJia.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_xia));
//        }
//
//        if (mTypeXiaoLiang == 0) {
//            mIvXiaoLiang.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_shang));
//        } else {
//            mIvXiaoLiang.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_xia));
//        }

        mIvQuanHouJia.setBackground(getResources().getDrawable(R.drawable.sousuo_unselect));
        mIvXiaoLiang.setBackground(getResources().getDrawable(R.drawable.sousuo_unselect));

        if (mSingeLine) {
            mIvLayout.setBackground(getResources().getDrawable(R.drawable.ic_hengpai));
        } else {
            mIvLayout.setBackground(getResources().getDrawable(R.drawable.ic_shupai));
        }


        //设置初始化默认选中综合
        upDataCurrentState(0);
        if (mIConditionScreenLisenter != null) {
            mIConditionScreenLisenter.wiperSitchChecked(mCheckState);//设置初始化仅显示优惠券商品
        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() != R.id.ll_zonghe){
            startAnimOut();
        }
        switch (v.getId()) {
            case R.id.transparent_layout:

                break;
            case R.id.ll_zonghe://综合
                setParentUnselect();
                setDefImage();

                //当前显示综合数据时，点击弹出选项，不加载数据。
                if (!mCurrentSelectZoneHe){
                    //点击综合的时候，默认先加载综合排序
                    mTypeZongHe = 0;
                    upDataCurrentState(mTypeZongHe);
                }
                changeZongHeImg();

                startAnimInto();
                mCurrentSelectZoneHe = true;
                mCurrentSelectQuanHouJia = false;
                mCurrentSelectXiaoLiang = false;

                changeParentState(true, mTvZongHe, vZongHe);
                upDataChildView();
                break;

            case R.id.ll_quanhoujia://券后价
                setParentUnselect();
                setDefImage();
                mCurrentSelectZoneHe = false;
                mCurrentSelectQuanHouJia = true;
                mCurrentSelectXiaoLiang = false;

                if (mTypeQuanHouJia == 0) {
                    mTypeQuanHouJia = 1;
                    mIvQuanHouJia.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_xia));
                } else {
                    mTypeQuanHouJia = 0;
                    mIvQuanHouJia.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_shang));
                }
                changeParentState(true, mTvQuanHouJia, vQuanHouJia);
                upDataCurrentState(mTypeQuanHouJia);
                break;

            case R.id.ll_xiaoliang://销量
                setParentUnselect();
                setDefImage();
                mCurrentSelectZoneHe = false;
                mCurrentSelectQuanHouJia = false;
                mCurrentSelectXiaoLiang = true;

                if (mTypeXiaoLiang == 0) {
                    mTypeXiaoLiang = 1;
                    mIvXiaoLiang.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_xia));
                } else {
                    mTypeXiaoLiang = 0;
                    mIvXiaoLiang.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_shang));
                }
                changeParentState(true, mTvXiaoLiang, vXiaoLiang);
                upDataCurrentState(mTypeXiaoLiang);
                break;

            case R.id.ll_layout://切换布局
                if (mSingeLine) {//如果是单行展示，则换成多列展示
                    mSingeLine = false;
                    mIvLayout.setBackground(getResources().getDrawable(R.drawable.ic_shupai));
                } else {//如果是多列展示，则换成单行展示
                    mSingeLine = true;
                    mIvLayout.setBackground(getResources().getDrawable(R.drawable.ic_hengpai));
                }
                if (mIConditionScreenLisenter != null) {
                    mIConditionScreenLisenter.changeLayout(mSingeLine);
                }
                break;

            case R.id.ll_item0:
                mTypeZongHe = 0;
                upDataCurrentState(mTypeZongHe);
                changeZongHeImg();
                break;

            case R.id.ll_item1:
                mTypeZongHe = 1;
                upDataCurrentState(mTypeZongHe);
                changeZongHeImg();
                break;

            case R.id.ll_item2:
                mTypeZongHe = 2;
                upDataCurrentState(mTypeZongHe);
                changeZongHeImg();
                break;

            case R.id.ll_item3:
                mTypeZongHe = 3;
                upDataCurrentState(mTypeZongHe);
                changeZongHeImg();

                break;
        }
    }

    private void setDefImage() {
        mIvZongHe.setBackground(getResources().getDrawable(R.drawable.sousuo_unselect));
        mIvQuanHouJia.setBackground(getResources().getDrawable(R.drawable.sousuo_unselect));
        mIvXiaoLiang.setBackground(getResources().getDrawable(R.drawable.sousuo_unselect));
    }

    private void changeZongHeImg() {
        switch (mTypeZongHe) {
            case 0:
            case 1:
            case 3:
                mIvZongHe.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_shang));
                break;
            case 2:
                mIvZongHe.setBackground(getResources().getDrawable(R.drawable.sousuohou_jiage_xia));
                break;
        }
    }

    private void setParentUnselect() {
        changeParentState(false, mTvZongHe, vZongHe);
        changeParentState(false, mTvQuanHouJia, vQuanHouJia);
        changeParentState(false, mTvXiaoLiang, vXiaoLiang);
    }

    private void changeParentState(boolean select, TextView textView, View view) {
        textView.setTextColor(select ? getResources().getColor(R.color.main) : getResources().getColor(R.color.miantext));
        view.setVisibility(select ? VISIBLE : INVISIBLE);
    }

    /**
     * 更新综合的选中状态
     */
    private void upDataChildView() {
        setChildUnselect();
        switch (mTypeZongHe) {
            case 0:
                changeChildState(true, mTvItem0, mIvItem0);
                break;

            case 1:
                changeChildState(true, mTvItem1, mIvItem1);
                break;

            case 2:
                changeChildState(true, mTvItem2, mIvItem2);
                break;

            case 3:
                changeChildState(true, mTvItem3, mIvItem3);
                break;
        }
    }

    /**
     * 设置全部都不选中
     */
    private void setChildUnselect() {
        changeChildState(false, mTvItem0, mIvItem0);
        changeChildState(false, mTvItem1, mIvItem1);
        changeChildState(false, mTvItem2, mIvItem2);
        changeChildState(false, mTvItem3, mIvItem3);
    }

    /**
     * 更改综合的选中状态
     *
     * @param select
     * @param textView
     * @param imageView
     */
    private void changeChildState(boolean select, TextView textView, ImageView imageView) {
        textView.setTextColor(select ? getResources().getColor(R.color.main) : getResources().getColor(R.color.miantext));
        imageView.setVisibility(select ? VISIBLE : INVISIBLE);
    }


    public void setConditionScreenLayout(IConditionScreenLisenter iConditionScreenLisenter) {
        this.mIConditionScreenLisenter = iConditionScreenLisenter;
    }

    private void upDataCurrentState(int typeValue) {
        if (mIConditionScreenLisenter != null) {
            mIConditionScreenLisenter.currentSelectType(mCurrentSelectZoneHe, mCurrentSelectQuanHouJia, mCurrentSelectXiaoLiang, typeValue);
        }
    }


    private ObjectAnimator mIntoAnimTrans;
    private ObjectAnimator mIntoAnimAlpha;

    private void startAnimInto() {
        int h = mLChildContent.getBottom() - mLChildContent.getTop();
        float startY = mLChildContent.getTop() - h - h /2;

        if(mIntoAnimAlpha == null){
            mIntoAnimAlpha= ObjectAnimator.ofFloat(mLChildContent, "alpha", 0f, 1f);
        }
        mIntoAnimAlpha.setDuration(500).start();

        if (mIntoAnimTrans == null) {
            mIntoAnimTrans = ObjectAnimator.ofFloat(mLChildContent, "translationY", startY, 0);
        }
//        mIntoAnimTrans.setDuration(500).start();
        mIntoAnimAlpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mLChildContent.setVisibility(VISIBLE);
                mTransparentLayout.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private ObjectAnimator mOutAnimTrans;
    private ObjectAnimator mOutAnimAlpha;

    private void startAnimOut() {
        int h = mLChildContent.getBottom() - mLChildContent.getTop();
        float startY = mLChildContent.getTop() - h / 2;
        float endY = h;
        if (mOutAnimTrans == null) {
            mOutAnimTrans = ObjectAnimator.ofFloat(mLChildContent, "translationY", startY, -endY);
        }
//        mOutAnimTrans.setDuration(500).start();

        if(mOutAnimAlpha == null){
            mOutAnimAlpha= ObjectAnimator.ofFloat(mLChildContent, "alpha", 1f, 0f);
        }
        mOutAnimAlpha.setDuration(200).start();
        mOutAnimAlpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLChildContent.setVisibility(GONE);
                mTransparentLayout.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public interface IConditionScreenLisenter{
        void currentSelectType(boolean zongHe, boolean quanHouJia, boolean xiaoLiang, int typeValue);

        void changeLayout(boolean singeLine);

        void wiperSitchChecked(boolean checkState);
    }

}
