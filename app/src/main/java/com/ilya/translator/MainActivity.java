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
import android.support.v7.widget.Toolbar;

import com.ilya.translator.Models.PossibleLanguages;
import com.ilya.translator.databinding.ActivityMainBinding;
import com.ilya.translator.fragments.BookmarkFragment;
import com.ilya.translator.fragments.SettingsFragment;
import com.ilya.translator.fragments.TranslateFragment;

import java.util.Date;
import java.util.List;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TranslatorManager translatorManager;

    List<LanguageType> languages;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        dialog = new Dialog(this);
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
        translatorManager = TranslatorManager.getInstance();
        translatorManager.loadLanguageVariations().subscribe(possibleLanguages -> {
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

    private void initialize() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new TranslateFragment())
                .commit();

        languages = translatorManager.getLanguageTypes();
        binding.lang1.setText(translatorManager.getCurrentFrom().longName);//something default
        binding.lang2.setText(translatorManager.getCurrentTo().longName);
        //binding.setTextEntity(textEntity);
        LanguageAdapter.LanguageAdapterCallback listener = position -> {
            binding.lang1.setText(languages.get(position).longName);
            translatorManager.setCurrentFrom(languages.get(position));
            dialog.dismiss();
        };
        binding.lang1.setOnClickListener(view1 -> {
            dialog.setContentView(R.layout.dialog_language_layout);
            RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.language_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new LanguageAdapter(languages, listener));
            dialog.show();
        });
        LanguageAdapter.LanguageAdapterCallback listener2 = position -> {
            binding.lang2.setText(languages.get(position).longName);
            translatorManager.setCurrentTo(languages.get(position));
            dialog.dismiss();
        };
        binding.lang2.setOnClickListener(view1 -> {
            dialog.setContentView(R.layout.dialog_language_layout);
            RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.language_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new LanguageAdapter(languages, listener2));
            dialog.show();
        });
    }
}
