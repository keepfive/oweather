package com.massivekinetics.ow.activities;

import android.app.Activity;

public class OWActivity extends Activity {
	public static OWActivity current;
	
	@Override
	protected void onResume() {
		super.onResume();
		current = this;
	}
}
