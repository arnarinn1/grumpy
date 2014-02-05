package is.grumpy.gui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import is.grumpy.R;
import is.grumpy.adapters.MessagesAdapter;
import is.grumpy.contracts.MessagesData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.rest.GrumpyClient;

/**
 * Created by Arnar on 5.2.2014.
 */
public class MessagesFragment extends BaseFragment
{
    private ListView mListView;

    public static MessagesFragment newInstance()
    {
        MessagesFragment fragment = new MessagesFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mListView = (ListView) getView().findViewById(R.id.messagesListView);

        new GetMessagesWorker().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_messages, container, false);
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
