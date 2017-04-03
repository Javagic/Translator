package com.ilya.translator.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 21:32.
 */
public class LanguageTranslation {


  @SerializedName("code")
  public String code;

  public String detectedLang;

  @SerializedName("lang")
  public String lang;

  public String text;
}
