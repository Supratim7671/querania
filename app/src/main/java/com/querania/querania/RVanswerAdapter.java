package com.querania.querania;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Supratim on 06/05/2017.
 */
public class RVanswerAdapter extends RecyclerView.Adapter<RVanswerAdapter.PersonViewHolder>{
    List<cardanswerdata> c;
    Bitmap bm;
    Activity main;
    PersonViewHolder pv;
    Intent answerpage,viewprofilepage;
    Bundle mBundle;
    String qid;
    String answerid;
    String quesid;
    String ansid;
    int sessionid,s;
    String data;
    String res;
    JSONObject jsonObject;
    boolean wait=true;
    Intent replypage;
    //boolean wait1=true;
    RVanswerAdapter(List asd,Activity main){
        this.c=asd;
        this.main=main;


    }
    public void addelement(cardanswerdata answer){
        c.add(0,answer);
        notifyItemChanged(0);

    }

    public void removeanswerAt(int position) {
        c.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, c.size());
    }



    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_answer_page, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;




    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {

        pv=holder;
        holder.answerusername.setText(c.get(position).answerusername);

        holder.answer.loadDataWithBaseURL("", c.get(position).answer, "text/html", "UTF-8", "");
        // holder.question.loadUrl((c.get(position).ques));
        final String path="http://www.palzone.ml/services";

        Picasso.with(main).load(path + c.get(position).answeruserpic).into(holder.userpic);

        holder.noofanswerlike.setText(String.valueOf(c.get(position).noofanswerlike));
        holder.noofanswerunlike.setText(String.valueOf(c.get(position).noofanswerunlike));
        holder.noofreplies.setText(String.valueOf(c.get(position).noofreplies));
        //holder.qid.setText(String.valueOf(c.get(position).id));

        holder.answerlikebtn=c.get(position).answerlikeabtn;
        holder.answerunlikebtn=c.get(position).answerunlikeabtn;
        holder.quesid=String.valueOf(c.get(position).qid);
        holder.answerid=String.valueOf(c.get(position).answerid);
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
        sessionid=sharedPref.getInt("sessionuserid", -1);
        s=sessionid;
        int answeruserid=c.get(position).qansweruserid;
        if(sessionid!=answeruserid)
        {
            holder.delabtn.setVisibility(View.GONE);
        }
        else
        {
            holder.delabtn.setVisibility(View.VISIBLE);
        }
        holder.delabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int qid=c.get(position).qid;
                int answerid=c.get(position).answerid;

                data="{\"qid\":\""+qid+"\",\"answerid\":\"" + answerid + "\",\"sessionuserid\":\""+s+"\",\"delabtn\":\"" + 1 + "\"}";

                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient();
                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url("http://www.palzone.ml/services/delete_answer.php")
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
                                        Toast.makeText(main,"Successful in deleting answer",Toast.LENGTH_LONG).show();
                                        removeanswerAt(position);
                                    }
                                });
                            }
                        });

                    }
                });
                th.start();

            }
        });
        holder.answerusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.userid=c.get(position).qansweruserid;
                int userid=holder.userid;


                viewprofilepage = new Intent(main,myprofile.class);

                // mBundle = new Bundle()
                viewprofilepage.putExtra("userid", userid);
                //answerpage.putExtras(mBundle);
                main.startActivity(viewprofilepage);
            }
        });
        //holder.quesid=String.valueOf(c.get(position).qid);
        //holder.ansid=String.valueOf(c.get(position).answerid);

        Log.v("holder", String.valueOf(c.get(position).noofanswerlike));

        if (holder.answerlikebtn==2)
        {
            holder.answerlikeabtn.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
        }

        else if (holder.answerlikebtn==3)
        {
            holder.answerlikeabtn.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        }
        else if (holder.answerunlikebtn==2)
        {
            holder.answerunlikeabtn.setImageResource(R.drawable.ic_thumb_down_blue_24dp);
        }
        else if (holder.answerunlikebtn==3)
        {
            holder.answerunlikeabtn.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        }
        //holder.personPhoto.setImageResource(persons.get(i).photoId);
        holder.replybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replypage=new Intent(main,reply_page.class);
                replypage.putExtra("qid", holder.quesid);
                replypage.putExtra("answerid",holder.answerid);
                main.startActivity(replypage);
            }
        });
        holder.answerlikeabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
                sessionid=sharedPref.getInt("sessionuserid", -1);
                s=sessionid;
                qid=String.valueOf(c.get(position).qid);
                answerid=String.valueOf(c.get(position).answerid);
                int answerlikebtn=c.get(position).answerlikeabtn;

                data="{\"qid\":\""+qid+"\",\"answerid\":\"" + answerid + "\",\"sessionuserid1\":\""+s+"\",\"likeabtn\":\"" + answerlikebtn + "\"}";
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        OkHttpClient client=new OkHttpClient();
                        if (wait==true)

                        {
                            wait=false;
                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://www.palzone.ml/services/answerupvoting.php")
                                    .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    main.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(main,"Error",Toast.LENGTH_LONG).show();
                                            wait=true;
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

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
                                                        holder.noofanswerlike.setText(String.valueOf(jsonObject.getInt("noofanswer_like")));
                                                        holder.noofanswerunlike.setText(String.valueOf(jsonObject.getInt("noofanswer_unlike")));
                                                        holder.answerlikebtn = jsonObject.getInt("likeabtn");
                                                        holder.answerunlikebtn = jsonObject.getInt("unlikeabtn");
                                                        c.get(position).answerlikeabtn = holder.answerlikebtn;
                                                        if (holder.answerlikebtn == 2) {

                                                            holder.answerlikeabtn.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                                                        } else if (holder.answerlikebtn == 3) {
                                                            holder.answerlikeabtn.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                                                        }

                                                        if (holder.answerunlikebtn == 2) {
                                                            holder.answerunlikeabtn.setImageResource(R.drawable.ic_thumb_down_blue_24dp);

                                                        } else if (holder.answerunlikebtn == 3) {
                                                            holder.answerunlikeabtn.setImageResource(R.drawable.ic_thumb_down_black_24dp);

                                                        }
                                                        //cad.add(i,new cardanswerdata(qansweruserid,qid,answerid,answer,answeruserpic,answerusername,noofanswerlike,noofanswerunlike, noofreplies, answerstatuslike, answerstatusunlike, sessionuserid1));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });


                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                wait=true;
                                }

                            });
                        }

                    }
                });
                th.start();

            }
        });

        holder.answerunlikeabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
                sessionid=sharedPref.getInt("sessionuserid", -1);
                s=sessionid;
                qid=String.valueOf(c.get(position).qid);
                answerid=String.valueOf(c.get(position).answerid);
                int answerunlikebtn=c.get(position).answerunlikeabtn;

                data="{\"qid\":\""+qid+"\",\"answerid\":\"" + answerid + "\",\"sessionuserid1\":\""+s+"\",\"unlikeabtn\":\"" + answerunlikebtn + "\"}";
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        OkHttpClient client=new OkHttpClient();
                        if (wait==true)

                        {
                            wait=false;
                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://www.palzone.ml/services/answerdownvoting.php")
                                    .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    main.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(main,"Error",Toast.LENGTH_LONG).show();
                                            wait=true;
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

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
                                                        holder.noofanswerlike.setText(String.valueOf(jsonObject.getInt("noofanswer_like")));
                                                        holder.noofanswerunlike.setText(String.valueOf(jsonObject.getInt("noofanswer_unlike")));
                                                        holder.answerlikebtn = jsonObject.getInt("likeabtn");
                                                        holder.answerunlikebtn = jsonObject.getInt("unlikeabtn");
                                                        c.get(position).answerunlikeabtn = holder.answerunlikebtn;
                                                        if (holder.answerlikebtn == 2) {

                                                            holder.answerlikeabtn.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                                                        } else if (holder.answerlikebtn == 3) {
                                                            holder.answerlikeabtn.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                                                        }

                                                        if (holder.answerunlikebtn == 2) {
                                                            holder.answerunlikeabtn.setImageResource(R.drawable.ic_thumb_down_blue_24dp);

                                                        } else if (holder.answerunlikebtn == 3) {
                                                            holder.answerunlikeabtn.setImageResource(R.drawable.ic_thumb_down_black_24dp);

                                                        }
                                                        //cad.add(i,new cardanswerdata(qansweruserid,qid,answerid,answer,answeruserpic,answerusername,noofanswerlike,noofanswerunlike, noofreplies, answerstatuslike, answerstatusunlike, sessionuserid1));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });


                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
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
    public int getItemCount() {
        return c.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView noofanswerlike;
        TextView noofanswerunlike;
        TextView noofreplies;
        ImageButton answerlikeabtn,answerunlikeabtn,replybutton,delabtn;
        WebView answer;
        TextView answerusername;
        ImageView userpic;
        ImageButton answerbutton;
        Boolean img=false;
        String quesid;
        String answerid;
        int answerlikebtn;
        int answerunlikebtn;
        int userid;
        //TextView ;
        //TextView noofanswer;
        ImageView personPhoto;


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cav);
            answer = (WebView)itemView.findViewById(R.id.answer);
            answerusername = (TextView)itemView.findViewById(R.id.answerusername);
            userpic=(ImageView)itemView.findViewById(R.id.answerimageButton);
            noofanswerlike= (TextView)itemView.findViewById(R.id.noofanswerlike);
            noofanswerunlike=(TextView)itemView.findViewById(R.id.noofanswerunlike);
            noofreplies= (TextView)itemView.findViewById(R.id.noofreply);
            answerlikeabtn=(ImageButton) itemView.findViewById(R.id.answerlikeabtn);
            answerunlikeabtn=(ImageButton) itemView.findViewById(R.id.answerunlikeabtn);
            replybutton=(ImageButton)itemView.findViewById(R.id.replyButton);
            delabtn=(ImageButton)itemView.findViewById(R.id.adeletebtn);

            //qid=(TextView)itemView.findViewById(R.id.qid);
           // answerbutton=(ImageButton)itemView.findViewById(R.id.answerButton);


            //quesid= Integer.parseInt(qid.getText().toString());
            //Log.v("The id of question is", String.valueOf(quesid));


        }
    }


}
