package com.ilya.translator.main;

import android.app.Application;

import com.ilya.translator.service.TranslatorService;
import com.ilya.translator.service.http.HttpService;
import com.ilya.translator.utils.CRUDService;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 16.04.17 17:37.
 */
public class TranslatorApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        CRUDService.getInstance(getApplicationContext());
        HttpService.getInstance();
        TranslatorService.getInstance();
    }
}
