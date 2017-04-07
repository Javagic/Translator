package com.ilya.translator;

import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 06.04.17 21:16.
 */
public class   LanguageType implements Parcelable {
  public  String ui;//короткое название

  public LanguageType(final String ui) {
    this.ui = ui;
  }
  public LanguageType(){

  }

  protected LanguageType(android.os.Parcel in) {
    ui = in.readString();
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
    parcel.writeString(ui);
  }

}
