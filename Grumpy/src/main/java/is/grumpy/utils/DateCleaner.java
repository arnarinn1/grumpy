package is.grumpy.utils;

import android.content.Context;
import android.support.v7.appcompat.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by thdg9_000 on 15.2.2014.
 */
public class DateCleaner {

    private final Date date;
    private Context context;

    private final long MAX_HOURS = 12;

    public DateCleaner(String date, Context context) throws ParseException {
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        this.context = context;
    }

    public String getRelativeDate() {
        Date now = new Date();
        long time = now.getTime() - date.getTime();

        long hours = TimeUnit.MILLISECONDS.toHours(time);
        if (hours<this.MAX_HOURS)
            return hoursAgo(hours);

        long days = TimeUnit.MILLISECONDS.toDays(time);
        String hour = new SimpleDateFormat("HH:mm").format(this.date);
        if (days == 1)
            return yesterdayAt(hour);
        else
            return daysAgo(days);

    }

    private String yesterdayAt(String hour) {
        return String.format(context.getString(is.grumpy.R.string.yesterday_at), hour);
    }

    private String daysAgo(long days) {
        return String.format(context.getString(is.grumpy.R.string.days_ago), days);
    }

    private String hoursAgo(long hours) {
        return String.format(context.getString(is.grumpy.R.string.hours_ago), hours);
    }
}
