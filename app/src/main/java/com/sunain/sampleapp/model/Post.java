package com.sunain.sampleapp.model;

public class Post
{
    String id,user,date,ur,p_name,p_type;
    public Post()
    {}
    public Post(String id,String user,String date,String ur,String p_name,String p_type)
    {
        this.id=id;
        this.date=date;
        this.user=user;
        this.ur=ur;
        this.p_name=p_name;
        this.p_type=p_type;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUr() {
        return ur;
    }

    public void setUr(String ur) {
        this.ur = ur;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }
}
