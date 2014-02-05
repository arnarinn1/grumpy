package is.grumpy.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

import is.grumpy.R;
import is.grumpy.contracts.MessagesData;

/**
 * Created by Arnar on 4.2.2014.
 */
public class MessagesAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutResourceId;
    private List<MessagesData> messages;

    public MessagesAdapter(Context context, int layoutResourceId, List<MessagesData> messages)
    {
        this.mContext = context;
        this.layoutResourceId = layoutResourceId;
        this.messages = messages;
    }

    static class GrumpyFeedHolder
    {
        TextView message;
        RelativeLayout layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        final GrumpyFeedHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new GrumpyFeedHolder();
            holder.message = (TextView) row.findViewById(R.id.message);
            holder.layout = (RelativeLayout) row.findViewById(R.id.messageLayout);

            row.setTag(holder);
        }
        else
        {
            holder = (GrumpyFeedHolder)row.getTag();
        }

        final MessagesData message = getItem(position);

        holder.message.setText(message.getUserMessage());

        if (message.getUserid() == 1)
        {
            holder.layout.setGravity(Gravity.RIGHT);
            holder.message.setBackground(mContext.getResources().getDrawable(R.drawable.message_background_user));
        }
        else
        {
            holder.layout.setGravity(Gravity.LEFT);
            holder.message.setBackground(mContext.getResources().getDrawable(R.drawable.message_background_me));
        }

        return row;
    }

    @Override
    public int getCount()
    {
        return (messages == null) ? 0 : messages.size();
    }

    @Override
    public MessagesData getItem(int position)
    {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return messages.indexOf(getItem(position));
    }
}
