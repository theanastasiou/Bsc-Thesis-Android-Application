package com.example.freelancer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.kofigyan.stateprogressbar.StateProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    EditText iName;
    EditText iSurname;
    EditText iUsername;
    EditText iPassword;
    EditText iPasswordR;
    EditText iEmail;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        String jwt=null;

//        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
//        String restoredText = prefs.getString("jwt", null);
//        jwt = restoredText;
//        System.out.println("from auction page jwt = "+jwt);
        String[] descriptionData = {"Start", "Finish"};
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.state);
        stateProgressBar.setStateDescriptionData(descriptionData);

        iName=(EditText)findViewById(R.id.etName);
        iSurname=(EditText)findViewById(R.id.etSurname);
        iUsername=(EditText)findViewById(R.id.etUsername);
        iEmail=(EditText)findViewById(R.id.etEmail);
        iPassword=(EditText)findViewById(R.id.etPassword);
        iPasswordR=(EditText)findViewById(R.id.etPasswordR);
        Button btn = (Button)findViewById(R.id.btnNext);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("wtfff");
                System.out.println("username" + iUsername.getText().toString());
                System.out.println("email" + iEmail.getText().toString());

                RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
                Call<Boolean> cUN = restAPI.checkUname(iUsername.getText().toString());
                final Call<Boolean> cEM = restAPI.checkEmail(iEmail.getText().toString());
                final boolean[] resUN = new boolean[1];
                final boolean[] resEM = new boolean[1];
                cUN.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(!response.body()) {
                            System.out.println("imin");
                            Toast.makeText(getApplicationContext(), "Username already used", Toast.LENGTH_SHORT).show();
                            resUN[0] = false;
                        }
                        else {
                            System.out.println("TR");
                            resUN[0]= true;
                            cEM.enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(!response.body()){
                                        Toast.makeText(getApplicationContext(), "Email already used", Toast.LENGTH_SHORT).show();
                                        resEM[0] = false;
                                    }
                                    else {
                                        System.out.println("TR");
                                        resEM[0]= true;
                                        String encPass = null;
                                        try {
                                            encPass = AESCrypt.encrypt(iPassword.getText().toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        String encRPass = null;
                                        try {
                                            encRPass = AESCrypt.encrypt(iPasswordR.getText().toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        if(!encPass.equals(encRPass)) {
                                            Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            System.out.println("hithere");
                                            Intent intent = new Intent(getApplicationContext(), SignUpStep2.class);
                                            intent.putExtra("iName", iName.getText().toString());
                                            intent.putExtra("iUsername", iUsername.getText().toString());
                                            intent.putExtra("iSurname", iSurname.getText().toString());
                                            intent.putExtra("iPassword", iPassword.getText().toString());
                                            intent.putExtra("iEmail", iEmail.getText().toString());
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable throwable) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable throwable) {

                    }
                });
            }
        });
    }

}
