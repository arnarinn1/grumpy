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
import is.grumpy.contracts.FollowingData;
import is.grumpy.contracts.UserData;

/**
 * Created by Arnar on 15.3.2014.
 */
public class FollowingAdapter extends BaseAdapter
{
    private static final int mLayoutResourceId = R.layout.listview_search_user;

    private Context mContext;
    private List<FollowingData> users;

    public FollowingAdapter(Context context, List<FollowingData> users)
    {
        this.mContext = context;
        this.users = users;
    }

    static class ViewHolder
    {
        TextView username;
        TextView fullname;
        ImageView profilePicture;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final ViewHolder holder;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.username = (TextView) row.findViewById(R.id.userName);
            holder.fullname = (TextView) row.findViewById(R.id.fullName);
            holder.profilePicture = (ImageView) row.findViewById(R.id.userProfilePicture);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }

        final UserData user = getItem(position).getUser();

        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getFirstName() + " " + user.getLastName());

        String avatar = user.getAvatar();

        Picasso.with(mContext)
                .load(avatar)
                .into(holder.profilePicture);

        return row;
    }

    @Override
    public int getCount()
    {
        return (users == null) ? 0 : users.size();
    }

    @Override
    public FollowingData getItem(int position)
    {
        return users.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return users.indexOf(getItem(position));
    }
}
