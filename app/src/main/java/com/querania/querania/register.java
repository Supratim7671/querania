package com.querania.querania;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class register extends AppCompatActivity {


    EditText email,username,password,cpassword;
    Button regbutton;
    TextView regloginbtn;
    String data,e,u,p,cp;
    JSONObject jsonObject;
    boolean wait=true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         email=(EditText)findViewById(R.id.regemail);
         username=(EditText)findViewById(R.id.username);
         password=(EditText)findViewById(R.id.regpassword);
         cpassword=(EditText)findViewById(R.id.regcpassword);
         regbutton=(Button)findViewById(R.id.regbutton);
         regloginbtn=(TextView)findViewById(R.id.reglogin);
        regloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginpage1 = new Intent(register.this, login.class);
                startActivity(loginpage1);
            }
        });
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 e=email.getText().toString();
                 u=username.getText().toString();
                 p=password.getText().toString();
                 cp=cpassword.getText().toString();
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        data = "{\"email1\":\"" + e + "\",\"password1\":\"" + p + "\",\"username1\":\"" + u + "\",\"cpassword1\":\"" + cp + "\"}";
                        Log.v("Print register detail", data);
                        OkHttpClient client = new OkHttpClient();

                        if (wait==true)
                        {
                            wait=false;
                        Request request = new Request.Builder()
                                .url("http://www.palzone.ml/services/login_register.php")
                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                // Request failed
                                register.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(register.this, "error", Toast.LENGTH_SHORT).show();
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


                                register.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            // Toast.makeText(login.this,String.valueOf(jsonObject.getInt("response")), Toast.LENGTH_LONG).show();

                                            int status_response = Integer.parseInt(String.valueOf(jsonObject.getInt("response")));

                                            Toast.makeText(register.this, String.valueOf(status_response), Toast.LENGTH_LONG).show();

                                            Log.v("status response is", String.valueOf(status_response));
                                            if (status_response == 3) {
                                                int timeout = 2000; // make the activity visible for 4 seconds
                                                Toast.makeText(register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                                Timer timer = new Timer();
                                                timer.schedule(new TimerTask() {

                                                    @Override
                                                    public void run() {
                                                        finish();
                                                        Intent loginpage = new Intent(register.this, login.class);
                                                        startActivity(loginpage);
                                                    }
                                                }, timeout);

                                            } else if (status_response == 4) {

                                                Toast.makeText(register.this, "Registration Failed", Toast.LENGTH_LONG).show();
                                            } else if (status_response == 5) {

                                                Toast.makeText(register.this, "Already Registered", Toast.LENGTH_LONG).show();
                                            } else if (status_response == 6) {

                                                Toast.makeText(register.this, "Insufficient Details", Toast.LENGTH_LONG).show();
                                            }


                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });

                            wait=true;
                            }
                        });
                    }
                }
                });
                th.start();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
