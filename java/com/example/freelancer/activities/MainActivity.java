package com.example.freelancer.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.rest.Login;
import com.example.freelancer.rest.ResObj;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class AESCrypt
{
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }

    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(AESCrypt.KEY.getBytes(),AESCrypt.ALGORITHM);
        return key;
    }
}


public class MainActivity extends AppCompatActivity {
    Button b1,b2;
    EditText ed1,ed2;

    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    Button btnSignup;
    RestAPI userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userService = RestClient.getStringClient().create(RestAPI.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.emailinput);
        edtPassword = (EditText) findViewById(R.id.passwordinput);
        btnLogin = (Button) findViewById(R.id.Loginbutton);
        btnSignup = (Button) findViewById(R.id.signupbutton);
        //userService = ApiUtils.getUserService();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("OKimin");
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                //validate form
                System.out.println(username+" "+password);
                if(validateLogin(username, password)){
                    //do login
                    System.out.println("OKimin1");
                    try {
                        doLogin(username, AESCrypt.encrypt(password).trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("OKimin2");
                }
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

    }

    private boolean validateLogin(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin(final String username,final String password){
        Login logIn = new Login();
        //Toast.makeText(this,username+password,Toast.LENGTH_SHORT).show();
        logIn.setPassword(password);
        logIn.setUsername(username);
        System.out.println(logIn.getPassword()+ " "+ logIn.getUsername());
        RestAPI restAPI =
                RestClient.getStringClient().create(RestAPI.class);
        restAPI.login(logIn).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String retToken = response.body();
                System.out.print(retToken);

                if (retToken != null && !retToken.equals("not")) {
                    System.out.println("tokkkkken "+retToken);
                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("jwt", MODE_PRIVATE).edit();
                    editor.putString("jwt",retToken);
                    editor.apply();

                    int i = retToken.lastIndexOf('.');
                    String withoutSignature = retToken.substring(0, i + 1);
                    Jwt<Header, Claims> untrusted; untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
                    String typeofuser = (String) untrusted.getBody().get("type");
                    System.out.println(editor.commit()+" coomit true");
                    System.out.println("TYPEOFUSER" + typeofuser);
                    Intent login2=null;
                    if(Integer.valueOf(typeofuser)==1)
                        login2 = new Intent(getApplicationContext(), MainActivity1.class);
                    if(Integer.valueOf(typeofuser)==2)
                        login2 = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(login2);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, "Wrong Username Or Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
