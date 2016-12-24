package com.tachyonlabs.popularmovies;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {
    private static final String TAG = PosterAdapter.class.getSimpleName();
    private static final String POSTERS_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_WIDTH = "w185/";

    private String[] mPosterUrls;

    public PosterAdapter() {

    }

    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        PosterAdapterViewHolder viewHolder = new PosterAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder holder, int position) {
        String posterUrl = POSTERS_BASE_URL + POSTER_WIDTH + mPosterUrls[position];
        Picasso.with(holder.ivPoster.getContext()).load(posterUrl).into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        if (mPosterUrls == null) {
            return 0;
        } else {
            return mPosterUrls.length;
        }
    }

    public void setPosterData(String[] posterUrls) {
        mPosterUrls = posterUrls;
        notifyDataSetChanged();
    }

    class PosterAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView ivPoster;

        PosterAdapterViewHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
        }
    }
}
