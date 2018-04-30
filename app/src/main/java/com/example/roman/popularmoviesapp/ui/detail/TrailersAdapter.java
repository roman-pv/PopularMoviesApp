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
import com.example.roman.popularmoviesapp.data.model.Video;
import com.example.roman.popularmoviesapp.databinding.ReviewRecyclerviewItemBinding;
import com.example.roman.popularmoviesapp.databinding.TrailerRecyclerviewItemBinding;
import com.example.roman.popularmoviesapp.utilities.MovieUtilitites;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Polly on 14.03.2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private final Context mContext;

    private Picasso mPicasso;

    private TrailersAdapter.TrailersAdapterOnItemClickHandler mClickHandler;

    private List<Video> mVideos;

    public TrailersAdapter(Context context, Picasso picasso) {
        this.mContext = context;
        this.mPicasso = picasso;
    }

    public void setOnItemClickHandler(TrailersAdapter.TrailersAdapterOnItemClickHandler onItemClickHandler) {
        this.mClickHandler = onItemClickHandler;
    }


    @Override
    public TrailersAdapter.TrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.trailer_recyclerview_item, viewGroup, false);
        view.setFocusable(true);
        return new TrailersAdapter.TrailersAdapterViewHolder(view);    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailersAdapterViewHolder holder, int position) {

        Video currentVideo = mVideos.get(position);

        String ThumbnailImagePath = MovieUtilitites.getTrailerThumbnailImagePath(currentVideo.getKey());
        mPicasso.with(mContext)
                .load(ThumbnailImagePath)
                .placeholder(R.drawable.ic_movies_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(holder.binding.trailerImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mVideos) return 0;
        else return mVideos.size();
    }

    void swapVideos(List<Video> newVideos) {
        mVideos = newVideos;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onReviewItemClick messages.
     */
    public interface TrailersAdapterOnItemClickHandler {
        void onTrailerItemClick(String key);
    }

    class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TrailerRecyclerviewItemBinding binding;

        public TrailersAdapterViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String key = mVideos.get(adapterPosition).getKey();
            mClickHandler.onTrailerItemClick(key);
        }
    }
}