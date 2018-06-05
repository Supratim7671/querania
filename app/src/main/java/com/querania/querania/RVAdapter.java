package com.querania.querania;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import org.apache.commons.lang3.StringEscapeUtils;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.data;
import static com.querania.querania.R.drawable.like;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
List<carddata> c;
    Bitmap bm;
    Activity main;
    PersonViewHolder pv;
    Intent answerpage,viewprofilepage;
    Bundle mBundle;
    String qid;
    int sessionid,s;
    String data;
    String res;
    JSONObject jsonObject;
    boolean wait=true;
    boolean wait1=true;

    RVAdapter(List asd,Activity main){
    this.c=asd;
    this.main=main;


    }

    public void addelement(carddata question){
        c.add(0,question);
        notifyItemChanged(0);

    }
    public void removequestionAt(int position) {
        c.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, c.size());
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;




    }

    @Override
    public void onBindViewHolder( final PersonViewHolder holder, final int position) {

        pv=holder;
        holder.questionaskname.setText(c.get(position).question_ask_name);
        holder.userid=c.get(position).quserid;
        //Log.v("The userid on Rv Adapter is"," "+holder.userid);
        //Log.v("The name"," "+c.get(position).question_ask_name);
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
        sessionid=sharedPref.getInt("sessionuserid", -1);
        s=sessionid;
        final int qaskuserid=c.get(position).quserid;

        if (holder.userid!=sessionid) {
            holder.delqbtn.setVisibility(View.GONE);
            Log.e(holder.questionaskname.getText().toString()," "+holder.userid);
        }
        else
        {
            holder.delqbtn.setVisibility(View.VISIBLE);
        }



        holder.delqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
                sessionid=sharedPref.getInt("sessionuserid", -1);
                s=sessionid;
                holder.quesid=String.valueOf(c.get(position).id);
                int qid=c.get(position).id;
                data="{\"qid\":\""+qid+"\",\"sessionuserid\":\""+s+"\",\"delqbtn\":\"" + 1 + "\"}";

                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://www.palzone.ml/services/delete_question.php")
                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                main.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(main,"error",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                main.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(main,"Successful in deleting question",Toast.LENGTH_LONG).show();
                                        removequestionAt(position);
                                    }
                                });
                            }
                        });

                    }
                });
                th.start();

            }
        });

        holder.questionaskname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userid=holder.userid;


                viewprofilepage = new Intent(main,myprofile.class);

                // mBundle = new Bundle()
                viewprofilepage.putExtra("userid", userid);
                //answerpage.putExtras(mBundle);
                main.startActivity(viewprofilepage);
            }
        });
        //String newquestion=c.get(position).ques;


        holder.question.loadDataWithBaseURL("", c.get(position).ques, "text/html","UTF-8", "");
       // holder.question.loadUrl((c.get(position).ques));
       final String path="http://www.palzone.ml/services";

        Picasso.with(main).load(path + c.get(position).userpic).into(holder.userpic);

        holder.nooflike.setText(String.valueOf(c.get(position).no_of_like));
        holder.noofunlike.setText(String.valueOf(c.get(position).no_of_unlike));
        holder.noofanswers.setText(String.valueOf(c.get(position).no_of_answers));
        //holder.qid.setText(String.valueOf(c.get(position).id));
         holder.quesid=String.valueOf(c.get(position).id);
         holder.likebtn=c.get(position).likeqbtn;
         holder.unlikebtn=c.get(position).unlikeqbtn;
         if (holder.likebtn==2)
         {
            Log.v("like button value is",String.valueOf(holder.likebtn));
             holder.likeqbtn.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
         }
         else if (holder.likebtn==3)
         {
             Log.v("like button value is",String.valueOf(holder.likebtn));
             holder.likeqbtn.setImageResource(R.drawable.ic_thumb_up_black_24dp);
         }

         if (holder.unlikebtn==2)
         {
             Log.v("like button value is",String.valueOf(holder.unlikebtn));
             holder.unlikeqbtn.setImageResource(R.drawable.ic_thumb_down_blue_24dp);

         }

         else  if (holder.unlikebtn==3)
         {
             Log.v("like button value is",String.valueOf(holder.unlikebtn));
             holder.unlikeqbtn.setImageResource(R.drawable.ic_thumb_down_black_24dp);

         }
        Log.v("holder",String.valueOf(c.get(position).no_of_like));
        //holder.personPhoto.setImageResource(persons.get(i).photoId);
        holder.answerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerpage = new Intent(main,answer_page.class);

               // mBundle = new Bundle()
               answerpage.putExtra("id", holder.quesid);
                //answerpage.putExtras(mBundle);
                main.startActivity(answerpage);
            }
        });

        holder.likeqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
                sessionid=sharedPref.getInt("sessionuserid", -1);
                s=sessionid;
                int id=c.get(position).id;
                int likeqbtn=c.get(position).likeqbtn;
                //boolean wait=true;

                data="{\"id\":\""+id+"\",\"sessionuserid\":\""+s+"\",\"likeqbtn\":\"" + likeqbtn + "\"}";
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        if (wait==true)
                        {
                            wait=false;
                        Request request = new Request.Builder()
                                .url("http://www.palzone.ml/services/upvoting.php")
                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                main.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(main, "error", Toast.LENGTH_LONG).show();
                                        wait=true;
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                Thread th1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            res = response.body().string();
                                            main.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.v("Your response is", res);
                                                    String s2 = res.substring(res.indexOf('[') + 1, res.lastIndexOf(']'));
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
                                                            //int follow=jsonObject.getInt("follow");
                                                            //int editqbtn=jsonObject.getInt("editqbtn");
                                                            //int likeqbtn=jsonObject.getInt("likeqbtn");
                                                            //int unlikeqbtn=jsonObject.getInt("unlikeqbtn");
                                                            holder.nooflike.setText(String.valueOf(jsonObject.getInt("noof_likes")));
                                                            holder.noofunlike.setText(String.valueOf(jsonObject.getInt("noof_unlikes")));
                                                            holder.likebtn = jsonObject.getInt("likeqbtn");
                                                            holder.unlikebtn = jsonObject.getInt("unlikeqbtn");
                                                            c.get(position).likeqbtn=holder.likebtn;
                                                            if (holder.likebtn==2)
                                                            {

                                                                holder.likeqbtn.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                                                            }
                                                            else if (holder.likebtn==3)
                                                            {
                                                                holder.likeqbtn.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                                                            }

                                                            if (holder.unlikebtn==2)
                                                            {
                                                                holder.unlikeqbtn.setImageResource(R.drawable.ic_thumb_down_blue_24dp);

                                                            }

                                                            else  if (holder.unlikebtn==3)
                                                            {
                                                                holder.unlikeqbtn.setImageResource(R.drawable.ic_thumb_down_black_24dp);

                                                            }
                                                            //cad.add(i,new cardanswerdata(qansweruserid,qid,answerid,answer,answeruserpic,answerusername,noofanswerlike,noofanswerunlike, noofreplies, answerstatuslike, answerstatusunlike, sessionuserid1));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });


                                        } catch (IOException e) {


                                            e.printStackTrace();
                                        }
                                        wait=true;
                                    }
                                });
                                th1.start();

                            }
                        });
                    }
                    }
                });
                th.start();

            }
        });
        holder.unlikeqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
                sessionid=sharedPref.getInt("sessionuserid", -1);
                s=sessionid;
                int id=c.get(position).id;
                int unlikeqbtn=c.get(position).unlikeqbtn;
                data="{\"id\":\""+id+"\",\"sessionuserid\":\""+s+"\",\"unlikeqbtn\":\"" + unlikeqbtn + "\"}";
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        if (wait == true) {
                            wait=false;

                            Request request = new Request.Builder()
                                    .url("http://www.palzone.ml/services/downvoting.php")
                                    .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    main.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(main, "error", Toast.LENGTH_LONG).show();
                                            wait=true;
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    Thread th2 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                res = response.body().string();
                                                main.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Log.v("Your response is", res);
                                                        String s2 = res.substring(res.indexOf('[') + 1, res.lastIndexOf(']'));
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
                                                                //int follow=jsonObject.getInt("follow");
                                                                //int editqbtn=jsonObject.getInt("editqbtn");
                                                                //int likeqbtn=jsonObject.getInt("likeqbtn");
                                                                //int unlikeqbtn=jsonObject.getInt("unlikeqbtn");
                                                                holder.nooflike.setText(String.valueOf(jsonObject.getInt("noof_likes")));
                                                                holder.noofunlike.setText(String.valueOf(jsonObject.getInt("noof_unlikes")));
                                                                holder.likebtn = jsonObject.getInt("likeqbtn");
                                                                holder.unlikebtn = jsonObject.getInt("unlikeqbtn");
                                                                c.get(position).unlikeqbtn=holder.unlikebtn;
                                                                if (holder.likebtn==2)
                                                                {
                                                                    holder.likeqbtn.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                                                                }
                                                                else if (holder.likebtn==3)
                                                                {
                                                                    holder.likeqbtn.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                                                                }

                                                                if (holder.unlikebtn==2)
                                                                {
                                                                    holder.unlikeqbtn.setImageResource(R.drawable.ic_thumb_down_blue_24dp);

                                                                }

                                                                else  if (holder.unlikebtn==3)
                                                                {
                                                                    holder.unlikeqbtn.setImageResource(R.drawable.ic_thumb_down_black_24dp);

                                                                }
                                                                //cad.add(i,new cardanswerdata(qansweruserid,qid,answerid,answer,answeruserpic,answerusername,noofanswerlike,noofanswerunlike, noofreplies, answerstatuslike, answerstatusunlike, sessionuserid1));
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                });


                                            } catch (IOException e) {


                                                e.printStackTrace();
                                            }
                                            wait=true;
                                        }
                                    });
                                    th2.start();

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
    public int getItemCount() {
        return c.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nooflike,noofunlike;
        TextView noofanswers;
        WebView question;
        TextView questionaskname,qid;
        ImageView userpic;
        ImageButton answerbutton,likeqbtn,unlikeqbtn,delqbtn;
        Boolean img=false;
        String quesid;
        int likebtn,unlikebtn;
        int userid;

        //TextView ;
        //TextView noofanswer;
        ImageView personPhoto;


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            question = (WebView)itemView.findViewById(R.id.question);
            questionaskname = (TextView)itemView.findViewById(R.id.username);
            userpic=(ImageView)itemView.findViewById(R.id.imageButton);
            nooflike= (TextView)itemView.findViewById(R.id.nooflike);
            noofunlike=(TextView)itemView.findViewById(R.id.noofunlike);

            noofanswers= (TextView)itemView.findViewById(R.id.noofanswer);

            answerbutton=(ImageButton)itemView.findViewById(R.id.answerButton);

            likeqbtn=(ImageButton)itemView.findViewById(R.id.likeqbtn);
            unlikeqbtn=(ImageButton)itemView.findViewById(R.id.unlikeqbtn);
            delqbtn=(ImageButton)itemView.findViewById(R.id.qdeletebtn);
            //quesid= Integer.parseInt(qid.getText().toString());
            //Log.v("The id of question is", String.valueOf(quesid));


        }
    }


}
