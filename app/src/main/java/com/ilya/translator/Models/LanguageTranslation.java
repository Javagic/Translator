package com.ilya.translator.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 21:32.
 */
public class LanguageTranslation {


  @SerializedName("code")
  public String code;

  @SerializedName("detected")
  public HashMap<String,String> detectedLang;

  @SerializedName("lang")
  public String lang;

  @SerializedName("text")
  public ArrayList<String> text;
}
