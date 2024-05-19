package com.nexis.herobot.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.widget.Button;

import com.nexis.herobot.databinding.FragmentHomeBinding;
import com.nexis.herobot.ChatActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button buttonStartChat = binding.buttonStartChat;

        buttonStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChatActivity();
            }
        });

        return root;
    }

    private void startChatActivity() {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        startActivity(intent);
    }
}

// test