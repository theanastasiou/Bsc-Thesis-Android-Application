package com.example.freelancer.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.rest.Chat;
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

enum TYPE {
    SEND,
    RECEIVE
}

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder>
{
    private List<Chat> mDataset;
    private Context mContext;
    private TYPE itemType;
    private String username;
//    private static ClickListener clickListener;

    public static class MessageHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        public TextView message;
        public TextView date;
//        public TextView sender;

        public MessageHolder(View v)  {
            super(v);
//            v.setOnClickListener(this);
            message = (TextView) v.findViewById(R.id.message_body);
            date= (TextView) v.findViewById(R.id.datetime);
//            sender = (TextView) v.findViewById(R.id.tvSender);
        }

//        @Override
//        public void onClick(View v) {
//            clickListener.onItemClick(getAdapterPosition(), v);
//        }

        private void setItemDetails(final Chat item) {

            if (item == null)
                return;
            message.setText(item.getContent());
//            System.out.println("okkk");
//            //title.setText(item.getTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date.setText( sdf.format(item.getDate()).toString());
//            System.out.println("dd") ;
//            if(item.getApplies()!=null){
//                System.out.println("ApplyNotification");
//                title.setText("New Apply for a Project");
//                sender.setText("From: " + item.getApplies().getUser().getUserName());
//            }
//            else if (item.getChatMessageID()!=null){
//                System.out.println("MessageNotification");
//                title.setText("New Message");
//                sender.setText("From: " + item.getChatMessageID().getUserSend().getUserName());
//            }
//            else if(item.getReviewReviewID()!=null){
//                System.out.println("ReviewNotification");
//                title.setText("New Review");
//                sender.setText("From: " + item.getReviewReviewID().getSender().getUserName());
//            }
        }
    }

    public void insert(List<Chat> myD) {
        if(myD==null) {
            System.out.println("EMPTY");
        }
        else {
            mDataset = myD;
            notifyDataSetChanged();
            System.out.println("NOT EMPTY");
        }
    }


//    public void setOnItemClickListener(ClickListener clickListener) {
//        MessagesAdapter.clickListener = clickListener;
//    }


//    public interface ClickListener {
//        void onItemClick(int position, View v);
//    }

    public MessagesAdapter(Context c, List<Chat> myDataset,String uname) {
        if(myDataset!=null) {
            mDataset = myDataset;
        }
        else{
            mDataset = myDataset;
        }
        this.username=uname;
        mContext = c;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        if ((mDataset.get(position) != null) && (mDataset.get(position).getUserSend().getUserName().equals(username)))
            return 0;
        else
            return 1;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 0:
                return new MessagesAdapter.MessageHolder(inflater.inflate(R.layout.mymessage, parent, false));
            case 1:
                return new MessagesAdapter.MessageHolder(inflater.inflate(R.layout.theirmessage, parent, false));
            default:
//                return new NotificationsAdapter.UserNotificationViewHolder(inflater.inflate(R.layout.row_layout_user_notification, parent, false));
        }
        //Toast.makeText(mContext,"Neither one",Toast.LENGTH_SHORT).show();
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.notification_adapter, parent, false);
        return new MessagesAdapter.MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        ((MessageHolder) holder).setItemDetails(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        return mDataset.size();
    }
}