package is.grumpy.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.adapters.MessagesAdapter;
import is.grumpy.contracts.MessagesData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.rest.GrumpyClient;

/**
 * Created by Arnar on 5.2.2014.
 */
public class MessagesFragment extends BaseFragment
{
    @InjectView(R.id.messagesListView) ListView mListView;

    public static MessagesFragment newInstance(int position)
    {
        MessagesFragment fragment = new MessagesFragment();

        Bundle args = new Bundle();
        args.putInt(BaseNavigationDrawer.DRAWER_POSITION, position);

        fragment.setRetainInstance(true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        new GetMessagesWorker().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    private class GetMessagesWorker extends AsyncTask<String, Void, List<MessagesData>>
    {
        @Override
        protected List<MessagesData> doInBackground(String... params)
        {
            return new GrumpyClient().GetMessages();
        }

        @Override
        protected void onPostExecute(List<MessagesData> messages)
        {
            if(messages != null)
            {
                MessagesAdapter adapter = new MessagesAdapter(IActivity.context(), R.layout.listview_messages, messages);

                mListView.setAdapter(adapter);
            }
        }
    }
}
