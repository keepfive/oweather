package com.massivekinetics.ow.data.tasks;

import android.os.AsyncTask;
import com.massivekinetics.ow.application.Configuration;
import com.massivekinetics.ow.application.IConfiguration;
import com.massivekinetics.ow.network.NetworkUtils;
import com.massivekinetics.ow.utils.StringUtils;

public class GetSessionTask extends AsyncTask<Void, Void, Boolean> {
    private static final int DEFAULT_TRIES = 4;

    private int tries;
    private LoadingListener<Boolean> listener;
    private IConfiguration IConfiguration;

    public GetSessionTask(){
        this(new Configuration(), new LoadingListener<Boolean>() {
            @Override
            public void onLoaded(Boolean result) {}

            @Override
            public void notifyStart() {}

            @Override
            public void notifyStop() {}
        }, DEFAULT_TRIES);
    }

    public GetSessionTask(IConfiguration IConfiguration, LoadingListener<Boolean> listener, int tries) {
        this.listener = listener;
        this.IConfiguration = IConfiguration;
        this.tries = tries;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.notifyStart();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return doTask();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        listener.onLoaded(result);
        listener.notifyStop();
    }

    public boolean doTask(){
        String session;
        boolean succeeded = false;

        if (!NetworkUtils.isOnline())
            return succeeded;

        while (tries >= 0) {
            session = NetworkUtils.getSession();
            succeeded = !StringUtils.isNullOrEmpty(session);
            if (succeeded) {
                IConfiguration.setActiveSession(session);
                break;
            }
            tries--;
        }

        return succeeded;
    }

}
