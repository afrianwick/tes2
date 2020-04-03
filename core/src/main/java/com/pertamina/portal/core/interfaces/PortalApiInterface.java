package com.pertamina.portal.core.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.models.paramsapi.DataApproval;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface PortalApiInterface {

    public static String DOWNLOAD_DOCUMENT = "https://apps.pertamina.com/PTM.HRIS.Service.WebService.Internet/GetFM.ashx?URL=";
    public static String DOWNLOAD_PAYSLIP = "https://apps.pertamina.com/PTM.HRIS.Service.WebService.Internet/GetPayslip.ashx?period=";

    @GET("PTM.OAUTH2/common/GetCompanies")
    Call<JsonArray> getCompanyCodes();

    @FormUrlEncoded
    @POST("PTM.OAUTH2/token")
    Call<JsonObject> login(@Field("grant_type") String grantType,
                           @Field("username") String username,
                           @Field("password") String password);

    @POST("PTM.OAUTH2/common/SendOTP")
    Call<JsonObject> sendOtp();

    @GET("PTM.OAUTH2/common/ValidateOTP")
    Call<JsonObject> validateOtp(@Query(value = "otpCode", encoded = true) String encryptedOtp);

    @GET("PTM.OAUTH2/common/ValidateUserPin")
    Call<JsonObject> validatePin(@Query(value = "userPin", encoded = false) String encryptedPin);

    @GET("PTM.OAUTH2/common/ValidateUserPin")
    Call<ResponseBody> resumeValidatePin(@Query(value = "userPin", encoded = false) String encryptedPin);

    @FormUrlEncoded
    @POST("PTM.OAUTH2/common/SetUserPin")
    Call<JsonObject> createPin(@Field("UserPin") String encryptedPin);

    @FormUrlEncoded
    @POST("PTM.OAUTH2/token")
    Call<JsonObject> refreshToken(@Field("grant_type") String grant_type,
                                  @Field("refresh_token") String refresh_token);


    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2&infoTypeName=ORGANIZATIONAL_ASSIGNMENT")
    Call<ResponseBody> getCurrentOrganizationalAssignment(@Header("ServiceName") String serviceName,
                                                          @Header("MethodName") String methodName,
                                                          @Query("personnelNumber") String personnelNumber,
                                                          @Query("startDate") String startDate,
                                                          @Query("endDate") String endDate);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2&sFunctionOrProcedureName=USP_PTM_MPPK_GET_STATUS")
    Call<ResponseBody> getMPPKStatus(@Header("ServiceName") String serviceName,
                                     @Header("MethodName") String methodName,
                                     @Query("sqlParam") String sqlParam);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2&prefix=FLC&startDate=20190101")
    Call<ResponseBody> getFCLHistory(@Header("ServiceName") String serviceName,
                                      @Header("MethodName") String methodName,
                                      @Query("personnelNumber") String personnelNumber,
                                      @Query("endDate") String endDate,
                                      @Query("companyCode") String companyCode
    );

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2&infoTypeName=ORGANIZATIONAL_ASSIGNMENT&startDate=19000101&endDate=99991231")
    Call<ResponseBody> getOrganizationalAssignmentHistory(@Header("ServiceName") String serviceName,
                                                          @Header("MethodName") String methodName,
                                                          @Query("personnelNumber") String personnelNumber);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getCompanyCodes(@Header("ServiceName") String serviceName,
                                       @Header("MethodName") String methodName,
                                       @Query("clientID") String clientID,
                                       @Query("referenceName") String referenceName,
                                       @Query("fieldNames") String fieldNames,
                                       @Query("conditions") String conditions);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getPositionAttribute(@Header("ServiceName") String serviceName,
                                           @Header("MethodName") String methodName,
                                           @Query("positionID") String positionID);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getEmployeeHiringDate(@Header("ServiceName") String serviceName,
                                               @Header("MethodName") String methodName,
                                               @Query("personnelNumber") String personnelNumber);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getUserAreaByADUsername(@Header("ServiceName") String serviceName,
                                                 @Header("MethodName") String methodName,
                                                 @Query("adUser") String adUser);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getRoleArea(@Header("ServiceName") String serviceName,
                                             @Header("MethodName") String methodName,
                                             @Query("RoleAreaName") String roleAreaName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getCompanies(@Header("ServiceName") String serviceName,
                                             @Header("MethodName") String methodName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getRoleOwnerForUser(@Header("ServiceName") String serviceName,
                                             @Header("MethodName") String methodName,
                                             @Query("roleName") String roleName,
                                             @Query("pernr") String pernr);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getEmployeeHeaderIdentityOfRoleOwner(@Header("ServiceName") String serviceName,
                                                             @Header("MethodName") String methodName,
                                                             @Query("personnelNumber") String personnelNumber,
                                                             @Query("startDate") String startDate,
                                                             @Query("endDate") String endDate);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getPositionAttributeOfRoleOwner(@Header("ServiceName") String serviceName,
                                                         @Header("MethodName") String methodName,
                                                         @Query("positionID") String positionID);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet")
    Call<ResponseBody> getWorklist(@Query("k2Folder") String k2Folder,
                                   @Query("outputType") String outputType,
                                   @Header("ServiceName") String serviceName,
                                   @Header("MethodName") String methodName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet")
    Call<ResponseBody> getWorklistHistory(@Header("ServiceName") String serviceName,
                                          @Header("MethodName") String methodName,
                                          @Query("adUserName") String adUserName,
                                          @Query("Status") String status,
                                          @Query("Limit") String limit,
                                          @Query("Offset") String offset,
                                          @Query("sortParameterInJson") String sortParameterInJson,
                                          @Query("searchLogic") String searchLogic,
                                          @Query("searchParameterInJson") String searchParameterInJson,
                                          @Query("outputType") String outputType);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?arrayCommaDelimetedDocumentType=Surat Keterangan Jalur Kemitraan,Surat Keterangan Jaminan Kesehatan,Surat Keterangan Tanpa Upah versi Inggris,Surat Keterangan Tanpa Upah versi Indonesia,Surat Keterangan Upah versi Inggris,Surat Keterangan Upah versi Indonesia&outputType=2")
    Call<ResponseBody> getNonVisa(@Header("ServiceName") String servieName,
                               @Header("MethodName") String methodName,
                               @Query("personID") String personID);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?arrayCommaDelimetedDocumentType=Keperluan Perusahaan - Dinas,Keperluan Pribadi - Pekerja,Keperluan Pribadi - Pekerja dan Keluarga&outputType=2")
    Call<ResponseBody> getVisa(@Header("ServiceName") String servieName,
                               @Header("MethodName") String methodName,
                               @Query("personID") String personID);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?Limit=500&Offset=0&sortParameterInJson=null&searchLogic=null&searchParameterInJson=null&outputType=2")
    Call<ResponseBody> getWorklistByStatus(@Query("Status") String status,
                                           @Header("ServiceName") String serviceName,
                                           @Header("MethodName") String methodName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?Limit=20&Offset=0&sortParameterInJson=&searchLogic=&searchParameterInJson=&outputType=2")
    Call<ResponseBody> getSearchEmployee(@Header("ServiceName") String serviceName,
                                           @Header("MethodName") String methodName,
                                           @Query("TableName") String tableName);


    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?Limit=500&Offset=0&sortParameterInJson=&searchLogic=NULL&searchParameterInJson=&outputType=2")
    Call<ResponseBody> getWorklistPending(@Header("ServiceName") String serviceName,
                                          @Header("MethodName") String methodName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getSkmjData(@Query("processInstanceID") String processInstanceID,
                                   @Header("ServiceName") String serviceName,
                                   @Header("MethodName") String methodName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getListPayslip(@Header("ServiceName") String serviceName,
                                      @Header("MethodName") String methodName);

    @GET("PTM.HRIS.Service.WebService.Internet/GetPayslip.ashx")
    Call<ResponseBody> getListPayslipFile(@Query("period") String period);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getCalendarEvents(@Header("ServiceName") String serviceName,
                                         @Header("MethodName") String methodName,
                                         @Query("personnelNumber") String personnelNumber,
                                         @Query("startDate") String startDate,
                                         @Query("endDate") String endDate);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2&infoTypeName=ABSENCE_QUOTAS")
    Call<ResponseBody> getLeaveQuotaHilight(@Header("ServiceName") String serviceName,
                                            @Header("MethodName") String methodName,
                                            @Query("personnelNumber") String personnelNumber,
                                            @Query("startDate") String startDate,
                                            @Query("endDate") String endDate);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/GetLatestSubmittedGratifikasi")
    Call<ResponseBody> getLatestSubmittedGratifikasi();

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?AttendanceOrAbsenceType=&outputType=2")
    Call<ResponseBody> getLeaveTypeCalendar(@Header("ServiceName") String serviceName,
                                            @Header("MethodName") String methodName,
                                            @Query("personnelNumber") String personnelNumber);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2" +
            "&clientID=100&referenceName=COUNTRY_NAMES&fieldNames= &conditions= ")
    Call<ResponseBody> getCountries(@Header("ServiceName") String serviceName,
                                    @Header("MethodName") String methodName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?startDate=0&endDate=0&outputType=2")
    Call<ResponseBody> getPersonalData(@Header("ServiceName") String serviceName,
                                       @Header("MethodName") String methodName,
                                       @Query("personnelNumber") String personnelNumber);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?infoTypeName=PERSONAL_DATA&outputType=2")
    Call<ResponseBody> getPersonalDataSKet(@Header("ServiceName") String serviceName,
                                       @Header("MethodName") String methodName,
                                       @Query("personnelNumber") String personnelNumber,
                                       @Query("startDate") String startDate,
                                       @Query("endDate") String endDate);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/GetRefDataGratifikasi")
    Call<ResponseBody> getRefDataGratification();

    @GET("PTM.HRIS.Service.WebService.Internet/GetFM.ashx")
    Call<ResponseBody> loadImage(@Query("URL") String url);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getSuratKetVisa(@Header("ServiceName") String serviceName,
                                       @Header("MethodName") String methodName,
                                       @Query("processInstanceID") String processInstanceID);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getPAN(@Header("ServiceName") String serviceName,
                              @Header("MethodName") String methodName,
                              @Query("processInstanceID") String processInstanceID);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getSimulateLeave(@Header("ServiceName") String serviceName,
                                        @Header("MethodName") String methodName,
                                        @Query("PersonalNumber") String personalNumber,
                                        @Query("Begda") String begda,
                                        @Query("Endda") String endda,
                                        @Query("AttendanceorAbsenceType") String attendanceorAbsenceType,
                                        @Query("IsOutOfTownLeave") String isOutOfTownLeave);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getLEV(@Header("ServiceName") String serviceName,
                              @Header("MethodName") String methodName,
                              @Query("processInstanceID") String processInstanceID);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getMPPK(@Header("ServiceName") String serviceName,
                              @Header("MethodName") String methodName,
                              @Query("processInstanceID") String processInstanceID);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getMPPKAdditional(@Header("ServiceName") String serviceName,
                                         @Header("MethodName") String methodName,
                                         @Query("sqlParam") String sqlParam,
                                         @Query("sFunctionOrProcedureName") String sFunctionOrProcedureName);

    @FormUrlEncoded
    @POST("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPPost")
    Call<ResponseBody> submitApprovalPart(@Header("ServiceName") String serviceName,
                                          @Header("MethodName") String methodName,
                                          @Field("folioNumber") String folioNumber,
                                          @Field("k2SerialNumber") String k2SerialNumber,
                                          @Field("k2Action") String k2Action,
                                          @Field("xmlData") String xmlData,
                                          @Field("xmlDataSummary") String xmlDataSummary,
                                          @Field("comment") String comment,
                                          @Field("K2DataFieldsInJSON") String K2DataFieldsInJSON
    );

    @FormUrlEncoded
    @POST("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPPost")
    Call<ResponseBody> submitRequestSKETNonVisa(@Header("ServiceName") String serviceName,
                                                @Header("MethodName") String methodName,
                                                @Field("K2WorkflowName") String K2WorkflowName,
                                                @Field("url") String url,
                                                @Field("executorPersonnelNumber") String executorPersonnelNumber,
                                                @Field("requestorPersonnelNumber") String requestorPersonnelNumber,
                                                @Field("requestorADUser") String requestorADUser,
                                                @Field("xmlData") String xmlData,
                                                @Field("xmlDataSummary") String xmlDataSummary,
                                                @Field("comment") String comment,
                                                @Field("K2DataFieldsInJSON") String K2DataFieldsInJSON
    );

    @FormUrlEncoded
    @POST("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPPost")
    Call<ResponseBody> submitLeaveRequest(@Header("ServiceName") String serviceName,
                                          @Header("MethodName") String methodName,
                                          @Field("K2WorkflowName") String kWorkflowName,
                                          @Field("url") String url,
                                          @Field("executorPersonnelNumber") String executorPersonnelNumber,
                                          @Field("requestorPersonnelNumber") String requestorPersonnelNumber,
                                          @Field("requestorADUser") String requestorADUser,
                                          @Field("xmlData") String xmlData,
                                          @Field("xmlDataSummary") String xmlDataSummary,
                                          @Field("comment") String comment,
                                          @Field("K2DataFieldsInJSON") String k2DataFieldsInJSON
    );

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/GetHTMLReport")
    Call<ResponseBody> getSKetDocument(@Header("ReportName") String reportName,
                                       @Query("PersonID") String personID,
                                       @Query("DocumentName") String documentName);

    @Multipart
    @POST("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/SubmitGratifikasi")
    Call<ResponseBody> submitInputGratification(@Part("tglPemberian") RequestBody tglPemberian,
                                          @Part("jenisPemberian") RequestBody jenisPemberian,
                                          @Part("nilaiPemberian") RequestBody nilaiPemberian,
                                          @Part("jumlahPemberian") RequestBody jumlahPemberian,
                                          @Part("penerimaPemberian") RequestBody penerimaPemberian,
                                          @Part("keteranganPemberian") RequestBody keteranganPemberian,
                                          @Part("tglsubmitPemberian") RequestBody tglsubmitPemberian,
                                          @Part("peristiwaPemberian") RequestBody peristiwaPemberian,
                                          @Part("tglPenerimaan") RequestBody tglPenerimaan,
                                          @Part("jenisPenerimaan") RequestBody jenisPenerimaan,
                                          @Part("nilaiPenerimaan") RequestBody nilaiPenerimaan,
                                          @Part("jumlahPenerimaan") RequestBody jumlahPenerimaan,
                                          @Part("pemberiPenerimaan") RequestBody penerimaPenerimaan,
                                          @Part("keteranganPenerimaan") RequestBody keteranganPenerimaan,
                                          @Part("tglsubmitPenerimaan") RequestBody tglsubmitPenerimaan,
                                          @Part("peristiwaPenerimaan") RequestBody peristiwaPenerimaan,
                                          @Part("tglPermintaan") RequestBody tglPermintaan,
                                          @Part("jenisPermintaan") RequestBody jenisPermintaan,
                                          @Part("nilaiPermintaan") RequestBody nilaiPermintaan,
                                          @Part("jumlahPermintaan") RequestBody jumlahPermintaan,
                                          @Part("pemintaPermintaan") RequestBody penerimaPermintaan,
                                          @Part("keteranganPermintaan") RequestBody keteranganPermintaan,
                                          @Part("tglsubmitPermintaan") RequestBody tglsubmitPermintaan,
                                          @Part("peristiwaPermintaan") RequestBody peristiwaPermintaan
    );

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getMclFlcOpc(@Header("ServiceName") String serviceName,
                              @Header("MethodName") String methodName,
                              @Query("processInstanceID") String processInstanceID);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getMclDocuments(@Header("ServiceName") String serviceName,
                                    @Header("MethodName") String methodName,
                                    @Query("folderName") String folderName,
                                    @Query("RowSize") String rowSize,
                                    @Query("RowSkip") String rowSkip,
                                    @Query("Condition") String condition,
                                    @Query("OrderBy") String orderBy);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getMclOptions(@Header("ServiceName") String serviceName,
                                    @Header("MethodName") String methodName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getCostCenter(@Header("ServiceName") String serviceName,
                                     @Header("MethodName") String methodName,
                                     @Query("clientID") String clientID,
                                     @Query("PBUKRSM") String PBUKRSM,
                                     @Query("PKOKRSM") String PKOKRSM,
                                     @Query("PKOSTLM") String PKOSTLM,
                                     @Query("PDATBIM") String PDATBIM);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?" +
            "Limit=500&Offset=0" +
            "&sortParameterInJson=&searchLogic=AND&outputType=2&searchParameterInJson=")
    Call<ResponseBody> getMyDocument(@Header("ServiceName") String serviceName,
                                     @Header("MethodName") String methodName,
                                     @Query("TableName") String tableName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getFamily(@Header("ServiceName") String serviceName,
                                 @Header("MethodName") String methodName,
                                 @Query("personnelNumber") String personnelNumber,
                                 @Query("infoTypeName") String infoTypeName,
                                 @Query("startDate") String startDate,
                                 @Query("endDate") String endDate);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPGet?outputType=2")
    Call<ResponseBody> getEmbassy(@Header("ServiceName") String serviceName,
                                 @Header("MethodName") String methodName);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/GetReportPersonalGratifikasi")
    Call<ResponseBody> getReportGratifikasi(@Query("type") String type,
                                            @Query("startDate") String startDate,
                                            @Query("endDate") String endDate);

    @GET("PTM.HRIS.Service.WebService.Internet/GetFM.ashx")
    Call<ResponseBody> loadFile(@Query("URL") String url);

    @FormUrlEncoded
    @POST("PTM.OAUTH2/common/SetFCM")
    Call<ResponseBody> fcm(@Field(value = "FCM", encoded = false) String fcm);

    @FormUrlEncoded
    @POST("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/HTTPPost")
    Call<ResponseBody> createFolder(@Header("ServiceName") String serviceName,
                                    @Header("MethodName") String methodName,
                                    @Field("FolderName") String folderName,
                                    @Field("ParentName") String parentName,
                                    @Field("outputType") String outputType);

    @FormUrlEncoded
    @POST("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/CreateFileFM")
    Call<ResponseBody> createFile(@Field("FileName") String fileName,
                                  @Field("ParentName") String parentName,
                                  @Field("FileExtention") String fileExtention,
                                  @Field("FileBinary") String fileBinary);

    @GET("PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/GetPDFReport")
    Call<ResponseBody> getPDFSket(@Header("ReportName") String reportName,
                                  @Header("PaperKind") String paperKind,
                                  @Query("PersonID") String personID,
                                  @Query("DocumentName") String documentName);

    @GET("https://apps.pertamina.com/PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/GetLatestSubmittedCOI")
    Call<ResponseBody> getCOI();

    @Multipart
    @POST("https://apps.pertamina.com/PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/SubmitCOI")
    Call<ResponseBody> getSubmitCOI(@Part("tempat") RequestBody tempat,
                                    @Part("status") RequestBody status,
                                    @Part("isi1") RequestBody isi1,
                                    @Part("tanggal") RequestBody tanggal);

    @GET("https://apps.pertamina.com/PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/GetLatestSubmittedCOC")
    Call<ResponseBody> getCOC();

    @Multipart
    @POST("https://apps.pertamina.com/PTM.HRIS.Service.WebService.Internet/MobileServices.asmx/SubmitCOC")
    Call<ResponseBody> getSubmitCOC(@Part("tempat") RequestBody tempat,
                                    @Part("idSoal1") RequestBody idSoal1,
                                    @Part("idJawaban1") RequestBody idJawaban1,
                                    @Part("idSoal2") RequestBody idSoal2,
                                    @Part("idJawaban2") RequestBody idJawaban2,
                                    @Part("idSoal3") RequestBody idSoal3,
                                    @Part("idJawaban3") RequestBody idJawaban3);

//    /**

//     * Gets the data.
//     *
//     * @param slsno the slsno
//     * @param mtgl the mtgl
//     * @return the data
//     */
//    @GET("download")
//    Call<Result> getData(@Query("slsno") String slsno, @Query("mtgl") String mtgl, @Query("distri") String distri);
//
//
//    @GET("myresource/kelurahans")
//    void getDataKelurahan(@Query("distri") String distri , Callback<List<Mblfkel>> callback);
//
//    @POST("uploaddata")
//    Call<List<ValueRespons>> pushData(@Body Upload data);
//
//    @POST("postlocation")
//    Call<List<ValueRespons>> pushData(@Body Mblflacak data);
//
//    @POST("postvisit")
//    void pushVisitData(@Body Mblfvisit data, Callback<List<ValueRespons>> callback);
//
//    @POST("biaya")
//    Call<List<ValueRespons>> pushExpenseData(@Body Biaya biaya, Callback<List<ValueRespons>> callback);
//
//    @POST("survey/submit")
//    Call<JsonObject> submitSurvey(@Body SurveyAnswer svAnswer);
//
//    @POST("notification/update-fcm")
//    Call<JsonObject> sendFcm(@Body SendFcmRequest fcmRequest);
//
//    @Multipart
//    @POST("survey/submit/photo/{distid}/{slsno}/{questionid}")
//    Call<JsonObject> uploadImageSurvey(
//            @Path("distid") String distid,
//            @Path("slsno") String slsno,
//            @Path("questionid") String questionid,
//            @Part("custno") RequestBody custno,
//            @Part MultipartBody.Part file);
//
//    @POST("absen")
//    Call<List<ValueRespons>> pushAbsent(@Body List<Absensi> absens);
//
//    @GET("aplikasi/versi")
//    Call<JsonObject> getLastVersion();
//
//    @GET("reason/absen/all")
//    Call<List<ReasonAbs>> getReasonAbsent();
//
//    @Multipart
//    @POST("sales/profile/photo/upload/{distid}/{slsno}")
//    Call<JsonObject> uploadImageSales(
//            @Path("distid") String distid,
//            @Path("slsno") String slsno,
//            @Part MultipartBody.Part file);
//
//    @Multipart
//    @POST("outlet/profile/photo/upload/{distid}/{custno}")
//    Call<JsonObject> uploadImageOutlet(
//            @Path("distid") String distid,
//            @Path("custno") String custno,
//            @Part MultipartBody.Part file);
//
}
