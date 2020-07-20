package com.paradisetechnologies.brithwine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.adapter.VideoAdapter;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.entity.BaseResponseArrayEntity;
import com.paradisetechnologies.brithwine.entity.ClassEntity;
import com.paradisetechnologies.brithwine.entity.SubjectEntity;
import com.paradisetechnologies.brithwine.entity.VideoEntity;
import com.paradisetechnologies.brithwine.network.APIRequestService;
import com.paradisetechnologies.brithwine.network.RetrofitClient;
import com.paradisetechnologies.brithwine.utils.StatMethods;
import com.paradisetechnologies.brithwine.utils.UtilitySharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHome extends AppCompatActivity
{
    private RecyclerView rvVideos;
    private LinearLayout error_layout;
    private Spinner spClass, spSubject;
    private ClassEntity classEntity;
    private SubjectEntity subjectEntity;
    private String token;
    private String selectedClassId, selectedSubjectId;
    private ArrayAdapter<ClassEntity> classAdapter;
    private ArrayAdapter<SubjectEntity> subjectAdapter;
    private VideoAdapter videoAdapter;
    private ArrayList<ClassEntity> classEntityArrayList = new ArrayList<>();
    private ArrayList<SubjectEntity> subjectEntityArrayList = new ArrayList<>();
    private ArrayList<VideoEntity> videoEntityArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        token = UtilitySharedPreferences.getPrefs(this, AppConstants.SHAREDPREFERENCES.USER_AUTH_TOKEN);
        bindViews();
    }

    private void bindViews()
    {
        rvVideos = findViewById(R.id.rvVideos);
        spClass = findViewById(R.id.spClass);
        spSubject = findViewById(R.id.spSubject);
        error_layout = findViewById(R.id.error_layout);

        setLayoutManager();
        getClassList();

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                classEntity = classEntityArrayList.get(position);
                selectedClassId = String.valueOf(classEntity.getId());
                getSubjectList(selectedClassId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        spSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                subjectEntity = subjectEntityArrayList.get(position);
                selectedSubjectId = String.valueOf(subjectEntity.getId());
                getVideoList(selectedClassId, selectedSubjectId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void setLayoutManager()
    {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        rvVideos.setLayoutManager(gridLayoutManager);
    }

    private void getClassList()
    {
        StatMethods.loadingView(this, true);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseArrayEntity<ClassEntity>> call = apiRequestService.getClassList("");
        call.enqueue(new Callback<BaseResponseArrayEntity<ClassEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseArrayEntity<ClassEntity>> call, Response<BaseResponseArrayEntity<ClassEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseArrayEntity<ClassEntity> entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            StatMethods.loadingView(ActivityHome.this, false);
                            classEntityArrayList = entity.getData();
                            if (classEntityArrayList != null && classEntityArrayList.size() > 0)
                            {
                                classAdapter = new ArrayAdapter(ActivityHome.this, R.layout.spinner_item_white, classEntityArrayList);
                                classAdapter.setDropDownViewResource(R.layout.spinner_item_white);
                                spClass.setAdapter(classAdapter);
                            }
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivityHome.this, msgCode);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseArrayEntity<ClassEntity>> call, Throwable t)
            {

            }
        });
    }

    private void getSubjectList(String selectedClassId)
    {
        StatMethods.loadingView(this, true);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseArrayEntity<SubjectEntity>> call = apiRequestService.getSubjectList(selectedClassId);
        call.enqueue(new Callback<BaseResponseArrayEntity<SubjectEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseArrayEntity<SubjectEntity>> call, Response<BaseResponseArrayEntity<SubjectEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseArrayEntity<SubjectEntity> entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            StatMethods.loadingView(ActivityHome.this, false);
                            subjectEntityArrayList = entity.getData();
                            if (subjectEntityArrayList != null && subjectEntityArrayList.size() > 0)
                            {
                                subjectAdapter = new ArrayAdapter(ActivityHome.this, R.layout.spinner_item_white, subjectEntityArrayList);
                                subjectAdapter.setDropDownViewResource(R.layout.spinner_item_white);
                                spSubject.setAdapter(subjectAdapter);
                            }
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivityHome.this, msgCode);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseArrayEntity<SubjectEntity>> call, Throwable t)
            {

            }
        });
    }

    private void getVideoList(String selectedClassId, String selectedSubjectId)
    {
        StatMethods.loadingView(this, true);
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseArrayEntity<VideoEntity>> call = apiRequestService.getVideoList(StatMethods.isToken(this), selectedClassId, selectedSubjectId);
        call.enqueue(new Callback<BaseResponseArrayEntity<VideoEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseArrayEntity<VideoEntity>> call, Response<BaseResponseArrayEntity<VideoEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseArrayEntity<VideoEntity> entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            videoEntityArrayList = entity.getData();

                            if (videoEntityArrayList != null && videoEntityArrayList.size() > 0)
                            {
                                rvVideos.setVisibility(View.VISIBLE);
                                error_layout.setVisibility(View.GONE);

                                videoAdapter = new VideoAdapter(ActivityHome.this, videoEntityArrayList);
                                rvVideos.setAdapter(videoAdapter);
                                videoAdapter.notifyDataSetChanged();
                                StatMethods.loadingView(ActivityHome.this, false);
                            }
                            else
                            {
                                rvVideos.setVisibility(View.GONE);
                                error_layout.setVisibility(View.VISIBLE);
                                StatMethods.loadingView(ActivityHome.this, false);
                            }
                        }
                        else if (status.equals(AppConstants.ERROR))
                        {
                            int msgCode = entity.getMsg_code();
                            StatMethods.showMsgCode(ActivityHome.this, msgCode);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseArrayEntity<VideoEntity>> call, Throwable t)
            {

            }
        });
    }
}