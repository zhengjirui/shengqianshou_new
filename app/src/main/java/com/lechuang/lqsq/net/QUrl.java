package com.lechuang.lqsq.net;

import com.lechuang.lqsq.BuildConfig;

/**
 * Created by zhf on 2017/8/17.
 * 【网络请求资源地址】
 */

public interface QUrl {

    //正式
    String url = BuildConfig.BASE_URL;
    //正式
    //String url = "http://192.168.1.200:8889/";
    String host = "appH/html/";
    String host1 = "appH/";
    //搜返利四个栏目图片接口
    String soufanli_programaImg = "indexShow/searchJuanIconImg";
    //搜返利数量
    String countSum = "indexShow/couponCountSum";
    String soufanliProduct = "indexShow/userLikeProduct";
    // 用户协议
    String coullect = "productCollections/coullect";
    /**
     * 首页\
     */
    String homeTodayProduct = "indexShow/getAdProduct1";

    //首页轮播图
    String homePageBanner = "indexShow/homePageBanner";
    //分类
    String home_classify = "indexShow/classifyShowAll";

    String classify = "indexShow/getNextTbClass";
    //滚动条接口(滚动的文字)
    String home_scrollTextView = "indexShow/homePageScrollBar";
    //首页四个栏目图片接口
    String home_programaImg = "indexShow/ProgramaImg";
    //首页最下栏目接口
    String home_lastPage = "indexShow/homeLastPage";
    //栏目1全部数据
    String recommend1 = "indexShow/programa1ShowAll";
    //栏目2全部数据
    String recommend2 = "indexShow/programa2ShowAll";
    //栏目3全部数据
    String recommend3 = "indexShow/programa3ShowAll";
    //栏目4全部数据
    String recommend4 = "indexShow/programa4ShowAll";
    //栏目5全部数据
    String recommend5 = "indexShow/programa5ShowAll";
    //搜索商品结果
//    String home_product = "indexShow/productShowAll";
    String home_product = "indexShow/productShowAll2.0";

    String home_programa = "indexShow/programaShowAll";
    //首页消息是否有未读消息
    String isUnread = "wjf/notice/unread";
    //首页所有消息
    String allNews = "wjf/notice/all_news";
    //首页数据
    String home_page = "indexShow/homePage";
    //爆款
    String home_hot = "indexShow/homeLastPage";
    //详情
    String details = "indexShow/productDetail";
    //获取信息，帮助中心，客服中心
    String getHelpInfo = "indexShow/getHelpInfo";
    //进入app时的广告图
    String advertisementInfo = "showAdviertisement/advertisingImg";
    //过夜单
    String overnightBill = "indexShow/OvernightBill";
    //滚动文字商品
    String gunDongText = "indexShow/daShu_ScrollBar";

    /*
    * 爆料
    * */
    //爆料
    String tipOff = "tipOff/tipOffShow";
    //爆料详情
    String tipOffDetail = "tipOff/tipOffDetail";
    //爆料评论列表
    String tipOffList = "tipOff/tipOffAppraise";
    //评论内容
    String tipContent = "api/tipOffConfirm/apprasieTipOff";
    //点赞
    String tipPraise = "api/tipOffConfirm/clickPraise";
    //给列表评论点赞
    String tipPraiseList = "api/tipOffConfirm/apprasieTipOffPrasie";
    //获取分享出去的url
    String tipOffShareUrl = "tipOff/get_tipOff_url";
    //通过商品id获取商品的详细信息  用于直播界面获取商品信息
    String getProductInfo = "zhuanPage/zhuanProductDetail";

    /**
     * 手记
     */
    //商品列表接口
    String productCircleShow = "circle/productCircleShow_1.0";
    //物料列表接口
    String posterCircleShow = "circle/posterCircleShow_1.0";
    //获取分享图片的接口
    String shareProductFriendCircle = "circle/shareProductFriendCircle_1.0";


    /**
     * 晒单
     */
    //晒单广场
    String theSun = "baskOrder/baskOrderShowList";
    //晒单详情
    String sunSquare = "baskOrder/baskOrderShowDetail";
    //我要晒单，上传图片
    String fileUpload = "api/fileUploadImage/upload";
    //我要晒单，评论
    String sunComment = "api/baskOrderConfirm/addBaskOrder";
    //晒单评论列表
    String sunCommentList = "baskOrder/baskAppraise";
    //校验订单
    String isRightOrder = "api/baskOrderConfirm/verifyOrderNum";
    /**
     * 登录，注册，找回密码,发送验证码
     */
    //注册
    String register = "retrieve/register";
    //找回密码
    String findPwd = "retrieve/sureUserPassword";
    //发送验证码 和三方登录绑定手机号，获取验证码
    String sendCode = "retrieve/registVerifiCode";
    //找回密码的验证码
    String findCode = "retrieve/sendVerifiCode";

    /*
    * 登录注册
    * */
    //登录
    // String login ="signin_check";
    //新登录接口
    String login = "retrieve/login";
    //登出
    String logout = "logout";
    //修改密码,获取短信验证码
    String updatePwdCode = "user/appUsers/updatePasswordSendVerifiCode";
    //确认修改密码
    String updatePwd = "user/appUsers/updateAppUserPassword";
    //用户修改自己的用户信息 (除开密码)
    String updateInfo = "user/appUsers/updateAppUser";
    //用户绑定手机号，发送验证码
    String bindingPhone = "retrieve/sendBindingVerifiCode";
    //第三方淘宝登陆绑定手机号码（校验接口）
    String threeLogin = "verifyThirdCheckUser";
    //第三方淘宝登陆,第一次绑定输入手机号码后调用此接口
    String threeBinding = "thirdCheckUser";

