package is.grumpy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;
import is.grumpy.gui.navigationdrawer.IDrawerItem;

/**
 * Created by Arnar on 27.1.2014.
 */
public class DrawerListAdapter extends ArrayAdapter<IDrawerItem>
{
    private LayoutInflater mInflater;

    public enum RowType
    {
        LIST_ITEM,
        HEADER_ITEM,
        USER_INFO
    }

    /**
     @param context This is the current context of the application activity
     @param items List of items to AddNewItem to ListView
     */
    public DrawerListAdapter(Context context, List<IDrawerItem> items)
    {
        super(context, 0, items);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getItem(position).getView(mInflater, convertView);
    }

    @Override
    public int getViewTypeCount()
    {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position)
    {
        return getItem(position).getViewType();
    }

    @Override
    public boolean isEnabled(int position)
    {
        return getItemViewType(position) == RowType.LIST_ITEM.ordinal();
    }
}
