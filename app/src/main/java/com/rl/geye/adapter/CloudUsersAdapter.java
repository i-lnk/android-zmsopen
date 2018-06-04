package com.rl.geye.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rl.commons.utils.ClickUtil;
import com.rl.geye.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Nicky on 2016/10/31.
 * 用户列表适配
 */
public class CloudUsersAdapter extends BaseQuickAdapter<String, CloudUsersAdapter.MyViewHolder> {
    private List<String> usernames;
    private OnMenuClickListener mListener;

    public CloudUsersAdapter(List<String> usernames) {
        super(R.layout.item_rv_user, usernames);
        this.usernames = usernames;
    }

    public interface OnMenuClickListener {
        void onDeleteClick(String username,int pos);
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        mListener = listener;
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
                    Log.e("event click","click delete user:[" + username + "] button");
                    mListener.onDeleteClick(username,position);
                    break;
            }
        }
    }

    @Override
    protected void convert(MyViewHolder myViewHolder,String s) {
        if(s == null) return;
        if(myViewHolder == null) return;

        Log.e("cloud user adapter","add new user:[" + s + "]");

        if(s.isEmpty() == false){
            myViewHolder.tvUser.setText(s);
            int realPosition = myViewHolder.getLayoutPosition() - getHeaderLayoutCount();
            myViewHolder.ivDelete.setOnClickListener(new MenuClickListener(s, realPosition));
        }
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        View view = super.getItemView(layoutResId, parent);
        view.setTag(R.id.BaseQuickAdapter_databinding_support, 1);
        return view;
    }

    public static class MyViewHolder extends BaseViewHolder {
        @BindView(R.id.ly_item)
        LinearLayout lyItem;
        @BindView(R.id.tv_user)
        TextView tvUser;
        @BindView(R.id.ic_del_usr)
        ImageView ivDelete;
        @BindView(R.id.middle_line)
        View middleLine;
        @BindView(R.id.bottom_line)
        View bottomLine;
        @BindView(R.id.bg_enable)
        View bgEnable;

        public MyViewHolder(View itemView) {
            super(itemView);
            Log.e("itemView","initialize cloud users adapter view holder");
            if (itemView.getTag(R.id.BaseQuickAdapter_databinding_support) != null) {
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
