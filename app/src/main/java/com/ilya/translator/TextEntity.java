package com.ilya.translator;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 22:18.
 */

public class TextEntity implements Parcelable{

    public Map<String, ArrayList<String>> meaning;
    public LanguageType currentLang;

    public TextEntity() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int i) {
    }
}
