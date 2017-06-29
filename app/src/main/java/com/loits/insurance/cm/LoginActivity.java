package com.loits.insurance.cm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.loits.insurance.cm.interfaces.LoginCallback;
import com.loits.insurance.cm.models.Login;
import com.loits.insurance.cm.network.Service;

import static com.loits.insurance.cm.util.Constants.ACCOUNT_NAME;
import static com.loits.insurance.cm.util.Constants.ACCOUNT_TYPE;
import static com.loits.insurance.cm.util.Constants.AUTHORITY;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_ID;
import static com.loits.insurance.cm.util.Constants.IDS_CLIENT_SECRET;
import static com.loits.insurance.cm.util.Constants.IDS_USER_STORE_NAME;
import static com.loits.insurance.cm.util.Constants.KEY_EXPIRE_TIME;
import static com.loits.insurance.cm.util.Constants.KEY_REFRESH_TOKEN;
import static com.loits.insurance.cm.util.Constants.KEY_USERNAME;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private AccountManager mAccountManager;
    Account mAccount;
    ContentResolver mResolver;

    FrameLayout progressBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        mAccount = CreateSyncAccount(this);
        mResolver = getContentResolver();

        mResolver.setSyncAutomatically(mAccount, AUTHORITY, true);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonLogin)
            oAuthLogin(view);
    }

    private void oAuthLogin(final View view) {

        progressBarHolder.setVisibility(View.VISIBLE);

        final String userName = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        Service service = new Service(this, IDS_CLIENT_ID, IDS_CLIENT_SECRET);

        String insuUser = IDS_USER_STORE_NAME + userName;

       service.login("password", insuUser, password, "iclaim", new LoginCallback() {
            @Override
            public void onResponse(Login result) {

                if (result != null) {

                    long mSec = System.currentTimeMillis() + (Integer.parseInt(result.getExpires_in()) * 1000);

                    mAccountManager.setAuthToken(mAccount, ACCOUNT_TYPE, result.getAccess_token());
                    mAccountManager.setUserData(mAccount, KEY_REFRESH_TOKEN, result.getRefresh_token());
                    mAccountManager.setUserData(mAccount, KEY_EXPIRE_TIME, String.valueOf(mSec));
                    mAccountManager.setUserData(mAccount, KEY_USERNAME, userName);

                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);

                    progressBarHolder.setVisibility(View.GONE);
                } else {
                    progressBarHolder.setVisibility(View.GONE);

                    Snackbar snackbar = Snackbar
                            .make(view, "Login Failed: ", Snackbar.LENGTH_SHORT);

                    snackbar.show();

                }
            }

            @Override
            public void onFailure(Throwable result) {
                progressBarHolder.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(view, "Login Failed: " + result.toString(), Snackbar.LENGTH_SHORT);

                snackbar.show();
            }
        });

        /*if (result != null) {

            long mSec = System.currentTimeMillis() + (Integer.parseInt(result.getExpires_in()) * 1000);

            mAccountManager.setAuthToken(mAccount, ACCOUNT_TYPE, result.getAccess_token());
            mAccountManager.setUserData(mAccount, KEY_REFRESH_TOKEN, result.getRefresh_token());
            mAccountManager.setUserData(mAccount, KEY_EXPIRE_TIME, String.valueOf(mSec));
            mAccountManager.setUserData(mAccount, KEY_USERNAME, userName);

            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);

            progressBarHolder.setVisibility(View.GONE);
        } else {
            progressBarHolder.setVisibility(View.GONE);

            Snackbar snackbar = Snackbar
                    .make(view, "Login Failed: ", Snackbar.LENGTH_SHORT);

            snackbar.show();

        }*/
    }

    public Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT_NAME, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        mAccountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (mAccountManager.addAccountExplicitly(newAccount, null, null)) {

        } else {

        }

        return newAccount;
    }
}
