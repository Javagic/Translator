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

    protected TextEntity(android.os.Parcel in) {
        currentLang = in.readParcelable(LanguageType.class.getClassLoader());
    }

    public static final Creator<TextEntity> CREATOR = new Creator<TextEntity>() {
        @Override
        public TextEntity createFromParcel(android.os.Parcel in) {
            return new TextEntity(in);
        }

        @Override
        public TextEntity[] newArray(int size) {
            return new TextEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int i) {
        parcel.writeParcelable(currentLang, i);
    }
}
