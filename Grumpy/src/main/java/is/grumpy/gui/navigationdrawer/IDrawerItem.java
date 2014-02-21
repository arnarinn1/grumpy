package is.grumpy.gui.navigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Arnar on 27.1.2014.
 */
public interface IDrawerItem
{
    public int getViewType();
    public View getView(Context context, LayoutInflater inflater, View convertView);
}
