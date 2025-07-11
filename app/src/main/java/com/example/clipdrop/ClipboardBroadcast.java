package com.example.clipdrop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import helper.CustomCallback;
import helper.PersonalBinObject;
import helper.S3FileManager;
import helper.TYPE_OF_FILE;

public class ClipboardBroadcast extends BroadcastReceiver {
    Context myContext;
    public static String FILE_NAME_STORAGE = "text.txt";
    public static String content = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.CLIPBOARD_CONTENT".equals(intent.getAction())) {
            String text = intent.getStringExtra("data");
            myContext = context;
            if (text == null || text.isEmpty() || text.equals(content)) {
                // Start download and handle everything inside the callback
                S3FileManager.downloadFile(myContext, new CustomCallback<PersonalBinObject>() {
                    @Override
                    public void onSuccess(PersonalBinObject result) {

                        new Thread(()->{
                                S3FileManager.downloadFileHttpHandler(myContext,result.getLink(),result.getFileName(),TYPE_OF_FILE.CLIPBOARD);
                                Log.i("TASK","DONE WITH DOWNLOADING");
                                String fileContent = readFile();
                                content = fileContent;
                                pasteToClipboard(fileContent);
                        }).start();
                    }

                    @Override
                    public void onFailure(PersonalBinObject errorMessage) {
                        Log.e("FILE DOWNLOAD FAILED", String.valueOf(errorMessage));
                    }
                }, TYPE_OF_FILE.CLIPBOARD);
            } else {
                content = text;
                writeToFile(text);
                upload();
            }
        }
    }

    private void writeToFile(String content) {
        try {
            File file = new File(myContext.getFilesDir(), FILE_NAME_STORAGE);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            Log.d("Upload", "File written at: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void upload() {
        File file = new File(myContext.getFilesDir(), FILE_NAME_STORAGE);

        Uri uri = FileProvider.getUriForFile(
                myContext,
                myContext.getPackageName() + ".fileprovider",
                file
        );
        S3FileManager.uploadFile(myContext, uri,TYPE_OF_FILE.CLIPBOARD);
    }


    private void pasteToClipboard(String content) {
        ClipboardManager clipboard = (ClipboardManager) myContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", content);
        clipboard.setPrimaryClip(clip);
    }

    private String readFile() {
        StringBuilder builder = new StringBuilder();
        try {
            File file = new File(myContext.getFilesDir(), FILE_NAME_STORAGE);
            FileInputStream fin = new FileInputStream(file);
            int chr;
            while ((chr = fin.read()) != -1) {
                builder.append((char) chr);
            }
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
