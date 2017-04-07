package com.ilya.translator;

import android.os.Parcelable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ilya.translator.Models.LanguageVariations;
import com.ilya.translator.fragments.BookmarkFragment;
import com.ilya.translator.fragments.SettingsFragment;
import com.ilya.translator.fragments.TranslateFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.ilya.translator.Const.API_KEY;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        languages = new ArrayList<>();

        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.bottom_navigation);

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
        RxBackgroundWrapper.doInBackground(
                HttpService.getInstance().getHttpApi().getLanguages(API_KEY, "en"))
                .subscribe(languageVariations -> {
                    languages.addAll(languageVariations.langs.values());
                    TranslateFragment translateFragment = TranslateFragment.newInstance();
                    Observable.just(languageVariations.langs.values())
                            .flatMapIterable(strings -> strings)
                            .map(LanguageType::new)
                            .toList()
                            .subscribe(languageTypes -> {
                                Bundle bundle = new Bundle();
                                ArrayList<LanguageType> arrayList = new ArrayList<LanguageType>(languageTypes);
                                bundle.putParcelableArrayList("languages", arrayList);
                                translateFragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame_layout, translateFragment)
                                        .commit();
                            }, throwable -> {

                            });
                }, throwable -> {

                });
    /*button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

    RxBackgroundWrapper.doInBackground(HttpService.getInstance().getHttpApi().detect(API_KEY,"русский","")).subscribe(
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

    RxBackgroundWrapper.doInBackground(HttpService.getInstance().getHttpApi().translate(API_KEY,"русский","ru-en","plain","1")).subscribe(
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
    }
}
