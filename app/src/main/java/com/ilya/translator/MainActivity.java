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
    CRUDService crudService;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        binding = DataBindingUtil.setContentView(this, R.layout.a_main);
        bottomNavigationView = binding.bottomNavigation;
        crudService = CRUDService.getInstance(this);
        translatorService = TranslatorService.getInstance();
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
        SharedPreferences prefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);

        if (!prefs.getBoolean(GET_LANGS, false)) {
            ProgressDialog loading = ProgressDialog.show(this, "Загружаю данные", "Подождите пожалуйста...", true);//set content
            translatorService.loadLanguageVariations().subscribe(possibleLanguages -> {
                Log.i(Const.MY_LOG, "getlangs loaded");

                crudService.addLanguageTypes(translatorService.getLanguageTypes());

                TranslateFragment translateFragment = TranslateFragment.newInstance();//init
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, translateFragment)
                        .commit();

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
            TranslateFragment translateFragment = TranslateFragment.newInstance();//init
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, translateFragment)
                    .commit();
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

    public void setTranslateFragment(TextEntity textEntity) {
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
