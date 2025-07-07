package helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class S3FileManager {
    private static int BUFFER_SIZE = 4096;
    //Return false if not downloaded else true if downloaded
    public static boolean downloadFile(Context context,CustomCallback<Boolean> myCallback,TYPE_OF_FILE type) {
        CustomCallback<PersonalBinObject> localCallback = new CustomCallback<PersonalBinObject>() {
            @Override
            public boolean onSuccess(PersonalBinObject result) {
                    new Thread(() -> {
                        try {
                            downloadFileHttpHandler(context,result.getLink(), result.getFileName()); // your network code
                            System.out.println("Done with downloading");
                            myCallback.onSuccess(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                            myCallback.onFailure(false);
                        }
                    }).start();
                return false;
            }

            @Override
            public boolean onFailure(PersonalBinObject errorMessage) {
                Log.e("Hello","Error from download file");
                return false;
            }
        };
        PersonalBinApiWrapper.getFile(type,Authentication.getToken(),localCallback);
        return false;
    }

    private static void downloadFileHttpHandler(Context context,String link, String fileName) throws IOException {
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Downloading failed because it is HTTP_NOT_OKAY");
        }
        InputStream is = conn.getInputStream();
        File file = new File(context.getFilesDir(), fileName);
        try (
                BufferedInputStream bufferedInput = new BufferedInputStream(is);
                FileOutputStream fileOutput = new FileOutputStream(file);
                BufferedOutputStream bufferOutput = new BufferedOutputStream(fileOutput);
        ) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = bufferedInput.read(buffer, 0, BUFFER_SIZE)) != -1) {
                bufferOutput.write(buffer, 0, bytesRead);
            }
            bufferOutput.flush();
        }
        conn.disconnect();
    }

    public static void uploadFile(Context context, Uri uri,TYPE_OF_FILE type) {
        String fileName;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
            cursor.close();
        } else {
            fileName = "";
        }

        String mimeType = context.getContentResolver().getType(uri);

        if (mimeType == null) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        new Thread(()->{
            String link = PersonalBinApiWrapper.postFile(type, Authentication.getToken(),fileName);

            if(link == null) {
                Log.e("Aws s3 link avail Error","Link not available");
            } else {
                uploadFileHelper(link,context,uri);
            }
        }).start();
    };

    private static void uploadFileHelper(String link, Context context, Uri uri) {
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            // Open input stream from the file URI
            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            // Write file data to the output stream
            try (BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream())) {
                byte[] buffer = new byte[8192]; // 8 KB buffer
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }

            inputStream.close();

            int responseCode = conn.getResponseCode();
            Log.d("UPLOAD", "Response Code: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
