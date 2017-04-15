package com.ilya.translator.utils;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 19:52.
 */
public interface Const {
  public final static String TRANSLATION_API_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
  public final static String DICTIONARY_API_URL = "https://dictionary.yandex.net/api/v1/dicservice.json/";
  public final static String TRANSLATION_API_KEY = "trnsl.1.1.20170403T152303Z.b21640a0a2ac9e61.0a8f8b338e75f888d9d6e2d308b3f41b5bdb78b8";
  public final static String DICTIONARY_API_KEY = "dict.1.1.20170408T161735Z.0de6737a8237f56e.ee84099d7d45c63eace886fc98389476d38006a0";
  public final static String MY_LOG = "myLog";

  interface Prefs{
    public static final String APP_PREFS = "preferences";
    public static final String GET_LANGS = "getLangs";
  }

}
