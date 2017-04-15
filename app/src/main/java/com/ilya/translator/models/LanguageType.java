package com.ilya.translator.models;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 06.04.17 21:16.
 */
public class   LanguageType implements Parcelable {
  public long id;
  public  String shortName;//короткое название
  public  String longName;//длинное  название

  public LanguageType(final String shortName) {
    this.shortName = shortName;
  }
  public LanguageType(){

  }

  public LanguageType(final String shortName, String longName) {
    this.shortName = shortName;
    this.longName = longName;
  }
  protected LanguageType(android.os.Parcel in) {
    shortName = in.readString();
  }

  public static final Creator<LanguageType> CREATOR = new Creator<LanguageType>() {
    @Override
    public LanguageType createFromParcel(android.os.Parcel in) {
      return new LanguageType(in);
    }

    @Override
    public LanguageType[] newArray(int size) {
      return new LanguageType[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(android.os.Parcel parcel, int i) {
    parcel.writeString(shortName);
  }


  public static List<LanguageType> getList(Map<String,String> map){
    List<LanguageType> languageTypes = new ArrayList<>();
    for (String s : map.keySet()) {
      languageTypes.add(new LanguageType(s,map.get(s)));
    }
    Collections.sort(languageTypes, (lang1, lang2) -> lang1.longName.compareToIgnoreCase(lang2.longName));
    return languageTypes;
  }


}
