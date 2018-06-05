package com.querania.querania;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class home_page extends AppCompatActivity {

    int s,sessionid;
    String data,s1;
    JSONObject jsonObject;
    ArrayList<carddata> cd;
    RecyclerView rv;
    RecyclerView.LayoutManager lm;
    RVAdapter rvd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        cd=new ArrayList<>();
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(this);
        sessionid=sharedPref.getInt("sessionuserid", -1);
        s=sessionid;
        data="{\"sessionid\":\""+sessionid+"\"}";
        Log.v("logv",data);
        rv=(RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        lm=new LinearLayoutManager(home_page.this);
        rv.setLayoutManager(lm);
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://www.palzone.ml/services/loadquestions.php")
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data))
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        home_page.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(home_page.this, "error", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        s1= response.body().string();

                        home_page.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(home_page.this,s1, Toast.LENGTH_LONG).show();

                                String s2=s1.substring(s1.indexOf('[')+1,s1.lastIndexOf(']'));
                                String s3[]=s2.split("[}][,]");

                                for(String s4:s3)
                                {
                                    Log.v("response",s4+'}');

                                }
                            for (int i=0;i<s3.length;i++)
                            {
                                if(i!=s3.length-1)
                                {
                                    s3[i]=s3[i]+'}';
                                }
                                try {
                                    jsonObject =new JSONObject(s3[i]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                try {
                                    String userpic2=jsonObject.getString("userpic2");
                                    int id=jsonObject.getInt("id");
                                    int quserid=jsonObject.getInt("quserid");
                                    String ques=jsonObject.getString("ques");
                                    String questionaskname=jsonObject.getString("questionaskname");
                                    int nooflike=jsonObject.getInt("nooflike");
                                    int noofunlike=jsonObject.getInt("noofunlike");
                                    int noofanswers=jsonObject.getInt("noofanswers");
                                    String statuslike=jsonObject.getString("statuslike");
                                    String statusunlike=jsonObject.getString("statusunlike");
                                    int sessionuserid=jsonObject.getInt("sessionuserid");
                                    int follow=jsonObject.getInt("follow");
                                    int editqbtn=jsonObject.getInt("editqbtn");
                                    int likeqbtn=jsonObject.getInt("likeqbtn");
                                    int unlikeqbtn=jsonObject.getInt("unlikeqbtn");

                                    cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }



                                rvd=new RVAdapter(cd,home_page.this);
                                rv.setAdapter(rvd);




                            }
                        });
                }
                });

            }
        });
        th.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
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
