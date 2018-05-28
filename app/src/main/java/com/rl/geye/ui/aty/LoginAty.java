package com.rl.geye.ui.aty;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.rl.geye.MyApp;
import com.rl.geye.R;
import com.rl.geye.bean.CloudCommoResponse;
import com.rl.geye.constants.Constants;
import com.rl.geye.db.bean.CloudUser;
import com.rl.geye.db.bean.CloudUserDao;
import com.rl.geye.util.CgiCallback;
import com.rl.p2plib.BridgeService;
import com.rl.p2plib.utils.JSONUtil;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginAty extends AppCompatActivity{
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextView mUsernameView;
    private EditText mPasswordView;
    private TextView mRegisterView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_login);
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.user_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mRegisterView = findViewById(R.id.user_register_view);
//        SpannableStringBuilder ssb = new SpannableStringBuilder();
//        String registText = ;
//        ssb.setSpan(new ForegroundColorSpan(Color.GREEN),0,-1,ssb.SPAN_EXCLUSIVE_EXCLUSIVE);
        mRegisterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegist();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            String user = bundle.getString("username");
            String pass = bundle.getString("password");
            if (user != null && pass != null) {
                mUsernameView.setText(user);
                mPasswordView.setText(pass);
            }
            return;
        }

        CloudUser cloudUser = null;
        try {
            CloudUserDao cloudUserDao = MyApp.getDaoSession().getCloudUserDao();
            List<CloudUser> userList = cloudUserDao.loadAll();
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getActived()) {
                    cloudUser = userList.get(i);
                    mUsernameView.setText(cloudUser.getUsername());
                    mPasswordView.setText(cloudUser.getPassword());
                    Log.e("LoginAty","auto login with username: " +  cloudUser.getUsername());
                    attemptLogin();
                    return;
                }
            }
        }catch (net.sqlcipher.database.SQLiteException sqlerr){
            Log.e("sqlite error",sqlerr.toString());
            cloudUser = null;
        }
    }

    private void attemptRegist(){
        startActivity(new Intent().setClass(LoginAty.this, RegistAty.class));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // showProgress(true);

            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("username", mUsername);
            requestParams.put("password", mPassword);
            String mLoginCgi = Constants.CloudCgi.CgiLogin;

            OkGo.post(mLoginCgi)
                    .tag(this)
                    .params(requestParams)
                    .execute(new CgiCallback(this) {
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            MyApp.showToast(R.string.error_lost_connection);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            CloudCommoResponse loginResponse = JSONUtil.fromJson(s, CloudCommoResponse.class);
                            Log.e("lwip login response",s );
                            if(loginResponse == null){
                                return;
                            }
                            if(loginResponse.getStatus() == 1){
                                try {
                                    CloudUserDao cloudUserDao = MyApp.getDaoSession().getCloudUserDao();
                                    List<CloudUser> cloudUsers = cloudUserDao.loadAll();
                                    for(CloudUser user:cloudUsers){
                                        if(TextUtils.equals(user.getUsername(),mUsername)){
                                            user.setActived(true);
                                            MyApp.setCloudUser(user);
                                            MyApp.getDaoSession().getCloudUserDao().update(user);
                                            break;
                                        }
                                    }

                                    if(MyApp.getCloudUser() == null) {
                                        CloudUser cu = new CloudUser();
                                        cu.setActived(true);
                                        cu.setUsername(mUsername);
                                        cu.setPassword(mPassword);
                                        cloudUserDao.insert(cu);
                                        MyApp.setCloudUser(cu);
                                    }
                                }catch(SQLiteConstraintException e){
                                    Log.e("sqlite",e.getLocalizedMessage());
                                }
                                MyApp.getCloudUtil().setUsername(mUsername);
                                MyApp.getCloudUtil().setPassword(mPassword);
                                MyApp.getCloudUtil().setAccessToken(loginResponse.getAccessToken());
                                Intent mainIntent = new Intent(LoginAty.this,MainAty.class);
                                startActivity(mainIntent);
                                finish();
                                return;
                            }

                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();

                            Log.e("lwip login request","[" + mUsername + "][" + mPassword + "]");
                            Log.e("lwip login response","status:[" + loginResponse.getStatus() + "] msg:[" + loginResponse.getMsg() + "]");
                        }
                    });

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

