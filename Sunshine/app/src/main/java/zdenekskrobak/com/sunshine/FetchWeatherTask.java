package zdenekskrobak.com.sunshine;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 * Task to get weather on background thread
 * <p>
 * Created by zskro on 27.06.2017.
 */

public class FetchWeatherTask extends AsyncTask<Void, Void, String> {

    public static String URL_BASE = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Praha&mode=json&units=metric&cnt=7&APPID=";

    private WeakReference<Context> context;
    private Callback mListener;

    public interface Callback {
        void onWeatherFetched(String response);
    }

    public FetchWeatherTask(Callback listener) {
        this.mListener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return Utils.makeNetworkCall(URL_BASE + context.get().getString(R.string.weather_key));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (mListener != null) {
            mListener.onWeatherFetched(s);
        }
    }
}
