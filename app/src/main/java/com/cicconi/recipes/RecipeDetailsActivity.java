package com.cicconi.recipes;

import android.content.Intent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cicconi.recipes.database.Recipe;
import com.cicconi.recipes.viewmodel.DetailsViewModel;

public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    private DetailsViewModel mViewModel;

    private Recipe mRecipe;

    ScrollView mRecipeLayout;
    TextView mErrorMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mRecipeLayout = findViewById(R.id.recipe_layout);
        mErrorMessage = findViewById(R.id.tv_error_message);

        Intent intent = getIntent();
            if (intent.hasExtra(Constants.EXTRA_RECIPE)) {
                mRecipe = (Recipe) intent.getExtras().getSerializable(Constants.EXTRA_RECIPE);

                if(null == mRecipe) {
                    showErrorMessage();
                } else {
                    showRecipeView();
                }
            }
    }

    private void showErrorMessage() {
        mRecipeLayout.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showRecipeView() {
        //DetailsViewModelFactory factory = new DetailsViewModelFactory(this, movie);
        //viewModel = new ViewModelProvider(this, factory).get(DetailsViewModel.class);

        loadRecipe(mRecipe);
    }

    private void loadRecipe(Recipe mRecipe) {

    }

    //private static final String TAG = RecipeDetailsActivity.class.getSimpleName();
    //
    //private TextView mTitle;
    //private TextView mSynopsis;
    //private TextView mReleaseDate;
    //private TextView mRating;
    //private ImageView mThumbnail;
    //private TextView mVideosLabel;
    //private TextView mReviewsLabel;
    //private ImageView mFavoriteIcon;
    //ScrollView mMovieLayout;
    //TextView mErrorMessage;
    //
    //private DetailsViewModel viewModel;
    //
    //private Movie movie;
    //
    //private CompositeDisposable compositeDisposable;
    //
    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_details);
    //
    //    mTitle = findViewById(R.id.tv_title);
    //    mSynopsis = findViewById(R.id.tv_synopsis);
    //    mReleaseDate = findViewById(R.id.tv_release_date);
    //    mRating = findViewById(R.id.tv_rating);
    //    mThumbnail = findViewById(R.id.iv_thumbnail);
    //    mVideosLabel = findViewById(R.id.tv_videos);
    //    mReviewsLabel = findViewById(R.id.tv_reviews);
    //    mFavoriteIcon = findViewById(R.id.iv_favorite);
    //    mMovieLayout = findViewById(R.id.movie_layout);
    //    mErrorMessage = findViewById(R.id.tv_error_message);
    //
    //    compositeDisposable = new CompositeDisposable();
    //
    //    Intent intent = getIntent();
    //    if (intent.hasExtra(Constants.EXTRA_MOVIE)) {
    //        movie = (Movie) intent.getExtras().getSerializable(Constants.EXTRA_MOVIE);
    //
    //        if(null == movie) {
    //            showErrorMessage();
    //        } else {
    //            showMovieView();
    //        }
    //    }
    //
    //}
    //
    //private void showErrorMessage() {
    //    mMovieLayout.setVisibility(View.GONE);
    //    mErrorMessage.setVisibility(View.VISIBLE);
    //}
    //
    //private void showMovieView() {
    //    DetailsViewModelFactory factory = new DetailsViewModelFactory(this, movie);
    //    viewModel = new ViewModelProvider(this, factory).get(DetailsViewModel.class);
    //
    //    loadMovie(movie);
    //    loadVideos();
    //    loadReviews();
    //    loadFavoriteIcon();
    //}
    //
    //private void loadMovie(Movie movie) {
    //    String title = movie.getTitle();
    //    String overview = movie.getOverview();
    //    String releaseDate = movie.getReleaseDate();
    //    Double voteAverage = movie.getVoteAverage();
    //
    //    if(null != title) {
    //        mTitle.setText(title);
    //    } else {
    //        mTitle.setText(R.string.unknown);
    //    }
    //
    //    if(null != overview) {
    //        mSynopsis.setText(overview);
    //    } else {
    //        mSynopsis.setText(R.string.unknown);
    //    }
    //
    //    if(null != releaseDate) {
    //        mReleaseDate.setText(releaseDate);
    //    } else {
    //        mReleaseDate.setText(R.string.unknown);
    //    }
    //
    //    if(null != voteAverage) {
    //        mRating.setText(String.valueOf(voteAverage));
    //    } else {
    //        mRating.setText(R.string.no_rating);
    //    }
    //
    //    Picasso.with(this)
    //        .load(Constants.IMAGE_URL + movie.getPosterPath())
    //        .placeholder(R.drawable.placeholder)
    //        .error(R.drawable.placeholder)
    //        .into(mThumbnail);
    //}
    //
    //private void loadVideos() {
    //    RecyclerView mVideoRecyclerView = findViewById(R.id.recyclerview_videos);
    //    LinearLayoutManager videoLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    //    mVideoRecyclerView.setLayoutManager(videoLayoutManager);
    //    mVideoRecyclerView.setHasFixedSize(true);
    //
    //    RecipeAdapter mVideoAdapter = new RecipeAdapter(this);
    //    mVideoRecyclerView.setAdapter(mVideoAdapter);
    //
    //    viewModel.getVideos().observe(this, new Observer<List<Video>>() {
    //        @Override
    //        public void onChanged(List<Video> videos) {
    //            Log.i(TAG, "video live data changed");
    //            if (!videos.isEmpty()) {
    //                mVideosLabel.setVisibility(View.VISIBLE);
    //                mVideoAdapter.setVideoData(videos);
    //            }
    //
    //            // Removing observer because this data won't be updated
    //            viewModel.getVideos().removeObserver(this);
    //        }
    //    });
    //}
    //
    //private void loadReviews() {
    //    RecyclerView mReviewRecyclerView = findViewById(R.id.recyclerview_reviews);
    //    LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    //    mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
    //    mReviewRecyclerView.setHasFixedSize(true);
    //
    //    ReviewAdapter mReviewAdapter = new ReviewAdapter();
    //    mReviewRecyclerView.setAdapter(mReviewAdapter);
    //
    //    viewModel.getReviews().observe(this, new Observer<List<Review>>() {
    //        @Override
    //        public void onChanged(List<Review> reviews) {
    //            Log.i(TAG, "video live data changed");
    //            if (!reviews.isEmpty()) {
    //                mReviewsLabel.setVisibility(View.VISIBLE);
    //                mReviewAdapter.setReviewData(reviews);
    //            }
    //
    //            // Removing observer because this data won't be updated
    //            viewModel.getReviews().removeObserver(this);
    //        }
    //    });
    //}
    //
    //@Override
    //public void onVideoClick(String videoKey) {
    //    String url = Constants.YOUTUBE_URL + videoKey;
    //    Uri webPage = Uri.parse(url);
    //
    //    Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
    //
    //    if(intent.resolveActivity(getPackageManager()) != null) {
    //        startActivity(intent);
    //    }
    //}
    //
    //private void loadFavoriteIcon() {
    //    viewModel.getIsFavoriteMovie().observe(this, isFavorite -> {
    //        Log.i(TAG, "isFavorite live data changed: " + isFavorite);
    //        if(isFavorite){
    //            mFavoriteIcon.setColorFilter(getResources().getColor(R.color.colorFavorite));
    //        }
    //
    //        onFavoriteIconClick(isFavorite);
    //    });
    //}
    //
    //private void onFavoriteIconClick(Boolean isFavorite) {
    //    mFavoriteIcon.setOnClickListener(view -> {
    //        if(isFavorite) {
    //            removeMovieFromFavorites();
    //        } else {
    //            addMovieToFavorites();
    //        }
    //    });
    //}
    //
    //private void addMovieToFavorites() {
    //    Disposable disposable = viewModel.onMovieAddedToFavorites()
    //        .observeOn(AndroidSchedulers.mainThread())
    //        .doOnError(e -> {
    //            e.printStackTrace();
    //            Toast.makeText(RecipeDetailsActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
    //        })
    //        .subscribe(
    //            () -> {
    //                mFavoriteIcon.setColorFilter(getResources().getColor(R.color.colorFavorite));
    //                Toast.makeText(RecipeDetailsActivity.this, "The movie was added to favorites", Toast.LENGTH_SHORT).show();
    //            },
    //            Throwable::printStackTrace
    //        );
    //
    //    compositeDisposable.add(disposable);
    //}
    //
    //private void removeMovieFromFavorites() {
    //    Disposable disposable = viewModel.onMovieRemovedFromFavorites()
    //        .observeOn(AndroidSchedulers.mainThread())
    //        .doOnError(e -> {
    //            e.printStackTrace();
    //            Toast.makeText(RecipeDetailsActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
    //        })
    //        .subscribe(
    //            () -> {
    //                mFavoriteIcon.setColorFilter(getResources().getColor(R.color.colorSecondaryText));
    //                Toast.makeText(RecipeDetailsActivity.this, "The movie was removed from favorites", Toast.LENGTH_SHORT).show();
    //            },
    //            Throwable::printStackTrace
    //        );
    //
    //    compositeDisposable.add(disposable);
    //}
    //
    //@Override
    //protected void onDestroy() {
    //    super.onDestroy();
    //    compositeDisposable.dispose();
    //}
}
