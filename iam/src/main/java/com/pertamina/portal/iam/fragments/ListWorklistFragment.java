package com.pertamina.portal.iam.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.fragments.BaseFragmentApi;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.MyWorklistActivity;
import com.pertamina.portal.iam.adapters.ListWorklistAdapter;
import com.pertamina.portal.iam.adapters.ListWorklistApprovedAdapter;
import com.pertamina.portal.iam.adapters.ListWorklistHistoryAdapter;
import com.pertamina.portal.iam.adapters.ListWorklistPendingAdapter;
import com.pertamina.portal.iam.adapters.ListWorklistRejectedAdapter;
import com.pertamina.portal.iam.models.Karyawan;
import com.pertamina.portal.iam.models.Task;
import com.pertamina.portal.iam.models.TaskApproval;
import com.pertamina.portal.iam.models.TaskHistory;
import com.pertamina.portal.iam.models.TaskPending;
import com.pertamina.portal.iam.models.TaskRejected;
import com.pertamina.portal.iam.utils.ErrorMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListWorklistFragment extends BaseFragmentApi {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "ListWorklistFragment";
    private static final String LIST_ALL = "all";
    public static final String LIST_APPROVED = "Approved";
    public static final String LIST_REJECTED = "Rejected";
    public static final String LIST_PENDING = "Pending";
    public static final String LIST_HISTORY = "History";
    private static final String ARG_LIST_STATUS = "list-status";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ListWorklistAdapter adapter;
    private ListWorklistApprovedAdapter adapterApproved;
    private ListWorklistRejectedAdapter adapterRejected;
    private ListWorklistHistoryAdapter adapterHistory;
    private ListWorklistPendingAdapter adapterPending;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swiperefresh;
    private String statusList;
    private AlertDialog alert;
    private AlertDialog loading;
    private Realm realm;
    private String[] filter;
    private boolean isFiltered;
    private String keyword;
    public static boolean sortAsc = false;

    public ListWorklistFragment() {
    }

    public static ListWorklistFragment newInstance(int columnCount, @NonNull String listStatus) {
        ListWorklistFragment fragment = new ListWorklistFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_LIST_STATUS, listStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            statusList = getArguments().getString(ARG_LIST_STATUS);
            realm = Realm.getDefaultInstance();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        if (mColumnCount <= 1) {
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        loading = ((MyWorklistActivity) getActivity()).getLoading();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (statusList.equalsIgnoreCase(LIST_ALL)) {
                    getAllWorklist();
                } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
                    getWorklistPending();
                } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
                    getWorklistByStatus(statusList);
                } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
                    getWorklistByStatus(statusList);
                } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
                    getWorklistHistory();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {

            if (statusList.equalsIgnoreCase(LIST_ALL)) {
//                Log.d(TAG, "statusList == " + statusList);
//
//                RealmQuery<Task> query = realm.where(Task.class).equalTo("processStep", statusList);
//                RealmResults<Task> result1 = query.findAll();
//                Iterator<Task> iterator = result1.iterator();
//                List<Task> taskList = new ArrayList<>();
//                Task task = null;
//
//                while (iterator.hasNext()) {
//                    task = iterator.next();
//                    taskList.add(task);
//                }
//
//                Log.d(TAG, "taskList.size() = " + taskList.size());

//                if (taskList.size() > 0) {
//                    loadAddWorklist(taskList);
//                } else {
                    getAllWorklist();
//                }
            } else {
                if (statusList.equalsIgnoreCase(LIST_PENDING)) {
                    getWorklistPending();
                } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
                    getWorklistHistory();
                } else {
                    getWorklistByStatus(statusList);
                }
            }
        }
    }

    private void loadAddWorklistAll(List<Task> taskList) {
        adapter = new ListWorklistAdapter(getActivity(), taskList, mListener);
        recyclerView.setAdapter(adapter);
    }

    private void loadAddWorklistPending(List<TaskPending> taskList) {
        adapterPending = new ListWorklistPendingAdapter(getActivity(), taskList, mListener);
        recyclerView.setAdapter(adapterPending);
    }

    private void loadAddWorklistApproval(List<TaskApproval> taskList) {
        adapterApproved = new ListWorklistApprovedAdapter(getActivity(), taskList, mListener);
        recyclerView.setAdapter(adapterApproved);
    }

    private void loadAddWorklistRejected(List<TaskRejected> taskList) {
        adapterRejected = new ListWorklistRejectedAdapter(getActivity(), taskList, mListener);
        recyclerView.setAdapter(adapterRejected);
    }

    private void loadAddWorklistHistory(List<TaskHistory> taskList) {
        adapterHistory = new ListWorklistHistoryAdapter(getActivity(), taskList, mListener);
        recyclerView.setAdapter(adapterHistory);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void searchAll(String keyword, String[] filter, boolean isFiltered) {
        this.keyword = keyword;
        this. filter = filter;
        this.isFiltered = isFiltered;
        if (statusList.equalsIgnoreCase(LIST_ALL)) {
            Log.d(TAG, "searchAll:" + keyword);

            RealmQuery<Task> query = realm.where(Task.class);
            RealmResults<Task> result1;
            List<Task> taskList = new ArrayList<>();
            List<Task> taskListTmp = new ArrayList<>();
            Task task = null;

//        if (!statusList.equalsIgnoreCase(LIST_ALL)) {
            query.equalTo("processStep", statusList);
//        }

            // With sorting
//            if (keyword.equalsIgnoreCase("*") || keyword.length() == 0) {
                if (sortAsc) {
                    query.sort("requestDate", Sort.ASCENDING).findAll();
                } else {
                    query.sort("requestDate", Sort.DESCENDING).findAll();
                }
//            } else {
//                if (sortAsc) {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.ASCENDING).findAll();
//                } else {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.DESCENDING).findAll();
//                }
//            }

            if (!isFiltered && keyword.isEmpty()) {
                result1 = query.findAll();
                Iterator<Task> iterator = result1.iterator();

                while (iterator.hasNext()) {
                    task = iterator.next();
                    taskList.add(task);
                }
            } else {
                if (!keyword.isEmpty()) {
                    result1 = query.contains("folioNumber", keyword, Case.INSENSITIVE).findAll();
                    Iterator<Task> iterator = result1.iterator();

                    while (iterator.hasNext()) {
                        task = iterator.next();
                        taskList.add(task);
                    }
                }

                if (isFiltered) {
                    for (String prefix : filter) {
                        if (prefix != null) {
                            taskListTmp.clear();
                            taskListTmp = populateFilterAll(prefix);
                            taskList.addAll(taskListTmp);
                        }
                    }
                }
            }

            // before combining filter
            Log.d(TAG, "taskList.size():" + taskList.size());

            if ((taskList.size() == 0) && (filter.length > 0)) {
                String strWording = "";
                if (statusList.equalsIgnoreCase(LIST_ALL)) {
                    strWording = "Anda tidak punya data Task List";
                } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
                    strWording = "Anda tidak punya data Pending";
                } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
                    strWording = "Anda tidak punya data Approved";
                } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
                    strWording = "Anda tidak punya data Rejected";
                } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
                    strWording = "Anda tidak punya data Approval History";
                }
                ErrorMessage.errorMessage(getContext(), swiperefresh, strWording);
            }
            loadAddWorklistAll(taskList);
        } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
            Log.d(TAG, "searchAll:" + keyword);

            RealmQuery<TaskApproval> query = realm.where(TaskApproval.class);
            RealmResults<TaskApproval> result1;
            List<TaskApproval> taskList = new ArrayList<>();
            List<TaskApproval> taskListTmp = new ArrayList<>();
            TaskApproval task = null;

