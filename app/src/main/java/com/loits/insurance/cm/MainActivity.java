package com.loits.insurance.cm;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loits.insurance.cm.db.DatabaseHandler;
import com.loits.insurance.cm.fragments.AboutPageFragment;
import com.loits.insurance.cm.fragments.CompletedClaimsPageFragment;
import com.loits.insurance.cm.fragments.NewClaimsPageFragment;
import com.loits.insurance.cm.fragments.PendingClaimsPageFragment;
import com.loits.insurance.cm.interfaces.LogoutCallback;
import com.loits.insurance.cm.interfaces.OnFragmentInteractionListener;
import com.loits.insurance.cm.util.Constants;

import java.io.IOException;
import java.util.Calendar;

import static com.loits.insurance.cm.util.Constants.ACCOUNT_TYPE;
import static com.loits.insurance.cm.util.Constants.AUTHORITY;
import static com.loits.insurance.cm.util.Constants.MY_PERMISSIONS_REQUEST_READ_MEDIA;
import static com.loits.insurance.cm.util.Constants.SYNC_INTERVAL;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    DatabaseHandler mDbHelper;
    Calendar maxTimeAllowed = Calendar.getInstance();
    private TabLayout mTabLayout;
    private int[] mTabsIcons = {
            R.drawable.ic_new,
            R.drawable.ic_pending,
            R.drawable.ic_completed,
            R.drawable.ic_info};
    private AccountManager mAccountManager;
    private Account[] mAccounts;
    private String mToken;

    //private int mLoadTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountManager = AccountManager.get(this);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }*/
        mAccounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                //image_column_index = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_MEDIA);
            }
        }

        grantUriPermission("com.android.providers.media.MediaProvider", MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Intent.FLAG_GRANT_READ_URI_PERMISSION);


        enableSync();

        maxTimeAllowed.add(Calendar.DATE, -Constants.DATA_VALIDITY_DAYS);

        mDbHelper = new DatabaseHandler(getApplicationContext());
        // Setup the viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOffscreenPageLimit(0);
        }

        if (mAccounts.length > 0) {
            ContentResolver.addPeriodicSync(
                    mAccounts[0],
                    AUTHORITY,
                    Bundle.EMPTY,
                    SYNC_INTERVAL);
        }

        /*if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mLoadTab = 0;
            } else {
                mLoadTab = extras.getInt("TAB_NO");

            }
        } else {
            mLoadTab = 0;
        }*/

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }

        //deleteOldRecords();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                    grantUriPermission("com.android.providers.media.MediaProvider", MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final boolean[] status = new boolean[1];
        switch (item.getItemId()) {
            case R.id.action_logout: {
                logout(new LogoutCallback() {
                    @Override
                    public void onResponse(Boolean result) {
                        status[0] = result;

                        ActivityCompat.finishAffinity(MainActivity.this);
                    }

                    @Override
                    public void onFailure(Boolean result) {
                        status[0] = result;
                        finish();
                    }
                });
                return status[0];
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*private void deleteOldRecords() {

        // / Delete All Expired Records
        new Runnable() {
            public void run() {

                List<Claim> toDeleteList = mDbHelper.fetchReadyToDeleteClaims(maxTimeAllowed
                        .getTimeInMillis());

                for (int i = 0; i < toDeleteList.size(); i++) {
                    mDbHelper.deleteImages(toDeleteList.get(i).getIntimationNo(), toDeleteList.get(i).getInspType());
                    mDbHelper.deleteClaim(toDeleteList.get(i).getId());
                }
            }
        }.run();
    }*/

    private void logout(final LogoutCallback callback) {
        mAccountManager.removeAccount(mAccounts[0], new AccountManagerCallback<Boolean>() {

            boolean wasAccountDeleted;

            @Override
            public void run(AccountManagerFuture<Boolean> accountManagerFuture) {
                try {
                    callback.onResponse(accountManagerFuture.getResult());
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                    wasAccountDeleted = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onFailure(false);
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                    wasAccountDeleted = false;
                }
            }
        }, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        ActivityCompat.finishAffinity(MainActivity.this);
    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void enableSync() {

        boolean isYourAccountSyncEnabled = ContentResolver.getSyncAutomatically(mAccounts[0], AUTHORITY);
        boolean isMasterSyncEnabled = ContentResolver.getMasterSyncAutomatically();

        if (!isMasterSyncEnabled) {
            // sync is not enable
            new AlertDialog.Builder(this)
                    .setTitle("Sync Disabled")
                    .setMessage("Please enable device sync option")
                    .setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            ContentResolver.setMasterSyncAutomatically(true);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 4;

        private final String[] mTabsTitle = {"New", "Pending", "Completed", /*"Request"*/"About"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {

            return addTab(position, mTabsTitle[position], mTabsIcons[position]);

            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            /*View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(mTabsTitle[position]);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;*/
        }

        private View addTab(int tabId, String tabName, int drawableId) {
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.title);
            ImageView iv = (ImageView) v.findViewById(R.id.icon);
            tv.setText(tabName);
            Drawable d = AppCompatDrawableManager.get().getDrawable(MainActivity.this, drawableId);
            iv.setImageDrawable(d);
            //iv.setImageResource(mTabsIcons[tabId]);
            mTabLayout.getTabAt(tabId).setCustomView(v);
            mTabLayout.setTabTextColors(Color.BLACK, Color.WHITE);

            return v;
        }

        @Override
        public Fragment getItem(int pos) {

            //pos = mLoadTab;//newly added

            switch (pos) {

                case 0:
                    return NewClaimsPageFragment.newInstance(1);
                case 1:
                    return PendingClaimsPageFragment.newInstance(2);
                case 2:
                    return CompletedClaimsPageFragment.newInstance(3);
                case 3:
                    /*return RequestClaimsPageFragment.newInstance(4);*/
                    return AboutPageFragment.newInstance(4);

            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
    }
}


