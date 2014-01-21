package com.massivekinetics.ow.domain.tasks;

import android.os.AsyncTask;
import com.massivekinetics.ow.application.Configuration;
import com.massivekinetics.ow.application.IConfiguration;
import com.massivekinetics.ow.services.network.NetworkService;
import com.massivekinetics.ow.services.utils.StringUtils;
import com.massivekinetics.ow.ui.interfaces.LoadingListener;

public class GetSessionTask extends AsyncTask<Void, Void, Boolean> {
    private static final int DEFAULT_TRIES = 4;

    private int maxTries;
    private LoadingListener<Boolean> listener;
    private IConfiguration configuration;

    public GetSessionTask() {
        this(Configuration.INSTANCE(), null, DEFAULT_TRIES);
    }

    public GetSessionTask(IConfiguration configuration, LoadingListener<Boolean> listener, int maxTries) {
        this.listener = listener;
        this.configuration = configuration;
        this.maxTries = maxTries;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null)
            listener.notifyStart();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return doTask();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onLoaded(result);
            listener.notifyStop();
        }
    }

    public boolean doTask() {
        String session;
        boolean succeeded = false;

        if (!NetworkService.isOnline())
            return succeeded;
        int tries = 0;

        while (tries <= maxTries) {
            session = NetworkService.getSession();
            succeeded = !StringUtils.isNullOrEmpty(session);
            if (succeeded) {
                configuration.setActiveSession(session);
                break;
            }
            tries++;
        }

        return succeeded;
    }

}
