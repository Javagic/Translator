package com.ilya.translator;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ilya.translator.databinding.ActivityMainBinding;
import com.ilya.translator.fragments.BookmarkFragment;
import com.ilya.translator.fragments.SettingsFragment;
import com.ilya.translator.fragments.TranslateFragment;
import com.ilya.translator.models.LanguageType;
import com.ilya.translator.service.TranslatorService;
import com.ilya.translator.utils.RecyclerBindingAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TranslatorService translatorService;
    RecyclerView recyclerView;
    List<LanguageType> languages;
    RecyclerBindingAdapter<LanguageType> adapter;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_language_layout);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.language_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.action_lang:
                    selectedFragment = TranslateFragment.newInstance();
                    break;
                case R.id.action_bookmark:
                    selectedFragment = BookmarkFragment.newInstance();
                    break;
                case R.id.action_settings:
                    selectedFragment = SettingsFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        });
        translatorService = TranslatorService.getInstance();
        translatorService.loadLanguageVariations().subscribe(possibleLanguages -> {
            initialize();
        }, throwable -> {

        });


//region detect
        /*
    RxBackgroundWrapper.doInBackground(HttpService.getInstance().getTranslationApi().translate(TRANSLATION_API_KEY,"русский","ru-en","plain","1")).subscribe(
        new Subscriber<Object>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(Object o) {

          }
        });
      }
    });*/
        //endregion
    }

    private void initialize() { //TODO: отрефачить
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new TranslateFragment())
                .commit();
        languages = translatorService.getLanguageTypes();
        binding.lang1.setText(translatorService.getCurrentFrom().longName);//something default
        binding.lang2.setText(translatorService.getCurrentTo().longName);
        adapter = new RecyclerBindingAdapter<>(R.layout.item_language, BR.languageName, languages);
        recyclerView.setAdapter(adapter);
        RecyclerBindingAdapter.OnItemClickListener<LanguageType> listener1 = (position, item) -> {
            binding.lang1.setText(languages.get(position).longName);
            translatorService.setCurrentFrom(languages.get(position));
            dialog.dismiss();
        };
        binding.lang1.setOnClickListener(view1 -> {
            adapter.setOnItemClickListener(listener1);
            dialog.show();
        });
        RecyclerBindingAdapter.OnItemClickListener<LanguageType> listener2 = (position, item) -> {
            binding.lang2.setText(languages.get(position).longName);
            translatorService.setCurrentTo(languages.get(position));
            dialog.dismiss();
        };
        binding.lang2.setOnClickListener(view1 -> {
            adapter.setOnItemClickListener(listener2);
            dialog.show();
        });
    }
}
