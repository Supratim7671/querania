package com.querania.querania;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class notification extends AppCompatActivity {
    int sessionid;
    String data;
    RecyclerView rnv;
    LinearLayoutManager lm;
    RVnotificationAdapter rvnd;
    String res;
    JSONObject jsonObject;
    ArrayList<cardnotificationdata> cnd=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifcation_main);
        //Log.v("The notification activity is getting called ","Yes it is true");
        Log.v("The notification activity is getting called ","Yes it is true");
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(notification.this);
        sessionid=sharedPref.getInt("sessionuserid", -1);
        data="{\"sessionid\":\""+sessionid+"\"}";
        rnv=(RecyclerView)findViewById(R.id.rnv);
        rnv.setHasFixedSize(true);
        lm=new LinearLayoutManager(notification.this);
        rnv.setLayoutManager(lm);

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client=new OkHttpClient();
                Log.d("The request of data is ",data);
                Request request = new Request.Builder()
                        .url("https://www.palzone.ml/services/notification.php")
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data))
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        notification.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(notification.this,"Error",Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        res=response.body().string();
                        Log.d("The response is coming",res);
                        notification.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String s2=res.substring(res.indexOf('[')+1,res.lastIndexOf(']'));
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
                                        int nid=jsonObject.getInt("nid");
                                        int qid=jsonObject.getInt("qid");
                                        String notification=jsonObject.getString("notification");
                                        int noofnotification=jsonObject.getInt("noofnotification");

                                        //cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                        cnd.add(i,new cardnotificationdata(qid,nid,notification,noofnotification));
                                    } catch (Exception e) {
                                        e.printStackTrace();


                                    }
                                    rvnd=new RVnotificationAdapter(cnd,notification.this);
                                    rnv.setAdapter(rvnd);
                                    Log.v("conm","shcjs");
                                }

                            }
                        });

                    }
                });
            }
        });
        th.start();




    }
}
