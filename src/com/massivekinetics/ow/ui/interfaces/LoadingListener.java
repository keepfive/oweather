package com.massivekinetics.ow.ui.interfaces;


public interface LoadingListener<T> {
	void onLoaded(T result);
	void notifyStart();
	void notifyStop();
}
