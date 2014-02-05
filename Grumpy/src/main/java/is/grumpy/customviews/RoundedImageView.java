package is.grumpy.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Arnar on 5.2.2014.
 */
public class RoundedImageView extends ImageView
{
    public RoundedImageView(Context context)
    {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Drawable drawable = getDrawable();

        if (drawable == null)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        Bitmap tempBitmap =  ((BitmapDrawable)drawable).getBitmap() ;
        Bitmap bitmap = tempBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap roundBitmap =  getCroppedBitmap(bitmap, getWidth());
        canvas.drawBitmap(roundBitmap, 0,0, null);
    }

    public Bitmap getCroppedBitmap(Bitmap bmp, int radius)
    {
        Bitmap sbmp;

        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setShadowLayer(2.0f, 0.0f, 3.0f, Color.BLACK);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f, sbmp.getWidth() / 2+0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }
}