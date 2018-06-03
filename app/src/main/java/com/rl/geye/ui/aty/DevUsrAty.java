package com.rl.geye.ui.aty;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.edwintech.vdp.jni.ApiMgrV2;
import com.rl.commons.BaseApp;
import com.rl.commons.interf.EdwinTimeoutCallback;
import com.rl.commons.utils.ClickUtil;
import com.rl.geye.MyApp;
import com.rl.geye.R;
import com.rl.geye.adapter.CloudUsersAdapter;
import com.rl.geye.adapter.TimeZoneAdapter;
import com.rl.geye.base.BaseP2PAty;
import com.rl.geye.bean.CloudDevicesResponse;
import com.rl.geye.constants.Constants;
import com.rl.geye.db.bean.CloudUser;
import com.rl.geye.db.bean.EdwinDevice;
import com.rl.geye.util.CgiCallback;
import com.rl.p2plib.bean.DevTimeZone;
import com.rl.p2plib.utils.JSONUtil;

import java.util.ArrayList;
import java.util.List;

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

    private List<CloudUser> mUsers;
    private CloudUsersAdapter mAdapter;

    private Handler mHandler;
    private BaseQuickAdapter.OnItemClickListener onItemClickListener;

    private volatile long opTime = 0; // 命令执行时间

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

    public class ListUser{
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public class CloudUserListByIDResponse{
        private int status;
        private int msg;
        private int userNo;
        private List<ListUser> users;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<ListUser> getUsers() {
            return users;
        }

        public void setUsers(List<ListUser> users) {
            this.users = users;
        }

        public int getMsg() {
            return msg;
        }

        public void setMsg(int msg) {
            this.msg = msg;
        }

        public int getUserNo() {
            return userNo;
        }

        public void setUserNo(int userNo) {
            this.userNo = userNo;
        }
    }

    @Override
    protected void initViewsAndEvents() {
        progressView = getActivity().getLayoutInflater().inflate(R.layout.loading_view, (ViewGroup) rvUsers.getParent(), false);
        mAdapter = new CloudUsersAdapter(new ArrayList<String>());
        mAdapter.setEmptyView(progressView);

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
                    MyApp.showToast(R.string.error_no_valid_user);
                    return;
                }
                switch (rsp.getStatus()) {
                    case 1:
                        mAdapter.getData().clear();
                        for(ListUser listUser:rsp.users){
                            mAdapter.getData().add(listUser.getUsername());
                        }
                        rvUsers.setAdapter(mAdapter);
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
