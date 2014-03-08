package is.grumpy.utils;

import android.content.Context;

/**
 * Created by Arnar on 8.3.2014.
 */
public class ConversionHelper
{
    public static int PixelsToDp(Context context, int sizeInDp)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (sizeInDp*scale + 0.5f);
    }
}
