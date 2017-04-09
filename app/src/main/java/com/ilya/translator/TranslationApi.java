package com.ilya.translator;

import com.ilya.translator.Models.LanguageTranslation;
import com.ilya.translator.Models.PossibleLanguages;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 19:37.
 */
public interface TranslationApi {

    @POST(Const.TRANSLATION_API_URL + "/getLangs")
    Observable<PossibleLanguages> getLanguages(@Query("key") String key, @Query("ui") String langCode);

    @POST(Const.TRANSLATION_API_URL + "/detect")
    Observable<Object> detect(@Query("key") String key,
                              @Query("text") String text,
                              @Query("hint") String hint);

    @POST(Const.TRANSLATION_API_URL + "/translate")
    Observable<LanguageTranslation> translate(@Query("key") String key,
                                              @Query("text") String text,
                                              @Query("lang") String lang,
                                              @Query("format") String plain,
                                              @Query("options") String options);
    @POST(Const.TRANSLATION_API_URL + "/translate")
    Observable<LanguageTranslation> translateSimple(@Query("key") String key,
                                              @Query("text") String text,
                                              @Query("lang") String lang);

}
