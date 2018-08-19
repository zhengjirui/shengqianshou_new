package com.lechuang.lqsq.widget.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.lechuang.lqsq.R;


/**
 * @author: LGH
 * @since: 2018/1/9
 * @describe: 选择成为合伙人
 */

public class PopChoseAgency {

    private PopupWindow popupWindow;
    private View view;
    private OnChoseAgency onAgencyClick;

    public PopChoseAgency(Context context, OnChoseAgency isAgencyClick) {
        onAgencyClick = isAgencyClick;
        view = LayoutInflater.from(context).inflate(R.layout.pop_chose_agency, null, false);
        RelativeLayout popAll = (RelativeLayout) view.findViewById(R.id.pop_chose_agency);
        ImageView ivBack = (ImageView) view.findViewById(R.id.iv_integral_back);
        ImageView ivAgency = (ImageView) view.findViewById(R.id.iv_integral_agency);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.showAsDropDown(view);

        popAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ivAgency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/1/10 成为合伙人页面
                if (onAgencyClick != null) {
                    onAgencyClick.gotoAgency();
                }
            }
        });
    }

    public interface OnChoseAgency {
        void gotoAgency();
    }
}
