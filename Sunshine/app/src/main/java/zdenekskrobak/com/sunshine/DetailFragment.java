package zdenekskrobak.com.sunshine;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zdenekskrobak.com.sunshine.data.WeatherContract;

/**
 * Detal fragment
 * <p>
 * Created by zskro on 08.07.2017.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String APP_TAG = " #Sunshine App";

    private static final int DETAIL_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_HUMIDITY = 5;
    private static final int COL_WEATHER_WIND_SPEED = 6;
    private static final int COL_WEATHER_DEGREES = 7;
    private static final int COL_WEATHER_PRESSURE = 8;

    private String mForecast;
    private ShareActionProvider mShareActionProvider;

    @BindView(R.id.date_textview)
    TextView mDateTv;
    @BindView(R.id.high_textview)
    TextView mHighTv;
    @BindView(R.id.low_textview)
    TextView mLowTv;
    @BindView(R.id.humidity_textview)
    TextView mHumidityTv;
    @BindView(R.id.wind_textview)
    TextView mWindTv;
    @BindView(R.id.pressure_textview)
    TextView mPressureTv;
    @BindView(R.id.icon)
    ImageView mIconIv;
    @BindView(R.id.forecast_textview)
    TextView mForecastTv;

    public static DetailFragment newInstance(String forecast) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DetailActivity.EXTRA_FORECAST, forecast);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        mForecast = getArguments().getString(DetailActivity.EXTRA_FORECAST);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mForecast != null) {
            inflater.inflate(R.menu.detail_fragment_menu, menu);

            MenuItem item = menu.findItem(R.id.share);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getArguments().getString(DetailActivity.EXTRA_FORECAST) + APP_TAG);
        shareIntent.setType("text/plain");
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }
        return new CursorLoader(getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        String weatherDescription = data.getString(COL_WEATHER_DESC);

        boolean isMetric = Utility.isMetric(getActivity());

        String high = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String low = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        String humidity = getString(R.string.format_humidity, data.getDouble(COL_WEATHER_HUMIDITY));
        String wind = Utility.getFormattedWind(getActivity(), data.getFloat(COL_WEATHER_WIND_SPEED), data.getFloat(COL_WEATHER_DEGREES));
        String pressure = getString(R.string.format_pressure, data.getDouble(COL_WEATHER_PRESSURE));
        int weatherIcon = Utility.getArtResourceForWeatherCondition(data.getInt(COL_WEATHER_ID));

        mDateTv.setText(dateString);
        mForecastTv.setText(weatherDescription);
        mHighTv.setText(high);
        mLowTv.setText(low);
        mHumidityTv.setText(humidity);
        mWindTv.setText(wind);
        mPressureTv.setText(pressure);
        mIconIv.setImageResource(weatherIcon);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
