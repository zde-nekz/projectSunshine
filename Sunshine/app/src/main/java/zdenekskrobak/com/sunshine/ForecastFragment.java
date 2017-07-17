package zdenekskrobak.com.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment with weather forecast
 * <p>
 * Created by zskro on 27.06.2017.
 */

public class ForecastFragment extends Fragment implements FetchWeatherTask.Callback, AdapterView.OnItemClickListener {

    @BindView(R.id.listview_forecast)
    ListView mListView;

    public ForecastFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, view);

        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecast_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                updateWeather();
                return true;
            case R.id.settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.location:
                openMap();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWeatherFetched(String[] dayForecasts) {
        List<String> weekForecast = new ArrayList<>(Arrays.asList(dayForecasts));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast,
                weekForecast);


        mListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String forecast = (String) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_FORECAST, forecast);
        startActivity(intent);
    }

    private void updateWeather() {

        String unit = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString("unit", "");
        String location = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString("location", "");


        new FetchWeatherTask(getActivity(), location, unit, this).execute();
    }

    private void openMap() {

        String location = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString("location", "");

        Uri uri = Uri.parse("geo:").buildUpon()
                .appendQueryParameter("q", location)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}