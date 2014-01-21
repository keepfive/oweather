package com.massivekinetics.ow.application;

/**
 * Created by bovy on 12/8/13.
 */
public class Session {
    private String mToken = "";

    private Session(){

    }

    public static Session Current(){
        return SessionHolder.sessionInstance;
    }
    public String getToken(){
        return mToken;
    }

    public void setToken(String token){
        mToken = token;
    }


    private static class SessionHolder{
        public static Session sessionInstance = new Session();
    }
}
