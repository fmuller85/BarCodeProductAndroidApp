package fr.miage.barcodeproduct.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageUtils {
    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "BarcodeProduct_".concat(timeStamp).concat(".jpg");

        File outputDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BarCodeProduct");
        boolean directoryCreated = outputDir.mkdirs();
        if (directoryCreated) {
            Log.i("createImageFile", "Directory path : " + outputDir.getAbsolutePath() + " created !");
        }

        File newFile = new File(outputDir, imageFileName);
        Log.i("CURRENT PATH", newFile.getAbsolutePath());

        return newFile;
    }

    public static void galleryAddPic(Context context, String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static String getFileNameFromPath(String path){
        return path.substring(path.lastIndexOf("/")+1);
    }
}
