package com.example.clipdrop;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.net.URI;

public class DownloadUploadFragment extends Fragment {
    private final ActivityResultLauncher<String> fileExplorerOpener =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                Log.i("URI", String.valueOf(uri));
            });

    public DownloadUploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_upload, container, false);

        FrameLayout frame = view.findViewById(R.id.frameLayout);

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileExplorerOpener.launch("*/*"); // For all file types
            }
        });

        return view;
    }
}