package com.example.clipdrop;

import android.content.Context;
import android.net.Uri;

import helper.S3FileManager;

public class UploadHelper {
    Uri uri;
    public ObserverForText fileName = new ObserverForText();

    public void setUri(Uri uri, Context context) {
        this.uri = uri;
        String temp = S3FileManager.getFileNameFromUri(context,uri);
        fileName.setText(temp);
    }

    public Uri getUri() {
        return uri;
    }

    public void observe(ObserverWorker observer) {
        fileName.register(observer);
    }
}
