package com.example.freelancer.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.freelancer.R;
import com.example.freelancer.rest.Review;

import java.util.ArrayList;
import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private List<Review> mDataset;
    private List<Review> mDatasetFull;
    private Context mContext;
//    private static ClickListener clickListener;
    public static class ReviewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView content;
        RatingBar rating;
        public TextView sender;
        public ReviewHolder(View v)  {
            super(v);
//            v.setOnClickListener(this);
            title = (TextView) v.findViewById(R.id.tvProjectTitle);
            content = (TextView) v.findViewById(R.id.tvReviewContent);
            sender = (TextView) v.findViewById(R.id.tvSender);
            rating = v.findViewById(R.id.ratingBar);
        }

//        @Override
//        public void onClick(View v) {
//            clickListener.onItemClick(getAdapterPosition(), v);
//        }

        private void setItemDetails(final Review item) {

            if (item == null)
                return;
            System.out.println("dd") ;
            if(item.getSender()!=null){
                System.out.println("Rating");
                title.setText(item.getProjectProjectID().getTitle());
                sender.setText("From: " + item.getSender().getUserName());
                rating.setRating(item.getRate().floatValue());
                content.setText(item.getContent());
            }
        }
    }
    public void insert(List<Review> myD) {
        if(myD==null) {
            System.out.println("EMPTY");
        }
        else {
            mDataset = myD;
            mDatasetFull = new ArrayList<>(myD);
            notifyDataSetChanged();
            System.out.println("NOT EMPTY");
        }
    }


//    public void setOnItemClickListener(ClickListener clickListener) {
//        ReviewAdapter.clickListener = clickListener;
//    }


//    public interface ClickListener {
//        void onItemClick(int position, View v);
//    }

    public ReviewAdapter(Context c, List<Review> myDataset) {
        if(myDataset!=null) {
            mDataset = myDataset;
            mDatasetFull = new ArrayList<>(myDataset);
        }
        else{
            mDataset = myDataset;
            mDatasetFull = new ArrayList<>();
        }

        mContext = c;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.review_adapter, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        ((ReviewHolder) holder).setItemDetails(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        return mDataset.size();
    }
}