    //QQ微信登录验证是否绑定手机号
    String QQANDWX_Vertify = "verifyThirdCheckOpenId";
    String QQANDWX_Binding = "thirdCheckVerifiCode";
    //直接绑定QQ或微信
    String boundThree="registVerifiCodeWeixinOrQQ";

    String tb_privilegeUrl = "indexShow/get_tbPrivilegeUrl_tbTpwd";
    //淘宝用户登陆，绑定手机号，发送验证码的功能
    String threeSendCode = "thirdVerifiCode";
    //连续签到第几天
    String sign = "api/sign/start_sign";
    //签到成功
    String signSuccess = "api/sign/userSigned";


    //自动找回
    String autoOrder = "api/baskOrderConfirm/getSelfMotionFindOrderNum";
    /**
     * 我的
     */
    //我的代理  里面的list是一级代理  list1 是二级代理
    String agent = "user/appUsers/getOwnAndUnderling";
    //申请代理
    String applyAgent = "user/appUsers/agencyDetail";
    //我的收益
    String myIncome = "wjf/userIntegral/ownIncome";
    // 我的收益
    String ownIncome = "PartnerCenter/OwnIncome";
    //订单详情
    String orderDetails = "PartnerCenter/orderDetail";
    // 我的团队
    String mineTeam = "PartnerCenter/MyTeam";
    // 第二级团队
    String nextTeam = "PartnerCenter/NextTeam";
    //设置里的意见反馈
    String opinion = "user/appUsers/insertOption";
    //提现的积分信息
    String txInfo = "userWithdraw";
    //点击提现按钮逻辑
    String tx = "sendWithdraw";
    //领取积分
    String getJf = "api/baskOrderConfirm/getIntegralOrderNum";
    //vip等级
    String userInfo = "user/appUsers/userInfo";
    //获取代理分享和分享赚钱的url和背景图
    String shareMoneyInfo = "/user/appUsers/get_share_html";
    //英雄榜
    String heroAgent = "user/appUsers/getAgencyIncome";

    /**
     * 支付
     */
    //请求支付信息
    String aply = "agency/agencySendOrder";
    //支付成功回调
    String aplySuccess = "verifyAgencyOrder";
    /**
     * 自动成为代理
     */
    String autoAgent = "get_agency_pid";

    /**
     * 订单编号
     */
    //订单编号
    String orderNum = "wjf/userIntegral/orderCallBack";
    //版本更新
    String updateVersion = "user/appUsers/get_appVersion";

    /**
     * 首次启动app图片
     */
    String loadingImg = "indexShow/start_img";
    //获取淘口令
    String sharePassword = "indexShow/get_tbTpwd";

    /*
    * H5页面
    * */

    //商品详情
    //String productDetails = url + host1 + "details-num/details/details.html";
    String productDetails = url + host + "details.html";
    //商品分享
    //String productShare = url + host1 + "details-num/details_share/details.html";
    String productShare = url + host + "details_share.html";
    //爆料分享
    String tipOffShare = url + host1 + "disclose/disclose.html";
    //任务中心
    String taskCenter = url + host + "messionCenter.html";
    //直播
    public static String live = url + host + "live.html";
    //会员中心
    public static String vipCenter = url + host + "vipCenter.html";
    //分享赚钱
    public static String shareMoney = url + host + "user_share.html";
    //申请代理
    //public static String applyAgent = url + host + "agent.html";
    //代理分享
    String agentShare = url + host + "agent_share.html";
    //爆料分享的网址
    //public static String tipOffShare = url + host + "bl_share.html";
    //晒单分享
    public static String theSunShare = url + host + "sun_share.html";
    //我的代理邀请好友分享出去的H5的网址
    String activityShare = url + host + "active_share.html";
    //商品分享的H5
    public static String detailShare = url + host + "detail_share.html";


    //赚

    //顶部tab列表
    String getTopTabList = "zhuanPage/classifyShowAll";
    //顶部广告图
    String getTopBanner = "zhuanPage/zhuanIndexBanner";
    //列表
    String getListInfo = "zhuanPage/zhuanProductShowAll";
    //赚页面介绍
    String zhaunInfo = "share_zhuan_info/showInfo";
    //获取分享商品的域名
    String getShareProductUrl = "indexShow/queryShowHide";

    //身份校验获取验证码
    String getCheckCode = "retrieve/erifiCodeSend";
    //校验获取的验证码
    String getVerifiCode = "retrieve/getVerifiCode";

    // 用户协议
    String userAgreement = url + host + "statement.html";

    String HotSearchWord = "indexShow/HotSearchWord";
    String zuji = "productCollections/getMyFootprint";
    String shoucang = "productCollections/getCoullect";
    String programSwitch = "indexShow/queryAllSwitch";
    String getHourTime = "indexShow/getHourOfTime";
    String duiba = "duiba/loginDuiBa";

    //分享APP
    String getShareImagesNew = "user/appUsers/get_share_images_new";

    /**
     * 获取活动
     */
    String envelopeactivity = "activity_desc/envelope_activity";
    /**
     * 拆红包
     */
    String pick_red_env = "activity_desc/pick_red_env";
    /**
     * 查询商品详情
     */
    String queryProductDetails = "zhuanPage/zhuanProductDetail";

    //爆料分享
    String tipOffNewShare = "appH/html/burst_share.html";
}
