package is.grumpy.gui.base;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import is.grumpy.R;
import is.grumpy.adapters.DrawerListAdapter;
import is.grumpy.gui.navigationdrawer.DrawerHeader;
import is.grumpy.gui.navigationdrawer.DrawerListItem;
import is.grumpy.gui.navigationdrawer.DrawerUserInfo;
import is.grumpy.gui.navigationdrawer.IDrawerItem;

/**
 * Created by Arnar on 27.1.2014.
 */
public class BaseNavigationDrawer extends ActionBarActivity
{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void InitializeNavigationDrawer()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        List<IDrawerItem> items = new ArrayList<IDrawerItem>();

        items.add(new DrawerUserInfo("Arnar Heimisson"));
        items.add(new DrawerHeader("FAVORITES"));
        items.add(new DrawerListItem("Grumpy Feed", R.drawable.ic_home, false));
        items.add(new DrawerListItem("Following", R.drawable.ic_people, false));
        items.add(new DrawerListItem("Messages", R.drawable.ic_messages, true));
        items.add(new DrawerListItem("Search Users", R.drawable.ic_search, false));
        items.add(new DrawerHeader("SETTINGS"));
        items.add(new DrawerListItem("App Settings", R.drawable.ic_settings, false));
        items.add(new DrawerListItem("Edit Profile", R.drawable.ic_settings, false));

        mDrawerList.setAdapter(new DrawerListAdapter(this, items));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return super.onOptionsItemSelected(item) || mDrawerToggle.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
            //Initialize Fragment Manager
        }
    }
}
