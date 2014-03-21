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

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.contracts.CommentData;
import is.grumpy.utils.StringHelper;

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
        public Holder(View view)
        {
            ButterKnife.inject(this, view);
        }

        @InjectView(R.id.commentProfilePicture) ImageView profilePicture;
        @InjectView(R.id.commentUserName)       TextView userName;
        @InjectView(R.id.comment)               TextView comment;
        @InjectView(R.id.commentCreatedAt)      TextView commentCreatedAt;
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

            holder = new Holder(row);
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
        holder.commentCreatedAt.setText(StringHelper.FormatDate(mContext, comment.getCreatedAt()));

        return row;
    }

    public void AddNewItem(CommentData data)
    {
        mComments.add(data);
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
