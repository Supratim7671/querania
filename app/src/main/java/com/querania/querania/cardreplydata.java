package com.querania.querania;

/**
 * Created by Supratim on 12/06/2017.
 */

public class cardreplydata {

    String replyuserpic;
    int qid;
    int answerid;
    int replyid;
    String replyusername;
    String answerreply;
    int answerreplyuserid;

    cardreplydata(String replyuserpic,int qid,int answerid,int replyid,String replyusername,String answerreply,int answerreplyuserid)
    {
        this.answerreplyuserid=answerreplyuserid;
        this.qid=qid;
        this.answerid=answerid;
        this.replyid=replyid;
        this.answerreply=answerreply;
        this.replyuserpic=replyuserpic;
        this.replyusername=replyusername;

    }
}
