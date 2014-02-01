package is.grumpy.gui.navigationdrawer;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Arnar on 27.1.2014.
 */
public interface IDrawerItem
{
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
