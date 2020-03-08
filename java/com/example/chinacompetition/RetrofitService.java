package com.example.chinacompetition;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("client/postjob.php")
    Call<ResponseBody> postJob(
            @Field("id") String id, @Field("subject") String subject, @Field("category") String category, @Field("term") String term, @Field("cost") String cost, @Field("content") String content

    );

    @FormUrlEncoded
    @POST("contract/setBiddingState.php")
    Call<ResponseBody> postBiddingState(
            @Field("id") String id, @Field("num") String num, @Field("state") String state

    );

    @FormUrlEncoded
    @POST("contract/selectMemberList.php")
    Call<ResponseBody> postMemberList(
            @Field("id") String id
    );

    @GET("client/loadjobs.php")
    Call<ResponseBody> loadJobs(
            @Query("category") String category
    );

    @GET("client/loadjobs.php")
    Call<ResponseBody> myloadJobs(
            @Query("category") String category,@Query("userId") String userId
    );

    @FormUrlEncoded
    @POST("client/editjob.php")
    Call<ResponseBody> editJob(
            @Field("num") String id, @Field("subject") String subject, @Field("category") String category, @Field("term") String term, @Field("cost") String cost, @Field("content") String content
    );

    // 사진을 보내기 위해 POST 방식에서 FormUrlEncoded 가 아닌 Multipart 방식을 사용. post가 여러가지 파트로 이루어져있다는 뜻.
    @Multipart
    @POST("user/updateprofile.php")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part files, @Part("listTitle") String listTitle, @Part("listName") String listName, @Part("listSkill") String listSkill
            ,@Part("listIntroduce") String listIntroduce,@Part("listLocation") String listLocation, @Part("id") String id);

    // upload file
    @Multipart
    @POST("/contract/uploadContract.php")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file, @Part("employer") String employer, @Part("employee") String employee, @Part("postNum") String postNum);



    @FormUrlEncoded
    @POST("contract/uploadBidding.php")
    Call<ResponseBody> uploadBidding(@Field("postNum") String postNum, @Field("speaker") String speaker, @Field("listener") String listener, @Field("price") String price);

    @FormUrlEncoded
    @POST("contract/selectBidderList.php")
    Call<ResponseBody> selectBidderList(@Field("postNum") String postNum);

    //블록체인 지갑생성
    @FormUrlEncoded
    @POST("/channels/mychannel/chaincodes/mycc")
    Call<ResponseBody> makeIdAndCash(
            @FieldMap StringBuffer stringBuffer
    );


    // 파일 다운로드를 위한 레트로핏 메소드. 파일 URL을 입력값으로 받는다.
    @Streaming
    @GET
    Call<ResponseBody> downloadFileFromUrl(@Url String fileUrl);

    @FormUrlEncoded
    @POST("contract/selectContract.php")
    Call<ResponseBody> selectContract(
            @Field("employer") String employer, @Field("employee") String employee
    );

    @FormUrlEncoded
    @POST("contract/updateContractAgree.php")
    Call<ResponseBody> updateContractAgree(
            @Field("num") String num, @Field("id") String id, @Field("agree") String agree
    );



    //블록체인 로그인

    @Headers("content-type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("users")
    Call<ResponseBody> loginRequest(
            @FieldMap HashMap<String, String> hashMap
    );

    //블록체인 문서저장
    @FormUrlEncoded
    @POST("channels/mychannel/chaincodes/mycc")
    Call<ResponseBody> exeChaincode(
            @Field("fcn") String fcn , @Field("peers") String peers, @Field("args") String args
    );




}
