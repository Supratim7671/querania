package com.querania.querania;

/**
 * Created by Supratim on 06/05/2017.
 */
public class cardanswerdata {

    int qansweruserid;
    int qid;
    int answerid;
    String answer;
    String answeruserpic;
    String answerusername;
    int noofanswerlike;
    int noofanswerunlike;
    int noofreplies;
    String answserstatuslike;
    String answerstatusunlike;
    int sessionuserid1;
    int answerunlikeabtn;
    int answerlikeabtn;

    cardanswerdata(int qansweruserid,int qid,int answerid,String answer,String answeruserpic,String answerusername,int noofanswerlike,int noofanswerunlike,int noofreplies,String answerstatuslike,String answerstatusunlike,int sessionuserid1,int answerlikeabtn,int answerunlikeabtn)
    {
        this.qansweruserid=qansweruserid;
        this.qid=qid;
        this.answerid=answerid;
        this.answer=answer;
        this.answeruserpic=answeruserpic;
        this.answerusername=answerusername;
        this.noofanswerlike=noofanswerlike;
        this.noofanswerunlike=noofanswerunlike;
        this.noofreplies=noofreplies;
        this.answserstatuslike=answerstatuslike;
        this.answerstatusunlike=answerstatusunlike;
        this.sessionuserid1=sessionuserid1;
        this.answerlikeabtn=answerlikeabtn;
        this.answerunlikeabtn=answerunlikeabtn;

    }

}
