package com.example.clipdrop;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    ViewPager2 viewPager;
    MyViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("NOTIFICATION", "Permission granted!");
                    NotificationUtils.showNotification(requireContext());
                } else {
                    Log.e("NOTIFICATION", "Permission denied.");
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        NotificationUtils.createNotificationChannel(requireContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                NotificationUtils.showNotification(requireContext());
            }
        } else {
            NotificationUtils.showNotification(requireContext());
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPagerHandler(view);
    }

    private void viewPagerHandler(@NonNull View view) {
        viewPagerAdapter = new MyViewPagerAdapter(
                getActivity().getSupportFragmentManager(),
                getLifecycle()
        );
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPagerAdapter.addFragment(new DownloadUploadFragment());
        viewPagerAdapter.addFragment(new HistoryFragment());
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(
                tabLayout,
                viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0) {
                            tab.setText("Home");
                        } else if (position == 1) {
                            tab.setText("History");
                        }
                    }
                }).attach();
    }
}
