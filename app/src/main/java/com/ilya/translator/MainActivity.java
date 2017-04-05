package com.ilya.translator;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ilya.translator.fragments.BookmarkFragment;
import com.ilya.translator.fragments.SettingsFragment;
import com.ilya.translator.fragments.TranslateFragment;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, TranslateFragment.newInstance()).commit();
    BottomNavigationView bottomNavigationView = (BottomNavigationView)
        findViewById(R.id.bottom_navigation);

    bottomNavigationView.setOnNavigationItemSelectedListener
        (item -> {
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

    /*button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
    RxBackgroundWrapper.doInBackground(
        HttpService.getInstance().getHttpApi().getLanguages(API_KEY, "en")).subscribe(new Subscriber<Object>() {
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
