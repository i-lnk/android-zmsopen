package com.rl.geye.ui.aty;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.rl.commons.BaseApp;
import com.rl.geye.MyApp;
import com.rl.geye.R;
import com.rl.geye.bean.CloudCommoResponse;
import com.rl.geye.bean.CloudVerificationResponse;
import com.rl.geye.constants.Constants;
import com.rl.p2plib.utils.JSONUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class RegistAty extends AppCompatActivity {

    @BindView(R.id.username)
    EditText    mUsernameView;  // 用户名控件
    @BindView(R.id.password)
    EditText    mPasswordView;  // 密码控件
    @BindView(R.id.confirm)
    EditText    mConfirmPasswordView;   //密码确认控件
    @BindView(R.id.email_or_phonenum)
    EditText    mPhonenumOrEmailView;   // 邮箱和电话号码控件
    @BindView(R.id.verification_code)
    EditText    mVerificationView;  // 验证码控件
    @BindView(R.id.verification_button)
    Button      mVerificationButton;   // 验证码获取按钮
    @BindView(R.id.regist_button)
    Button      mRegistButton;  // 注册按钮

    RegistTask registTask = null;
    VerificationCodeTask verificationCodeTask = null;

    public static ExecutorService exec = Executors.newFixedThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_regist);
        ButterKnife.bind(this); // VIEW 绑定

        mRegistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegist();
            }
        });

        mVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptVerificationCode();
            }
        });

        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("focus change","view:" + v.getId());
                if(v.getId() == mPasswordView.getId())
                    attemptCheckPassword();
            }
        });

        mUsernameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("focus change","view:" + v.getId());
                if(v.getId() == mUsernameView.getId())
                    attemptCheckUsername();
            }
        });

        mPhonenumOrEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("focus change","view:" + v.getId());
                if(v.getId() == mPhonenumOrEmailView.getId())
                    attemptCheckPhonenum();
            }
        });
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    private void attemptRegist(){
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);
        mPhonenumOrEmailView.setError(null);
        mVerificationView.setError(null);

        String mUsername = mUsernameView.getText().toString();
        String mPassword = mPasswordView.getText().toString();
        String mPhonenumOrEmail = mPhonenumOrEmailView.getText().toString();
        String mConfirmPassword = mConfirmPasswordView.getText().toString();
        String mVerificationCode = mVerificationView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(mUsername)){
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if(TextUtils.isEmpty(mPassword) || isPasswordValid(mPassword) == false){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if(TextUtils.isEmpty(mConfirmPassword)){
            mConfirmPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        if(!mConfirmPassword.equals(mPassword)){
            mConfirmPasswordView.setError(getString(R.string.tips_err_pwd_new_2));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        if(TextUtils.isEmpty(mPhonenumOrEmail)){
            mPhonenumOrEmailView.setError(getString(R.string.error_invalid_phonenum_or_email));
            focusView = mPhonenumOrEmailView;
            cancel = true;
        }

        if(TextUtils.isEmpty(mVerificationCode)){
            mVerificationView.setError(getString(R.string.error_invalid_verification_code));
            focusView = mVerificationView;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        }else{
            mRegistButton.setEnabled(false);
            registTask = new RegistTask(mUsername,mPassword,mPhonenumOrEmail,mVerificationCode);
            registTask.executeOnExecutor(exec);
        }
    }

    private void attemptCheckPassword(){
        String mPassword = mPasswordView.getText().toString();
        if(TextUtils.isEmpty(mPassword) || isPasswordValid(mPassword) == false){
            mPasswordView.setError(getString(R.string.error_invalid_password));
        }
    }

    private void attemptCheckUsername(){
        String mUsername = mUsernameView.getText().toString();
        if(TextUtils.isEmpty(mUsername)){
            return;
        }
        RegistCheckTask registCheckTask = new RegistCheckTask(mUsername,null);
        registCheckTask.executeOnExecutor(exec);
    }

    private void attemptCheckPhonenum(){
        String mPhonenum = mPhonenumOrEmailView.getText().toString();
        if(TextUtils.isEmpty(mPhonenum)){
            return;
        }
        RegistCheckTask registCheckTask = new RegistCheckTask(null,mPhonenum);
        registCheckTask.executeOnExecutor(exec);
    }

    private void attemptVerificationCode(){
        String address = mPhonenumOrEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        String mUsername = mUsernameView.getText().toString();
        if(TextUtils.isEmpty(mUsername)){
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if(TextUtils.isEmpty(address)){
            mPhonenumOrEmailView.setError(getString(R.string.error_invalid_phonenum_or_email));
            focusView = mPhonenumOrEmailView;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        }else {
            mVerificationButton.setEnabled(false);
            verificationCodeTask = new VerificationCodeTask(60, mUsername, address, mVerificationButton, mVerificationView);
            verificationCodeTask.executeOnExecutor(exec);
        }
    }

    public class RegistCheckTask extends AsyncTask<Void, Void, Boolean> {
        private String mUsername = null;
        private String mPhonenumOrEmail = null;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        public RegistCheckTask(String username,String phonenumOrEmail) {
            mUsername = username;
            mPhonenumOrEmail = phonenumOrEmail;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Map<String, String> requestParams = new HashMap<>();
            if(mUsername != null) requestParams.put("username", mUsername);
            if(mPhonenumOrEmail != null) requestParams.put("phone", mPhonenumOrEmail);
            String mRegistCheckCgi = Constants.CloudCgi.CgiCheckUserOrPhone;

            Log.e("lwip regist check",mRegistCheckCgi);

            OkGo.post(mRegistCheckCgi)
            .tag(this)
            .params(requestParams)
            .execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    CloudCommoResponse rsp = JSONUtil.fromJson(s, CloudCommoResponse.class);
                    Log.e("lwip regist check",s);
                    if(rsp == null){
                        Log.e("lwip regist check","rsp is null");
                        return;
                    }

                    switch(rsp.getMsg()){
                        case 10:
                            mUsernameView.setError(null);
                            mUsernameView.setError(getString(R.string.error_regist_user));
                            break;
                        case 19:
                            mPhonenumOrEmailView.setError(null);
                            mPhonenumOrEmailView.setError(getString(R.string.error_regist_phone));
                            break;
                        case 20:
                            mUsernameView.setError(null);
                            mUsernameView.setError(getString(R.string.error_regist_user));
                            mPhonenumOrEmailView.setError(null);
                            mPhonenumOrEmailView.setError(getString(R.string.error_regist_phone));
                            break;
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    Log.e("lwip regist",e.toString());
                    BaseApp.showToastLong(R.string.error_lost_connection);
                    super.onError(call, response, e);
                }
            });
            return null;
        }
    }

    public class RegistTask extends AsyncTask<Void, Void, Boolean>{
        private String mUsername;
        private String mPassword;
        private String mPhonenumOrEmail;
        private String mVerificationCode;

        public RegistTask(String username,String password,String phonenumOrEmail,String verificationCode) {
            mUsername = username;
            mPassword = password;
            mPhonenumOrEmail = phonenumOrEmail;
            mVerificationCode = verificationCode;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mRegistButton.setEnabled(true);
            registTask = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("username", mUsername);
            requestParams.put("password", mPassword);
            requestParams.put("code", mVerificationCode);
            requestParams.put("phone", mPhonenumOrEmail);
            requestParams.put("thirdid", "goke123456");
            requestParams.put("login_type", "0");
            String mRegistCgi = Constants.CloudCgi.CgiReg;

            Log.e("lwip verify code",mRegistCgi);

            OkGo.post(mRegistCgi)
            .tag(this)
            .params(requestParams)
            .execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    CloudCommoResponse rsp = JSONUtil.fromJson(s, CloudCommoResponse.class);
                    Log.e("lwip regist",s);
                    if(rsp == null){
                        Log.e("lwip regist","rsp is null");
                        BaseApp.showToastLong(R.string.err_data);
                        return;
                    }

                    switch(rsp.getStatus()){
                        case 0:
                            switch (rsp.getMsg()){
                                case 0:
                                    mUsernameView.setError(null);
                                    mUsernameView.setError(getString(R.string.error_regist_user));
                                    BaseApp.showToastLong(R.string.error_regist_user);
                                    break;
                                case 1:
                                    mPhonenumOrEmailView.setError(null);
                                    mPhonenumOrEmailView.setError(getString(R.string.error_invalid_phonenum_or_email));
                                    BaseApp.showToastLong(R.string.error_invalid_phonenum_or_email);
                                    break;
                                case 2:
                                case 3:
                                case 4:
                                    mVerificationView.setError(null);
                                    mVerificationView.setError(getString(R.string.error_invalid_verification_code));
                                    BaseApp.showToastLong(R.string.error_invalid_verification_code);
                                    break;
                                default:
                                    BaseApp.showToastLong(R.string.err_data);
                                    break;
                            }
                            break;
                        case 1:
                            Intent mainIntent = new Intent(RegistAty.this, LoginAty.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Bundle bundle = new Bundle();
                            bundle.putString("username",mUsername);
                            bundle.putString("password",mPassword);
                            mainIntent.putExtras(bundle);
                            startActivity(mainIntent);
                            break;
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    Log.e("lwip regist",e.toString());
                    BaseApp.showToastLong(R.string.error_lost_connection);
                    super.onError(call, response, e);
                }
            });
            return false;
        }
    }

    public class VerificationCodeTask extends AsyncTask<Void, Integer, Boolean>{
        private String mEmailOrPhonenumber;
        private String mUsername;
        private Button clickButton;
        private EditText editText;
        private int mInterval;
        private boolean isMailAddress;
        private boolean mStop = false;
        private int mVerificationCode = 0;

        public VerificationCodeTask(int interval,String username,String emailOrPhonenumber,Button verificationCodeButton,EditText verificationCodeView){
            mEmailOrPhonenumber = emailOrPhonenumber;
            mUsername = username;
            isMailAddress = mEmailOrPhonenumber.contains("@");
            clickButton = verificationCodeButton;
            editText = verificationCodeView;
            mInterval = interval;
            mStop = false;
        }

        public void Stop(){
            mStop = true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            clickButton.setText(String.valueOf(values[0]));
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            clickButton.setText(R.string.action_verification_code);
            clickButton.setEnabled(true);
            verificationCodeTask = null;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("username",mUsername);
            requestParams.put("phone", mEmailOrPhonenumber);
            String mVerificationCodeCgi = Constants.CloudCgi.CgiVerificationCode;

            OkGo.post(mVerificationCodeCgi)
            .tag(this)
            .params(requestParams)
            .execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    CloudVerificationResponse rsp = JSONUtil.fromJson(s, CloudVerificationResponse.class);
                    Log.e("lwip verify code",s);
                    if(rsp == null){
                        mVerificationCode = 0;
                        Log.e("lwip verify code","rsp is null");
                        BaseApp.showToastLong(R.string.err_data);
                        return;
                    }
                    mVerificationCode = rsp.getStatus();
                    switch (mVerificationCode){
                        case 0:
                            MyApp.showToast(getString(R.string.error_request_verification_code) + ":" + rsp.getMsg());
                            break;
                        case 1:
                            break;
                        default:
                            editText.setText(String.valueOf(mVerificationCode));
                            break;
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    Log.e("lwip verify code",e.toString());
                    BaseApp.showToastLong(R.string.error_lost_connection);
                    super.onError(call, response, e);
                }
            });

            Log.e("verification code","[" + mVerificationCode + "]");

            int count = mInterval;
            while (count-- != 0 && !mStop){
                try {
                    Thread.sleep(1000);
                }catch(InterruptedException interr){
                    break;
                }
                publishProgress(count);
            }
            return true;
        }
    }
}
