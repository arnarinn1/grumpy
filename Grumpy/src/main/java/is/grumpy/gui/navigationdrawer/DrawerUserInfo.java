package is.grumpy.gui.navigationdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import is.grumpy.R;
import is.grumpy.adapters.DrawerListAdapter;

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
    public View getView(LayoutInflater inflater, View convertView)
    {
        View view;
        if (convertView == null)
        {
            view = inflater.inflate(R.layout.drawer_user_info, null);
        }
        else
        {
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.drawerUserName)).setText(mName);

        return view;
    }
}
