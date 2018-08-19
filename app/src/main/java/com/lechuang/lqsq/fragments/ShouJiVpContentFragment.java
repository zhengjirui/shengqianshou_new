package com.lechuang.lqsq.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lechuang.lqsq.MyApplication;
import com.lechuang.lqsq.R;
import com.lechuang.lqsq.activity.BaseActivity;
import com.lechuang.lqsq.activity.PreviewImgActivity;
import com.lechuang.lqsq.activity.ProductDetailsActivity;
import com.lechuang.lqsq.bean.LiveProductInfoBean;
import com.lechuang.lqsq.bean.ShouJiBean;
import com.lechuang.lqsq.bean.ShouJiShareBean;
import com.lechuang.lqsq.net.Constant;
import com.lechuang.lqsq.net.NetResultHandler;
import com.lechuang.lqsq.net.Network;
import com.lechuang.lqsq.net.api.CommenApi;
import com.lechuang.lqsq.net.api.ShouJiApi;
import com.lechuang.lqsq.utils.LoadMoreImage;
import com.lechuang.lqsq.utils.Utils;
import com.lechuang.lqsq.widget.dialog.ShowShareDialog;
import com.lechuang.lqsq.widget.views.refeshview.CommonRecyclerAdapter;
import com.lechuang.lqsq.widget.views.refeshview.OnItemClick;
import com.lechuang.lqsq.widget.views.refeshview.ViewHolderRecycler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: zhengjr
 * @since: 2018/6/28
 * @describe:
 */

public class ShouJiVpContentFragment extends BaseFragment implements OnRefreshLoadMoreListener, LoadMoreImage.LoadListenter {


    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefresh;
    @BindView(R.id.smart_refresh_header)
    ClassicsHeader mSmartRefreshHeader;
    @BindView(R.id.smart_refresh_footer)
    ClassicsFooter mSmartRefreshFooter;

    private ArrayList<ShouJiBean.ListBean> mShouJiBeans = new ArrayList<>();
    private int mType;//手记的类型，用于区分是每日必推还是推广物料（1为每日必推）
    private CommonRecyclerAdapter<ShouJiBean.ListBean> mCommonRecyclerAdapter;
    private int page = 1;
    private String productType;

    /**
     * 这里注意fragment千万不要用到单利，很容易内存泄漏
     * 无参数
     *
     * @return
     */
    public static ShouJiVpContentFragment newInstance() {
        ShouJiVpContentFragment shouJiVpContentFragment = new ShouJiVpContentFragment();
        return shouJiVpContentFragment;
    }

    /**
     * 这里注意fragment千万不要用到单利，很容易内存泄漏
     * 传递参数
     *
     * @param bundle
     * @return
     */
    public static ShouJiVpContentFragment newInstance(Bundle bundle) {
        ShouJiVpContentFragment shouJiVpContentFragment = new ShouJiVpContentFragment();
        shouJiVpContentFragment.setArguments(bundle);
        return shouJiVpContentFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shouji_vp_content;
    }

