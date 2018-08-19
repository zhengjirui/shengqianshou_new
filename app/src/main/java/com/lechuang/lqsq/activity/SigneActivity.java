package com.lechuang.lqsq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.bean.SignBean;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/02
 * 时间：13:27
 * 描述：
 */

public class SigneActivity extends Activity {
    @BindView(R.id.iv_dismiss)
    ImageView ivDismiss;
    @BindView(R.id.gv_days)
    GridView gv_days;

    private Context mContext = SigneActivity.this;
    private String[] days = {"1天", "2天", "3天", "4天", "5天", "6天", "7天"};
    private ColorStateList redColors;

    private static int count;  //签到天数
    private static List<SignBean.ListBean> list;   //签到信息

    public static void launchActivity(Context context, List<SignBean.ListBean> data, int continuousSigned) {
        count = continuousSigned;
        list = data;
        context.startActivity(new Intent(context, SigneActivity.class));
    }

    private Map<Integer, SignBean.ListBean> mapSigne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.CENTER);
        setContentView(R.layout.activity_signe);
        ButterKnife.bind(this);
        initView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initView() {
        redColors = ColorStateList.valueOf(0xfffe738d);
        int size = list.size();
        mapSigne = new HashMap<Integer, SignBean.ListBean>();
        for (int i = 0; i < size; i++) {
            mapSigne.put(list.get(i).continuousSigned, list.get(i));
        }
        if (count >= 6) {
            gv_days.setAdapter(new SingeAdapter(6));
        } else {
            gv_days.setAdapter(new SingeAdapter(count - 1));
        }
    }

    @OnClick(R.id.iv_dismiss)
    public void onViewClicked(View view) {
        finish();
    }

    /**
     * 天数的适配器
     */
    class SingeAdapter extends BaseAdapter {
        private int index;
        private int images[] = {R.mipmap.round_r, R.mipmap.round};

        public SingeAdapter(int index) {
            this.index = index;
        }

        @Override
        public int getCount() {
            return days.length;
        }

        @Override
        public Object getItem(int position) {
            return days[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyTag mt = null;
            if (convertView == null) {
                mt = new MyTag();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_signin_days, null);
                mt.tv_days = (TextView) convertView.findViewById(R.id.tv_days);
                mt.iv_round = (ImageView) convertView.findViewById(R.id.iv_round);
                convertView.setTag(mt);
            } else {
                mt = (MyTag) convertView.getTag();
            }

            //修改显示，用map集合储存（天数，签到信息）；
            SignBean.ListBean listBean = mapSigne.get(position + 1);
            if (index == position) {
                mt.tv_days.setTextColor(getResources().getColor(R.color.rgb_fe596c));
                mt.tv_days.setText("第" + (index + 1) + "天");
                mt.iv_round.setImageResource(images[0]);
            } else {
                mt.tv_days.setTextColor(getResources().getColor(R.color.white));
                mt.tv_days.setText(days[position]);
                mt.iv_round.setImageResource(images[1]);
            }

            notifyDataSetChanged();
            return convertView;
        }
    }

    /**
     * viewHolder类
     */
    class MyTag {
        private TextView tv_days;//连续签到天数，积分
        private ImageView iv_round;
    }
}
