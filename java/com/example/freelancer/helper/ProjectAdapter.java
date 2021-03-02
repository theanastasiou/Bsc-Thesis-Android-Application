package com.example.freelancer.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.Skills;
import com.example.freelancer.rest.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> implements Filterable {
    private List<Project> mDataset;
    private List<Project> mDatasetFull;
    private Context mContext;

    private static ClickListener clickListener;

    public static class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public TextView date;
        public TextView skills;
        public TextView ownerN;
        public TextView type;
        public TextView budget;
        public TextView payment;

        public ProjectHolder(@NonNull View v) {
            super(v);
            v.setOnClickListener(this);
            title = (TextView) v.findViewById(R.id.Title);
            date= (TextView) v.findViewById(R.id.PostedDate);
            skills = (TextView) v.findViewById(R.id.Skills);
            ownerN = (TextView) v.findViewById(R.id.OwnerName);
            budget = (TextView) v.findViewById(R.id.Budget);
            payment = (TextView) v.findViewById(R.id.PaymentType);
            type = (TextView) v.findViewById(R.id.Type);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        private void setItemDetails(final Project item) {

            if (item == null)
                return;
//            System.out.println("okkk");
            title.setText(item.getTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date.setText("Posted on: " + sdf.format(item.getDate()).toString());
            budget.setText(String.valueOf(item.getBudget()) + " " + item.getCoin());
            payment.setText("Payment Type: " + item.getPaymentType());
            switch (item.getCategory()){
                case "0":
                    type.setText("Category : "+"Website & IT");
                    break;
                case "1":
                    type.setText("Category : "+"Mobile");
                    break;
                case "2":
                    type.setText("Category : "+"Art & Design");
                    break;
                case "3":
                    type.setText("Category : "+"Data Entry");
                    break;
                case "4":
                    type.setText("Category : "+"Software Dev");
                    break;
                case "5":
                    type.setText("Category : "+"Writing");
                    break;
                case "6":
                    type.setText("Category : "+"Business");
                    break;
                case "7":
                    type.setText("Category : "+"Sales");
                    break;
                default:
                    break;
            }

            String sk = "";
//            System.out.println("ff");
            if(item.getSkillsCollection()!=null)
                for (Skills s : item.getSkillsCollection())
                    sk += s.getDescription() + " ";
            skills.setText(sk);
//            System.out.println("dd") ;


//            id=item.getId();
//            title.setText(item.getName());
//            String tprice;
//            if (item.getCurrentPrice() != null)
//                tprice = "FROM:\n" + item.getCurrentPrice() + " EUR";
//            else
//                tprice = "FROM:\n" + item.getFirstBid() + " EUR";
//            price.setText(tprice);
//            String tends;
//            if (item.getEndDate() != null)
//                tends = "UNTIL:\n" + item.getEndDate().toString();
//            else
//                tends = "Not yet started";
//            ends.setText(tends);
            RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
            Call<User> call = restAPI.fetchUser(item.getUserIDOwner().getUserID());
//            System.out.println("failure") ;

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
//                    System.out.println("TRYIN TO FETCH USER WITH ID "+response.body().getUserID());
//                    System.out.println("TRYIN TO FETCH USER WITH ID "+response.body().getUserName());
                    ownerN.setText(response.body().getName()+" "+response.body().getSurname());

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    System.out.println("failure") ;
                }
            });
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    if (response.body() != null) {
//                        byte[] encodeByte = Base64.decode(response.body(), Base64.DEFAULT);
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                        image.setImageBitmap(bitmap);
//                    } else {
//                        System.out.println(item.getId() + " IMAGE IS NULL");
//                        image.setImageResource(R.drawable.no_image_available);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    System.out.println("FAILURE ID id " + item.getId());
//                    System.out.println(t.getMessage());
//                    System.out.println(t.fillInStackTrace());
//                }
//            });
        }
    }

    public void insert(List<Project> myD) {
        if(myD==null) {
            System.out.println("EMPTY");
        }
        else {
            mDataset = myD;
            mDatasetFull = new ArrayList<>(myD);
            notifyDataSetChanged();
//            System.out.println("NOT EMPTY");
        }
    }


    public void setOnItemClickListener(ClickListener clickListener) {
        ProjectAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public ProjectAdapter(Context c, List<Project> myDataset) {
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

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.project_list_item, parent, false);
        return new ProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
        ((ProjectHolder) holder).setItemDetails(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        return mDataset.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Project> filteredList = new ArrayList<>();
            System.out.println(charSequence);
            if (charSequence==null || charSequence.length()==0){
                filteredList.addAll(mDatasetFull);
            }
            else{
                String filterPattern = charSequence.toString();
                System.out.println(filterPattern +" is fp");

                for(Project item:mDatasetFull) {
                    String skills = "";
                    if(item.getSkillsCollection()!=null)
                        for (Skills s:item.getSkillsCollection())
                            skills+= s.getDescription()+" ";
                    else
                        System.out.println("SKILLS FOUND!!!!!!!!!!!!!!!!");
                    String cat = null;
                    switch (item.getCategory()){
                        case "0":
                            cat = "Category : "+"Website & IT";
                            break;
                        case "1":
                            cat="Category : "+"Mobile";
                            break;
                        case "2":
                            cat = "Category : "+"Art & Design";
                            break;
                        case "3":
                            cat = "Category : "+"Data Entry";
                            break;
                        case "4":
                            cat = "Category : "+"Software Dev";
                            break;
                        case "5":
                            cat = "Category : "+"Writing";
                            break;
                        case "6":
                            cat = "Category : "+"Business";
                            break;
                        case "7":
                            cat = "Category : "+"Sales";
                            break;
                        default:
                            break;
                    }

                    if ((item.getCategory() != null && item.getCategory().toLowerCase().contains(filterPattern)) ||
                            (item.getDescription() != null && item.getDescription().toLowerCase().contains(filterPattern)) ||
                            (item.getTitle() != null && item.getTitle().toLowerCase().contains(filterPattern))||
                            skills.toLowerCase().contains(filterPattern)||
                            (cat != null && cat.toLowerCase().contains(filterPattern)))
                        filteredList.add(item);
                }
            }
            FilterResults results = new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mDataset.clear();
            List<Project> list = (List<Project>) filterResults.values;
            if(list==null)
                return;
            System.out.println(list.size());
            if(list.size()>0)
                mDataset.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };


}