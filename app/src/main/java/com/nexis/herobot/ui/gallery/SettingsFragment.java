package com.nexis.herobot.ui.gallery;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nexis.herobot.databinding.FragmentSettingsBinding;
import com.nexis.herobot.databinding.FragmentSettingsBinding;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel galleryViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot(); // Hata burada çıkabilir, kontrol edelim

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        preferences = getActivity().getSharedPreferences("Settings", getActivity().MODE_PRIVATE);
        editor = preferences.edit();

        // Kullanıcının dil tercihini kontrol et
        String language = preferences.getString("My_Lang", "en");
        setLocale(language);

        Switch languageSwitch = binding.languageSwitch;
        languageSwitch.setChecked(language.equals("tr"));

        languageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setLocale("tr");
            } else {
                setLocale("en");
            }
            getActivity().recreate(); // Aktiviteyi yeniden başlat
        });

        return root; // Yine root döndürülüyor, sorun olmamalı
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        editor.putString("My_Lang", lang);
        editor.apply();
    }
}
