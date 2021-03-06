package com.ilya.translator.models.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 03.04.17 20:55.
 */
public class PossibleLanguages {

  @SerializedName("dirs")
  public ArrayList<String> dirs;

  @SerializedName("langs")
  public Map<String,String>  langs;
}
