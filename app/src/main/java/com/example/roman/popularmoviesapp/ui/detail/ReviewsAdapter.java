package com.example.roman.popularmoviesapp.ui.detail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roman.popularmoviesapp.R;
import com.example.roman.popularmoviesapp.data.model.Review;
import com.example.roman.popularmoviesapp.databinding.ReviewRecyclerviewItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Polly on 14.03.2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private final Context mContext;

    private ReviewsAdapter.ReviewsAdapterOnItemClickHandler mClickHandler;

    private List<Review> mReviews;

    public ReviewsAdapter(Context context) {
        this.mContext = context;
    }

    public void setOnItemClickHandler(ReviewsAdapter.ReviewsAdapterOnItemClickHandler onItemClickHandler) {
        this.mClickHandler = onItemClickHandler;
    }


    @Override
    public ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.review_recyclerview_item, viewGroup, false);
        view.setFocusable(true);
        return new ReviewsAdapter.ReviewsAdapterViewHolder(view);    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsAdapterViewHolder holder, int position) {

        Review currentReview = mReviews.get(position);

        holder.binding.reviewTextView.setText(currentReview.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        else return mReviews.size();
    }

    void swapReviews(List<Review> newReviews) {
        mReviews = newReviews;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onReviewItemClick messages.
     */
    public interface ReviewsAdapterOnItemClickHandler {
        void onReviewItemClick(String url);
    }

    class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ReviewRecyclerviewItemBinding binding;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String url = mReviews.get(adapterPosition).getUrl();
            mClickHandler.onReviewItemClick(url);
        }
    }
}