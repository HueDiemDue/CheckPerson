package vision.computer.client.hue.com.checkperson.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huedi on 4/24/2018.
 */

public class Utils {
    public static String TAG = "Utils";

    public static String saveImageFromBuffer(byte[] buffer) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/ImageReceivedServer");
        myDir.mkdirs();
        String fname = "Image_" + currentDateandTime + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.write(buffer);
            out.flush();
            out.close();
            Log.d(TAG, "save bitmap" + file.length());
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
