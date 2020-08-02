package com.paradisetechnologies.brithwine.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.configuration.SkinConfig;
import com.longtailvideo.jwplayer.events.CaptionsChangedEvent;
import com.longtailvideo.jwplayer.events.CompleteEvent;
import com.longtailvideo.jwplayer.events.CustomButtonClickEvent;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.PauseEvent;
import com.longtailvideo.jwplayer.events.PlayEvent;
import com.longtailvideo.jwplayer.events.ReadyEvent;
import com.longtailvideo.jwplayer.events.SeekEvent;
import com.longtailvideo.jwplayer.events.SeekedEvent;
import com.longtailvideo.jwplayer.events.TimeEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.paradisetechnologies.brithwine.R;
import com.paradisetechnologies.brithwine.constants.AppConstants;
import com.paradisetechnologies.brithwine.jwplayer.JWEventHandler;
import com.paradisetechnologies.brithwine.jwplayer.KeepScreenOnHandler;

public class ActivityContentDetail extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener, VideoPlayerEvents.OnReadyListener, VideoPlayerEvents.OnPlayListener, VideoPlayerEvents.OnPauseListener, VideoPlayerEvents.OnCompleteListener, VideoPlayerEvents.OnTimeListener, VideoPlayerEvents.OnCaptionsChangedListener, VideoPlayerEvents.OnSeekListener, VideoPlayerEvents.OnSeekedListener, VideoPlayerEvents.OnErrorListener {

    private String url, title, img;
    private JWPlayerView mPlayerView;
    private JWEventHandler mEventHandler;
    private PlaybackState mPlaybackState;
    private ImageView ivBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        url = getIntent().getStringExtra(AppConstants.STRINGS.VIDEO_URL);
        title = getIntent().getStringExtra(AppConstants.STRINGS.VIDEO_TITLE);
        img = getIntent().getStringExtra(AppConstants.STRINGS.VIDEO_IMG);

        mPlayerView = findViewById(R.id.jwplayer);
        ivBackArrow = findViewById(R.id.ivBackArrow);

        mPlayerView.addOnFullscreenListener(this);
        mPlayerView.addOnReadyListener(this);
        mPlayerView.addOnPlayListener(this);
        mPlayerView.addOnPauseListener(this);
        mPlayerView.addOnCompleteListener(this);
        mPlayerView.addOnTimeListener(this);
        mPlayerView.addOnCaptionsChangedListener(this);
        mPlayerView.addOnSeekListener(this);
        mPlayerView.addOnSeekedListener(this);
        mPlayerView.addOnErrorListener(this);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = (screenWidth * 9) / 16;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, screenHeight);
        mPlayerView.setLayoutParams(params);

        new KeepScreenOnHandler(mPlayerView, getWindow());
        mEventHandler = new JWEventHandler(mPlayerView);

        loadPlayer(url, title);

        ivBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mPlayerView != null)
                {
                    mPlayerView.pause();
                    mPlayerView.stop();
                    mPlayerView.onStop();
                    mPlayerView.onDestroy();
                    mPlayerView.destroySurface();
                    onBackPressed();
                }
            }
        });
    }

    private void loadPlayer(String url, String title)
    {
        if (mPlayerView != null)
        {
            mPlayerView.setVisibility(View.VISIBLE);
        }

        SkinConfig skinConfig = new SkinConfig.Builder()
                .name("epic")
                .build();

        PlaylistItem pi = new PlaylistItem.Builder()
                .file(url)
                .title(title)
                .image(img)
                .build();

        PlaylistItem item = new PlaylistItem(url);
        item.setTitle(title);

        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .file(url)
                .autostart(true)
                .displayTitle(false)
                .image(img)
                .stretching(PlayerConfig.STRETCHING_EXACT_FIT)
                .skinConfig(skinConfig)
                .build();

        if (mPlayerView != null)
        {
            mPlayerView.setup(playerConfig);
            mPlayerView.load(item);
        }
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent)
    {

    }

    @Override
    public void onReady(ReadyEvent readyEvent)
    {
        if (mPlayerView != null)
        {
            addButtons();
            mPlayerView.play();
        }
    }

    @Override
    public void onPlay(PlayEvent playEvent)
    {

    }

    @Override
    public void onPause(PauseEvent pauseEvent)
    {

    }

    @Override
    public void onComplete(CompleteEvent completeEvent)
    {

    }

    @Override
    public void onTime(TimeEvent timeEvent)
    {

    }

    @Override
    public void onCaptionsChanged(CaptionsChangedEvent captionsChangedEvent)
    {

    }

    @Override
    public void onSeek(SeekEvent seekEvent)
    {

    }

    @Override
    public void onSeeked(SeekedEvent seekedEvent)
    {

    }

    @Override
    public void onError(ErrorEvent errorEvent)
    {

    }

    private void addButtons()
    {
        mPlayerView.removeButton("jw-ff-btn");
        mPlayerView.removeButton("jw-rw-btn");
        mPlayerView.addButton("https://dev.epicon.in/appassets/forward.png"
                , "Forward", new VideoPlayerEvents.OnCustomButtonClickListener()
                {
                    @Override
                    public void onCustomButtonClick(CustomButtonClickEvent customButtonClickEvent)
                    {
                        mPlayerView.seek(mPlayerView.getPosition() + 10);
                    }
                }, "jw-ff-btn", "jw-ff-btn");

        mPlayerView.addButton("https://dev.epicon.in/appassets/backward.png"
                , "Backward", new VideoPlayerEvents.OnCustomButtonClickListener()
                {
                    @Override
                    public void onCustomButtonClick(CustomButtonClickEvent customButtonClickEvent)
                    {
                        if (mPlayerView.getPosition() > 10)
                        {
                            mPlayerView.seek(mPlayerView.getPosition() - 10);
                        }
                        else
                        {
                            mPlayerView.seek(0);
                        }
                    }
                }, "jw-rw-btn", "jw-rw-btn");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (mPlayerView.getFullscreen())
            {
                mPlayerView.setFullscreen(false, false);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mPlayerView.stop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mPlayerView != null)
        {
            mPlayerView.pause();
            mPlayerView.stop();
            mPlayerView.onStop();
            mPlayerView.onDestroy();
            mPlayerView.destroySurface();
            mPlayerView.removeOnFullscreenListener(this);
            mPlayerView.removeOnReadyListener(this);
            mPlayerView.removeOnPauseListener(this);
            mPlayerView.removeOnPlayListener(this);
            mPlayerView.removeOnCompleteListener(this);
            mPlayerView.removeOnSeekedListener(this);
            mPlayerView.removeOnSeekListener(this);
            mPlayerView.removeOnErrorListener(this);
        }
    }
}