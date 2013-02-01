package com.massivekinetics.ow.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.massivekinetics.ow.R;
import com.massivekinetics.ow.data.WeatherForecast;
import com.massivekinetics.ow.data.WeatherModel;
import com.massivekinetics.ow.data.parser.ParserStatus;
import com.massivekinetics.ow.data.tasks.GetWeatherTask;
import com.massivekinetics.ow.data.tasks.LoadingListener;

public class MainActivity extends OWActivity {
	/**
	 * Called when the activity is first created.
	 */
	EditText city, days;
	TextView result;
	Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	private void init() {
		city = (EditText) findViewById(R.id.editText);
		days = (EditText) findViewById(R.id.editText1);
		result = (TextView) findViewById(R.id.tvResult);
		button = (Button) findViewById(R.id.btnGet);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String query = getString(R.string.weather_base_url_city);
				String key = getString(R.string.weather_key);
				String cityName = city.getText().toString();
				String numOfDays = days.getText().toString();

				query = query.replace("%CITY", cityName);
				query = query.replace("%DAYS", numOfDays);
				query = query.replace("%KEY", key);

				new GetWeatherTask(new LoadingListener<WeatherForecast>() {

					@Override
					public void notifyStop() {
					}

					@Override
					public void notifyStart() {
					}

					@Override
					public void callback(WeatherForecast result) {
						if (result.getStatus() == ParserStatus.ERROR)
							MainActivity.this.result.setText("Cant find info for your location");
						else {
							WeatherModel curr = result.getForecastList().get(0);

							MainActivity.this.result.setText("Curr in Kiev: " + curr.getHumidity() + " hum");
						}
					}
				});//.execute(cityName, numOfDays);
				

			}
		});

	}
}
