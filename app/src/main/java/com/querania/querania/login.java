package com.querania.querania;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Types;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class login extends AppCompatActivity {


    EditText email,password;
    String e,p;
    JSONObject jsonObject;
    String data;
    int sessionid;

    static class Contributor {
        String login;
        int contributions;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button login;
        final TextView register;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(login.this);
        sessionid = sharedPref.getInt("sessionuserid", -1);

        if (sessionid == -1) {
            email = (EditText) findViewById(R.id.email);
            password = (EditText) findViewById(R.id.password);
            login = (Button) findViewById(R.id.login);
            register = (TextView) findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerpage = new Intent(login.this, register.class);
                    startActivity(registerpage);
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    e = email.getText().toString();
                    p = password.getText().toString();
                    //Toast.makeText(login.this,s,Toast.LENGTH_SHORT).show();
                    //Log.v("sdhsdh", s);
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            data = "{\"email\":\"" + e + "\",\"password\":\"" + p + "\"}";
                            // JSONParser parser = new JSONParser();

                            Log.v("Print String", data);

                            OkHttpClient client = new OkHttpClient();

                            Request request = new Request.Builder()
                                    .url("http://www.palzone.ml/services/login_register.php")
                                    .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, final IOException e) {
                                    // Request failed
                                    login.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(login.this, "error", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    // Handle whatever comes back from the server
                                    String s1 = response.body().string();
                                    final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                                    try {
                                        jsonObject = new JSONObject(s2);
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }


                                    login.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // Toast.makeText(login.this,String.valueOf(jsonObject.getInt("response")), Toast.LENGTH_LONG).show();

                                                int status_response = Integer.parseInt(String.valueOf(jsonObject.getInt("response")));
                                                int sessionid = Integer.parseInt(String.valueOf(jsonObject.getInt("userid")));
                                                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(login.this);
                                                SharedPreferences.Editor ed = sharedPrefs.edit();
                                                ed.putInt("sessionuserid", sessionid);
                                                ed.commit();
                                                Toast.makeText(login.this, "Successful Login...Please have patience...", Toast.LENGTH_LONG).show();

                                                //Log.v("status response is",String.valueOf(status_response));
                                                if (status_response == 1) {
                                                    int timeout = 2000; // make the activity visible for 4 seconds

                                                    Timer timer = new Timer();
                                                    timer.schedule(new TimerTask() {

                                                        @Override
                                                        public void run() {
                                                            finish();
                                                            Intent homepage = new Intent(login.this, navigationdrawer.class);
                                                            startActivity(homepage);
                                                        }
                                                    }, timeout);

                                                } else if (status_response == 2) {

                                                    Toast.makeText(login.this, "Login Failed.Please try again", Toast.LENGTH_LONG).show();
                                                }


                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    });


                                }
                            });
                        }
                    });
                    th.start();
                }
            });
        }
        else
         {
            Intent home_page = new Intent(login.this, navigationdrawer.class);
            startActivity(home_page);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
