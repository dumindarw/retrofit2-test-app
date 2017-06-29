package com.loits.insurance.cm.network;

import com.loits.insurance.cm.models.ActionResponse;
import com.loits.insurance.cm.models.Assessor;
import com.loits.insurance.cm.models.Claim;
import com.loits.insurance.cm.models.Image;
import com.loits.insurance.cm.models.JobAction;
import com.loits.insurance.cm.models.JobList;
import com.loits.insurance.cm.models.Login;
import com.loits.insurance.cm.models.TakafulClaim;
import com.loits.insurance.cm.models.TakafulImage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceApi {

    public static final String IDS_LOGIN_PATH = "oauth2/token";
    public static final String DOWNLOAD_PENDING_JOBS_PATH = "insuAssessorJobPending1_0Rest";
    public static final String DOWNLOAD_COMPLETED_JOBS_PATH = "insuAssessorJobCompleted1_0Rest";
    public static final String ACCEPT_PENDING_JOB_PATH = "insuAssessorJobAccept1_0Rest";
    public static final String REJECT_PENDING_JOB_PATH = "insuAssessorJobReject1_0Rest";
    public static final String REVERT_JOB_PATH = "insuAssessorJobRevert1_0Rest";
    public static final String COMPLETE_JOB_PATH = "insuAssessorJobComplete1_0Rest";

    public static final String UPLOAD_CLAIM_VALUES_PATH = "MInsuAssessorValuesService1_0Rest";
    public static final String UPLOAD_IMAGES_PATH = "MInsuImageValuesService1_0Rest";

    public static final String CLAIM_URL = "insuservice/claim/";
    public static final String IMAGE_URL = "insuservice/image/";

    @FormUrlEncoded
    @POST(IDS_LOGIN_PATH)
    public Call<Login> login(@Field("grant_type") String grant_type,
                             @Field("username") String username,
                             @Field("password") String password,
                             @Field("scope") String scope);


    @FormUrlEncoded
    @POST(IDS_LOGIN_PATH)
    public Call<Login> getTokenUsingRefreshToken(@Field("grant_type") String grant_type,
                                                 @Field("scope") String scope,
                                                 @Field("refresh_token") String refresh_token);

    @POST(DOWNLOAD_PENDING_JOBS_PATH)
    public Call<JobList> downloadPendingJobs(@Body Assessor assessor);

    @POST(DOWNLOAD_COMPLETED_JOBS_PATH)
    public Call<JobList> downloadCompletedJobs(@Body Assessor assessor);

    @POST(ACCEPT_PENDING_JOB_PATH)
    public Call<ActionResponse> acceptPendingJob(@Body JobAction assessor);

    @POST(REJECT_PENDING_JOB_PATH)
    public Call<ActionResponse> rejectPendingJob(@Body JobAction assessor);

    @POST(REVERT_JOB_PATH)
    public Call<ActionResponse> revertJob(@Body JobAction assessor);

    @POST(COMPLETE_JOB_PATH)
    public Call<ActionResponse> completeJob(@Body JobAction assessor);

    @POST(UPLOAD_CLAIM_VALUES_PATH)
    public Call<ActionResponse> uploadClaimValues(@Body Claim assessor);

    @POST(UPLOAD_IMAGES_PATH)
    public Call<ActionResponse> uploadImages(@Body Image image);

    @POST(CLAIM_URL+"{intimationNo}")
    public Call<String> uploadAppEngineClaimValues(@Path("intimationNo") int intimationNo, @Body TakafulClaim assessor);

    @POST(IMAGE_URL+"{intimationNo}")
    public Call<String> uploadAppEngineImages(@Path("intimationNo") int intimationNo, @Body TakafulImage image);
}
