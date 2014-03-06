package is.grumpy.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import is.grumpy.R;
import is.grumpy.contracts.CommentData;
import is.grumpy.utils.DateCleaner;

/**
 * Created by Arnar on 6.3.2014.
 */
public class CommentsAdapter extends BaseAdapter
{
    private static final int mLayoutResourceId = R.layout.listview_comments;
    private List<CommentData> mComments;
    private Context mContext;

    public CommentsAdapter(Context context, List<CommentData> comments)
    {
        this.mContext = context;
        this.mComments = comments;
    }

    static class Holder
    {
        ImageView profilePicture;
        TextView userName;
        TextView comment;
        TextView commentCreatedAt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final Holder holder;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new Holder();
            holder.profilePicture = (ImageView) row.findViewById(R.id.commentProfilePicture);
            holder.userName = (TextView) row.findViewById(R.id.commentUserName);
            holder.comment = (TextView) row.findViewById(R.id.comment);
            holder.commentCreatedAt = (TextView) row.findViewById(R.id.commentCreatedAt);
            row.setTag(holder);
        }
        else
        {
            holder = (Holder) row.getTag();
        }

        final CommentData comment = getItem(position);

        Picasso.with(mContext)
                .load(comment.getUser().getAvatar())
                .into(holder.profilePicture);

        holder.userName.setText(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());
        holder.comment.setText(comment.getComment());
        holder.commentCreatedAt.setText(FormatDate(comment.getCreatedAt()));

        return row;
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
        return mComments.size();
    }

    @Override
    public CommentData getItem(int position)
    {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return mComments.indexOf(getItem(position));
    }
}
