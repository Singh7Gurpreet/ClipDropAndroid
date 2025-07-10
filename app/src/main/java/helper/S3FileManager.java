package helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
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
    public static boolean downloadFile(Context context,CustomCallback<PersonalBinObject> myCallback,TYPE_OF_FILE type) {
        CustomCallback<PersonalBinObject> localCallback = new CustomCallback<PersonalBinObject>() {
            @Override
            public void onSuccess(PersonalBinObject result) {
                    new Thread(() -> {
                            myCallback.onSuccess(result);
                    }).start();
            }

            @Override
            public void onFailure(PersonalBinObject errorMessage) {
                Log.e("Hello","Error from download file");
                myCallback.onFailure(null);
            }
        };
        PersonalBinApiWrapper.getFile(type,Authentication.getToken(),localCallback);
        return false;
    }

    public static void downloadFileHttpHandler(Context context, String link, String fileName) {
        new Thread(() -> {
            try {
                assert (link != null);
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("Downloading failed: HTTP_NOT_OK");
                }

                InputStream is = conn.getInputStream();


                File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS); // You can choose another dir
                if (dir != null && !dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(dir, fileName);

                try (
                        BufferedInputStream bufferedInput = new BufferedInputStream(is);
                        FileOutputStream fileOutput = new FileOutputStream(file);
                        BufferedOutputStream bufferOutput = new BufferedOutputStream(fileOutput)
                ) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = bufferedInput.read(buffer, 0, buffer.length)) != -1) {
                        bufferOutput.write(buffer, 0, bytesRead);
                    }
                    bufferOutput.flush();
                }

                conn.disconnect();
                Log.d("Download", "File saved at: " + file.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public static void uploadFile(Context context, Uri uri,TYPE_OF_FILE type) {
       String fileName = getFileNameFromUri(context,uri);

       String mimeType = getMimeTypeFromUri(context,uri);
        new Thread(()->{
            String link = PersonalBinApiWrapper.postFile(type, Authentication.getToken(),fileName);

            if(link == null) {
                Log.e("A3 ERROR","Link not available");
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

            //long totalLength = getFileSizeFromUri(context,uri);
            // Open input stream from the file URI
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
//
            // Write file data to the output stream
            try (BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream())) {
                byte[] buffer = new byte[16824]; // 8 KB buffer
                int bytesRead;
//                long totalBytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);

//                    totalBytesRead += bytesRead;
//                    int progress = (int) ((totalBytesRead/(float)totalLength) * 100);
//
//                    Log.i("Downloaded", String.valueOf(progress));
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

    public static String getFileNameFromUri(Context context, Uri uri) {
        String fileName;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
            cursor.close();
        } else {
            fileName = "NameNotFound";
        }
        return fileName;
    }

    public static String getMimeTypeFromUri(Context  context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);

        if (mimeType == null) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return mimeType;
    }

    public static long getFileSizeFromUri(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        long size = -1;

        if (cursor != null) {
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            if (sizeIndex != -1 && cursor.moveToFirst()) {
                size = cursor.getLong(sizeIndex);
            }
            cursor.close();
        }

        return size;
    }

}
