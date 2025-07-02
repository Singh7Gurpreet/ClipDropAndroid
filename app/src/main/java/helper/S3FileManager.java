package helper;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class S3FileManager {
    private static int BUFFER_SIZE = 4096;

    //Return false if not downloaded else true if downloaded
    public static boolean downloadFile(Context context) {
        CustomCallback<PersonalBinObject> myCallBack = new CustomCallback<PersonalBinObject>() {
            @Override
            public void onSuccess(PersonalBinObject result) {
                    new Thread(() -> {
                        try {
                            downloadFileHttpHandler(context,result.getLink(), result.getFileName()); // your network code
                            System.out.println("Done with downloading");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
            }

            @Override
            public void onFailure(PersonalBinObject errorMessage) {
                Log.e("Hello","Error from download file");
            }
        };
        PersonalBinApiWrapper.getFile(Authentication.getToken(),myCallBack);
        return false;
    }

    private static void downloadFileHttpHandler(Context context,String link, String fileName) throws IOException {
        System.out.println(link+fileName);
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Downloading failed because it is HTTP_NOT_OKAY");
        }
        InputStream is = conn.getInputStream();
        File file = new File(context.getFilesDir(), "text.txt");
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

    public static void uploadFile(URI uri,String mimeType) {

    }

    private static void uploadFileHelper() {

    }
}
