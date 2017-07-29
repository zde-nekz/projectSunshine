package zdenekskrobak.com.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Weather details
 * <p>
 * Created by zskro on 08.07.2017.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_FORECAST = "forecast";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String forecastStr = null;
        if (intent != null) {
            forecastStr = intent.getDataString();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, DetailFragment.newInstance(forecastStr)).commit();
    }
}
