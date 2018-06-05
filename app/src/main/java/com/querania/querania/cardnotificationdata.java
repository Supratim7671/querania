package com.querania.querania;

/**
 * Created by Supratim on 15/06/2017.
 */

public class cardnotificationdata {

    public int qid;
    public int nid;
    public String notification;
    public int noofnotification;

    cardnotificationdata(int qid,int nid,String notification,int noofnotification){

        this.qid=qid;
        this.nid=nid;
        this.notification=notification;
        this.noofnotification=noofnotification;
    }
}
