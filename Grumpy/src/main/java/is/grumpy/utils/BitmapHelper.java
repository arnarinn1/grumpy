package is.grumpy.utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import is.grumpy.rest.ExifUtil;

/**
 * Created by Arnar on 17.3.2014.
 */
public class BitmapHelper
{
    public static Bitmap getBitmapFromGallery(Activity activity, Uri selectedImage)
    {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap b = BitmapFactory.decodeFile(picturePath.toString(), options);
        return ExifUtil.rotateBitmap(picturePath.toString(), b);
    }

    public static String getBase64StringFromBitmap(Bitmap bitmap)
    {
        byte[] array = BitmapToByteArray(bitmap);
        return Base64.encodeToString(array, Base64.DEFAULT);
    }

    private static byte[] BitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
