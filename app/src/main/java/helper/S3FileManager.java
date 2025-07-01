package helper;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class S3FileManager {
    private static int BUFFER_SIZE = 4096;

    //Return false if not downloaded else true if downloaded
    public static boolean downloadFile() {
        PersonalBinObject object = PersonalBinApiWrapper.getFile(Authentication.getToken());
        if (object != null) {
            String link = object.getLink();
            String fileName = object.getFileName();
            try {
                downloadFileHttpHandler(link, fileName);
                return true;
            } catch (IOException e) {
                Log.e("Downloading file error", e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static void downloadFileHttpHandler(String link, String fileName) throws IOException {
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Downloading failed because it is HTTP_NOT_OKAY");
        }
        InputStream is = conn.getInputStream();

        try (
                BufferedInputStream bufferedInput = new BufferedInputStream(is);
                FileOutputStream fileOutput = new FileOutputStream(fileName);
                BufferedOutputStream bufferOutput = new BufferedOutputStream(fileOutput);
        ) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = bufferedInput.read(buffer, 0, BUFFER_SIZE)) != -1) {
                bufferOutput.write(buffer, 0, BUFFER_SIZE);
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
