package com.example.clipdrop;

import static androidx.core.content.ContextCompat.registerReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import helper.CustomCallback;
import helper.PersonalBinObject;
import helper.S3FileManager;
import helper.TYPE_OF_FILE;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class DownloadUploadFragment extends Fragment {

    StorageUploadBroadcast s = new StorageUploadBroadcast();
    private final ActivityResultLauncher<String> fileExplorerOpener =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                Intent intent = new Intent("com.example.STORAGE_BROADCAST");
                intent.putExtra("uri",uri);
                requireContext().sendBroadcast(intent);
            });

    public DownloadUploadFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter("com.example.STORAGE_BROADCAST");
        requireContext().registerReceiver(s, filter, Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    public void onDestroy() {
        requireContext().unregisterReceiver(s);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //if download does not exists smiply give the option to upload
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        FrameLayout contentContainer = view.findViewById(R.id.containerContent);

        CustomCallback<PersonalBinObject> callback = new CustomCallback<PersonalBinObject>() {
            @Override
            public void onSuccess(PersonalBinObject result) {
                requireActivity().runOnUiThread(() -> {
                    contentContainer.removeAllViews();
                    View downloadView = inflater.inflate(R.layout.fragment_dowload, container, false);
                    contentContainer.addView(downloadView);
                    Log.i("TASK","Found download link: "+result.getLink());
                });
            }

            @Override
            public void onFailure(PersonalBinObject errorMessage) {
                requireActivity().runOnUiThread(()->{
                contentContainer.removeAllViews();
                View uploadView = inflater.inflate(R.layout.fragment_upload,container,false);
                contentContainer.addView(uploadView);
                FrameLayout frame = uploadView.findViewById(R.id.frameLayout);
                frame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fileExplorerOpener.launch("*/*"); // For all file types
                    }
                });
                });
            }
        };
        S3FileManager.downloadFile(requireContext(),callback,TYPE_OF_FILE.STORAGE);
        return view;
    }
}