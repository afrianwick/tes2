package com.pertamina.portal.iam.utils.BackgroundService;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.interfaces.ReqSuratVisiView;
import com.pertamina.portal.iam.utils.FileUtils.FileUtils;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserInterface;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserUtils;

import org.w3c.dom.NodeList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SketVisaBackground extends AsyncTask<String, String, String> {

    private Context context;
    private String sket;
    private Uri uri;

    public SketVisaBackground(Context context, String sket, Uri uri) {
        this.context = context;
        this.sket = sket;
        this.uri = uri;
    }

    public static SketVisaBackground getCreateFolder (Context context, String sket, Uri uri) {
        return new SketVisaBackground(context, sket, uri);
    }


    @Override
    protected String doInBackground(String... strings) {
        createFolder();
        return null;
    }

    private void createFolder() {
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(context, 2000);
        restApi.createFolder("FileManagementServices", "CreateFolder", sket, "ROOT\\3rdPartyApps\\IAM\\RequestAttachment", "2")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("Success submit sket", "getAllWorklist, true _ " + response.raw().toString());
                                XMLParserUtils.parseXml(strResponse, new XMLParserInterface() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Gson gson = new Gson();
                                        JsonObject jo = gson.fromJson(result, JsonObject.class);
                                        JsonArray jArr = jo.getAsJsonArray("Table0");

                                        if (jArr.get(0).getAsJsonObject().get("ReturnType").getAsString().equalsIgnoreCase("S")) {
                                            createFile(sket, uri);
                                            Log.d("createfolder", "created");
                                        }
                                    }

                                    @Override
                                    public void onFailure(NodeList nodeListError) {

                                    }

                                    @Override
                                    public void onSuccessReturnMessage(NodeList nodeListError) {

                                    }
                                });
//                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Error submit sket", "getAllWorklist, false _ " + response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Failure get history", "getAllWorklist, onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    private void createFile(String sket, Uri uri) {
        String strFileName = FileUtils.getFileName(context, uri).split("\\.")[0];
        String fileExt = "";

        if (FileUtils.getMimeType(context, uri) != null) {
            fileExt = FileUtils.getMimeType(context, uri);
        }
        String fileBinary = "";

        byte[] bytes;

        try {
            InputStream in = context.getContentResolver().openInputStream(uri);
            bytes=FileUtils.getBytes(in);
            Log.d("data", "onActivityResult: bytes size="+bytes.length);
            Log.d("data", "onActivityResult: Base64string="+ Base64.encodeToString(bytes, Base64.DEFAULT));
            fileBinary = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(context, 2000);
        restApi.createFile(strFileName, "ROOT\\3rdPartyApps\\IAM\\RequestAttachment\\"+sket, fileExt, fileBinary)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("Success submit sket", "getAllWorklist, true _ " + response.raw().toString());

                                XMLParserUtils.parseXml(strResponse, new XMLParserInterface() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Log.d("createfile", "created");
                                    }

                                    @Override
                                    public void onFailure(NodeList nodeListError) {

                                    }

                                    @Override
                                    public void onSuccessReturnMessage(NodeList nodeListError) {

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Error submit sket", "getAllWorklist, false _ " + response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Failure get history", "getAllWorklist, onFailure..");
                        t.printStackTrace();
                    }
                });
    }
}
