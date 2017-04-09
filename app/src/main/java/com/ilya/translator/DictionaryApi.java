package com.ilya.translator;

import com.ilya.translator.Models.LanguageTranslation;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 08.04.17 20:22.
 */
public interface DictionaryApi {

    @POST(Const.DICTIONARY_API_URL + "/lookup")
    Observable<Object> lookup(@Query("key") String key,
                                                    @Query("text") String text,
                                                    @Query("lang") String lang);
}
