package com.lechuang.lqsq.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lechuang.lqsq.R;
import com.lechuang.lqsq.widget.views.NoScrollListView;

/**
 * 作者：尹松涛.
 * 邮箱：yinsongtaoshmily@outlook.com
 * 日期：2018/02/01
 * 时间：15:00
 * 描述：
 */

public class ListDialog extends Dialog implements AdapterView.OnItemClickListener {
    private Context context;
    private NoScrollListView listView;
    private String[] datas;
    private AdapterView.OnItemClickListener listener;
    private ListDialogAdapter adapter;

    public ListDialog(Context context, String[] datas) {
        super(context, R.style.myDialogStyle);
        this.datas = datas;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list);
        listView = (NoScrollListView) findViewById(R.id.listview);
        adapter = new ListDialogAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void show() {
        if (datas.length == 0) {
            dismiss();
        }
        super.show();
    }

    public void setItemClick(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(String[] datas) {
        this.datas = datas;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isShowing()) {
            dismiss();
        }
        if (listener != null) {
            listener.onItemClick(parent, view, position, id);
        }
    }


    class ListDialogAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.length;
        }

        @Override
        public String getItem(int position) {
            return datas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(context, R.layout.listdialog_item, null);
            ((TextView) view.findViewById(R.id.list_item)).setText(getItem(position));
            return view;
        }
    }
}

