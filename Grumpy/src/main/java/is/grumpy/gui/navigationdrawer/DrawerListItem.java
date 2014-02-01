package is.grumpy.gui.navigationdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import is.grumpy.R;
import is.grumpy.adapters.DrawerListAdapter;

/**
 * Created by Arnar on 27.1.2014.
 */
public class DrawerListItem implements IDrawerItem
{
    private final String mName;
    private final int mIcon;
    private boolean mMessageCounterVisible;

    public DrawerListItem(String name, int iconId, boolean messageCounterVisible)
    {
        this.mName = name;
        this.mIcon = iconId;
        this.mMessageCounterVisible = messageCounterVisible;
    }

    public String getName() { return this.mName; }

    @Override
    public int getViewType()
    {
        return DrawerListAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView)
    {
        View view;
        if (convertView == null)
        {
            view = inflater.inflate(R.layout.drawer_list_item, null);
        }
        else
        {
            view = convertView;
        }

        ((TextView) view.findViewById(R.id.drawer_action_id)).setText(mName);
        ((ImageView) view.findViewById(R.id.drawer_image)).setImageResource(mIcon);

        //TODO: Hard coded for now, will implement an async task to communicate with the REST API to get messages
        if(mMessageCounterVisible)
            ((TextView) view.findViewById(R.id.message_counter)).setText("10");
        else
            view.findViewById(R.id.message_counter).setVisibility(View.GONE);

        return view;
    }
}
