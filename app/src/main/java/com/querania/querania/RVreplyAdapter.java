package com.querania.querania;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Supratim on 12/06/2017.
 */

public class RVreplyAdapter extends  RecyclerView.Adapter<RVreplyAdapter.PersonViewHolder> {

    List<cardreplydata> c;
    Bitmap bm;
    Activity main;
    PersonViewHolder pv;
    CardView cv;
    String data;
    int s,sessionid;
    Intent viewprofilepage;

    RVreplyAdapter(List asd, Activity main){
        this.c=asd;
        this.main=main;


    }

    public void addelement(cardreplydata reply){
        c.add(0,reply);
        notifyItemChanged(0);

    }

    public void removereplyAt(int position) {
        c.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, c.size());
    }

    @Override
    public RVreplyAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reply_page, parent, false);
        RVreplyAdapter.PersonViewHolder pvh = new RVreplyAdapter.PersonViewHolder(v);
        return pvh;

        //return null;
    }

    @Override
    public void onBindViewHolder(final RVreplyAdapter.PersonViewHolder holder, final int position) {
        Log.v("print","run");
        holder.replyusername.setText(c.get(position).replyusername);
        holder.userid=c.get(position).answerreplyuserid;
        //int userid=holder.userid;
        holder.replyusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // holder.userid=c.get(position).qansweruserid;
                int userid=holder.userid;


                viewprofilepage = new Intent(main,myprofile.class);

                // mBundle = new Bundle()
                viewprofilepage.putExtra("userid", userid);
                //answerpage.putExtras(mBundle);
                main.startActivity(viewprofilepage);
            }
        });

        holder.reply.loadDataWithBaseURL("", c.get(position).answerreply, "text/html", "UTF-8", "");
        final String path="http://www.palzone.ml/services";

        Picasso.with(main).load(path + c.get(position).replyuserpic).into(holder.replyimagebtn);

        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
        sessionid=sharedPref.getInt("sessionuserid", -1);
        s=sessionid;
        int replyuserid=c.get(position).answerreplyuserid;

        if (sessionid!=replyuserid)
        {
            holder.delrbtn.setVisibility(View.GONE);
        }
            else
        {
            holder.delrbtn.setVisibility(View.VISIBLE);
        }
        holder.delrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
                sessionid=sharedPref.getInt("sessionuserid", -1);
                s=sessionid;
                int qid=c.get(position).qid;
                int answerid=c.get(position).answerid;
                int replyid=c.get(position).replyid;

                data="{\"qid\":\""+qid+"\",\"answerid\":\"" + answerid + "\",\"replyid\":\"" + replyid + "\",\"sessionuserid\":\""+s+"\",\"delrbtn\":\"" + 1 + "\"}";
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient();
                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url("http://www.palzone.ml/services/delete_reply.php")
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
                                        Toast.makeText(main,"Successful in deleting reply",Toast.LENGTH_LONG).show();
                                        removereplyAt(position);
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

    @Override
    public int getItemCount() {
        return c.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        WebView reply;
        TextView replyusername;
        ImageButton replyimagebtn,delrbtn;
        int userid;
        public PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.crv);
            reply=(WebView)itemView.findViewById(R.id.reply);
            replyusername=(TextView) itemView.findViewById(R.id.replyusername);
            replyimagebtn=(ImageButton)itemView.findViewById(R.id.replyimageButton);
            delrbtn=(ImageButton)itemView.findViewById(R.id.rdeletebtn);
        }
    }
}
