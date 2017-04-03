package com.ilya.translator;

import com.ilya.translator.Models.LanguageVariations;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 19:37.
 */
public interface HttpApi {

  @POST(Const.API_URL + "/getLanguages")
  Observable<Object> getLanguages(@Query("key") String key, @Query("ui") String langCode);

  @POST(Const.API_URL +"/detect")
  Observable<Object> detect(@Query("key") String key,
                            @Query("text") String text,
                            @Query("hint") String hint);

  @POST(Const.API_URL + "/translate")
  Observable<Object> translate(@Query("key") String key,
                               @Query("text") String text,
                               @Query("lang") String lang,
                               @Query("format") String plain,
                               @Query("options") String options);
}
