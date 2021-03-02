package com.example.freelancer.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.helper.ReviewAdapter;
import com.example.freelancer.rest.Applies;
import com.example.freelancer.rest.AppliesPK;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.Review;
import com.example.freelancer.rest.Skills;
import com.example.freelancer.rest.User;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_LOAD_IMAGE = 1;

    // TODO: Rename and change types of parameters
    boolean changedPic=false;
    private String mParam1;
    private String mParam2;
    String imgStr = null;
    int userid;
    EditText edEmail;
    EditText edAbout;
    EditText edSkills;
    EditText edPhoneNumber;
    TextView edUserName;
    EditText edDateofbirth;
    TextView edRating;
    TextView tvname;
    TextView tvBUdget;
    RatingBar ratingBar;
    TextView tvsurname;
    Date date11;
    String date1;
    String DD;
    EditText edCountry;
    String jwt;
    ImageView image;
    Button btnUpload;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Jwt<Header, Claims> untrusted;
    private OnFragmentInteractionListener mListener;
    ReviewAdapter adapter;

    public PhotosFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;
        System.out.println(jwt);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        final View view = getView();
        final RecyclerView listView = (RecyclerView) getView().findViewById(R.id.rev);
        final List<Review> list = null;
        adapter = new ReviewAdapter(view.getContext(), list);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL);
        listView.addItemDecoration(mDividerItemDecoration);
        System.out.println("calling fun");
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        Call<List<Review>> call = restAPI.fetchrev(untrusted.getBody().getSubject());
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, final Response<List<Review>> response) {
                ReviewAdapter adapter = (ReviewAdapter) listView.getAdapter();
                System.out.println("WWWWWWWWWWWWWWWWW");
                adapter.insert(response.body());
                System.out.println(adapter.getItemCount()+ " is size");
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<User> user = restAPI.findUsername(untrusted.getBody().getSubject());
        user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User myuser = response.body();
                System.out.println("UUUU ");
                System.out.println("USERNAME " + myuser.getUserName());
                System.out.println("name  " + myuser.getName());
                System.out.println("surname " + myuser.getSurname());
                System.out.println("country " + myuser.getCountry());
                System.out.println("phone " + myuser.getPhoneNumber());
                userid = Integer.valueOf(response.body().getUserID());
                edUserName.setText("Username: "+myuser.getUserName());
                tvname.setText("Name: "+myuser.getName());
                tvsurname.setText("Surname: "+myuser.getSurname());
                edEmail.setText(myuser.getEmail());
                edAbout.setText(myuser.getAbout());
                Integer phonenumber = myuser.getPhoneNumber();
                Double avgrating = myuser.getAverageRating();
                System.out.println(avgrating.toString());
                System.out.println(phonenumber);

                double rat = (myuser.getAverageRating());
                float rt = (float)rat;
                System.out.println("RATING:" +rt);
                ratingBar.setIsIndicator(true);
                ratingBar.setRating(rt);

                Date dateofbirth =  myuser.getDateOfBirth();
                System.out.println(dateofbirth.toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                edDateofbirth.setText(sdf.format(dateofbirth));
                tvBUdget.setText("Earnings: "+String.valueOf(myuser.getEarnings()));
                edPhoneNumber.setText(phonenumber.toString());
               // edRating.setText("Rating: "+avgrating.toString());
                String skills="";
                if(!myuser.getSkillsCollection().isEmpty()) {
                    for (Skills s : myuser.getSkillsCollection())
                        skills += s.getDescription() + ",";
                    skills = skills.substring(0, skills.length() - 1);
                }
                edSkills.setText(skills);
                Call<String> fPhoto = restAPI.getPhoto(myuser.getUserID());
                System.out.println("EDWWWWWWWWWWWW!!!!!!!!@@@");
                fPhoto.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body()==null) {
                            System.out.println("EMPTYY");
                            image.setImageResource(R.drawable.glyph28512);
                        }
                        else{
                            System.out.println("NOT EMPTYY");
                            byte[] encodeByte = Base64.decode(response.body(), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            image.setImageBitmap(bitmap);
                        }

                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });

                System.out.println(skills);
                edCountry.setText(myuser.getCountry());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
        Button secondViewBtn = (Button) v.findViewById(R.id.btnEdit);
        secondViewBtn.setOnClickListener(this);
        return v;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        edCountry=getView().findViewById(R.id.edCountry);
        edCountry.setEnabled(false);
        ratingBar=getView().findViewById(R.id.ratingBar2);
        tvname=getView().findViewById(R.id.tvName);
        tvsurname=getView().findViewById(R.id.tvSurname);
        edEmail=(EditText)getView().findViewById(R.id.edEmail);
        edEmail.setEnabled(false);
        edAbout=(EditText)getView().findViewById(R.id.edAbout);
        edAbout.setEnabled(false);
        edDateofbirth=(EditText)getView().findViewById(R.id.edDateOfBirth);
        edDateofbirth.setEnabled(false);
        edPhoneNumber=(EditText)getView().findViewById(R.id.edPhoneNumber);
        edPhoneNumber.setEnabled(false);
        edSkills=(EditText)getView().findViewById(R.id.edSkills);
        edSkills.setEnabled(false);
        tvBUdget=getView().findViewById(R.id.tvBudget);
        edUserName=getView().findViewById(R.id.tvUsername);
        image = getView().findViewById(R.id.imageViewU);
        image.setImageResource(R.drawable.glyph28512);
        btnUpload = getView().findViewById(R.id.btnUploadImage);
        btnUpload.setEnabled(false);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            }
        });

