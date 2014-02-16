package is.grumpy.gui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import is.grumpy.R;

/**
 * Created by Arnar on 16.2.2014.
 */
public class NewPostActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
}
