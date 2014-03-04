package is.grumpy.gui.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import is.grumpy.R;
import is.grumpy.adapters.DrawerListAdapter;
import is.grumpy.cache.Credentials;

/**
 * Created by Arnar on 27.1.2014.
 */
public class DrawerUserInfo implements IDrawerItem
{
    private final String mName;

    public DrawerUserInfo(String name)
    {
        this.mName = name;
    }

    public String getName() { return this.mName; }

    @Override
    public int getViewType()
    {
        return DrawerListAdapter.RowType.USER_INFO.ordinal();
    }

    @Override
    public View getView(Context context, LayoutInflater inflater, View convertView)
    {
        View row = convertView;
        final UserInfoHolder holder;

        if (row == null)
        {
            row = inflater.inflate(R.layout.drawer_user_info, null);

            holder = new UserInfoHolder();
            holder.userName = (TextView) row.findViewById(R.id.drawerUserName);
            holder.avatar = (RoundedImageView) row.findViewById(R.id.avatar);

            row.setTag(holder);
        }
        else
        {
            holder = (UserInfoHolder) row.getTag();
        }

        Credentials credentials = new Credentials(context);
        String userName = credentials.GetCacheToken(Credentials.mFullName);
        String avatar = credentials.GetCacheToken(Credentials.mAvatar);

        holder.userName.setText(userName);

        Picasso.with(context)
               .load(avatar)
               .error(R.drawable.ic_launcher)
               .placeholder(R.drawable.ic_launcher)
               .into(holder.avatar);

        return row;
    }

    static class UserInfoHolder
    {
        TextView userName;
        RoundedImageView avatar;
    }
}
