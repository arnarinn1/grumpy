package is.grumpy.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import is.grumpy.contracts.FeedData;

/**
 * Created by Arnar on 7.3.2014.
 */
public class StringHelper
{
    public static void SetCommentLikeLogic(FeedData feed, TextView commentLikeTextView)
    {
        String commentLikes = null;
        String commentArray[] = {"comment", "comments"};
        String likeArray[] = {"like", "likes"};

        if(feed.getComments().size() > 0)
        {
            int commentSize = feed.getComments().size();
            commentLikes = String.format("%d %s", commentSize, commentArray[commentSize == 1 ? 0 : 1]);
        }

        if (feed.getLikes().size() > 0)
        {
            int likesSize = feed.getLikes().size();

            if (commentLikes != null)
                commentLikes += (String.format(" %d %s", likesSize, likeArray[likesSize == 1 ? 0 : 1]));
            else
                commentLikes = String.format("%d %s", likesSize, likeArray[likesSize == 1 ? 0 : 1]);
        }

        if (commentLikes != null)
            commentLikeTextView.setText(commentLikes);

        commentLikeTextView.setVisibility(commentLikes == null ? View.GONE : View.VISIBLE);
    }

    public static String FormatDate(Context context, String timeCreated)
    {
        String posted;
        try
        {
            DateCleaner cleaner = new DateCleaner(timeCreated, context);
            posted = cleaner.getRelativeDate();
        }
        catch (Exception e)
        {
            posted = timeCreated;
        }

        return posted;
    }
}