//        if (!statusList.equalsIgnoreCase(LIST_ALL)) {
            query.equalTo("processStep", statusList);
//        }

            // With sorting
//            if (keyword.equalsIgnoreCase("*") || keyword.length() == 0) {
                if (sortAsc) {
                    query.sort("requestDate", Sort.ASCENDING).findAll();
                } else {
                    query.sort("requestDate", Sort.DESCENDING).findAll();
                }
//            } else {
//                if (sortAsc) {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.ASCENDING).findAll();
//                } else {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.DESCENDING).findAll();
//                }
//            }

            if (!isFiltered && keyword.isEmpty()) {
                result1 = query.findAll();
                Iterator<TaskApproval> iterator = result1.iterator();

                while (iterator.hasNext()) {
                    task = iterator.next();
                    taskList.add(task);
                }
            } else {
                if (!keyword.isEmpty()) {
                    result1 = query.contains("folioNumber", keyword, Case.INSENSITIVE).findAll();
                    Iterator<TaskApproval> iterator = result1.iterator();

                    while (iterator.hasNext()) {
                        task = iterator.next();
                        taskList.add(task);
                    }
                }

                if (isFiltered) {
                    for (String prefix : filter) {
                        if (prefix != null) {
                            taskListTmp.clear();
                            taskListTmp = populateFilterApproved(prefix);
                            taskList.addAll(taskListTmp);
                        }
                    }
                }
            }

            // before combining filter
            Log.d(TAG, "taskList.size():" + taskList.size());

            if ((taskList.size() == 0) && (filter.length > 0)) {
                String strWording = "";
                if (statusList.equalsIgnoreCase(LIST_ALL)) {
                    strWording = "Anda tidak punya data Task List";
                } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
                    strWording = "Anda tidak punya data Pending";
                } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
                    strWording = "Anda tidak punya data Approved";
                } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
                    strWording = "Anda tidak punya data Rejected";
                } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
                    strWording = "Anda tidak punya data Approval History";
                }
                ErrorMessage.errorMessage(getContext(), swiperefresh, strWording);
            }
            loadAddWorklistApproval(taskList);
        } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
            Log.d(TAG, "searchAll:" + keyword);

            RealmQuery<TaskRejected> query = realm.where(TaskRejected.class);
            RealmResults<TaskRejected> result1;
            List<TaskRejected> taskList = new ArrayList<>();
            List<TaskRejected> taskListTmp = new ArrayList<>();
            TaskRejected task = null;

