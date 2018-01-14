package fr.miage.barcodeproduct.listProduct;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.miage.barcodeproduct.model.Document;
import fr.miage.barcodeproduct.model.remote.BarcodeProductAPI;
import fr.miage.barcodeproduct.utils.ApplicationPreferences;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Download file from server and write it to disk
 */
public class DownloadFileTask extends AsyncTask<String, Integer, Boolean> {
    private static final String TAG = "DownloadFileTask";
    private Context context;

    DownloadFileTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.context = null;
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        this.context = null;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String documentId = params[0];
        return downloadFile(documentId);
    }

    private boolean downloadFile(String documentId) {
        boolean success = false;
        String path;

        ApplicationPreferences appPrefs = new ApplicationPreferences(context);

        Realm realm = Realm.getDefaultInstance();
        Document document = realm
                .where(Document.class)
                .equalTo("id", documentId)
                .findFirst();

        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("barcodeproduct_files", Context.MODE_PRIVATE);

            File file = new File(directory, document.getFileName());
            if (!file.exists()) {
                Response<ResponseBody> responseBody = BarcodeProductAPI
                        .getAuthInstance(appPrefs.getEmail(), appPrefs.getPassword())
                        .getApiService()
                        .downloadDocument(document.getProductSyncId(), document.getId())
                        .execute();

                if (responseBody.isSuccessful()) {
                    path = writeResponseBodyToDisk(responseBody.body(), file);
                    updateDocumentPath(document.getId(), path);
                    success = true;
                }
            }else{ // IF file already exists
                updateDocumentPath(document.getId(), file.getAbsolutePath());
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            realm.close();
            realm = null;
        }

        return success;
    }

    private void updateDocumentPath(final String documentId, final String newPath){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                Document document = realm
                        .where(Document.class)
                        .equalTo("id", documentId)
                        .findFirst();
                if(document != null){
                    document.setAbsolutePath(newPath);
                    document.setDownloaded(true);
                }
            }
        });
        realm.close();
    }

    /**
     * Write response to disk
     *
     * @param body content to write to disk
     * @param file that contains body
     * @return filename or null if error
     */
    private String writeResponseBodyToDisk(ResponseBody body, File file) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return file.getAbsolutePath();
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }
}
