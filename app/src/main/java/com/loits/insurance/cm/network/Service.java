package com.loits.insurance.cm.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loits.insurance.cm.LoginActivity;
import com.loits.insurance.cm.R;
import com.loits.insurance.cm.interfaces.ActionResponseCallback;
import com.loits.insurance.cm.interfaces.JobCallback;
import com.loits.insurance.cm.interfaces.LoginCallback;
import com.loits.insurance.cm.models.ActionResponse;
import com.loits.insurance.cm.models.Assessor;
import com.loits.insurance.cm.models.Claim;
import com.loits.insurance.cm.models.Image;
import com.loits.insurance.cm.models.JobAction;
import com.loits.insurance.cm.models.JobList;
import com.loits.insurance.cm.models.Login;
import com.loits.insurance.cm.models.TakafulClaim;
import com.loits.insurance.cm.models.TakafulImage;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.loits.insurance.cm.util.Constants.ACCOUNT_TYPE;
import static com.loits.insurance.cm.util.Constants.APP_ENGINE_BASE_URL;
import static com.loits.insurance.cm.util.Constants.IDS_TOKEN_PATH;
import static com.loits.insurance.cm.util.Constants.KEEP_ALIVE_DURATION_MS;
import static com.loits.insurance.cm.util.Constants.KEY_EXPIRE_TIME;
import static com.loits.insurance.cm.util.Constants.KEY_REFRESH_TOKEN;
import static com.loits.insurance.cm.util.Constants.KEY_SCOPE;
import static com.loits.insurance.cm.util.Constants.KEY_USERNAME;
import static com.loits.insurance.cm.util.Constants.MAX_IDLE_CONNECTIONS;
import static com.loits.insurance.cm.util.Constants.SERVICES_BASE_URL;
import static com.loits.insurance.cm.util.Constants.TIME_OUT;

public class Service {

    private Context mContext;
    private ServiceApi appEngineServiceApi;
    private ServiceApi serviceApi;
    private ServiceApi loginServiceApi;
    private String mToken;
    private String mRefreshToken;

    private AccountManager mAccountManager;
    private Account[] mAccounts;

    public Service(Context ctx, String oauthKey, String oauthPass) {

        mAccountManager = AccountManager.get(ctx);
        mAccounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);

        mContext = ctx;

        if(mAccounts.length > 0)
            mToken = mAccountManager.peekAuthToken(mAccounts[0], ACCOUNT_TYPE);
        else
            mToken = "xxx";