//        if (!statusList.equalsIgnoreCase(LIST_ALL)) {
            query.equalTo("processStep", statusList);
//        }

            // With sorting
//            if (keyword.equalsIgnoreCase("*") || keyword.length() == 0) {
                if (sortAsc) {
                    query.sort("requestDate", Sort.ASCENDING).findAll();
                } else {
                    query.sort("requestDate", Sort.DESCENDING).findAll();
                }
//            } else {
//                if (sortAsc) {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.ASCENDING).findAll();
//                } else {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.DESCENDING).findAll();
//                }
//            }

            if (!isFiltered && keyword.isEmpty()) {
                result1 = query.findAll();
                Iterator<TaskRejected> iterator = result1.iterator();

                while (iterator.hasNext()) {
                    task = iterator.next();
                    taskList.add(task);
                }
            } else {
                if (!keyword.isEmpty()) {
                    result1 = query.contains("folioNumber", keyword, Case.INSENSITIVE).findAll();
                    Iterator<TaskRejected> iterator = result1.iterator();

                    while (iterator.hasNext()) {
                        task = iterator.next();
                        taskList.add(task);
                    }
                }

                if (isFiltered) {
                    for (String prefix : filter) {
                        if (prefix != null) {
                            taskListTmp.clear();
                            taskListTmp = populateFilterRejected(prefix);
                            taskList.addAll(taskListTmp);
                        }
                    }
                }
            }

            // before combining filter
            Log.d(TAG, "taskList.size():" + taskList.size());

            if ((taskList.size() == 0) && (filter.length > 0)) {
                String strWording = "";
                if (statusList.equalsIgnoreCase(LIST_ALL)) {
                    strWording = "Anda tidak punya data Task List";
                } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
                    strWording = "Anda tidak punya data Pending";
                } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
                    strWording = "Anda tidak punya data Approved";
                } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
                    strWording = "Anda tidak punya data Rejected";
                } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
                    strWording = "Anda tidak punya data Approval History";
                }
                ErrorMessage.errorMessage(getContext(), swiperefresh, strWording);
            }
            loadAddWorklistRejected(taskList);
        } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
            Log.d(TAG, "searchAll:" + keyword);

            RealmQuery<TaskPending> query = realm.where(TaskPending.class);
            RealmResults<TaskPending> result1;
            List<TaskPending> taskList = new ArrayList<>();
            List<TaskPending> taskListTmp = new ArrayList<>();
            TaskPending task = null;

            // With sorting
