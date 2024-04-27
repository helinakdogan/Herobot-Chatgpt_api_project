package com.nexis.herobot.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nexis.herobot.databinding.FragmentHomeBinding;
import com.nexis.herobot.ChatActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Butona tıklandığında ChatActivity'yi başlat
        binding.startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatActivity();
            }
        });

        return root;
    }

    // ChatActivity'i başlatmak için metod
    private void startChatActivity() {
        // Intent ile ChatActivity'i başlat
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        startActivity(intent);
    }
}
