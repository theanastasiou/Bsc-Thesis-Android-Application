package com.example.freelancer.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.fragment.Saved;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.Skills;
import com.example.freelancer.rest.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProjectAdapterS extends RecyclerView.Adapter<ProjectAdapterS.ProjectHolder> implements Filterable {
    private List<Project> mDataset;
    private List<Project> mDatasetFull;
    private Context mContext;
    private static ClickListener clickListener;
    private String jwt;
    private Saved act;

    public static class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public TextView date;
        public TextView skills;
        public TextView ownerN;
        public TextView type;
        public TextView budget;
        public TextView payment;
        public ImageButton delete;

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
            delete = (ImageButton) v.findViewById(R.id.imageButton);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        private void setItemDetails(final Project item, final String jwt, final Saved p) {

            if (item == null)
                return;
//            System.out.println("okkk");
            int i = jwt.lastIndexOf('.');
            String withoutSignature = jwt.substring(0, i + 1);
            final Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
                    Call<Void> unsave = restAPI.unsavePrj(jwt,untrusted.getBody().getSubject(),item.getProjectID());
                    unsave.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            p.resume();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }
            });
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
        ProjectAdapterS.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public ProjectAdapterS(Context c, List<Project> myDataset,String j,Saved a) {
        jwt=j;
        act=a;
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
        view = LayoutInflater.from(mContext).inflate(R.layout.project_list_items, parent, false);
        return new ProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
        ((ProjectHolder) holder).setItemDetails(mDataset.get(position),jwt,act);
    }

    public void notif(){
        notifyDataSetChanged();
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
                    if ((item.getCategory() != null && item.getCategory().toLowerCase().contains(filterPattern)) ||
                            (item.getDescription() != null && item.getDescription().toLowerCase().contains(filterPattern)) ||
                            (item.getTitle() != null && item.getTitle().toLowerCase().contains(filterPattern))||
                            skills.toLowerCase().contains(filterPattern)||
                            (item.getCategory() != null && item.getCategory().toLowerCase().contains(filterPattern)))
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