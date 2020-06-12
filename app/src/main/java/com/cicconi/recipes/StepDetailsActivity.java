package com.cicconi.recipes;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.cicconi.recipes.database.Step;
import com.cicconi.recipes.viewmodel.StepDetailsViewModel;
import com.cicconi.recipes.viewmodel.StepDetailsViewModelFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.button.MaterialButton;

public class StepDetailsActivity extends AppCompatActivity {

    private static final String TAG = StepDetailsActivity.class.getSimpleName();

    private StepDetailsViewModel mViewModel;
    private Step mStep;

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;

    ScrollView mStepLayout;
    TextView mErrorMessage;
    TextView mStepInstruction;
    MaterialButton mPreviousButton;
    MaterialButton mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        mStepLayout = findViewById(R.id.step_layout);
        mErrorMessage = findViewById(R.id.tv_error_message);
        mStepInstruction = findViewById(R.id.tv_step_instruction);
        mPlayerView = findViewById(R.id.pv_step_video);
        mPreviousButton = findViewById(R.id.btn_previous_step);
        mNextButton = findViewById(R.id.btn_next_step);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_STEP)) {
            mStep = (Step) intent.getExtras().getSerializable(Constants.EXTRA_STEP);

            if(null == mStep) {
                showErrorMessage();
            } else {
                showStepView();
            }
        }
    }

    private void showErrorMessage() {
        mStepLayout.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showStepView() {
        StepDetailsViewModelFactory factory = new StepDetailsViewModelFactory(this, mStep);
        mViewModel = new ViewModelProvider(this, factory).get(StepDetailsViewModel.class);

        if(!mStep.description.isEmpty()) {
            mStepInstruction.setText(mStep.description);
        }

        if(!mStep.videoURL.isEmpty()) {
            initializePlayer();
        }

        mPreviousButton.setOnClickListener(view -> {
            getPreviousStep();
        });

        mNextButton.setOnClickListener(view -> {
            getNextStep();
        });
    }

    private void initializePlayer() {
        mPlayerView.setVisibility(View.VISIBLE);

        if (mExoPlayer == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(this);
            mExoPlayer = new SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build();
            mPlayerView.setPlayer(mExoPlayer);

            MediaSource videoMediaSource = buildVideoMediaSource(Uri.parse(mStep.videoURL));
            mExoPlayer.prepare(videoMediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private MediaSource buildVideoMediaSource(Uri mp4VideoUri) {
        DataSource.Factory dataSourceFactory =
            new DefaultDataSourceFactory(this, Util.getUserAgent(this, "Recipes"));

        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri);
    }

    private void getPreviousStep() {
        mViewModel.getPreviousStep().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(Step previousStep) {
                Log.i(TAG, "previous step live data changed");
                if (previousStep != null) {
                    navigateToAnotherStep(previousStep);
                }
                // Removing observer because this data won't be updated
                mViewModel.getPreviousStep().removeObserver(this);
            }
        });
    }

    private void getNextStep() {
        mViewModel.getNextStep().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(Step nextStep) {
                Log.i(TAG, "next step live data changed");
                if (nextStep != null) {
                    navigateToAnotherStep(nextStep);
                }
                // Removing observer because this data won't be updated
                mViewModel.getPreviousStep().removeObserver(this);
            }
        });
    }

    private void navigateToAnotherStep(Step step) {
        // Release the player before starting another step
        releasePlayer();

        Intent stepDetailsActivityIntent = new Intent(this, StepDetailsActivity.class);
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_STEP, step);
        startActivity(stepDetailsActivityIntent);
    }

    private void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
