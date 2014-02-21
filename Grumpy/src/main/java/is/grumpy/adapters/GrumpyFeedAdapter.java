package is.grumpy.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageButton showOptions;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
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
            holder.showOptions = (ImageButton) row.findViewById(R.id.postOptions);
            row.setTag(holder);
        }
        else
        {
            holder = (GrumpyFeedHolder)row.getTag();
        }

        final GrumpyFeedData feed = getItem(position);

        String picture = feed.getUser().getAvatar();

        Picasso.with(mContext)
                .load(picture == null ? "https://notendur.hi.is/~arh36/Grumpy/rest/api/arnar2.jpg" : picture)
                .noFade()
                .into(holder.profilePicture);

        holder.userName.setText(feed.getUser().getFullName());
        holder.post.setText(feed.getPost());

        String postedAt = FormatDate(feed.getTimeCreated());
        holder.timeCreated.setText(postedAt);

        holder.showOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RemoveItem(position);
            }
        });

        return row;
    }

    public void AddNewItem(GrumpyFeedData data)
    {
        feed.add(0, data);
    }

    //TODO: This is just to show functionality.  Only a creator of a post can destroy it.
    public void RemoveItem(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, 2);
        builder
            .setTitle("Destroy Post")
            .setMessage("Are you sure you want to destroy that post ?")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    feed.remove(position);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        AlertDialog alert = builder.show();
        alert.show();
    }

    private String FormatDate(String timeCreated)
    {
        String posted;
        try
        {
            DateCleaner cleaner = new DateCleaner(timeCreated, mContext);
            posted = cleaner.getRelativeDate();
        }
        catch (Exception e)
        {
            posted = timeCreated;
        }

        return posted;
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
