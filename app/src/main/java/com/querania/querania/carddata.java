package com.querania.querania;

/**
 * Created by Supratim on 02/05/2017.
 */
public class carddata {

    String ques;
    int quserid;
    int id,sessionuserid,follow;
    String question_ask_name;
    int no_of_like;
    int no_of_unlike;
    String userpic;
    int no_of_answers;
    int editqbtn;
    int likeqbtn;
    int unlikeqbtn;


    String statuslike,statusunlike;

    carddata(String userpic,int id,int quserid,String ques,String question_ask_name,int no_of_like,int no_of_unlike,int no_of_answers,String statuslike,String statusunlike,int sessionuserid,int follow,int editqbtn,int likeqbtn,int unlikeqbtn){

        this.userpic=userpic;
        this.id=id;
        this.quserid=quserid;
        this.ques=ques;
        this.question_ask_name=question_ask_name;
        this.no_of_like=no_of_like;
        this.no_of_unlike=no_of_unlike;
        this.no_of_answers=no_of_answers;
        this.statuslike=statuslike;
        this.statusunlike=statusunlike;
        this.sessionuserid=sessionuserid;
        this.editqbtn=editqbtn;
        this.likeqbtn=likeqbtn;
        this.unlikeqbtn=unlikeqbtn;
        this.follow=follow;

    }



}
