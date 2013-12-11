package com.massivekinetics.ow.application;

/**
 * Created by bovy on 12/8/13.
 */
public class Session {
    private String mToken = "";
    private static Session instance;

    private Session(){}
    public static Session Current(){
        if (instance ==null){
            synchronized (Session.class){
                if (instance == null)
                    instance = new Session();
            }
        }
        return instance;
    }
    public String getToken(){
        return mToken;
    }

    public void setToken(String token){
        mToken = token;
    }

}
