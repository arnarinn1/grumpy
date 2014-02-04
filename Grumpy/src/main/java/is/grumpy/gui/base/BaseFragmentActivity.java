package is.grumpy.gui.base;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Arnar on 4.2.2014.
 */
public class BaseFragmentActivity extends Activity implements IActivity
{
    @Override
    public Context context()
    {
        return this;
    }
}
