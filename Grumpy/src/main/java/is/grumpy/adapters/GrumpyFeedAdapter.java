package is.grumpy.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.security.spec.ECField;
import java.util.List;
import is.grumpy.R;
import is.grumpy.contracts.GrumpyFeedData;
import is.grumpy.utils.DateCleaner;

/**
 * Created by Arnar on 4.2.2014.
 */
public class GrumpyFeedAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutResourceId;
    private List<GrumpyFeedData> feed;

    public GrumpyFeedAdapter(Context context, int layoutResourceId, List<GrumpyFeedData> feed)
    {
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.feed = feed;
    }

    static class GrumpyFeedHolder
    {
        com.makeramen.RoundedImageView profilePicture;
        TextView userName;
        TextView post;
        TextView timeCreated;
        int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final GrumpyFeedHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GrumpyFeedHolder();
            holder.userName = (TextView) row.findViewById(R.id.grumpyFeedUserName);
            holder.profilePicture = (com.makeramen.RoundedImageView) row.findViewById(R.id.feedProfilePicture);
            holder.post = (TextView) row.findViewById(R.id.grumpyFeedPost);
            holder.timeCreated = (TextView) row.findViewById(R.id.grumpyFeedTimeCreated);
            row.setTag(holder);
        }
        else
        {
            holder = (GrumpyFeedHolder)row.getTag();
        }

        final GrumpyFeedData feed = getItem(position);

        Picasso.with(mContext)
                .load(feed.getProfilePicture())
                .noFade()
                .into(holder.profilePicture);

        holder.position = position;
        holder.userName.setText(feed.getUserName());
        holder.post.setText(feed.getPost());

        String posted;
        try {
            DateCleaner cleaner = new DateCleaner(feed.getTimeCreated(), mContext);
            posted = cleaner.getRelativeDate();
        } catch (Exception e) {
            posted = feed.getTimeCreated();
        }
        holder.timeCreated.setText(posted);

        return row;
    }

    public void AddNewItem(GrumpyFeedData data)
    {
        feed.add(0, data);
    }

    @Override
    public int getCount()
    {
        return (feed == null) ? 0 : feed.size();
    }

    @Override
    public GrumpyFeedData getItem(int position)
    {
        return feed.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return feed.indexOf(getItem(position));
    }
}
