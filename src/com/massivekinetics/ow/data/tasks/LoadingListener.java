package com.massivekinetics.ow.data.tasks;


public interface LoadingListener<T> {
	void callback(T result);
	void notifyStart();
	void notifyStop();
}
