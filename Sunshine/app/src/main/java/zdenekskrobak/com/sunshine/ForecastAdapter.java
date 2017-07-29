package zdenekskrobak.com.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zdenekskrobak.com.sunshine.R;
import zdenekskrobak.com.sunshine.Utility;
import zdenekskrobak.com.sunshine.data.WeatherContract;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int type = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        switch (type) {
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.list_item_forecast_today;
                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId = R.layout.list_item_forecast;
                break;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();

        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(COL_WEATHER_ID);
        // Use placeholder image for now
        holder.iconView.setImageResource(R.mipmap.ic_launcher);

        // Read date from cursor
        long dateInMillis = cursor.getLong(COL_WEATHER_DATE);
        holder.dateView.setText(Utility.getFriendlyDayString(context, dateInMillis));

        // Read weather forecast from cursor
        String description = cursor.getString(COL_WEATHER_DESC);
        holder.descriptionView.setText(description);

        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(COL_WEATHER_MAX_TEMP);
        holder.highTempView.setText(Utility.formatTemperature(context, high, isMetric));

        // Read low temperature from cursor
        double low = cursor.getDouble(COL_WEATHER_MIN_TEMP);
        holder.lowTempView.setText(Utility.formatTemperature(context, low, isMetric));
    }


    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(mContext, high, isMetric) + "/" + Utility.formatTemperature(mContext, low, isMetric);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {

        String highAndLow = formatHighLows(
                cursor.getDouble(COL_WEATHER_MAX_TEMP),
                cursor.getDouble(COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(COL_WEATHER_DATE)) +
                " - " + cursor.getString(COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        @BindView(R.id.list_item_icon)
        public ImageView iconView;
        @BindView(R.id.list_item_date_textview)
        public TextView dateView;
        @BindView(R.id.list_item_forecast_textview)
        public TextView descriptionView;
        @BindView(R.id.list_item_high_textview)
        public TextView highTempView;
        @BindView(R.id.list_item_low_textview)
        public TextView lowTempView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}