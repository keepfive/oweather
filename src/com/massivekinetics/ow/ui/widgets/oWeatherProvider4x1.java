package com.massivekinetics.ow.ui.widgets;

public class oWeatherProvider4x1{}/* extends AppWidgetProvider {

    public static final String ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    public static final String WARNING = "AHTUNG!!! HARD CODE IN HERE!!! GO AWAY AND SAVE YOUR SOUL!!!";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SimpleDateFormat dataFormatter = new SimpleDateFormat("dd.MM.yyyy");
        // Get all ids
        ComponentName thisWidget = new ComponentName(context, oWeatherProvider4x1.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            WeatherModel current = null;

            try{
                current = Application.getInstance().getDataManager().getCurrentWeather();
            }catch (Throwable e){

            }

            String cityStr = Application.getInstance().getConfiguration().getLocationName();
            String todayDate = "Today / " + dataFormatter.format(new Date());
            String weatherDescStr = "";
            String currentTemp = "";
            String nightTempStr = "";
            int backgroundColor = R.color.background;
            int weatherImage = R.drawable.weather_state_1;

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_img_4x1);

            if (current != null && current != WeatherModel.NULL) {
                cityStr = (cityStr == null) ? "Unknown" : cityStr;
                weatherDescStr = current.getState().getDisplayName();
                currentTemp = current.getMaxTemperature();
                nightTempStr = current.getMinTemperature();
                WeatherState weatherState = current.getState();
                backgroundColor = ResourcesCodeUtils.getWeatherBackgroundColor(weatherState);
                weatherImage = ResourcesCodeUtils.getWeatherImageResource(weatherState);

                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferQualityOverSpeed = true;

                Bitmap weatherbitmap = BitmapFactory.decodeResource(Application.getInstance().getResources(), weatherImage, opt);
                BitmapDrawable weatherDrawable = new BitmapDrawable(Application.getInstance().getResources(), weatherbitmap);
                weatherDrawable.setAntiAlias(true);
                weatherDrawable.setFilterBitmap(true);

                Bitmap city = BitmapUtils.getStringAsBitmap(cityStr, 40, 4, 35, 11);
                Bitmap weatherDesc = BitmapUtils.getStringAsBitmap(weatherDescStr, 28, 10, 32, -1);


                Bitmap night = BitmapUtils.getStringAsBitmapWithSize("Night", 22, 0, 23, -1, 45, 25);

                boolean isTablet = false;
                try{
                    isTablet = BaseActivity.current.isTablet();
                }catch(Throwable e){}

                int xOffset = nightTempStr.length() > 1 ? (isTablet ? 10 : 5) : (isTablet ? 15 : 10);

                int nightTempTextSize = isTablet ? 24 : 27;
                Bitmap nightTemp = BitmapUtils.getStringAsBitmapWithSize(nightTempStr, nightTempTextSize, xOffset, 29, -1, 40, 25);

                xOffset = currentTemp.length() > 1 ? 7 : 60;
                xOffset = currentTemp.length() > 2 ? -25 : xOffset;
                int textSize = currentTemp.length() > 2 ? 102 : 110;

                Bitmap temp = null;
                if (isXhdpiDevice(context, isTablet)) {

                    temp = BitmapUtils.getStringAsBitmap2(currentTemp, 150, xOffset - 10, 115);
                } else {
                    int yOffset = isTablet ? 80 : 88;
                    temp = BitmapUtils.getStringAsBitmap2(currentTemp, 102, xOffset, yOffset);
                }
                remoteViews.setImageViewBitmap(R.id.ivCity, city);
                remoteViews.setImageViewBitmap(R.id.ivWeatherDesc, weatherDesc);
                remoteViews.setImageViewBitmap(R.id.ivTemp, temp);
                remoteViews.setImageViewBitmap(R.id.ivDaytime, night);
                remoteViews.setImageViewBitmap(R.id.ivNightTemp, nightTemp);
                remoteViews.setImageViewBitmap(R.id.ivWeather, weatherDrawable.getBitmap());
                remoteViews.setImageViewResource(R.id.ivDegrees, R.drawable.degrees);
                //remoteViews.setTextViewText(R.id.tvCity, cities[ran]);


            } else {
               fillEmptyWeather(remoteViews);
            }

            Bitmap date = BitmapUtils.getStringAsBitmap(todayDate, 20, 5, 28, -1);
            remoteViews.setImageViewBitmap(R.id.ivDate, date);
            remoteViews.setImageViewResource(R.id.background, backgroundColor);

            Intent configIntent = new Intent(context, UpdatePageActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.main, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }
    }

    private void fillEmptyWeather(RemoteViews remoteViews){

        Bitmap city = BitmapUtils.getStringAsBitmap("oW : No data", 40, 4, 35, 13);

        remoteViews.setImageViewBitmap(R.id.ivCity, city);
        remoteViews.setImageViewResource(R.id.ivTemp, R.drawable.icon);

        remoteViews.setImageViewResource(R.id.ivWeatherDesc, R.color.transparent);
        remoteViews.setImageViewResource(R.id.ivTemp, R.color.transparent);
        remoteViews.setImageViewResource(R.id.ivDaytime, R.color.transparent);
        remoteViews.setImageViewResource(R.id.ivNightTemp, R.color.transparent);
        remoteViews.setImageViewResource(R.id.ivWeather, R.color.transparent);
        remoteViews.setImageViewResource(R.id.ivDegrees, R.color.transparent);
    }

    private boolean isXhdpiDevice(Context context, boolean isTablet) {
        float density = context.getResources().getDisplayMetrics().densityDpi;
        return density != DisplayMetrics.DENSITY_LOW && density != DisplayMetrics.DENSITY_MEDIUM && density != DisplayMetrics.DENSITY_HIGH && !isTablet;
    }
}
                  */