package com.massivekinetics.ow.data.tasks;


public interface LoadingListener<T> {
	void onLoaded(T result);
	void notifyStart();
	void notifyStop();
}
