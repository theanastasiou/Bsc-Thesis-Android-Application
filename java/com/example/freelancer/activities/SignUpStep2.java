package com.example.freelancer.activities;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.health.SystemHealthManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannedString;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.rest.Skills;
import com.example.freelancer.rest.User;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.security.Key;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class AESCrypt1
{
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt1.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }

    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt1.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(AESCrypt1.KEY.getBytes(),AESCrypt1.ALGORITHM);
        return key;
    }
}


public class SignUpStep2 extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    EditText iCountry;
    EditText iPhonenumber;
    RadioButton rd;
    RadioGroup radioGroup;
    String date1;
    EditText iSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);
        String[] descriptionData = {"Start", "Finish"};
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.state);
        stateProgressBar.setStateDescriptionData(descriptionData);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        final String iUsername = getIntent().getExtras().getString("iUsername");
        final String iName = getIntent().getExtras().getString("iName");
        final String iSurname = getIntent().getExtras().getString("iSurname");
        final String iEmail = getIntent().getExtras().getString("iEmail");
        final String iPassword = getIntent().getExtras().getString("iPassword");

        mDisplayDate = (TextView) findViewById(R.id.etDate);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUpStep2.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                System.out.println("Month " + (datePicker.getMonth()+ 1));

                Log.d(TAG,  year + "-" + month + "-" + day);

                date1 = year + "-" + month + "-" + day ;
                mDisplayDate.setText(date1);
            }
        };

        System.out.println("DATE" + date1);


        iCountry = (EditText) findViewById(R.id.etCountry);
        iPhonenumber = (EditText) findViewById(R.id.editText);
        iSkills = (EditText) findViewById(R.id.edSkills);


        Button btnDisplay = (Button) findViewById(R.id.btnLogin);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                rd = (RadioButton) findViewById(selectedId);
                //Toast.makeText(SignUpStep2.this, rd.getText(), Toast.LENGTH_SHORT).show();
                int workorhirer = 0;
                if (rd.getText().equals("Work"))
                    workorhirer = 2;
                if (rd.getText().equals("Hire"))
                    workorhirer = 1;

                System.out.println(workorhirer);
                final int id = 0;
                //DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

                //to convert Date to String, use format method of SimpleDateFormat class.
                //String strDate = dateFormat.format(mDisplayDate);
                String[] params = {String.valueOf(id), iName , iSurname,iUsername,iPassword,iEmail,iPhonenumber.getText().toString(),iCountry.getText().toString(), String.valueOf(workorhirer),date1,iSkills.getText().toString()};
                new RegisterTask().execute(params);
                finish();

            }

        });


    }


    public class RegisterTask extends AsyncTask<String[], Void, User> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected User doInBackground(String[]... params) {
            final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
            final User newUser = new User();

            System.out.println("iuserID"+params[0][0]);
            System.out.println("iName"+params[0][1]);
            System.out.println("iSurname"+params[0][2]);
            System.out.println("iUsername"+params[0][3]);
            System.out.println("iPassword"+params[0][4]);
            System.out.println("iEmail"+params[0][5]);
            System.out.println("iPhonenumber"+params[0][6]);
            System.out.println("iCountry"+params[0][7]);
            System.out.println("iTypeofuser"+params[0][8]);
            System.out.println("iDateofbirth"+params[0][9]);



            newUser.setUserName(params[0][3]);
            try {
                newUser.setPassword(AESCrypt.encrypt(params[0][4]).trim());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            newUser.setName(params[0][1]);
            newUser.setSurname(params[0][2]);
            newUser.setEmail(params[0][5]);
            newUser.setPhoneNumber(Integer.parseInt(params[0][6]));
            newUser.setTypeOfUser(Integer.parseInt(params[0][8]));


            String datee = params[0][9];

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            try {
                formatter.setLenient(false);
                date1 = (Date)formatter.parse(datee);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            System.out.println(datee+"\t"+date1);
            newUser.setDateOfBirth(date1);
            newUser.setCountry(params[0][7]);

            StringTokenizer multiTokenizer = new StringTokenizer(params[0][10], ",");

            Collection<Skills> Userskills=new ArrayList<>();
            int j=0;
            while (multiTokenizer.hasMoreTokens())
            {
                Skills newskill = new Skills();
                newskill.setSkillID(j++);
                newskill.setDescription(multiTokenizer.nextToken().trim());
               Userskills.add(newskill);

            }
            newUser.setSkillsCollection(Userskills);

            System.out.println("NUser is " + newUser.toString());
            Call<Integer> call = restAPI.create1(newUser);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Toast.makeText(getApplicationContext(),"User created",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),"User could not be created",Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }
}
