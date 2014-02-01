package is.grumpy.gui.navigationdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import is.grumpy.R;
import is.grumpy.adapters.DrawerListAdapter;

/**
 * Created by Arnar on 27.1.2014.
 */
public class DrawerHeader implements IDrawerItem
{
    private final String mName;

    public DrawerHeader(String name)
    {
        this.mName = name;
    }

    public String getName() { return this.mName; }

    @Override
    public int getViewType()
    {
        return DrawerListAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView)
    {
        View view;
        if (convertView == null)
        {
            view = inflater.inflate(R.layout.drawer_header, null);
        }
        else
        {
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.drawerHeaderText)).setText(mName);

        return view;
    }
}
