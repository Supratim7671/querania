package com.querania.querania;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.picasso.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class reply_page extends AppCompatActivity {

    int sessionid;
    int questionid;
    int answerid;
    String res;
    String res1;
    String data;
    String data1;
    JSONObject jsonObject;
    LinearLayoutManager lm;
    RecyclerView rv;
    ArrayList<cardreplydata> crd=new ArrayList<>();
    RVreplyAdapter rvrd;
    EditText etrp;
    ImageButton ib;
    String newreply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_reply_main);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(this);
        sessionid=sharedPref.getInt("sessionuserid", -1);
        questionid=Integer.parseInt(getIntent().getExtras().getString("qid"));
        answerid=Integer.parseInt(getIntent().getExtras().getString("answerid"));
        etrp=(EditText)findViewById(R.id.replyhere);
        ib=(ImageButton)findViewById(R.id.replyimgButton);



        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread th1=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newreply=etrp.getText().toString();
                        Log.v("Reply from android is ",newreply);
                        data1="{\"sessionuserid1\":\""+sessionid+"\",\"qid\":\""+questionid+"\",\"answerid\":\"" + answerid + "\",\"newreply\":\"" + newreply + "\"}";



                        Log.v("The requested data is ",data1);
                        OkHttpClient client=new OkHttpClient();

                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url("http://www.palzone.ml/services/post_reply.php")
                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data1))
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                reply_page.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(reply_page.this,"Error",Toast.LENGTH_LONG);
                                    }
                                });

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                res1=response.body().string();

                                reply_page.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.v("Your response is",res1);
                                        String s2 = res1.substring(res1.indexOf('[') + 1, res1.lastIndexOf(']'));
                                        String s3[] = s2.split("[}][,]");
                                        for (String s4 : s3) {
                                            Log.v("response", s4 + '}');

                                        }
                                        for (int i = 0; i < s3.length; i++) {
                                            if (i != s3.length - 1) {
                                                s3[i] = s3[i] + '}';
                                            }
                                            try {
                                                jsonObject = new JSONObject(s3[i]);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                            try {
                                                String replyuserpic=jsonObject.getString("userpic");
                                                String replyusername=jsonObject.getString("username");
                                                int questionid=jsonObject.getInt("questionid");
                                                int ansid=jsonObject.getInt("ansid");
                                                int replyid=jsonObject.getInt("replyid");
                                                int answerreplyuserid=jsonObject.getInt("sessionuid");
                                                String answerreply=jsonObject.getString("reply");

                                                rvrd.addelement(new cardreplydata(replyuserpic,questionid,ansid,replyid,replyusername,answerreply,answerreplyuserid));
                                                Log.v("elr","ele");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                th1.start();


            }
        });

        data="{\"sessionuid\":\""+sessionid+"\",\"quesid\":\""+questionid+"\",\"ansid\":\"" + answerid + "\"}";

        Log.v("Print String", data);

        rv=(RecyclerView)findViewById(R.id.rrv);
        rv.setHasFixedSize(true);
        lm=new LinearLayoutManager(reply_page.this);
        rv.setLayoutManager(lm);

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client=new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://www.palzone.ml/services/loadreplies.php")
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                        .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    reply_page.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(reply_page.this,"error",Toast.LENGTH_LONG);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    res=response.body().string();

                    reply_page.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Your response is",res);
                            String s2=res.substring(res.indexOf('[')+1,res.lastIndexOf(']'));
                            String s3[]=s2.split("[}][,]");

                            for (String s4:s3)
                            {
                                //Log.v("response",s4+'}');
                            }

                            for (int i=0;i<s3.length;i++)
                            {
                                if(s3[i].charAt(s3[i].length()-1)!='}')
                                {
                                    s3[i]=s3[i]+"}";
                                }
                                Log.v("response",s3[i]+'}');
                                try {
                                    jsonObject=new JSONObject(s3[i]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                   String replyuserpic;
                                    try {
                                        try {
                                            replyuserpic = jsonObject.getString("userpic");
                                        } catch (Exception e)
                                        {
                                             replyuserpic="";
                                        }
                                        int qid=jsonObject.getInt("answerquestionid");
                                        int answerid=jsonObject.getInt("answerreplyid");
                                        int replyid=jsonObject.getInt("replyid");
                                        String replyusername=jsonObject.getString("answerreplyname");
                                        String answerreply=jsonObject.getString("answerreply");
                                        int answerreplyuserid=jsonObject.getInt("answerreplyuserid");
                                        crd.add(i,new cardreplydata(replyuserpic,qid,answerid,replyid,replyusername,answerreply,answerreplyuserid));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                rvrd=new RVreplyAdapter(crd,reply_page.this);
                                rv.setAdapter(rvrd);
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
