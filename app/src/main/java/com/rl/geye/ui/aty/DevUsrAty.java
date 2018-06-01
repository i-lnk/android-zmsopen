package com.rl.geye.ui.aty;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.edwintech.vdp.jni.ApiMgrV2;
import com.rl.commons.BaseApp;
import com.rl.commons.interf.EdwinTimeoutCallback;
import com.rl.commons.utils.ClickUtil;
import com.rl.geye.R;
import com.rl.geye.adapter.TimeZoneAdapter;
import com.rl.geye.base.BaseP2PAty;
import com.rl.geye.constants.Constants;
import com.rl.p2plib.bean.DevTimeZone;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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

    private List<DevTimeZone> mZoneList;
    private TimeZoneAdapter mAdapter;

    private Handler mHandler;
    private BaseQuickAdapter.OnItemClickListener onItemClickListener;

    private volatile long opTime = 0; // 命令执行时间

    @Override
    protected int getLayoutId() {
        return R.layout.aty_time_zone;
    }

    @Override
    protected void onP2PStatusChanged() {
        mAdapter.setOnline(isOnline);
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
        tvTitle.setText(R.string.time_zone);
    }

    @Override
    protected void initViewsAndEvents() {
        int[] zones = getResources().getIntArray(R.array.time_zone_val);
        initHandler();

        mAdapter = new TimeZoneAdapter(mZoneList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvUsers.setLayoutManager(layoutManager);
        rvUsers.setItemAnimator(new DefaultItemAnimator());
        rvUsers.setAdapter(mAdapter);
        onP2PStatusChanged();
    }

    @Override
    protected void onClickView(View v) {

    }

    /**
     * 设置时区
     */
    private void setTimeZone(int timeZone) {
        ApiMgrV2.setTimeZone(mDevice.getDevId(), timeZone);
    }

    private void initHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case R.id.msg_update_time_zone:
                        if (mAdapter != null) {
                            hideLoadDialog();
                            mAdapter.chooseItem((DevTimeZone) msg.obj);
                            fromIntent.putExtra(Constants.BundleKey.KEY_TIME_ZONE, (DevTimeZone) msg.obj);
                            setResult(RESULT_OK, fromIntent);
                            finish();
                        }
                        break;
                }
            }
        };
    }

}
