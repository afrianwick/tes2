package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.activity.worklist.LevDetailActivity;
import com.pertamina.portal.iam.adapters.MyDocument2Adapter;
import com.pertamina.portal.iam.adapters.worklist.MyDocumentAdapter;
import com.pertamina.portal.iam.interfaces.MyDocumentView;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.MyDocumentData;
import com.pertamina.portal.iam.models.Task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDocumentActivity extends BaseWorklistActivity implements MyDocumentView {

    private static final String TAG = "MyDocumentActivity";
    private AlertDialog loading;
    private RecyclerView recyclerView;
    private Realm realm;
    private MyDocumentAdapter adapter;
//    private MyDocument2Adapter adapter2;
    private EditText etSearch;
    private boolean sortAsc = false;
    private ImageView ivSort;
    private List<MyDocumentData> list;
    private List<Object> list2;
    private List<Integer> visibilities;
    private HashMap<Integer, List<MyDocumentData>> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_document);
        super.onCreateBackable(this, R.id.ivBack);

        View cvPersonal = findViewById(R.id.cvPersonal);
        recyclerView = findViewById(R.id.recyclerview);
        loading = getLoading();
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivSort = (ImageView) findViewById(R.id.ivSort);
        realm = Realm.getDefaultInstance();
        LinearLayoutManager llm = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    int rightMinWidth = (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width());
                    if(event.getRawX() >= rightMinWidth) {
                        search(etSearch.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });

        ivSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortAsc) {
                    sortAsc = false;
                } else {
                    sortAsc = true;
                }

                search(etSearch.getText().toString());
            }
        });

        getProcessInstance();
    }

    private void getProcessInstance() {
        loading.show();
        String pernr = PrefUtils.Build(this).getPref().getString(Constants.KEY_PERSONAL_NUM, null);
        String tableName = "dbo.UFN_PTM_CT_HPF_GET_FILES (" + pernr + ")";

        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getMyDocument("DataManagementServices", "GetDataFromTable", tableName)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getProcessInstance sip.. " + strResponse);
                                parseXml(strResponse, successListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            MyDocumentActivity.super.handleError(response);
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        MyDocumentActivity.super.showError("Failed connect to server");
                        t.printStackTrace();
                    }
                });
    }

    private void parseJson(String strJson) {
        Log.d(TAG, "parseJson: strJson" + strJson);
        Gson gson = new Gson();
        JsonObject jo = gson.fromJson(strJson, JsonObject.class);

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        final HashMap<String, List<MyDocumentData>> listGrouped = new HashMap();
        final HashMap<String, List<Object>> listGrouped2 = new HashMap();

        JsonArray jarr = jo.getAsJsonArray("records");
        JsonObject itemJo;
        MyDocumentData mdd;
        visibilities = new ArrayList<>();

        for (int i = 0; i < jarr.size(); i++) {
            itemJo = jarr.get(i).getAsJsonObject();
            mdd = new MyDocumentData();

            mdd.action = itemJo.get("FileFullName").getAsString();
            mdd.documentType = itemJo.get("DocumentType").getAsString().replaceAll(" ", "");
            mdd.name = itemJo.get("Name").getAsString();
            mdd.dateOfIssue = itemJo.get("DateOfIssue").getAsInt();
            mdd.filename = itemJo.get("OriginalFileName").getAsString();
            mdd.docName = itemJo.get("DocumentName").getAsString();
            mdd.docIssuer = itemJo.get("DocumentIssuer").getAsString();
            mdd.companyCode = itemJo.get("CompanyCode").getAsString();
            mdd.uploadDate = itemJo.get("CreatedOn").getAsString();
            mdd.description = itemJo.get("Description").getAsString();

            Log.d(TAG, "mdd.dateOfIssue" + mdd.dateOfIssue);

            List<MyDocumentData> myDocumentData;

            if (listGrouped.containsKey(itemJo.get("DocumentType").getAsString().replaceAll(" ", ""))) {
                myDocumentData = listGrouped.get(itemJo.get("DocumentType").getAsString().replaceAll(" ", ""));
                myDocumentData.add(mdd);
                listGrouped.put(itemJo.get("DocumentType").getAsString().replaceAll(" ", ""), myDocumentData);
                Log.d("datakeberapa", String.valueOf(i));
            } else {
                myDocumentData = new ArrayList<>();
                myDocumentData.add(mdd);
                listGrouped.put(itemJo.get("DocumentType").getAsString().replaceAll(" ", ""), myDocumentData);
                Log.d("datakeberapanew", String.valueOf(i));
            }

            visibilities.add(View.VISIBLE);
        }

        Set set = listGrouped.entrySet();
        Iterator iterator = set.iterator();
        hashMap = new HashMap<>();
        int index = 0;
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            list.addAll((List<MyDocumentData>) mentry.getValue());
            hashMap.put(index, (List<MyDocumentData>) mentry.getValue());
            index++;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<MyDocumentData> rows = realm.where(MyDocumentData.class).findAll();
                rows.deleteAllFromRealm();
                realm.insert(list);
            }
        });

        adapter = new MyDocumentAdapter(this, list, visibilities, this);
//        adapter2 = new MyDocument2Adapter(this, this, hashMap);
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < list.size(); i++) {
            Log.d("typenya", list.get(i).documentType + " sizenya = " + list.size());
        }
    }

    private OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);
            parseJson(strJson);
        }
    };

    public void search(String keyword) {
        Log.d(TAG, "searchAll:" + keyword);

        RealmQuery<MyDocumentData> query = realm.where(MyDocumentData.class);
        RealmResults<MyDocumentData> result1;
        list = new ArrayList<>();
        visibilities = new ArrayList<>();
        MyDocumentData data = null;

        // With sorting
        if (keyword.equalsIgnoreCase("*") || keyword.length() == 0) {
            if (sortAsc) {
                result1 = query.sort("dateOfIssue", Sort.ASCENDING).findAll();
            } else {
                result1 = query.sort("dateOfIssue", Sort.DESCENDING).findAll();
            }
        } else {
            if (sortAsc) {
                result1 = query.contains("name", keyword, Case.INSENSITIVE)
                        .sort("dateOfIssue", Sort.ASCENDING).findAll();
            } else {
                result1 = query.contains("name", keyword, Case.INSENSITIVE)
                        .sort("dateOfIssue", Sort.DESCENDING).findAll();
            }
        }

        Iterator<MyDocumentData> iterator = result1.iterator();

        while (iterator.hasNext()) {
            data = iterator.next();
            visibilities.add(View.VISIBLE);
            list.add(data);
        }

        // before combining filter
        Log.d(TAG, "list.size():" + list.size());

        if (list.size() == 0) {
            String strWording = getString(R.string.search_not_found);
            Toast.makeText(this, strWording, Toast.LENGTH_LONG).show();
        } else {
            adapter = new MyDocumentAdapter(this, list, visibilities, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onShowHideDocument(int position, String type) {
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).documentType.equals(type)) {
                    visibilities.set(i, visibilities.get(i) == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            }
            adapter = new MyDocumentAdapter(this, list, visibilities, this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {

        }
    }
}
