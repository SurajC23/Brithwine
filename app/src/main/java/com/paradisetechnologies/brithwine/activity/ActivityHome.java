package com.paradisetechnologies.brithwine.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.adapter.VideoAdapter;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.entity.BaseResponseArrayEntity;
import com.paradisetechnologies.brithwine.entity.BaseResponseObjectEntity;
import com.paradisetechnologies.brithwine.entity.ClassEntity;
import com.paradisetechnologies.brithwine.entity.SubjectEntity;
import com.paradisetechnologies.brithwine.entity.UserClassEntity;
import com.paradisetechnologies.brithwine.entity.VideoEntity;
import com.paradisetechnologies.brithwine.network.APIRequestService;
import com.paradisetechnologies.brithwine.network.RetrofitClient;
import com.paradisetechnologies.brithwine.interfcae.DownloadClick;
import com.paradisetechnologies.brithwine.interfcae.PlayVideoClick;
import com.paradisetechnologies.brithwine.utils.StatMethods;
import com.paradisetechnologies.brithwine.interfcae.SubscribedNowClicked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHome extends AppCompatActivity implements DownloadClick, PlayVideoClick, SubscribedNowClicked
{
    private RecyclerView rvVideos;
    private LinearLayout error_layout, llTopBar;
    private RelativeLayout rlMenu;
    private Spinner spClass, spSubject;
    private UserClassEntity classEntity;
    private SubjectEntity subjectEntity;
    private String selectedClassId, selectedSubjectId, selectedSubjectName, selectedClassFee, selectedClassName;
    private int selectedVideoID;
    private ArrayAdapter<ClassEntity> classAdapter;
    private ArrayAdapter<SubjectEntity> subjectAdapter;
    private VideoAdapter videoAdapter;
    private ArrayList<UserClassEntity> classEntityArrayList = new ArrayList<>();
    private ArrayList<SubjectEntity> subjectEntityArrayList = new ArrayList<>();
    private ArrayList<VideoEntity> videoEntityArrayList = new ArrayList<>();
    private TextView tvSubscribed;
    private boolean isSubscribe = false;
    private DownloadZipFileTask downloadZipFileTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        verifyToken();
        bindViews();

    }

    private void verifyToken()
    {
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseObjectEntity> call = apiRequestService.verifyToken(StatMethods.isToken(this), "");
        call.enqueue(new Callback<BaseResponseObjectEntity>() {
            @Override
            public void onResponse(Call<BaseResponseObjectEntity> call, Response<BaseResponseObjectEntity> response)
            {
            }

            @Override
            public void onFailure(Call<BaseResponseObjectEntity> call, Throwable t)
            {
            }
        });
    }

    private void bindViews()
    {
        rvVideos = findViewById(R.id.rvVideos);
        rvVideos.setLayoutManager(new GridLayoutManager(this, 2));

        spClass = findViewById(R.id.spClass);
        spSubject = findViewById(R.id.spSubject);
        error_layout = findViewById(R.id.error_layout);
        tvSubscribed = findViewById(R.id.tvSubscribed);
        rlMenu = findViewById(R.id.rlMenu);

        llTopBar = findViewById(R.id.llTopBar);

        getClassList();

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                classEntity = classEntityArrayList.get(position);
                selectedClassId = String.valueOf(classEntity.getId());
                selectedClassFee = classEntity.getClass_fee();
                selectedClassName = classEntity.getClass_name();

                if (classEntity.getIs_subscribed() == 1)
                {
                    isSubscribe = true;
                    tvSubscribed.setText(getString(R.string.subscribe_home));
                }
                else
                {
                    isSubscribe = false;
                    tvSubscribed.setText(getString(R.string.unsubscribe_home));
                }

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
                selectedSubjectName = subjectEntity.getSubject_name();
                getVideoList(selectedClassId, selectedSubjectId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        rlMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ActivityHome.this, ActivityProfile.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        tvSubscribed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (tvSubscribed.getText().equals(getString(R.string.unsubscribe_home)))
                {
                    Intent ii = new Intent(ActivityHome.this, ActivitySubscription.class);
                    ii.putExtra(AppConstants.STRINGS.CLASS_ID, selectedClassId);
                    ii.putExtra(AppConstants.STRINGS.CLASS_FEE, selectedClassFee);
                    ii.putExtra(AppConstants.STRINGS.CLASS_NAME, selectedClassName);
                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(ii);
                }
            }
        });
    }

    private void getClassList()
    {
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<BaseResponseArrayEntity<UserClassEntity>> call = apiRequestService.getUserClassList(StatMethods.isToken(this), "");
        call.enqueue(new Callback<BaseResponseArrayEntity<UserClassEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseArrayEntity<UserClassEntity>> call, Response<BaseResponseArrayEntity<UserClassEntity>> response)
            {
                if (response != null)
                {
                    BaseResponseArrayEntity<UserClassEntity> entity = response.body();
                    if (entity != null)
                    {
                        String status = entity.getStatus();
                        if (status.equals(AppConstants.SUCCESS))
                        {
                            classEntityArrayList = entity.getData();
                            if (classEntityArrayList != null && classEntityArrayList.size() > 0)
                            {
                                classAdapter = new ArrayAdapter(ActivityHome.this, R.layout.spinner_item_black, classEntityArrayList);
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
            public void onFailure(Call<BaseResponseArrayEntity<UserClassEntity>> call, Throwable t)
            {

            }
        });
    }

    private void getSubjectList(String selectedClassId)
    {
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
                            subjectEntityArrayList = entity.getData();
                            if (subjectEntityArrayList != null && subjectEntityArrayList.size() > 0)
                            {
                                subjectAdapter = new ArrayAdapter(ActivityHome.this, R.layout.spinner_item_black, subjectEntityArrayList);
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
        error_layout.setVisibility(View.GONE);
        rvVideos.setVisibility(View.GONE);

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

                                videoAdapter = new VideoAdapter(ActivityHome.this, videoEntityArrayList, isSubscribe, selectedClassId, selectedClassFee, selectedClassName);
                                rvVideos.setAdapter(videoAdapter);
                                videoAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                rvVideos.setVisibility(View.GONE);
                                error_layout.setVisibility(View.VISIBLE);
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

    @Override
    public void dwnloadClicked(String url, int videoID)
    {
        selectedVideoID = videoID;
        final APIRequestService apiRequestService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiRequestService.downloadFileByUrl(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                downloadZipFileTask = new DownloadZipFileTask();
                downloadZipFileTask.execute(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
            }
        });
    }

    @Override
    public void playVideoClicked(String url, int videoID, String title, String thumbnail_path)
    {
        Intent ii = new Intent(ActivityHome.this, ActivityContentDetail.class);
        ii.putExtra(AppConstants.STRINGS.VIDEO_URL, url);
        ii.putExtra(AppConstants.STRINGS.VIDEO_TITLE, title);
        ii.putExtra(AppConstants.STRINGS.VIDEO_IMG, thumbnail_path);
        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(ii);
    }

    @Override
    public void subscribeNowClicked(String selectedClassId, String selectedClassFee, String selectedClassName)
    {
        Intent ii = new Intent(ActivityHome.this, ActivitySubscription.class);
        ii.putExtra(AppConstants.STRINGS.CLASS_ID, selectedClassId);
        ii.putExtra(AppConstants.STRINGS.CLASS_FEE, selectedClassFee);
        ii.putExtra(AppConstants.STRINGS.CLASS_NAME, selectedClassName);
        ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(ii);
    }

    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        @Override
        protected String doInBackground(ResponseBody... urls)
        {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0], selectedSubjectName + "_" + selectedVideoID + "_" + "brightwing.pdf");
            return null;
        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }
    }

    private void saveToDisk(ResponseBody body, String filename) {
        try {
            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.d("TAG", "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                    Log.d("TAG", "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                }

                outputStream.flush();

                Log.d("TAG", destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.d("TAG", "Failed to save the file!");
                return;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "Failed to save the file!");
            return;
        }
    }
}