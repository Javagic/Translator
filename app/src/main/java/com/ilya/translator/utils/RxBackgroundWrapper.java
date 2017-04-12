package com.ilya.translator.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 20:14.
 */
public class RxBackgroundWrapper {

  public static <T> Observable<T> doInBackground(final Observable<T> observable) {
    return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
  }

  <T> Observable.Transformer<T, T> applySchedulers() {
    return observable -> observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
