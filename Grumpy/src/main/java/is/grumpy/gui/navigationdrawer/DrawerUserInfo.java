package is.grumpy.gui.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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

            row.setTag(holder);
        }
        else
        {
            holder = (UserInfoHolder) row.getTag();
        }

        String userName = new Credentials(context).GetCacheToken(Credentials.mFullName);

        holder.userName.setText(userName);

        return row;
    }

    static class UserInfoHolder
    {
        TextView userName;
    }
}
