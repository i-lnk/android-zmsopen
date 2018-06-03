package com.rl.geye.adapter;


import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rl.commons.utils.ClickUtil;
import com.rl.geye.R;
import com.rl.geye.db.bean.EdwinDevice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Nicky on 2016/10/31.
 * 用户列表适配
 */
public class CloudUsersAdapter extends BaseQuickAdapter<String, CloudUsersAdapter.MyViewHolder> {
    private List<String> usernames;

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    public CloudUsersAdapter(List<String> usernames) {
        super(R.layout.item_rv_user, usernames);
        this.usernames = usernames;
    }

    public class MenuClickListener implements View.OnClickListener {
        private String username;
        private int position;

        public MenuClickListener(String username, int position) {
            this.username = username;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (ClickUtil.isFastClick(mContext, v))
                return;
            switch (v.getId()) {
                case R.id.ic_del_usr:
                    Log.e("event click","click delete user button");
                    break;
            }
        }
    }

    @Override
    protected void convert(MyViewHolder myViewHolder, String s) {
        if(s != null && s.isEmpty() == false){
            myViewHolder.tvName.setText(s);
            final int realPosition = myViewHolder.getLayoutPosition() - getHeaderLayoutCount();
            myViewHolder.ivDelete.setOnClickListener(new MenuClickListener(s, realPosition));
        }
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    public static class MyViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_item)
        ImageView ivItem;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ic_del_usr)
        ImageView ivDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
//            Logger.e("itemView: "+itemView);
            if (itemView.getTag(R.id.BaseQuickAdapter_databinding_support) != null) {
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