    @Override
    protected void initView(View root) {
        super.initView(root);
        Bundle arguments = getArguments();
        this.mType = arguments.getInt("type");
        mSmartRefresh.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

        if (mCommonRecyclerAdapter == null) {
            mCommonRecyclerAdapter = new CommonRecyclerAdapter<ShouJiBean.ListBean>(getContext(), R.layout.item_shoujivpcontent_, mShouJiBeans) {
                @Override
                public void convert(ViewHolderRecycler holder, final ShouJiBean.ListBean listBean) {
                    if (listBean == null) {
                        return;
                    }
                    try {
                        //发布者头像
                        holder.displayImageCircle(R.id.riv_user_photo, listBean.cfMasterImg, R.drawable.pic_morentouxiang);
                        //发布者名称
                        holder.setText(R.id.tv_user_name, listBean.cfMasterName);
                        //发布创建日期
                        holder.setText(R.id.tv_time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(listBean.cfCreateTime)));
                        //发布的内容描述（分享的文案）
                        holder.setText(R.id.tv_describe, listBean.cfShareCopy);
                        //评论
                        holder.setText(R.id.tv_pinglun, listBean.cfComments);

                        //查看
                        TextView tvChaKan = holder.getView(R.id.tv_chakan);
                        //分享
                        TextView tvShare = holder.getView(R.id.tv_share);
                        //一键分享
                        TextView tvOnekeyShare = holder.getView(R.id.tv_onekey_share);
                        //复制
                        TextView tvCopy = holder.getView(R.id.tv_copy);

                        tvChaKan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //查看是跳转到商品详情
                                if (TextUtils.isEmpty(listBean.cfProductId)) {
                                    Utils.show(getContext(), getResources().getString(R.string.parm_fail));
                                    return;
                                }
                                findProductInfo(listBean.cfProductId);//查询商品详情，然后跳转到商品详情页

                            }
                        });

                        tvShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (TextUtils.isEmpty(listBean.cfType)
                                        || TextUtils.isEmpty(listBean.cfNumber + "")
                                        || TextUtils.isEmpty(listBean.id)) {
                                    Utils.show(getContext(), getResources().getString(R.string.parm_fail));
                                    return;
                                }
                                getShareImage(listBean.cfType, listBean.id, listBean.cfNumber + "", listBean.cfShareCopy);
                            }
                        });

                        tvOnekeyShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(listBean.cfType)
                                        || TextUtils.isEmpty(listBean.cfNumber + "")
                                        || TextUtils.isEmpty(listBean.id)) {
                                    Utils.show(getContext(), getResources().getString(R.string.parm_fail));
                                    return;
                                }
                                getShareImage(listBean.cfType, listBean.id, listBean.cfNumber + "", listBean.cfShareCopy);
                            }
                        });
                        tvCopy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.copyText2Clipboard(MyApplication.getContext(), listBean.cfComments);

                            }
                        });


                        productType = listBean.cfType;

                        if (TextUtils.equals(productType, "2")) {//如果是物料推广，只有分享按钮，其他隐藏
                            tvChaKan.setVisibility(View.GONE);
                            tvOnekeyShare.setVisibility(View.GONE);
                            tvShare.setVisibility(View.VISIBLE);
                        } else if (TextUtils.equals(productType, "1")) {//如果是多个商品，只有一键分享按钮，其他隐藏
                            tvChaKan.setVisibility(View.GONE);
                            tvOnekeyShare.setVisibility(View.VISIBLE);
                            tvShare.setVisibility(View.GONE);
                        } else if (TextUtils.equals(productType, "0")) {//如果是单个商品，显示查看和分享按钮，其他隐藏
                            tvChaKan.setVisibility(View.VISIBLE);
                            tvOnekeyShare.setVisibility(View.GONE);
                            tvShare.setVisibility(View.VISIBLE);
                        } else {
                            tvChaKan.setVisibility(View.GONE);
                            tvOnekeyShare.setVisibility(View.GONE);
                            tvShare.setVisibility(View.GONE);
                        }

                        final RecyclerView rvImg = holder.getView(R.id.rv_img);
                        rvImg.setLayoutManager(new GridLayoutManager(getContext(), 3));

                        //处理单商品和多商品的图片问题
                        ShouJiBean.ListImageInfo listImageInfo = new ShouJiBean.ListImageInfo();
                        if (TextUtils.equals(productType, "1")) {
                            for (ShouJiBean.ListBean.ProductBean productBean : listBean.product) {
                                listImageInfo.detailImages = new ArrayList<>();
                                listImageInfo.detailImages.add(productBean.imgUrl);
                            }
                        } else {
                            if (listBean.detailImages == null) {
                                listBean.detailImages = new ArrayList<>();
                            }
                            listImageInfo.detailImages = listBean.detailImages;
                        }

                        if (TextUtils.equals(productType, "1")){
                            CommonRecyclerAdapter rvImgAdapter2 = new CommonRecyclerAdapter<ShouJiBean.ListBean.ProductBean>(getContext(), R.layout.item_shoujivpcontent_child, listBean.product) {

                                @Override
                                public void convert(ViewHolderRecycler holder, ShouJiBean.ListBean.ProductBean productBean) {
                                    if (productBean == null) {
                                        return;
                                    }

                                    TextView tvProductPrice = holder.getView(R.id.tv_product_price);
                                    if (TextUtils.equals(productType, "1")) {
                                        tvProductPrice.setVisibility(View.VISIBLE);
                                        tvProductPrice.setText("¥" + productBean.cfCouponAfterPrice);
                                    } else {
                                        tvProductPrice.setVisibility(View.GONE);
                                    }

                                    ImageView sivProductImg = holder.getView(R.id.siv_product_img);

                                    holder.displayImage(R.id.siv_product_img, productBean.imgUrl);
//                                sivProductImg.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                    }
//                                });
                                }
                            };
                            rvImg.setAdapter(rvImgAdapter2);
                            rvImgAdapter2.setOnItemClick(new OnItemClick() {
                                @Override
                                public void itemClick(View v, int position) {
                                    //单个商品的时候才能查看详情
                                    if (TextUtils.equals(productType, "1")) {
                                        //查看是跳转到商品详情
                                        if (TextUtils.isEmpty(listBean.product.get(position).cfProductId)) {
                                            Utils.show(getContext(), getResources().getString(R.string.parm_fail));
                                            return;
                                        }
                                        findProductInfo(listBean.product.get(position).cfProductId);//查询商品详情，然后跳转到商品详情页
                                    }else if (TextUtils.equals(productType, "2")) {
                                        Intent intent = new Intent(getActivity(), PreviewImgActivity.class);
                                        if (listBean.detailImages.size() > 0) {
                                            intent.putStringArrayListExtra("urls", listBean.detailImages);
                                            intent.putExtra("position", position);
                                            startActivity(intent);
                                        }

                                    }
                                }
                            });
                        }else {
                            CommonRecyclerAdapter rvImgAdapter1 = new CommonRecyclerAdapter<String>(getContext(), R.layout.item_shoujivpcontent_child, listBean.detailImages) {

                                @Override
                                public void convert(ViewHolderRecycler holder, String s) {
                                    if (s == null) {
                                        return;
                                    }

                                    TextView tvProductPrice = holder.getView(R.id.tv_product_price);
                                    tvProductPrice.setVisibility(View.GONE);

                                    ImageView sivProductImg = holder.getView(R.id.siv_product_img);

                                    holder.displayImage(R.id.siv_product_img, s);
//                                sivProductImg.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                    }
//                                });
                                }
                            };
                            rvImg.setAdapter(rvImgAdapter1);
                            rvImgAdapter1.setOnItemClick(new OnItemClick() {
                                @Override
                                public void itemClick(View v, int position) {
                                    //单个商品的时候才能查看详情
                                    if (TextUtils.equals(productType, "0")) {
                                        //查看是跳转到商品详情
                                        if (TextUtils.isEmpty(listBean.cfProductId)) {
                                            Utils.show(getContext(), getResources().getString(R.string.parm_fail));
                                            return;
                                        }
                                        findProductInfo(listBean.cfProductId);//查询商品详情，然后跳转到商品详情页
                                    } else if (TextUtils.equals(productType, "2")) {
                                        Intent intent = new Intent(getActivity(), PreviewImgActivity.class);
                                        if (listBean.detailImages.size() > 0) {
                                            intent.putStringArrayListExtra("urls", listBean.detailImages);
                                            intent.putExtra("position", position);
                                            startActivity(intent);
                                        }

                                    }
                                }
                            });
                        }
                    } catch (Exception e) {

                    }
                }
            };
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
            mRecyclerView.setAdapter(mCommonRecyclerAdapter);

        } else {
            mCommonRecyclerAdapter.notifyDataSetChanged();
        }
        firstCreate = true;//表示第一次新建成功，等待显示的时候加载数据；
    }

    //懒加载
    private boolean firstCreate;
    private boolean firstInit;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && firstCreate && !firstInit) {
            getData();
            firstInit = true;
            showWaitDialog("");
        }
    }


    /**
     * 情景：fragment 嵌套 viewpager ，viewpager 中添加fragment
     * 问题 ： 这里当fragment显示时，setUserVisibleHint回调时还没有firstCreate，所以不会请求数据，
     * 处理方式：这里的处理方式是在获取焦点的时候再验证一下。
     * 注意点： 这里的setUserVisibleHint是在viewpager中第二次可以使用（viewpager有预加载功能）
     */
    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && firstCreate && !firstInit) {
            getData();
            firstInit = true;
            showWaitDialog("");
        }
    }

    private NetResultHandler<ShouJiBean> mNetResultHandler;

    private void getData() {
        if (mNetResultHandler == null) {
            mNetResultHandler = new NetResultHandler<ShouJiBean>(ShouJiVpContentFragment.this) {
                @Override
                protected void success(ShouJiBean data) {
                    if (data == null || data.list.size() <= 0) {
                        //没有更多数据
                        onFinishRefreshLoadMore(true);
                        return;
                    }

                    updata(data.list);
                    //加载成功
                    onFinishRefreshLoadMore(true);
                }

                @Override
                public void error(int code, String msg) {
                    onFinishRefreshLoadMore(false);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    onFinishRefreshLoadMore(false);
                }

            };
        }

        if (this.mType == 1) {
            getProductData();
        } else if (this.mType == 2) {
            getPosterData();
        }
    }

    private void getProductData() {
        Network.getInstance().getApi(ShouJiApi.class)
                .productCircleShow(page + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mNetResultHandler);
    }

    private void getPosterData() {
        Network.getInstance().getApi(ShouJiApi.class)
                .posterCircleShow(page + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mNetResultHandler);
    }

    public void updata(List<ShouJiBean.ListBean> shouJiBeans) {
        if (page == 1) {
            mShouJiBeans.clear();
        }
        mShouJiBeans.addAll(shouJiBeans);
        mCommonRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        this.page = 1;
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mShouJiBeans.size() == 0) {
            mSmartRefresh.autoRefresh();
            return;
        }

        this.page++;
        getData();

    }

    private void onFinishRefreshLoadMore(boolean stateRefreshLoadMore) {
        if (page == 1) {
            mSmartRefresh.finishRefresh(500, stateRefreshLoadMore);
        } else {
            mSmartRefresh.finishLoadMore(500);
        }
        hideWaitDialog();
    }

    /**
     * 查询商品信息，跳转到商品详情
     *
     * @param productId
     */
    private void findProductInfo(String productId) {
        showWaitDialog("");
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        map.put("type", 4 + "");//后台需要添加
        Network.getInstance().getApi(CommenApi.class)
                .getProductInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<LiveProductInfoBean>((BaseActivity) getActivity()) {
                    @Override
                    public void success(LiveProductInfoBean result) {
                        if (result == null) {
                            return;
                        }
                        startActivity(new Intent(getActivity(), ProductDetailsActivity.class)
                                .putExtra(Constant.listInfo, JSON.toJSONString(result.productWithBLOBs))
                                .putExtra("type", 4 + ""));
                    }

                    @Override
                    public void error(int code, String msg) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideWaitDialog();
                    }
                });

    }


    private void getShareImage(String cfType, String id, String cfNumber, final String cfShareCopy) {
        Map<String, String> map = new HashMap<>();
        map.put("cfType", cfType);
        map.put("id", id);
        map.put("cfNumber", cfNumber);
        showWaitDialog("");
        Network.getInstance().getApi(ShouJiApi.class)
                .shareProductFriendCircle(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultHandler<ShouJiShareBean>((BaseActivity) getActivity()) {
                    @Override
                    protected void success(ShouJiShareBean data) {
                        if (data == null || data.circleFriend == null || data.circleFriend.detailImages == null || data.circleFriend.detailImages.size() <= 0) {
                            return;
                        }
                        if (!TextUtils.isEmpty(cfShareCopy)) {
                            Utils.copyText2Clipboard(MyApplication.getContext(), cfShareCopy);
                        }

                        List<String> detailImages = data.circleFriend.detailImages;
                        shareImage(detailImages);

                    }

                    @Override
                    public void error(int code, String msg) {
                        hideWaitDialog();
                    }
                });
    }


    private void shareImage(final List<String> detailImages) {
        File filePath = new File(MyApplication.getContext().getCacheDir() + "/cache_image");

        Utils.deleteAllFiles(filePath);

        LoadMoreImage loadMoreImage = LoadMoreImage.getInstance();
        loadMoreImage.setDetailImages(detailImages);

        loadMoreImage.loadImages();

        loadMoreImage.setLoadLisenter(ShouJiVpContentFragment.this);
    }

    private ShowShareDialog mShowShareDialog;

    @Override
    public void loadAllSuccess(File[] files) {
        if (mShowShareDialog == null) {
            mShowShareDialog = new ShowShareDialog(getContext());
        }
        mShowShareDialog.setImgUrlFiles(files);
        if (mShowShareDialog.isShowing()) {
            mShowShareDialog.dismiss();
        } else {
            mShowShareDialog.show();
        }
        hideWaitDialog();
    }

    @Override
    public void loadPartSuccess(File[] files, List<Integer> position) {
        if (mShowShareDialog == null) {
            mShowShareDialog = new ShowShareDialog(getContext());
        }
        mShowShareDialog.setImgUrlFiles(files);
        if (mShowShareDialog.isShowing()) {
            mShowShareDialog.dismiss();
        } else {
            mShowShareDialog.show();
        }

        hideWaitDialog();
    }

    @Override
    public void loadAllError() {
        Utils.show(MyApplication.getContext(), "创建分享失败！");
        hideWaitDialog();
    }

    @Override
    public void onWaitCancel() {
        super.onWaitCancel();
        LoadMoreImage.getInstance().clearLoad();
    }
}
