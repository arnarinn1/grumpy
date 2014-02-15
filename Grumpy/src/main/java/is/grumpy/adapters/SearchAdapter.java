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
import is.grumpy.contracts.UserData;

/**
 * Created by Arnar on 15.2.2014.
 */
public class SearchAdapter extends BaseAdapter
{
    private static final int mLayoutResourceId = R.layout.listview_search_user;

    private Context mContext;
    private List<UserData> users;

    public SearchAdapter(Context context, List<UserData> users)
    {
        this.mContext = context;
        this.users = users;
    }

    static class UserHolder
    {
        TextView username;
        ImageView profilePicture;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final UserHolder holder;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new UserHolder();

            holder.username = (TextView) row.findViewById(R.id.userName);
            holder.profilePicture = (ImageView) row.findViewById(R.id.userProfilePicture);

            row.setTag(holder);
        }
        else
        {
            holder = (UserHolder) row.getTag();
        }

        final UserData user = getItem(position);

        holder.username.setText(user.getUsername());

        Picasso.with(mContext)
                .load("https://notendur.hi.is/~arh36/Grumpy/rest/api/arnar2.jpg")
                .into(holder.profilePicture);

        return row;
    }

    @Override
    public int getCount()
    {
        return (users == null) ? 0 : users.size();
    }

    @Override
    public UserData getItem(int position)
    {
        return users.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return users.indexOf(getItem(position));
    }
}
