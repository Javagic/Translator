package com.ilya.translator.main;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ilya.translator.R;
import com.ilya.translator.databinding.AMainBinding;
import com.ilya.translator.fragments.BookmarkFragment;
import com.ilya.translator.fragments.SettingsFragment;
import com.ilya.translator.fragments.TranslateFragment;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.service.translation.TranslatorManager;
import com.ilya.translator.utils.CRUDService;
import com.ilya.translator.utils.Const;

import java.net.UnknownHostException;

import static com.ilya.translator.utils.Const.Prefs.APP_PREFS;
import static com.ilya.translator.utils.Const.Prefs.GET_LANGS;

/**
 * Основное активити, производится загрузка первоначальных данных
 * или заполняется из базы данных
 */
public class MainActivity extends AppCompatActivity {
    AMainBinding binding;
    TranslatorManager translatorManager;
    CRUDService crudService;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        binding = DataBindingUtil.setContentView(this, R.layout.a_main);
        bottomNavigationView = binding.bottomNavigation;
        crudService = CRUDService.getInstance(this);
        translatorManager = TranslatorManager.getInstance();
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
        loadInitialData();
    }

    private void loadInitialData() {
        SharedPreferences prefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        if (!prefs.getBoolean(GET_LANGS, false)) {
            ProgressDialog loading = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.please_wait), true);//set content
            translatorManager.loadLanguageVariations().subscribe(possibleLanguages -> {
                Log.i(Const.MY_LOG, "getlangs loaded");

                crudService.addLanguageTypes(translatorManager.getLanguageTypes());
                crudService.addPairs(translatorManager.getPairs());

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
                    loading.setTitle(getString(R.string.e_error));
                    loading.setMessage(getString(R.string.e_loading));
                }
                Log.i(Const.MY_LOG, "error getlangs" + throwable.getMessage());
            });
        } else {
            Log.i(Const.MY_LOG, "local getlangs");
            translatorManager.setLanguageTypes(crudService.getLanguageTypeList());
            translatorManager.setPairs(crudService.getPairs());
            TranslateFragment translateFragment = TranslateFragment.newInstance();//init
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, translateFragment)
                    .commit();
        }
    }

    public void setTranslateFragment(TextEntity textEntity) {
        translatorManager.setTextEntity(textEntity);
        bottomNavigationView.findViewById(R.id.action_lang).performClick();
    }
}
