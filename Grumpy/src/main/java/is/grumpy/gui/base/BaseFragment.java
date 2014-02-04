package is.grumpy.gui.base;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by Arnar on 4.2.2014.
 */
public class BaseFragment extends Fragment
{
    protected IActivity IActivity;

    @Override
    public void onAttach (Activity activity)
    {
        super.onAttach(activity);
        try
        {
            this.IActivity = (IActivity) activity;
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
