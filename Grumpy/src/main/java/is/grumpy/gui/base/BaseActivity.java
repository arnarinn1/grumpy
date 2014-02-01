package is.grumpy.gui.base;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Created by Arnar on 27.1.2014.
 */
public class BaseActivity extends ActionBarActivity
{
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
