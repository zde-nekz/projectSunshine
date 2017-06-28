package zdenekskrobak.com.sunshine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ForecastFragment extends Fragment {

    @BindView(R.id.listview_forecast)
    ListView mListView;

    public ForecastFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        String[] forecastArray = {
                "Today - Sunny - 30",
                "Tomorrow - Storms - 35",
                "28th - Cloudy - 25",
                "29th - Sunny - 30",
                "30th - Cloudy - 30",
                "31st - Cloudy - 30",
                "1st - Sunny - 35"
        };
        List<String> weekForecast = new ArrayList<>(Arrays.asList(forecastArray));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast,
                weekForecast);


        mListView.setAdapter(adapter);

        return view;
    }
}