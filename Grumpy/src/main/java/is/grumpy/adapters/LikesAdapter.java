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
import is.grumpy.contracts.LikeData;
import is.grumpy.utils.StringHelper;

/**
 * Created by Arnar on 8.3.2014.
 */
public class LikesAdapter extends BaseAdapter
{
    private static final int mLayoutResourceId = R.layout.listview_comments;
    private List<LikeData> mLikes;
    private Context mContext;

    public LikesAdapter(Context context, List<LikeData> comments)
    {
        this.mContext = context;
        this.mLikes = comments;
    }

    static class Holder
    {
        public Holder(View view)
        {
            ButterKnife.inject(this, view);
        }

        @InjectView(R.id.commentProfilePicture) ImageView profilePicture;
        @InjectView(R.id.commentUserName)       TextView userName;
        @InjectView(R.id.commentCreatedAt)      TextView likeCreatedAt;
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

        final LikeData like = getItem(position);

        Picasso.with(mContext)
                .load(like.getUser().getAvatar())
                .into(holder.profilePicture);

        holder.userName.setText(like.getUser().getFirstName() + " " + like.getUser().getLastName());
        holder.likeCreatedAt.setText(StringHelper.FormatDate(mContext, like.getCreatedAt()));

        return row;
    }

    @Override
    public int getCount()
    {
        return mLikes.size();
    }

    @Override
    public LikeData getItem(int position)
    {
        return mLikes.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return mLikes.indexOf(getItem(position));
    }
}
