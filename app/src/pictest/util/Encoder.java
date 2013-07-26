package pictest.util;

import java.io.ByteArrayOutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class Encoder {

	/**
	 * Decode string to image
	 * 
	 * @param imageString
	 *            The string to decode
	 * @return decoded image
	 */
	public static Drawable decodeToImage(String imageString, Resources res) {
		byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
		Drawable ret = null;
		ret = new BitmapDrawable(res, BitmapFactory.decodeByteArray(bytes, 0,
				bytes.length));
		return ret;
	}

	/**
	 * Encode image to string
	 * 
	 * @param image
	 *            The image to encode
	 * @param type
	 *            jpeg, bmp, ...
	 * @return encoded string
	 */
	public static String encodeToString(Drawable d) {
		Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bytes = stream.toByteArray();
		String ret = Base64.encodeToString(bytes, Base64.DEFAULT);
		return ret;
	}
}
