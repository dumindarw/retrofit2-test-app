package com.loits.insurance.cm;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;

import static com.loits.insurance.cm.util.Constants.ACCOUNT_TYPE;
import static com.loits.insurance.cm.util.Constants.KEY_EXPIRE_TIME;
import static com.loits.insurance.cm.util.Constants.KEY_REFRESH_TOKEN;
import static com.loits.insurance.cm.util.Constants.SPLASH_TIME_OUT;

public class SplashActivity extends AppCompatActivity {

    private static String TAG = "=== SplashActivity ===";
    private AccountManager mAccountManager;
    private String mAuthTokenType;
    private Account[] mAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spash);

        mAccountManager = AccountManager.get(getBaseContext());
        mAccounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mAccounts.length > 0) {

                    String loginExpiryTime = mAccountManager.getUserData(mAccounts[0], KEY_EXPIRE_TIME);

                    if (loginExpiryTime != null) {

                        long expiryTime = Long.parseLong(mAccountManager.getUserData(mAccounts[0], KEY_EXPIRE_TIME));
                        String refresh = mAccountManager.getUserData(mAccounts[0], KEY_REFRESH_TOKEN);
                        long currentMillis = Calendar.getInstance().getTimeInMillis();

                        if (expiryTime < currentMillis) {

                            Intent dashboardIntent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(dashboardIntent);
                        }else{
                            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                        }

                    } else {
                        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                } else {
                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
