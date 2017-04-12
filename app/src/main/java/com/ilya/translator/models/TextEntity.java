package com.ilya.translator.models;

import android.databinding.BindingAdapter;
import android.os.Parcelable;
import android.widget.TextView;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 22:18.
 */

public class TextEntity implements Parcelable{

    public LanguageType fromLanguage;
    public LanguageType toLanguage;
    public String textFrom;
    public String textTo;
    public boolean isMarked;

    public TextEntity() {

    }

    protected TextEntity(android.os.Parcel in) {

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

    }

}
