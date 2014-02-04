package is.grumpy.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import is.grumpy.R;
import is.grumpy.contracts.GrumpyFeedData;
import is.grumpy.utils.BitmapTask;

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
        ImageView profilePicture;
        TextView userName;
        TextView post;
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
            holder.profilePicture = (ImageView) row.findViewById(R.id.feedProfilePicture);
            holder.post = (TextView) row.findViewById(R.id.grumpyFeedPost);
            row.setTag(holder);
        }
        else
        {
            holder = (GrumpyFeedHolder)row.getTag();
        }

        final GrumpyFeedData feed = getItem(position);

        holder.profilePicture.setImageBitmap(null);
        holder.position = position;
        holder.userName.setText(feed.getUserName());
        holder.post.setText(feed.getPost());

        new ProfilePictureWorker(feed.getProfilePicture(), position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, holder);

        return row;
    }

    private class ProfilePictureWorker extends AsyncTask<GrumpyFeedHolder, Void, Bitmap>
    {
        private String posterUrl;
        private GrumpyFeedHolder holder;
        private int position;

        /**
         * @param posterUrl The url of a picture to download
         */
        public ProfilePictureWorker(String posterUrl, int position)
        {
            this.posterUrl = posterUrl;
            this.position = position;
        }

        @Override

        protected Bitmap doInBackground(GrumpyFeedHolder... view)
        {
            holder = view[0];
            return GetPoster();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (holder.position == position)
            {
                holder.profilePicture.setImageBitmap(bitmap);
            }
        }

        private Bitmap GetPoster()
        {
            try
            {
                return BitmapTask.getBitmapFromUrl(posterUrl);
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }
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
