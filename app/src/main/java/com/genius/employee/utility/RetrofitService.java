package com.genius.employee.utility;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by Robert
 */

 public  interface RetrofitService {
    /*@Multipart
    @POST("/upload_multi_files/MultiUpload.php")postOutTimeForNone
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);*/
    @Multipart
    @POST("AddClientVisitMaster")
    Call<UploadObject> uploadRegister(@Part("ID") String ID, @Part("ClientName") String ClientName, @Part("ClientContactPerson") String ClientContactPerson, @Part("EmployeeId") String EmployeeId, @Part("VisitPurpose") String VisitPurpose, @Part("Remark") String Remark, @Part("PunchLat") String PunchLat, @Part("PunchLon") String PunchLon);

    @Multipart
    @POST("AddVisitLocationMaster")
    Call<UploadObject> inTime(@Part("ClientVisitMasterID") String ClientVisitMasterID, @Part("LocationLat") String LocationLat, @Part("LocationLon") String LocationLon, @Part("LocationAddress") String LocationAddress, @Part("EmployeeId") String EmployeeId);


   @Multipart
   @POST("AddVisitOutMarkMaster")
   Call<UploadObject> postOutTimeForTwo(@Part("VisitLocationID") String VisitLocationID, @Part MultipartBody.Part file, @Part MultipartBody.Part file1, @Part("ClientVisitMasterID") String ClientVisitMasterID, @Part("OutMarkAddress") String address, @Part("OutMarkLat") String culat, @Part("OutMarkLon") String cutlong, @Part("ContactName") String contactname, @Part("ContactNumber") String contactnumber, @Part("ContactEmail") String contactemail, @Part("ContactRemark") String remark, @Part("CreatedBy") String createdby, @Part("fileFlag") String fileFlag);
    @Multipart
    @POST("AddVisitOutMarkMaster")
    Call<UploadObject> postOutTimeForOne(@Part("VisitLocationID") String VisitLocationID, @Part MultipartBody.Part file, @Part("ClientVisitMasterID") String ClientVisitMasterID, @Part("OutMarkAddress") String address, @Part("OutMarkLat") String culat, @Part("OutMarkLon") String cutlong, @Part("ContactName") String contactname, @Part("ContactNumber") String contactnumber, @Part("ContactEmail") String contactemail, @Part("ContactRemark") String remark, @Part("CreatedBy") String createdby, @Part("fileFlag") String fileFlag);

    @Multipart
    @POST("AddVisitOutMarkMaster")
    Call<UploadObject> postOutTimeForNone(@Part("VisitLocationID") String VisitLocationID, @Part("ClientVisitMasterID") String ClientVisitMasterID, @Part("OutMarkAddress") String address, @Part("OutMarkLat") String culat, @Part("OutMarkLon") String cutlong, @Part("ContactName") String contactname, @Part("ContactNumber") String contactnumber, @Part("ContactEmail") String contactemail, @Part("ContactRemark") String remark, @Part("CreatedBy") String createdby, @Part("fileFlag") String fileFlag);


    @Multipart
    @POST("post_ClientSurveyQuestionmaster")
    Call<UploadObject> postwithimageanswer(@Part("Answer") String Answer, @Part("UserId") String UserId, @Part("ClientID") String ClientID, @Part("ClientofficeID") String ClientofficeID, @Part("ClientOfficeName") String ClientOfficeName, @Part("DomainNo") String DomainNo, @Part("CPName") String CPName, @Part("CPPhone") String CPPhone, @Part("CPEmailId") String CPEmailId, @Part("CPDesignation") String CPDesignation, @Part("Remarks") String Remarks, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("Address") String Address, @Part MultipartBody.Part file, @Part("ClientName") String ClientName, @Part("UserName") String UserName, @Part("SecurityCode") String SecurityCode);
    @Multipart
    @POST("post_ClientSurveyQuestionAnswerWithoutimage")
    Call<UploadObject> postanswer(@Part("Answer") String Answer, @Part("UserId") String UserId, @Part("ClientID") String ClientID, @Part("ClientofficeID") String ClientofficeID, @Part("ClientOfficeName") String ClientOfficeName, @Part("DomainNo") String DomainNo, @Part("CPName") String CPName, @Part("CPPhone") String CPPhone, @Part("CPEmailId") String CPEmailId, @Part("CPDesignation") String CPDesignation, @Part("Remarks") String Remarks, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("Address") String Address, @Part("ClientName") String ClientName, @Part("UserName") String UserName, @Part("SecurityCode") String SecurityCode);


    @Multipart
    @POST("AddDailyLog")
    Call<UploadObject> dailyactivityTATAGY(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID, @Part("ApprovalStatus") String ApprovalStatus, @Part("Remarks") String Remarks, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("Address") String Address, @Part("Year") String Year, @Part("Month") String Month, @Part("FName") String FName);

    @Multipart
    @POST("AddDailyLogv2")
    Call<UploadObject> dailyactivityTATAGYV2(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID, @Part("ApprovalStatus") String ApprovalStatus, @Part("Remarks") String Remarks, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("Address") String Address, @Part("Year") String Year, @Part("Month") String Month, @Part("FName") String FName,@Part("SecurityCode") String SecurityCode);

    @Multipart
    @POST("AddDailyLog")
    Call<UploadObject> dailyactivityWithOutImage(@Part MultipartBody.Part file, @Part("AEMEmployeeID") String AEMEmployeeID, @Part("ApprovalStatus") String ApprovalStatus, @Part("Remarks") String Remarks, @Part("Longitude") String Longitude, @Part("Latitude") String Latitude, @Part("Address") String Address, @Part("Year") String Year, @Part("Month") String Month, @Part("FName") String FName);
}
