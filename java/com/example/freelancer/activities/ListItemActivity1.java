package com.example.freelancer.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.freelancer.R;
import com.example.freelancer.client.RestAPI;
import com.example.freelancer.client.RestClient;
import com.example.freelancer.helper.MessagesAdapter;
import com.example.freelancer.helper.ProjectAdapter;
import com.example.freelancer.rest.Chat;
import com.example.freelancer.rest.Project;
import com.example.freelancer.rest.User;

import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class ListItemActivity1 extends AppCompatActivity {

    String jwt;
    MessagesAdapter adapter;
    Jwt<Header, Claims> untrusted;
    String user2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user2 = getIntent().getStringExtra("receiver");
        setContentView(R.layout.activity_messages);
        SharedPreferences prefs = getSharedPreferences("jwt", MODE_PRIVATE);
        String restoredText = prefs.getString("jwt", null);
        jwt = restoredText;
        getSupportActionBar().setTitle(user2);

    }

    @Override
    public void onResume() {
        int i = jwt.lastIndexOf('.');
        String withoutSignature = jwt.substring(0, i + 1);
        untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        final RecyclerView listView = (RecyclerView) findViewById(R.id.list_of_message);
        FloatingActionButton sendMsg = findViewById(R.id.fab);
        final EditText msgToSend = findViewById(R.id.user_message);
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
                Call<User> sender = restAPI.findUsername(untrusted.getBody().getSubject());
                sender.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, final Response<User> response) {
                        final User senderr = response.body();
                        Call<User> receiver = restAPI.findUsername(user2);
                        receiver.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                Chat message = new Chat();
                                message.setContent(msgToSend.getText().toString());
                                message.setDate(new Date());
                                message.setMessageID(0);
//                                response.body().setPhoto(null);
                                message.setUserRec(response.body());
//                                senderr.setPhoto(null);
                                message.setUserSend(senderr);
//                                message.setNotificationsCollection(null);
                                Call<Void> calls = restAPI.sendMsg(jwt,message);
                                calls.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        msgToSend.clearFocus();
                                        msgToSend.setText("");
                                        onResume();
                                        Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(),"Message was not sent successfully",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });
        final List<Chat> list = null;
        adapter = new MessagesAdapter(getApplicationContext(), list,untrusted.getBody().getSubject());
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        RestAPI restAPI = RestClient.getStringClient().create(RestAPI.class);
        Call<List<Chat>> call = restAPI.findMessages(jwt,untrusted.getBody().getSubject(),user2);
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                MessagesAdapter adapter = (MessagesAdapter) listView.getAdapter();
                adapter.insert(response.body());
                adapter.notifyDataSetChanged();
//                System.out.println(adapter.getItemCount()+ " is size");
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        super.onResume();
    }
}