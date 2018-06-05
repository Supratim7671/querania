package com.querania.querania;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class myprofile extends AppCompatActivity {
    int sessionid,s,userid,userid1;
    String data;
    String jsonresponse,newjsonresponse;
    JSONObject jsonObject;
    TextView tv1,tv2,upn,q,a,r;
    EditText et1,et2,et6;
    CircleImageView civ;
    ImageView edbtn;
    Button upbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(myprofile.this);
        sessionid=sharedPref.getInt("sessionuserid", -1);
        s=sessionid;
        edbtn=(ImageView)findViewById(R.id.edit);
        upbtn=(Button)findViewById(R.id.updatebtn);
        final String path="http://www.palzone.ml/services";
        edbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setEnabled(true);
                et2.setEnabled(true);
                et6.setEnabled(true);
                upbtn.setVisibility(View.VISIBLE);

                upbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phoneno=et1.getText().toString();
                        String aboutme=et2.getText().toString();
                        String profilebio=et6.getText().toString();

                        data="{\"user_id\":\""+sessionid+"\",\"mobile_no\":\"" + phoneno + "\",\"about_me\":\"" + aboutme + "\",\"profile_bio\":\"" + profilebio + "\"}";

                        Thread th=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client=new OkHttpClient();
                            Request request=new Request.Builder()
                                    .url("https://www.palzone.ml/services/user_profile_php.php")
                                    .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data))
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    myprofile.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(myprofile.this,"Error",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                     newjsonresponse=response.body().string();
                                    Log.v("The new jsonresponse is",newjsonresponse);
                                    myprofile.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                jsonObject =new JSONObject(newjsonresponse);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                            try {
                                                String userpic=jsonObject.getString("userpic");
                                                //int id=jsonObject.getInt("id");
                                                //int quserid=jsonObject.getInt("quserid");
                                                //String ques=jsonObject.getString("ques");
                                                String username=jsonObject.getString("username");
                                                String email=jsonObject.getString("email");
                                                String profilebio=jsonObject.getString("profilebio");
                                                String gender=jsonObject.getString("gender");
                                                String mobileno=jsonObject.getString("mobile");
                                                String aboutme=jsonObject.getString("aboutme");
                                                int noofquestionasked=jsonObject.getInt("noofquestionasked");
                                                int noofanswergiven=jsonObject.getInt("noofanswergiven");
                                                int noofreplygiven=jsonObject.getInt("noofreplygiven");
                                                tv1=(TextView)findViewById(R.id.editText);
                                                tv2=(TextView)findViewById(R.id.editText2);
                                                et1=(EditText)findViewById(R.id.editText3);
                                                et2=(EditText) findViewById(R.id.editText4);
                                                et6=(EditText)findViewById(R.id.editText6);
                                                upn=(TextView)findViewById(R.id.user_profile_name);
                                                q=(TextView)findViewById(R.id.textView11);
                                                a=(TextView)findViewById(R.id.textView10);
                                                r=(TextView)findViewById(R.id.textView12);

                                                tv1.setText(username);
                                                tv2.setText(email);
                                                et1.setText(mobileno);
                                                et2.setText(aboutme);
                                                et6.setText(profilebio);
                                                q.setText(""+noofquestionasked+"questions");
                                                a.setText(""+noofanswergiven+"answers");
                                                r.setText(""+noofreplygiven+"replies");
                                                upn.setText(username);
                                                civ=(CircleImageView)findViewById(R.id.profile_image);
                                                Picasso.with(myprofile.this).load(path + userpic).into(civ);
                                                upbtn.setVisibility(View.GONE);
                                                et1.setEnabled(false);
                                                et2.setEnabled(false);
                                                et6.setEnabled(false);
                                                // cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
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
        });
        try{
             userid=getIntent().getExtras().getInt("userid");
          //  userid1=userid;
            Log.v("The userid"," "+userid);
            if (userid!=sessionid){
                sessionid=userid;

                edbtn.setVisibility(View.GONE);
                //et1.setEnabled(false);
               // et1.setEnabled(false);
               // et2.setEnabled(false);
               // et6.setEnabled(false);

            }
        }
        catch(Exception e){

            e.printStackTrace();

        }

        data="{\"sessionuserid\":\""+sessionid+"\"}";
        Log.v("logv",data);

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();

                Request request=new Request.Builder()
                        .url("https://www.palzone.ml/services/selectone.php")
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data))
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        myprofile.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(myprofile.this, "error", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        jsonresponse=response.body().string();
                        Log.v("The response is",jsonresponse);
                        myprofile.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //String s2=jsonresponse.substring(jsonresponse.indexOf('[')+1,jsonresponse.lastIndexOf(']'));
                                //Log.v("The String is",s2);
                                //String s3[]=s2.split("[}][,]");
                                try {
                                    jsonObject =new JSONObject(jsonresponse);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                try {
                                    String userpic=jsonObject.getString("userpic");
                                    //int id=jsonObject.getInt("id");
                                    //int quserid=jsonObject.getInt("quserid");
                                    //String ques=jsonObject.getString("ques");
                                    String username=jsonObject.getString("username");
                                    String email=jsonObject.getString("email");
                                    String profilebio=jsonObject.getString("profilebio");
                                    String gender=jsonObject.getString("gender");
                                    String mobileno=jsonObject.getString("mobile");
                                    String aboutme=jsonObject.getString("aboutme");
                                    int noofquestionasked=jsonObject.getInt("noofquestionasked");
                                    int noofanswergiven=jsonObject.getInt("noofanswergiven");
                                    int noofreplygiven=jsonObject.getInt("noofreplygiven");
                                    tv1=(TextView)findViewById(R.id.editText);
                                    tv2=(TextView)findViewById(R.id.editText2);
                                    et1=(EditText)findViewById(R.id.editText3);
                                    et2=(EditText) findViewById(R.id.editText4);
                                    et6=(EditText)findViewById(R.id.editText6);
                                    upn=(TextView)findViewById(R.id.user_profile_name);
                                    q=(TextView)findViewById(R.id.textView11);
                                    a=(TextView)findViewById(R.id.textView10);
                                    r=(TextView)findViewById(R.id.textView12);

                                    tv1.setText(username);
                                    tv2.setText(email);
                                    et1.setText(mobileno);
                                    et2.setText(aboutme);
                                    et6.setText(profilebio);
                                    q.setText(""+noofquestionasked+"questions");
                                    a.setText(""+noofanswergiven+"answers");
                                    r.setText(""+noofreplygiven+"replies");
                                    upn.setText(username);
                                        civ=(CircleImageView)findViewById(R.id.profile_image);
                                    Picasso.with(myprofile.this).load(path + userpic).into(civ);

                                   // cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                } catch (JSONException e) {
                                    e.printStackTrace();
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