//            if (keyword.equalsIgnoreCase("*") || keyword.length() == 0) {
                if (sortAsc) {
                    query.sort("requestDate", Sort.ASCENDING).findAll();
                } else {
                    query.sort("requestDate", Sort.DESCENDING).findAll();
                }
//            } else {
//                if (sortAsc) {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.ASCENDING).findAll();
//                } else {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.DESCENDING).findAll();
//                }
//            }

//        if (!statusList.equalsIgnoreCase(LIST_ALL)) {
            query.equalTo("processStep", statusList);
//        }

            if (!isFiltered && keyword.isEmpty()) {
                result1 = query.findAll();
                Iterator<TaskPending> iterator = result1.iterator();

                while (iterator.hasNext()) {
                    task = iterator.next();
                    taskList.add(task);
                }
            } else {
                if (!keyword.isEmpty()) {
                    result1 = query.contains("folioNumber", keyword, Case.INSENSITIVE).findAll();
                    Iterator<TaskPending> iterator = result1.iterator();

                    while (iterator.hasNext()) {
                        task = iterator.next();
                        taskList.add(task);
                    }
                }

                if (isFiltered) {
                    for (String prefix : filter) {
                        if (prefix != null) {
                            taskListTmp.clear();
                            taskListTmp = populateFilterPending(prefix);
                            taskList.addAll(taskListTmp);
                        }
                    }
                }
            }

            // before combining filter
            Log.d(TAG, "taskList.size():" + taskList.size());

            if ((taskList.size() == 0) && (filter.length > 0)) {
                String strWording = "";
                if (statusList.equalsIgnoreCase(LIST_ALL)) {
                    strWording = "Anda tidak punya data Task List";
                } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
                    strWording = "Anda tidak punya data Pending";
                } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
                    strWording = "Anda tidak punya data Approved";
                } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
                    strWording = "Anda tidak punya data Rejected";
                } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
                    strWording = "Anda tidak punya data Approval History";
                }
                ErrorMessage.errorMessage(getContext(), swiperefresh, strWording);
            }
            loadAddWorklistPending(taskList);
        } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
            Log.d(TAG, "searchAll:" + keyword);

            RealmQuery<TaskHistory> query = realm.where(TaskHistory.class);
            RealmResults<TaskHistory> result1;
            List<TaskHistory> taskList = new ArrayList<>();
            List<TaskHistory> taskListTmp = new ArrayList<>();
            TaskHistory task = null;

            // With sorting
//            if (keyword.equalsIgnoreCase("*") || keyword.length() == 0) {
                if (sortAsc) {
                    query.sort("requestDate", Sort.ASCENDING).findAll();
                } else {
                    query.sort("requestDate", Sort.DESCENDING).findAll();
                }
//            } else {
//                if (sortAsc) {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.ASCENDING).findAll();
//                } else {
//                    query.contains("name", keyword, Case.INSENSITIVE)
//                            .sort("requestDate", Sort.DESCENDING).findAll();
//                }
//            }

//        if (!statusList.equalsIgnoreCase(LIST_ALL)) {
            query.equalTo("processStep", statusList);