        serviceApi = createRegularServiceApi(mContext, mToken);
        loginServiceApi = createLoginServiceApi(mContext, oauthKey, oauthPass);
        appEngineServiceApi = createAppEngineServiceApi();
    }



    private ServiceApi createRegularServiceApi(Context ctx, String token) {

        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkClient(ctx, token))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVICES_BASE_URL)
                .build();

        ServiceApi serviceApi = retrofit.create(ServiceApi.class);

        return serviceApi;
    }

    private ServiceApi createAppEngineServiceApi() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(getAppEngineOkClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APP_ENGINE_BASE_URL)
                .build();

        ServiceApi serviceApi = retrofit.create(ServiceApi.class);

        return serviceApi;
    }

    private ServiceApi createLoginServiceApi(Context ctx, String oauthKey, String oauthPass) {

        Retrofit retrofit = new Retrofit.Builder()
                .client(getIDSOkClient(ctx, oauthKey, oauthPass))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(IDS_TOKEN_PATH)
                .build();

        ServiceApi loginServiceApi = retrofit.create(ServiceApi.class);

        return loginServiceApi;
    }

    public void getTokenUsingRefreshToken(String grant_type, String scope, String refresh_token, final LoginCallback loginCallback) {

        Call<Login> call = loginServiceApi.getTokenUsingRefreshToken(grant_type, scope, refresh_token);

        /*try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e){
            return null;
        }*/

        call.enqueue(new Callback<Login>() {
             @Override
             public void onResponse(Call<Login> call, retrofit2.Response<Login> response) {
                 Log.i("Refresh Token Msg", response.message());
                 loginCallback.onResponse(response.body());
             }

             @Override
             public void onFailure(Call<Login> call, Throwable t) {

                 String stackTrace = Log.getStackTraceString(t);

                 Log.e("Refresh Token Err",stackTrace);
                 loginCallback.onFailure(t);
                 /*if (((RetrofitError) e).getResponse().getStatus() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                     logout();
                 }*/
             }
        });


    }

    private void logout() {
        //mAccountManager.invalidateAuthToken(ACCOUNT_TYPE, mAccountManager.peekAuthToken(mAccounts[0], ACCOUNT_TYPE));

        mAccountManager.removeAccount(mAccounts[0], new AccountManagerCallback<Boolean>() {
            @Override
            public void run(AccountManagerFuture<Boolean> future) {
                try {
                    if (future.getResult()) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, null);
    }

    public void login(String grant_type, String username, String password,  String scope, final LoginCallback loginCallback) {

        Call<Login> call = loginServiceApi.login(grant_type, username, password, scope);

        /*try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e){
            return null;
        }*/

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, retrofit2.Response<Login> response) {
                Log.i("Login Msg", response.message());
                loginCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

                String stackTrace = Log.getStackTraceString(t);

                Log.e("Login Err", stackTrace);
                loginCallback.onFailure(t);
                /*if (((RetrofitError) e).getResponse().getStatus() == HttpsURLConnection.HTTP_BAD_REQUEST) {
                     logout();
                 }*/
            }
        });

    }

    public OkHttpClient getESBOkHttpClient(final Context ctx, final String token) {

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {

                return true;
            }
        };

        OkHttpClient client = null;

        client = new OkHttpClient.Builder()
                .hostnameVerifier(hostnameVerifier)
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token)
                                .build();

                        Response response = chain.proceed(request);

                        if (response.code() == HttpsURLConnection.HTTP_ACCEPTED) {

                            String refreshToken = mAccountManager.getUserData(mAccounts[0], KEY_REFRESH_TOKEN);

                           getTokenUsingRefreshToken(KEY_REFRESH_TOKEN, KEY_SCOPE, refreshToken, new LoginCallback() {
                                @Override
                                public void onResponse(final Login result) {

                                    if (result != null && !result.getAccess_token().isEmpty()) {

                                        final long mSec = System.currentTimeMillis() + (Integer.parseInt(result.getExpires_in())) * 1000;
                                        Date currentDate = new Date(mSec);
                                        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                        String dateNow = df.format(currentDate);

                                        mAccountManager.getAuthToken(mAccounts[0], ACCOUNT_TYPE, null, null,
                                                new AccountManagerCallback<Bundle>() {
                                                    @Override
                                                    public void run(AccountManagerFuture<Bundle> fresult) {
                                                        try {
                                                            Bundle bundle = fresult.getResult();

                                                           if(bundle != null){
                                                               String token = bundle
                                                                       .getString(AccountManager.KEY_AUTHTOKEN);

                                                               mAccountManager.invalidateAuthToken(ACCOUNT_TYPE, token);

                                                               mAccountManager.setAuthToken(mAccounts[0], ACCOUNT_TYPE, result.getAccess_token());
                                                               mAccountManager.setUserData(mAccounts[0], KEY_REFRESH_TOKEN, result.getRefresh_token());
                                                               mAccountManager.setUserData(mAccounts[0], KEY_EXPIRE_TIME, String.valueOf(mSec));
                                                           }
                                                        } catch (Exception e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    }
                                                }, null);
                                    } else {
                                        logout();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable result) {
                                    //logout();
                                }
                            });

                           /* if (result != null && !result.getAccess_token().isEmpty()) {

                                long mSec = System.currentTimeMillis() + (Integer.parseInt(result.getExpires_in()) * 1000);
                                Date currentDate = new Date(mSec);
                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                String dateNow = df.format(currentDate);

                                mAccountManager.setAuthToken(mAccounts[0], ACCOUNT_TYPE, result.getAccess_token());
                                mAccountManager.setUserData(mAccounts[0], KEY_REFRESH_TOKEN, result.getRefresh_token());
                                mAccountManager.setUserData(mAccounts[0], KEY_EXPIRE_TIME, dateNow);

                            } else {
                                //logout();
                            }*/

                        }

                        String bodyString = response.body().string();

                        return response.newBuilder()
                                .body(ResponseBody.create(response.body().contentType(), bodyString))
                                .build();
                    }
                })
                .sslSocketFactory(newESBSslSocketFactory(ctx).getSocketFactory(), getESBTrustManager(ctx))
                .connectionPool(new ConnectionPool(
                        MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION_MS, TimeUnit.MILLISECONDS))
                .build();


        return client;
    }

    public OkHttpClient getAppEngineOkHttpClient() {

        OkHttpClient client = null;

        client = new OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(
                        MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION_MS, TimeUnit.MILLISECONDS))
                .build();

        return client;
    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {

        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            try {
                Bundle bundle = result.getResult();

                Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (launch != null) {

                } else {
                    String token = bundle
                            .getString(AccountManager.KEY_AUTHTOKEN);

                    //authPreferences.setToken(token);

                    //doCoolAuthenticatedStuff();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public OkHttpClient getLoginOkHttpClient(Context ctx, String oauthKey, String oauthPass) {

        String authString = oauthKey + ":" + oauthPass;
        byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.NO_WRAP);
        final String authStringEnc = new String(authEncBytes);

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {

                return true;
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .hostnameVerifier(hostnameVerifier)
                .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                                .addHeader("Authorization", "Basic " + authStringEnc)
                                .build();

                        Response response = chain.proceed(request);

                        String bodyString = response.body().string();

                        Log.d("TAG","response only"+"\n"+bodyString);

                        return response.newBuilder()
                                .body(ResponseBody.create(response.body().contentType(), bodyString))
                                .build();
                    }
                })
                .sslSocketFactory(newLoginSslSocketFactory(ctx).getSocketFactory(), getIDSTrustManager(ctx))
                .connectionPool(new ConnectionPool(
                        MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION_MS, TimeUnit.MILLISECONDS))
                .build();

        return client;
    }

    public OkHttpClient getOkClient(Context ctx, String token) {
        OkHttpClient client = new OkHttpClient();
        client = getESBOkHttpClient(ctx, token);
        return client;
    }

    public OkHttpClient getAppEngineOkClient() {
        OkHttpClient client = new OkHttpClient();
        client = getAppEngineOkHttpClient();
        return client;
    }

    public OkHttpClient getIDSOkClient(Context ctx,String oauthKey, String oauthPass) {
        OkHttpClient client = new OkHttpClient();
        client = getLoginOkHttpClient(ctx, oauthKey, oauthPass);
        return client;
    }

    private X509TrustManager getESBTrustManager(Context ctx) {
        try {

            TrustManagerFactory originalTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            originalTrustManagerFactory.init((KeyStore) null);
            KeyStore trusted = KeyStore.getInstance(KeyStore.getDefaultType());
            //dev
            //InputStream in = ctx.getResources().openRawResource(R.raw.clienttrustoreesbdevnew);
            //qa
            InputStream in = ctx.getResources().openRawResource(R.raw.lolcdc2esbqauat);
            try {
                trusted.load(in, "wso2carbon".toCharArray());
                //trusted.load(in, "Lolc1231145".toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                in.close();
            }

            originalTrustManagerFactory.init(trusted);

            TrustManager[] trustManagers = originalTrustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            return trustManager;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private X509TrustManager getIDSTrustManager(Context ctx) {
        try {

            TrustManagerFactory originalTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            originalTrustManagerFactory.init((KeyStore) null);
            KeyStore trusted = KeyStore.getInstance(KeyStore.getDefaultType());
            //dev
            //InputStream in = ctx.getResources().openRawResource(R.raw.lolcdc2idsdev);
            //qa
            InputStream in = ctx.getResources().openRawResource(R.raw.lolcdc2idsqauat);
            try {
                trusted.load(in, "wso2carbon".toCharArray());
                //trusted.load(in, "LolcIDS908".toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                in.close();
            }

            originalTrustManagerFactory.init(trusted);

            TrustManager[] trustManagers = originalTrustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            return trustManager;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private SSLContext newESBSslSocketFactory(Context ctx) {
        try {

            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, new TrustManager[]{getESBTrustManager(ctx)}/*originalTrustManagerFactory.getTrustManagers()*//*new TrustManager[]{tm}*/, /*new java.security.SecureRandom()*/null);

            return sc;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private SSLContext newLoginSslSocketFactory(Context ctx) {
        try {

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{getIDSTrustManager(ctx)}/*new TrustManager[]{tm}*/, /*new java.security.SecureRandom()*/null);

            return sc;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public void downloadPendingJobs(Assessor assessor, final JobCallback jobCallback) {

        Call<JobList> call = serviceApi.downloadPendingJobs(assessor);

        call.enqueue(new Callback<JobList>() {
            @Override
            public void onResponse(Call<JobList> call, retrofit2.Response<JobList> response) {
                Log.i("Job Download Msg", response.message());
                jobCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<JobList> call, Throwable t) {

                String stackTrace = Log.getStackTraceString(t);

                Log.e("Job Download Err", stackTrace);
                jobCallback.onFailure(t);
            }
        });
    }

    public void downloadCompletedJobs(Assessor assessor, final JobCallback jobCallback) {

        Call<JobList> call = serviceApi.downloadCompletedJobs(assessor);

        call.enqueue(new Callback<JobList>() {
            @Override
            public void onResponse(Call<JobList> call, retrofit2.Response<JobList> response) {
                Log.i("Job Download Msg", response.message());
                jobCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<JobList> call, Throwable t) {

                String stackTrace = Log.getStackTraceString(t);

                Log.e("Job Download Err", stackTrace);
                jobCallback.onFailure(t);
            }
        });
    }

    public void acceptPendingJob(JobAction jobAction, final ActionResponseCallback responseCallback) {

        Call<ActionResponse> call = serviceApi.acceptPendingJob(jobAction);

        call.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(Call<ActionResponse> call, retrofit2.Response<ActionResponse> response) {
                Log.i("Job Accept Msg", response.message());
                responseCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {

                String stackTrace = Log.getStackTraceString(t);

                Log.e("Job Accept Err", stackTrace);
                responseCallback.onFailure(t);
            }
        });
    }

    public void rejectPendingJob(JobAction jobAction, final ActionResponseCallback responseCallback) {

        Call<ActionResponse> call = serviceApi.rejectPendingJob(jobAction);

        call.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(Call<ActionResponse> call, retrofit2.Response<ActionResponse> response) {
                Log.i("Job Reject Msg", response.message());
                responseCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {

                String stackTrace = Log.getStackTraceString(t);

                Log.e("Job Reject Err", stackTrace);
                responseCallback.onFailure(t);
            }
        });
    }

    public void revertJob(JobAction jobAction, final ActionResponseCallback responseCallback) {

        Call<ActionResponse> call = serviceApi.revertJob(jobAction);

        call.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(Call<ActionResponse> call, retrofit2.Response<ActionResponse> response) {
                Log.i("Job Revert Msg", response.message());
                responseCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {

                String stackTrace = Log.getStackTraceString(t);

                Log.e("Job Revert Err", stackTrace);
                responseCallback.onFailure(t);
            }
        });
    }

    public void completeJob(JobAction jobAction, final ActionResponseCallback responseCallback) {

        Call<ActionResponse> call = serviceApi.completeJob(jobAction);
        /*int code = 0;
        try {
            code = call.execute().body().getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        call.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(Call<ActionResponse> call, retrofit2.Response<ActionResponse> response) {
                Log.i("Job Complete Msg", response.message());
                responseCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {

                String stackTrace = Log.getStackTraceString(t);

                Log.e("Job Complete Err", stackTrace);
                responseCallback.onFailure(t);
            }
        });

        //return code;
    }

    public static String getDateString(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public ActionResponse uploadClaimValues(Claim values, int sequenceNo) {


        String username = mAccountManager.getUserData(mAccounts[0], KEY_USERNAME).toUpperCase();
        values.setUser(username);
        //values.setInspDate(inspDateStr);

        if(values.getIntimationNo() > 99999) { // Takaful ?

            Call<ActionResponse> call = serviceApi.uploadClaimValues(values);

            try {
                return call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(values.getInspDate()));
            String dateString = sdfDate.format(calendar.getTime());

           /*String dateString  = values.getInspDate();
            String formattedDate = dateString.substring(0, 4) + "-" + dateString.substring(5, 7) + "-" + dateString.substring(8, 10);
            values.setInspDate(formattedDate);*/

            ActionResponse errorRes =  new ActionResponse("ERROR", 0);

            TakafulClaim takafulClaim = new TakafulClaim(values.getExcessAmt(),values.getMiles(),
                    values.getPolicyNo(),values.getRespondDate(),values.getReason(),
                    values.getAssessorRemark(),values.getLocation(),values.getPoliceReport(),
                    values.getOtherCharges(),values.getRate(),values.getOnsiteOfferAmount(),
                    sequenceNo,values.getPayableAmount(),values.getSettlementMethod(),
                    values.getBaldTirePenalty(), values.getSumInsured(),values.getCallerName(),
                    values.getRepairCompleted(), values.getImagesCount(),values.getYom(),
                    values.getStatus(),values.getContactNo(),values.getSpecialRemarks(),
                    values.getCallCenterRemark(),values.getPhotoCharges(),values.getProffesionalFee(),
                    values.getSalvageReceived(),values.getInvestigateClaim(),values.getMiles(),
                    values.getcDate(),values.getPreAccValue(),/*values.getInspDate()*/dateString,
                    values.getIntimationNo(),values.getCopies(), values.getPrice(), values.getInspType() + 1,
                    values.getUnderInsPenalty(),values.getInspectionRemarks(),values.getVehicleNo(),
                    values.getTelephone(),values.getUser(),values.getTotalCharges(),values.getAcr());
            
            Call<String> call = appEngineServiceApi.uploadAppEngineClaimValues(values.getIntimationNo(), takafulClaim);

            try {
                String res =  call.execute().body();
                if(res != null && res.equals("Hi")){
                    return new ActionResponse("SUCCESS", 1);
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public ActionResponse uploadImages(Image image, int sequenceNo) {
        if(image.getIntim_no() > 99999) { // Takaful ?

            Call<ActionResponse> call = serviceApi.uploadImages(image);

            try {
                ActionResponse res =  call.execute().body();
                return res;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{

            ActionResponse errorRes =  new ActionResponse("ERROR", 0);

            TakafulImage takafulImage = new TakafulImage(image.getIntim_no(), image.getInspection_type(),
                    sequenceNo, image.getImage_date(),image.getImg_no(), image.getMyimage(), image.getLogitude(), image.getLatitude());

            Call<String> call = appEngineServiceApi.uploadAppEngineImages(image.getIntim_no(), takafulImage);

            try {
                String res =  call.execute().body();
                if(res != null && res.equals("Image")){
                    return new ActionResponse("SUCCESS", 1);
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /*public ActionResponse uploadAppEngineClaimValues(Claim values) {

        Call<ActionResponse> call = appEngineServiceApi.uploadAppEngineClaimValues(values);

        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ActionResponse uploadAppEngineImages(Image image) {

        Call<ActionResponse> call = appEngineServiceApi.uploadAppEngineImages(image);

        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
