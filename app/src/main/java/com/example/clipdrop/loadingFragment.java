package com.example.clipdrop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import helper.Authentication;
import helper.CustomCallback;

public class loadingFragment extends Fragment {

    MutableLiveData<String> token = new MutableLiveData<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe LiveData
        token.observe(getViewLifecycleOwner(), value -> {
            if (value != null) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_loadingFragment_to_homeFragment);
            }
        });

        // Trigger auth check
        CustomCallback<String> callback = new CustomCallback<String>() {
            @Override
            public void onSuccess(String result) {
                token.setValue(result);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("TOKEN", errorMessage);
            }
        };
        Authentication.initialize(requireContext(), callback);
    }
}