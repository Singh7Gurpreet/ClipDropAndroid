package com.example.clipdrop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UploadFragment extends Fragment {
    StorageUploadBroadcast s = new StorageUploadBroadcast();
    Intent intent;
    private final ActivityResultLauncher<String> fileExplorerOpener =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::setUri);
    private Uri uri;

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return this.uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View uploadView = inflater.inflate(R.layout.fragment_upload,container,false);
        FrameLayout frame = uploadView.findViewById(R.id.frameLayout);
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileExplorerOpener.launch("*/*"); // For all file types
            }
        });

        Button button = uploadView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent("com.example.STORAGE_BROADCAST");
                intent.putExtra("uri",getUri());
                requireContext().sendBroadcast(intent);
            }
        });
        return uploadView;
    }

    @Override
    public void onDestroy() {
        requireContext().unregisterReceiver(s);
        super.onDestroy();
    }
}
