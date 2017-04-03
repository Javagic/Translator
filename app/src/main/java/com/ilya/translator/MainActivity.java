package com.ilya.translator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ilya.translator.Models.LanguageVariations;

import rx.Subscriber;

import static com.ilya.translator.Const.API_KEY;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button button = (Button) findViewById(R.id.btn);
    button.setOnClickListener(new View.OnClickListener() {
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
    });
  }
}
