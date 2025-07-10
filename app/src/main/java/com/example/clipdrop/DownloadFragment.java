package com.example.clipdrop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import helper.PersonalBinObject;

public class DownloadFragment extends Fragment {
    private PersonalBinObject object;

    DownloadFragment(PersonalBinObject object) {
        super(R.layout.fragment_dowload);
        this.object = object;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
