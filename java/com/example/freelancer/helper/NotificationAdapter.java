package com.example.freelancer.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.rest.Notifications;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.Skills;
import com.example.freelancer.rest.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder>
    {
        private List<Notifications> mDataset;
        private List<Notifications> mDatasetFull;
        private Context mContext;

        private static ClickListener clickListener;

        public static class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView title;
            public TextView date;
            public TextView sender;


            public NotificationHolder(View v)  {
                super(v);
                v.setOnClickListener(this);
                title = (TextView) v.findViewById(R.id.tvTitle);
                date= (TextView) v.findViewById(R.id.tvDate);
                sender = (TextView) v.findViewById(R.id.tvSender);
            }

            @Override
            public void onClick(View v) {
                clickListener.onItemClick(getAdapterPosition(), v);
            }

            private void setItemDetails(final Notifications item) {

                if (item == null)
                    return;
                System.out.println("okkk");
                //title.setText(item.getTitle());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date.setText("Posted on: " + sdf.format(item.getDate()).toString());
                System.out.println("dd") ;
                if(item.getApplies()!=null){
                    System.out.println("ApplyNotification");
                    title.setText("New Apply for a Project");
                    sender.setText("From: " + item.getApplies().getUser().getUserName());
                }
                else if (item.getChatMessageID()!=null){
                    System.out.println("MessageNotification");
                    title.setText("New Message");
                    sender.setText("From: " + item.getChatMessageID().getUserSend().getUserName());
                }
                else if(item.getReviewReviewID()!=null){
                    System.out.println("ReviewNotification");
                    title.setText("New Review");
                    sender.setText("From: " + item.getReviewReviewID().getSender().getUserName());
                }
                if(item.isSeen())
                    title.setTypeface(null, Typeface.NORMAL);
            }
        }
        public void insert(List<Notifications> myD) {
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


        public void setOnItemClickListener(ClickListener clickListener) {
            NotificationAdapter.clickListener = clickListener;
        }


        public interface ClickListener {
            void onItemClick(int position, View v);
        }

        public NotificationAdapter(Context c, List<Notifications> myDataset) {
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
        public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(mContext).inflate(R.layout.notification_adapter, parent, false);
            return new NotificationAdapter.NotificationHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationHolder holder, int position) {
            ((NotificationHolder) holder).setItemDetails(mDataset.get(position));
        }

        @Override
        public int getItemCount() {
            if (mDataset == null)
                return 0;
            return mDataset.size();
        }
    }
