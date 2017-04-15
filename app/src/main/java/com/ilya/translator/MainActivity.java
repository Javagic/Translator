package com.ilya.translator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

import com.ilya.translator.databinding.AMainBinding;
import com.ilya.translator.fragments.BookmarkFragment;
import com.ilya.translator.fragments.SettingsFragment;
import com.ilya.translator.fragments.TranslateFragment;
import com.ilya.translator.models.LanguageType;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.service.TranslatorService;
import com.ilya.translator.utils.CRUDService;
import com.ilya.translator.utils.Const;
import com.ilya.translator.utils.RecyclerBindingAdapter;

import java.net.UnknownHostException;
import java.util.List;

import static com.ilya.translator.utils.Const.Prefs.APP_PREFS;
import static com.ilya.translator.utils.Const.Prefs.GET_LANGS;

public class MainActivity extends AppCompatActivity {
    AMainBinding binding;
    TranslatorService translatorService;
    RecyclerView recyclerView;
    CRUDService crudService;
    List<LanguageType> languages;
    RecyclerBindingAdapter<LanguageType> adapter;
    Dialog dialog;
    public TextEntity textEntity;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        binding = DataBindingUtil.setContentView(this, R.layout.a_main);
        setSupportActionBar(binding.toolbar);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_language_layout);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.language_recycler);
        textEntity = new TextEntity();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = binding.bottomNavigation;

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.action_lang:
                    selectedFragment = TranslateFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("textEntity", textEntity);
                    selectedFragment.setArguments(bundle);
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
        crudService = CRUDService.getInstance(this);
        translatorService = TranslatorService.getInstance();
        SharedPreferences prefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);

        if (!prefs.getBoolean(GET_LANGS, false)) {
            ProgressDialog loading = ProgressDialog.show(this, "Загружаю данные", "Подождите пожалуйста...", true);//set content
            translatorService.loadLanguageVariations().subscribe(possibleLanguages -> {
                Log.i(Const.MY_LOG, "getlangs loaded");

                crudService.addLanguageTypes(translatorService.getLanguageTypes());
                initialize();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(GET_LANGS, true);
                editor.apply();
                loading.dismiss();
            }, throwable -> {
                if (throwable instanceof UnknownHostException) {
                    loading.setTitle("Ошибка");
                    loading.setMessage("не удалось загрузить данные, проверьте интернет");
                }
                Log.i(Const.MY_LOG, "error getlangs" + throwable.getMessage());
            });
        } else {
            Log.i(Const.MY_LOG, "local getlangs");
            translatorService.setLanguageTypes(crudService.getLanguageTypeList());
            initialize();
        }


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
        setTranslateFragment(textEntity);
        binding.swapLanguage.setOnClickListener(view -> {
            translatorService.swapLanguages();
            binding.lang1.setText(translatorService.getCurrentInput().longName);
            binding.lang2.setText(translatorService.getCurrentOutput().longName);

        });
        languages = translatorService.getLanguageTypes();
        binding.lang1.setText(translatorService.getCurrentInput().longName);//something default
        binding.lang2.setText(translatorService.getCurrentOutput().longName);
        adapter = new RecyclerBindingAdapter<>(R.layout.item_language, BR.languageName, languages);
        recyclerView.setAdapter(adapter);
        RecyclerBindingAdapter.OnItemClickListener<LanguageType> listener1 = (position, item) -> {
            binding.lang1.setText(languages.get(position).longName);
            translatorService.setCurrentInput(languages.get(position));
            dialog.dismiss();
        };
        binding.lang1.setOnClickListener(view1 -> {
            adapter.setOnItemClickListener(listener1);
            dialog.show();
        });
        RecyclerBindingAdapter.OnItemClickListener<LanguageType> listener2 = (position, item) -> {
            binding.lang2.setText(languages.get(position).longName);
            translatorService.setCurrentOutput(languages.get(position));
            dialog.dismiss();
        };
        binding.lang2.setOnClickListener(view1 -> {
            adapter.setOnItemClickListener(listener2);
            dialog.show();
        });
    }

    private void saveLangs() {

    }

    public  void setTranslateFragment(TextEntity textEntity){
        TranslateFragment translateFragment = TranslateFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable("textEntity", textEntity);
        translateFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, translateFragment)
                .commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
