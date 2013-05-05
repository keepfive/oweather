package com.massivekinetics.ow.data.tasks;

import android.os.AsyncTask;
import com.massivekinetics.ow.data.manager.ConfigManager;
import com.massivekinetics.ow.network.NetworkUtils;
import com.massivekinetics.ow.utils.StringUtils;

public class GetSessionTask extends AsyncTask<Void, Void, Boolean> {

    private int tries;
    private LoadingListener<Boolean> listener;
    private ConfigManager configManager;

    public GetSessionTask(ConfigManager configManager, LoadingListener<Boolean> listener, int tries) {
        this.listener = listener;
        this.configManager = configManager;
        this.tries = tries;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.notifyStart();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String session;
        boolean succeeded = false;

        if (!NetworkUtils.isOnline())
            return succeeded;

        while (tries >= 0) {
            session = NetworkUtils.getSession();
            succeeded = !StringUtils.isNullOrEmpty(session);
            if (succeeded) {
                configManager.setActiveSession(session);
                break;
            }
            tries--;
        }

        return succeeded;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        listener.callback(result);
        listener.notifyStop();
    }

}
