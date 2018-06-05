package com.querania.querania;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Supratim on 15/06/2017.
 */

public class RVnotificationAdapter extends RecyclerView.Adapter<RVnotificationAdapter.PersonViewHolder> {
    Activity main;
    List<cardnotificationdata> c;
    int s;
    int sessionid;
    String data;



    RVnotificationAdapter(List asd,Activity main){
        this.c=asd;
        this.main=main;


    }
    public void removeAt(int position) {
        c.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, c.size());
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notification, parent, false);
        RVnotificationAdapter.PersonViewHolder pvh = new RVnotificationAdapter.PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder,final int position) {

        PersonViewHolder pv = (PersonViewHolder) holder;


        holder.notification.setText(c.get(position).notification);
        holder.nid.setText(String.valueOf(c.get(position).nid));
        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);
                sessionid=sharedPref.getInt("sessionuserid", -1);
                s=sessionid;

                int nid=c.get(position).nid;
                data="{\"nid\":\""+nid+"\",\"sessionuserid\":\""+s+"\",\"notifbtn\":\"" + 1 + "\"}";
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://www.palzone.ml/services/delete_notification.php")
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
                                        Toast.makeText(main,"Successful in deleting notification",Toast.LENGTH_LONG).show();
                                        removeAt(position);
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

        CardView cv;
        TextView notification;
        TextView nid;
        ImageButton delbtn;

        public PersonViewHolder(View itemView) {


            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.crv);
            notification=(TextView)itemView.findViewById(R.id.notificationmessage);
            nid=(TextView)itemView.findViewById(R.id.nid);
            delbtn=(ImageButton)itemView.findViewById(R.id.delnotification);



        }
    }
}
