package com.ilya.translator.service.api;

import com.ilya.translator.models.pojo.DictionaryModel;
import com.ilya.translator.utils.Const;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 08.04.17 20:22.
 */

/**
 * Api Yandex Dictionary
 */
public interface DictionaryApi {

    @POST(Const.DICTIONARY_API_URL + "/lookup")
    Observable<DictionaryModel> lookup(@Query("key") String key,
                                       @Query("text") String text,
                                       @Query("lang") String lang);
}
