package com.rl.geye.ui.aty;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rl.geye.MyApp;
import com.rl.geye.R;
import com.rl.geye.adapter.CloudUsersAdapter;
import com.rl.geye.base.BaseP2PAty;
import com.rl.geye.bean.CloudCommoResponse;
import com.rl.geye.bean.CloudListUser;
import com.rl.geye.bean.CloudUserListByIDResponse;
import com.rl.geye.util.CgiCallback;
import com.rl.p2plib.utils.JSONUtil;

import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Nicky on 2016/10/20.
 * 时区选择
 */
public class DevUsrAty extends BaseP2PAty {

    @BindView(R.id.rv_users)
    RecyclerView rvUsers;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ly_all)
    View lyAll;

    View progressView;


    private CloudUsersAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.aty_dev_usr;
    }

    @Override
    protected void onP2PStatusChanged() {

    }

    @Override
    protected boolean initPrepareData() {
        return super.initPrepareData();
    }

    @Override
    public View getVaryTargetView() {
        return lyAll;
    }

    @Override
    protected void initToolBar() {
        initCommonToolBar(toolbar);
        tvTitle.setText(R.string.dev_user);
    }

    private void deleteShareUser(String username,int position){
        MyApp.getCloudUtil().removeCloudDevice(mDevice.getDevId(),username,new CgiCallback(position) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                MyApp.showToast(R.string.error_lost_connection);
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                Log.e("cloud rsp",s);
                CloudCommoResponse rsp = JSONUtil.fromJson(s, CloudCommoResponse.class);
                if (rsp == null) {
                    MyApp.showToast(R.string.err_data);
                    return;
                }
                switch (rsp.getStatus()) {
                    case 1:
                        mAdapter.remove((int)paramOutside);
                        mAdapter.notifyDataSetChanged();
                        if(mAdapter.getData().size() == 0) finish();
                        break;
                    default:
                        MyApp.showToast(R.string.tips_delete_failed);
                        break;
                }
            }
        });
    }

    // 菜单项按钮点击的回调函数
    private CloudUsersAdapter.OnMenuClickListener onMenuClickListener = new CloudUsersAdapter.OnMenuClickListener() {
        @Override // 删除分享
        public void onDeleteClick(final String username,final int pos) {
            AlertDialog.Builder exitDlg = new AlertDialog.Builder(DevUsrAty.getRunningActivity());
            exitDlg.setTitle(R.string.tips_warning);
            exitDlg.setMessage(R.string.tips_delete_usr);
            exitDlg.setPositiveButton(R.string.str_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteShareUser(username,pos);
                    }
                });
            exitDlg.setNegativeButton(R.string.str_cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //...To-do
                        }
                    });
            exitDlg.show();
        }
    };

    @Override
    protected void initViewsAndEvents() {
        progressView = getActivity().getLayoutInflater().inflate(R.layout.loading_view, (ViewGroup) rvUsers.getParent(), false);
        mAdapter = new CloudUsersAdapter(new ArrayList<String>());
        mAdapter.setEmptyView(progressView);
        mAdapter.setOnMenuClickListener(onMenuClickListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvUsers.setLayoutManager(layoutManager);
        rvUsers.setItemAnimator(new DefaultItemAnimator());

        MyApp.getCloudUtil().getUsersByDevID(mDevice.getDevId(),new CgiCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                MyApp.showToast(R.string.error_lost_connection);
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                Log.e("cloud rsp",s);
                CloudUserListByIDResponse rsp = JSONUtil.fromJson(s, CloudUserListByIDResponse.class);
                if (rsp == null) {
                    MyApp.showToast(R.string.err_data);
                    return;
                }
                switch (rsp.getStatus()) {
                    case 1:
                        if(rsp.getUsers().size() == 0){
                            MyApp.showToast(R.string.error_no_valid_user);
                            ((DevUsrAty)paramOutside).finish();
                            return;
                        }

                        mAdapter.getData().clear();
                        for(CloudListUser listUser:rsp.getUsers()){
                            mAdapter.getData().add(listUser.getUsername());
                        }
                        rvUsers.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        MyApp.showToast(R.string.error_no_valid_user);
                        break;
                }
            }
        });
    }

    @Override
    protected void onClickView(View v) {

    }
}
