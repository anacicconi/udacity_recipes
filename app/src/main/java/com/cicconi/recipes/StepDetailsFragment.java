package com.cicconi.recipes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment {

    private static final String TAG = StepDetailsFragment.class.getSimpleName();

    private StepDetailsViewModel mViewModel;
    private Step mStep;
    private String mRecipeName;

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;

    private ScrollView mStepLayout;
    private TextView mErrorMessage;
    private TextView mRecipeTitle;
    private TextView mStepInstruction;
    private MaterialButton mPreviousButton;
    private MaterialButton mNextButton;

    private int mStepsCount;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    // Convenience method so the recipe details activity can instantiate this fragment and pass the extra
    // without navigating to the StepDetailsActivity in case of tablets
    static StepDetailsFragment newInstance(Step step, String recipeName, int stepsCount) {
        StepDetailsFragment fragment = new StepDetailsFragment();

        Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA_STEP, step);
        args.putSerializable(Constants.EXTRA_RECIPE_NAME, recipeName);
        args.putSerializable(Constants.EXTRA_STEP_COUNT, stepsCount);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        mStepLayout = rootView.findViewById(R.id.step_layout);
        mErrorMessage = rootView.findViewById(R.id.tv_error_message);
        mStepInstruction = rootView.findViewById(R.id.tv_step_instruction);
        mRecipeTitle = rootView.findViewById(R.id.tv_recipe_title);
        mPlayerView = rootView.findViewById(R.id.pv_step_video);
        mPreviousButton = rootView.findViewById(R.id.btn_previous_step);
        mNextButton = rootView.findViewById(R.id.btn_next_step);

        // If there was a saved state on configuration change use it
        // Otherwise, check if the fragment received something during the creation
        if(savedInstanceState != null) {
            mStep = (Step) savedInstanceState.getSerializable(Constants.EXTRA_STEP);
            mRecipeName = (String) savedInstanceState.getSerializable(Constants.EXTRA_RECIPE_NAME);
            mStepsCount = (int) savedInstanceState.getSerializable(Constants.EXTRA_STEP_COUNT);
        } else if(getArguments() != null) {
            mStep = (Step) getArguments().getSerializable(Constants.EXTRA_STEP);
            mRecipeName = (String) getArguments().getSerializable(Constants.EXTRA_RECIPE_NAME);
            mStepsCount = (int) getArguments().getSerializable(Constants.EXTRA_STEP_COUNT);
        }

        if(null == mStep) {
            showErrorMessage();
        } else {
            setRecipeInfo();
            showStepView();
        }

        //Intent intent = requireActivity().getIntent();
        //if (intent.hasExtra(Constants.EXTRA_STEP)) {
        //    mStep = (Step) intent.getExtras().getSerializable(Constants.EXTRA_STEP);
        //
        //    if(null == mStep) {
        //        showErrorMessage();
        //    } else {
        //        setRecipeInfo(intent);
        //        showStepView();
        //    }
        //}

        return rootView;
    }

    private void showErrorMessage() {
        mStepLayout.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void setRecipeInfo() {
        if(!mRecipeName.isEmpty()) {
            mRecipeTitle.setText(mRecipeName);
        }
    }

    private void showStepView() {
        StepDetailsViewModelFactory factory = new StepDetailsViewModelFactory(requireContext(), mStep);
        mViewModel = new ViewModelProvider(this, factory).get(StepDetailsViewModel.class);

        if(!mStep.description.isEmpty()) {
            mStepInstruction.setText(mStep.description);
        }

        if(!mStep.videoURL.isEmpty()) {
            initializePlayer();
        }

        handlePreviousButton();
        handleNextButton();
    }

    private void handlePreviousButton() {
        // Do not display the previous button if it iis the first step
        if(mStep.stepId == 0) {
            mPreviousButton.setVisibility(View.GONE);
        } else {
            mPreviousButton.setOnClickListener(view -> {
                getPreviousStep();
            });
        }
    }

    private void handleNextButton() {
        // Do not display the next button if it is the last step
        if(mStep.stepId == mStepsCount - 1) {
            mNextButton.setVisibility(View.GONE);
        } else {
            mNextButton.setOnClickListener(view -> {
                getNextStep();
            });
        }
    }

    private void initializePlayer() {
        mPlayerView.setVisibility(View.VISIBLE);

        if (mExoPlayer == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(requireContext());
            mExoPlayer = new SimpleExoPlayer.Builder(requireContext())
                .setTrackSelector(trackSelector)
                .build();
            mPlayerView.setPlayer(mExoPlayer);

            MediaSource videoMediaSource = buildVideoMediaSource(Uri.parse(mStep.videoURL));
            mExoPlayer.prepare(videoMediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private MediaSource buildVideoMediaSource(Uri mp4VideoUri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                requireActivity(), Util.getUserAgent(requireContext(), "Recipes"));

        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mp4VideoUri);
    }

    private void getPreviousStep() {
        mViewModel.getPreviousStep().observe(requireActivity(), new Observer<Step>() {
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
        mViewModel.getNextStep().observe(requireActivity(), new Observer<Step>() {
            @Override
            public void onChanged(Step nextStep) {
                Log.i(TAG, "next step live data changed");
                if (nextStep != null) {
                    navigateToAnotherStep(nextStep);
                }
                // Removing observer because this data won't be updated
                mViewModel.getNextStep().removeObserver(this);
            }
        });
    }

    private void navigateToAnotherStep(Step step) {
        // Release the player before starting another step
        releasePlayer();

        Intent stepDetailsActivityIntent = new Intent(requireContext(), StepDetailsActivity.class);
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_STEP, step);
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_RECIPE_NAME, mRecipeName);
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_STEP_COUNT, mStepsCount);
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
     * Release the player when the fragment is stopped.
     */
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(Constants.EXTRA_STEP, mStep);
        outState.putString(Constants.EXTRA_RECIPE_NAME, mRecipeName);
        outState.putInt(Constants.EXTRA_STEP_COUNT, mStepsCount);
    }
}
