package com.ilya.translator;

import com.google.gson.internal.LinkedTreeMap;
import com.ilya.translator.Models.LanguageVariations;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 19:38.
 */
public class HttpService {
  private static HttpService instance;

  private HttpApi httpApi;

  public static HttpService getInstance(){
    if(instance == null){
      instance = new HttpService();
    }
    return instance;
  }

  public HttpService() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Const.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
    httpApi = retrofit.create(HttpApi.class);
  }

  public HttpApi getHttpApi() {
    return httpApi;
  }

  //public Observable<LanguageVariations> getLanguages(String key, String langCode){
  //  return httpApi.getLanguages(key,langCode).flatMap(o -> {
  //
  //
  //  });
  //}
}