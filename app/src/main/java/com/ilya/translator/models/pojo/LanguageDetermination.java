package com.ilya.translator.models.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 21:30.
 */
public class LanguageDetermination {

  @SerializedName("code")
  String code;

  @SerializedName("detected")
  String detectedLanguage;

}