//        }

            if (!isFiltered && keyword.isEmpty()) {
                result1 = query.findAll();
                Iterator<TaskHistory> iterator = result1.iterator();

                while (iterator.hasNext()) {
                    task = iterator.next();
                    taskList.add(task);
                }
            } else {
                if (!keyword.isEmpty()) {
                    result1 = query.contains("folioNumber", keyword, Case.INSENSITIVE).findAll();
                    Iterator<TaskHistory> iterator = result1.iterator();

                    while (iterator.hasNext()) {
                        task = iterator.next();
                        taskList.add(task);
                    }
                }

                if (isFiltered) {
                    for (String prefix : filter) {
                        if (prefix != null) {
                            taskListTmp.clear();
                            taskListTmp = populateFilterHistory(prefix);
                            taskList.addAll(taskListTmp);
                        }
                    }
                }
            }

            // before combining filter
            Log.d(TAG, "taskList.size():" + taskList.size());

            if ((taskList.size() == 0) && (filter.length > 0)) {
                String strWording = "";
                if (statusList.equalsIgnoreCase(LIST_ALL)) {
                    strWording = "Anda tidak punya data Task List";
                } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
                    strWording = "Anda tidak punya data Pending";
                } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
                    strWording = "Anda tidak punya data Approved";
                } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
                    strWording = "Anda tidak punya data Rejected";
                } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
                    strWording = "Anda tidak punya data Approval History";
                }
                ErrorMessage.errorMessage(getContext(), swiperefresh, strWording);
            }
            loadAddWorklistHistory(taskList);
        }
    }

    private List<Task> populateFilterAll(String prefix) {
        Log.d(TAG, "populateFilter:" + prefix);

        Task task = null;
        List<Task> list = new ArrayList<>();
        RealmQuery<Task> queryFilter = realm.where(Task.class);
        RealmResults<Task> result = queryFilter.equalTo("processStep", statusList)
                .contains("folioNumber", prefix).findAll();
        Iterator<Task> iteratorFilter = result.iterator();

        while (iteratorFilter.hasNext()) {
            task = iteratorFilter.next();
            list.add(task);
        }

        Log.d(TAG, "list.size():" + list.size());

        return list;
    }

    private List<TaskPending> populateFilterPending(String prefix) {
        Log.d(TAG, "populateFilter:" + prefix);

        TaskPending task = null;
        List<TaskPending> list = new ArrayList<>();
        RealmQuery<TaskPending> queryFilter = realm.where(TaskPending.class);
        RealmResults<TaskPending> result = queryFilter.equalTo("processStep", statusList)
                .contains("folioNumber", prefix).findAll();
        Iterator<TaskPending> iteratorFilter = result.iterator();

        while (iteratorFilter.hasNext()) {
            task = iteratorFilter.next();
            list.add(task);
        }

        Log.d(TAG, "list.size():" + list.size());

        return list;
    }

    private List<TaskApproval> populateFilterApproved(String prefix) {
        Log.d(TAG, "populateFilter:" + prefix);

        TaskApproval task = null;
        List<TaskApproval> list = new ArrayList<>();
        RealmQuery<TaskApproval> queryFilter = realm.where(TaskApproval.class);
        RealmResults<TaskApproval> result = queryFilter.equalTo("processStep", statusList)
                .contains("folioNumber", prefix).findAll();
        Iterator<TaskApproval> iteratorFilter = result.iterator();

        while (iteratorFilter.hasNext()) {
            task = iteratorFilter.next();
            list.add(task);
        }

        Log.d(TAG, "list.size():" + list.size());

        return list;
    }

    private List<TaskRejected> populateFilterRejected(String prefix) {
        Log.d(TAG, "populateFilter:" + prefix);

        TaskRejected task = null;
        List<TaskRejected> list = new ArrayList<>();
        RealmQuery<TaskRejected> queryFilter = realm.where(TaskRejected.class);
        RealmResults<TaskRejected> result = queryFilter.equalTo("processStep", statusList)
                .contains("folioNumber", prefix).findAll();
        Iterator<TaskRejected> iteratorFilter = result.iterator();

        while (iteratorFilter.hasNext()) {
            task = iteratorFilter.next();
            list.add(task);
        }

        Log.d(TAG, "list.size():" + list.size());

        return list;
    }

    private List<TaskHistory> populateFilterHistory(String prefix) {
        Log.d(TAG, "populateFilter:" + prefix);

        TaskHistory task = null;
        List<TaskHistory> list = new ArrayList<>();
        RealmQuery<TaskHistory> queryFilter = realm.where(TaskHistory.class);
        RealmResults<TaskHistory> result = queryFilter.equalTo("processStep", statusList)
                .contains("folioNumber", prefix).findAll();
        Iterator<TaskHistory> iteratorFilter = result.iterator();

        while (iteratorFilter.hasNext()) {
            task = iteratorFilter.next();
            list.add(task);
        }

        Log.d(TAG, "list.size():" + list.size());

        return list;
    }

    public void searchRejected(String keyword, String[] filter) {

    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Task item, int position);
        void onListFragmentInteraction(TaskPending item, int position);
        void onListFragmentInteraction(TaskRejected item, int position);
        void onListFragmentInteraction(TaskApproval item, int position);
        void onListFragmentInteraction(TaskHistory item, int position);
    }

    private void getAllWorklist() {
        swiperefresh.setRefreshing(true);
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        restApi.getWorklist("*", "2", "K2Services", "GetWorklist")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getAllWorklist, sip.. " + strResponse);
                                parseXml(strResponse, successListener);
//                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "getAllWorklist, false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                        swiperefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        swiperefresh.setRefreshing(false);
                        Log.d(TAG, "getAllWorklist, onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    private void getWorklistByStatus(final String statusList) {
        Log.d(TAG, "getWorklistByStatus.. " + statusList);

        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        restApi.getWorklistByStatus(statusList,"K2Services", "GetWorkListHistory")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "statusList.. " + statusList);
                                Log.d(TAG, "sip.. " + strResponse);
                                parseXml(strResponse, successListener);
//                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "statusList.. " + statusList);
                            Log.d(TAG, "false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                        swiperefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        swiperefresh.setRefreshing(false);
                        Log.d(TAG, "onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    private void getWorklistHistory() {
        Log.d(TAG, "getWorklistByStatus.. " + statusList);

//        String searchParameterInJson = "[{\"FieldName\":\"FolioNumber\",\"DataType\":\"text\",\"SearchValue\":\"2019\",\"SearchOperator\":\"contains\"}]";

        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        restApi.getWorklistHistory("K2Services", "GetApprovalHistory",
                PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_USERNAME, ""),
                "", "20", "0", "", "AND", "", "2")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "statusList.. " + statusList);
                                Log.d(TAG, "sip.. " + strResponse);
                                parseXml(strResponse, successListener);
//                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "statusList.. " + statusList);
                            Log.d(TAG, "false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                        swiperefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        swiperefresh.setRefreshing(false);
                        Log.d(TAG, "onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    private void getWorklistPending() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        restApi.getWorklistPending("K2Services", "GetPending")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getWorklistPending sip.. " + strResponse);
                                parseXml(strResponse, successListener);
//                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "getWorklistPending false _ " + response.raw().toString());
                            ListWorklistFragment.super.handleError(response);
                        }

                        loading.dismiss();
                        swiperefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            loading.dismiss();
                            swiperefresh.setRefreshing(false);
                            Log.d(TAG, "getWorklistPending onFailure..");
                            ListWorklistFragment.super.showError("Failed connecting to server");
                            t.printStackTrace();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsoArray = jsonObject.getAsJsonArray("records");
        final List<Task> taskList = new ArrayList<>();
        final List<TaskPending> taskListPending = new ArrayList<>();
        final List<TaskApproval> taskListApproved = new ArrayList<>();
        final List<TaskRejected> taskListRejected = new ArrayList<>();
        final List<TaskHistory> taskListHistory = new ArrayList<>();

        for (int i = 0; i < jsoArray.size(); i++) {
            JsonObject jo = jsoArray.get(i).getAsJsonObject();

            String strRecId = jo.get("recid").getAsString();
            int recid = Integer.parseInt(strRecId);
            String folioNum = jo.get("FolioNumber").getAsString();

            // adding dynamic filter
            if (!MyWorklistActivity.filterNew.containsKey(folioNum.split("-")[0])) {
                MyWorklistActivity.filterNew.put(folioNum.split("-")[0], folioNum.split("-")[0]);
            }

            JsonElement jeProcessName = jo.get("ProcessName");
            JsonElement jeActivityName = jo.get("Activity");
            JsonElement jeExecutorADName = jo.get("ExecutorADName");
            JsonElement jeProcessInstanceID = jo.get("ProcessInstanceID");
            JsonElement jeRequesterPERNR = jo.get("RequesterPERNR");
            JsonElement jeSerialNumber = jo.get("SerialNumber");
            JsonElement jeAction = jo.get("Action");

            if (jeProcessName == null) {
                jeProcessName = jo.get("FullName");
            }

            if (jeActivityName == null) {
                jeActivityName = jo.get("LastActivityName");
            }

            if (jeExecutorADName == null) {
                jeExecutorADName = jo.get("CreatedBy");
            }

            if (jeRequesterPERNR == null) {
                Log.d(TAG, "RequesterPERNR = null , folioNum = " + folioNum);
                jeRequesterPERNR = jo.get("RequestorID");
            }

            String strProcessName = jeProcessName.getAsString();

            if (strProcessName.contains("\\")) {
                Log.d(TAG, "strProcessName = " + strProcessName);
                strProcessName = strProcessName.replace("\\", "__");
                String arrStr[] = strProcessName.split("__");

                if (arrStr.length >= 3) {
                    strProcessName = arrStr[2];
                }
            }

            String createdOn = jo.get("CreatedOn").getAsString();
            String activity = jeActivityName.getAsString();
            String processInstanceID = jeProcessInstanceID.getAsString();
            String employeeName = jeExecutorADName.getAsString();
            String serialNumber = jeSerialNumber != null ? jeSerialNumber.getAsString() : "-";
            String action = jeAction != null ? jeAction.getAsString() : "-";

            Karyawan karyawan = new Karyawan();
            karyawan.fullName = employeeName;

            // PARAH response ExecutorPERNR null, response api tidak konsisten
            if (jeRequesterPERNR != null) {
                karyawan.personalNum = jeRequesterPERNR.getAsString();
            }

            if (statusList.equalsIgnoreCase(LIST_ALL)) {
                Task task = new Task(strRecId, false, folioNum, strProcessName, activity);
                task.processInstanceID = processInstanceID;
                task.karyawan = karyawan;
                task.requestDate = StringUtils.toDate(createdOn);
                task.k2SerailNumber = serialNumber;
                task.action = action;
                task.processStep = statusList;

                taskList.add(task);
            } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
                TaskPending task = new TaskPending(strRecId, false, folioNum, strProcessName, activity);
                task.processInstanceID = processInstanceID;
                task.karyawan = karyawan;
                task.requestDate = StringUtils.toDate(createdOn);
                task.k2SerailNumber = serialNumber;
                task.action = action;
                task.processStep = statusList;

                taskListPending.add(task);
            } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
                TaskApproval task = new TaskApproval(strRecId, false, folioNum, strProcessName, activity);
                task.processInstanceID = processInstanceID;
                task.karyawan = karyawan;
                task.requestDate = StringUtils.toDate(createdOn);
                task.k2SerailNumber = serialNumber;
                task.action = action;
                task.processStep = statusList;

                taskListApproved.add(task);
            } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
                TaskRejected task = new TaskRejected(strRecId, false, folioNum, strProcessName, activity);
                task.processInstanceID = processInstanceID;
                task.karyawan = karyawan;
                task.requestDate = StringUtils.toDate(createdOn);
                task.k2SerailNumber = serialNumber;
                task.action = action;
                task.processStep = statusList;

                taskListRejected.add(task);
            } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
                TaskHistory task = new TaskHistory(strRecId, false, folioNum, strProcessName, activity);
                task.processInstanceID = processInstanceID;
                task.karyawan = karyawan;
                task.requestDate = StringUtils.toDate(createdOn);
                task.k2SerailNumber = serialNumber;
                task.action = action;
                task.processStep = statusList;

                taskListHistory.add(task);
            }
        }

        if (statusList.equalsIgnoreCase(LIST_ALL)) {
            adapter = new ListWorklistAdapter(getActivity(), taskList, mListener);

            recyclerView.setAdapter(adapter);

//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.delete(Task.class);
//                    realm.insert(taskList);
//                }
//            });
//
//            // save
//            realm.beginTransaction();
//            realm.insert(taskList);
//            realm.commitTransaction();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Task> rows = realm.where(Task.class).equalTo("processStep", statusList).findAll();
                    rows.deleteAllFromRealm();
                    realm.insert(taskList);
                }
            });

            if ((taskList.size() == 0) && (filter.length > 0)) {
                String strWording = "";
                if (statusList.equalsIgnoreCase(LIST_ALL)) {
                    strWording = "Anda tidak punya data Task List";
                } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
                    strWording = "Anda tidak punya data Pending";
                } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
                    strWording = "Anda tidak punya data Approved";
                } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
                    strWording = "Anda tidak punya data Rejected";
                } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
                    strWording = "Anda tidak punya data Approval History";
                }
                ErrorMessage.errorMessage(getContext(), swiperefresh, strWording);
            }
        } else if (statusList.equalsIgnoreCase(LIST_PENDING)) {
            adapterPending = new ListWorklistPendingAdapter(getActivity(), taskListPending, mListener);

            recyclerView.setAdapter(adapterPending);

//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.delete(TaskPending.class);
//                    realm.insert(taskList);
//                }
//            });
//
//            // save
//            realm.beginTransaction();
//            realm.insert(taskList);
//            realm.commitTransaction();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<TaskPending> rows = realm.where(TaskPending.class).equalTo("processStep", statusList).findAll();
                    rows.deleteAllFromRealm();
                    realm.insert(taskListPending);
                }
            });
        } else if (statusList.equalsIgnoreCase(LIST_APPROVED)) {
            adapterApproved = new ListWorklistApprovedAdapter(getActivity(), taskListApproved, mListener);

            recyclerView.setAdapter(adapterApproved);

//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.delete(TaskApproval.class);
//                    realm.insert(taskList);
//                }
//            });
//
//            // save
//            realm.beginTransaction();
//            realm.insert(taskList);
//            realm.commitTransaction();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<TaskApproval> rows = realm.where(TaskApproval.class).equalTo("processStep", statusList).findAll();
                    rows.deleteAllFromRealm();
                    realm.insert(taskListApproved);
                }
            });
        } else if (statusList.equalsIgnoreCase(LIST_REJECTED)) {
            adapterRejected = new ListWorklistRejectedAdapter(getActivity(), taskListRejected, mListener);

            recyclerView.setAdapter(adapterRejected);

//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.delete(TaskRejected.class);
//                    realm.insert(taskList);
//                }
//            });
//
//            // save
//            realm.beginTransaction();
//            realm.insert(taskList);
//            realm.commitTransaction();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<TaskRejected> rows = realm.where(TaskRejected.class).equalTo("processStep", statusList).findAll();
                    rows.deleteAllFromRealm();
                    realm.insert(taskListRejected);
                }
            });
        } else if (statusList.equalsIgnoreCase(LIST_HISTORY)) {
            adapterHistory = new ListWorklistHistoryAdapter(getActivity(), taskListHistory, mListener);

            recyclerView.setAdapter(adapterHistory);

//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.delete(TaskHistory.class);
//                    realm.insert(taskList);
//                }
//            });
//
//            // save
//            realm.beginTransaction();
//            realm.insert(taskList);
//            realm.commitTransaction();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<TaskHistory> rows = realm.where(TaskHistory.class).equalTo("processStep", statusList).findAll();
                    rows.deleteAllFromRealm();
                    realm.insert(taskListHistory);
                }
            });
        }

        try {
            searchAll(keyword, filter, isFiltered);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
        }
    };

    public String getStatusList() {
        return statusList;
    }
}