//        edUserName.setEnabled(false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!= null ){
            Uri selectedImage = data.getData();
            image.setImageURI(selectedImage);
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) image.getDrawable());
            Bitmap bitmap;
            bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            String imageInByte = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
//                            System.out.println("!!!!!!!!!! "+imageInByte+" ______");
            imgStr = imageInByte;
        }
    }

    public void onClick(final View view) {
        btnUpload.setEnabled(true);
        tvname=getView().findViewById(R.id.tvName);
        tvsurname=getView().findViewById(R.id.tvSurname);
        tvBUdget=getView().findViewById(R.id.tvBudget);
        edEmail=(EditText)getView().findViewById(R.id.edEmail);
        edEmail.setEnabled(true);
        edAbout=(EditText)getView().findViewById(R.id.edAbout);
        edAbout.setEnabled(true);
        edDateofbirth= (EditText) getView().findViewById(R.id.edDateOfBirth);
        edDateofbirth.setEnabled(true);
        Toast.makeText(getContext(),edDateofbirth.getText().toString(),Toast.LENGTH_SHORT).show();
        ratingBar = getView().findViewById(R.id.ratingBar2);
        edDateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                System.out.println("Month " + (datePicker.getMonth()+ 1));
                date1 = year + "-" + month + "-" + day ;
                edDateofbirth.setText(date1);

            }
        };
        edPhoneNumber=(EditText)getView().findViewById(R.id.edPhoneNumber);
        edPhoneNumber.setEnabled(true);
        edSkills=(EditText)getView().findViewById(R.id.edSkills);
        edSkills.setEnabled(true);
        edUserName=getView().findViewById(R.id.tvUsername);
        edUserName.setEnabled(false);
        edCountry=getView().findViewById(R.id.edCountry);
        edCountry.setEnabled(true);
        Button button = (Button)getView().findViewById(R.id.btnEdit);
        button.setText("Save");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                btnUpload.setEnabled(false);
                final RestAPI restAPII = RestClient.getStringClient().create(RestAPI.class);
                Call<User> user = restAPII.findUsername(untrusted.getBody().getSubject());
                user.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        response.body().setAbout(edAbout.getText().toString());
                        response.body().setUserName(untrusted.getBody().getSubject());
                        response.body().setPhoneNumber(Integer.valueOf((edPhoneNumber.getText().toString())));
                        response.body().setCountry(edCountry.getText().toString());
                        response.body().setEmail(edEmail.getText().toString());
//                        if(changedPic){
//                            BitmapDrawable bitmapDrawable = ((BitmapDrawable) image.getDrawable());
//                            Bitmap bitmap;
//                            bitmap = bitmapDrawable.getBitmap();
//                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                            String imageInByte = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
////                            System.out.println("!!!!!!!!!! "+imageInByte+" ______");
//                            imgStr = imageInByte;
////                            changedPic=false;
//                        }
//                        response.body().setPhoto(null);
                        StringTokenizer multiTokenizer = new StringTokenizer(edSkills.getText().toString(), ",");

                        Collection<Skills> userskills=new ArrayList<>();
                        int j=100;
                        while (multiTokenizer.hasMoreTokens())
                        {
                            Skills newskill = new Skills();
                            newskill.setSkillID(j++);
                            newskill.setDescription(multiTokenizer.nextToken().trim());
                            userskills.add(newskill);

                        }
                        response.body().setSkillsCollection(userskills);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        DD=edDateofbirth.getText().toString();
                        System.out.println("DATE" + date1);
                        try {
                            formatter.setLenient(false);
                            date11 = formatter.parse(DD);
                            Toast.makeText(getContext(),"TRY",Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date11 = new Date(edDateofbirth.getText().toString());
                            Toast.makeText(getContext(),"TRY CATCH",Toast.LENGTH_SHORT).show();
                        }
                        System.out.println("YIOOOO "+date11.toString());
                        response.body().setDateOfBirth(date11);
                        Toast.makeText(getContext(),new String ("newdate"+response.body().getDateOfBirth()),Toast.LENGTH_SHORT).show();
                        System.out.println("newdate"+response.body().getDateOfBirth());
                        User updateduser=new User();
                        updateduser=response.body();
                        final RestAPI restAP = RestClient.getStringClient().create(RestAPI.class);
                        Call<Integer> call1 = restAP.update(response.body().getUserID(),updateduser,jwt);
                        final User finalUpdateduser = updateduser;
                        call1.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {

                                Toast.makeText(getActivity(),"Profile updated",Toast.LENGTH_SHORT).show();
                                PhotosFragment fragment = new PhotosFragment();
                                replaceFragment(fragment);
                                Call<Void> upIm = restAP.upload(finalUpdateduser.getUserID(),imgStr,jwt);
                                upIm.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        System.out.println("UPLOADED ");
                                        byte[] encodeByte = Base64.decode(imgStr, Base64.DEFAULT);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                        image.setImageBitmap(bitmap);
                                        imgStr=null;
//                                        onResume();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable throwable) {
                                Toast.makeText(getActivity(),"Profile could not be updated",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
