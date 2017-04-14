package com.ilya.translator.models;

import android.os.Parcelable;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 22:18.
 */

public class TextEntity implements Parcelable{
    public int id;
    public String inputLanguage;
    public String outputLanguage;
    public String inputText;
    public String outputText;
    public boolean isMarked;

    public TextEntity() {

    }

    public TextEntity(int id,String inputLanguage,String outputLanguage, String inputText,String outputText) {
        this.id = id;
        this.inputLanguage = inputLanguage;
        this.outputLanguage = outputLanguage;
        this.inputText = inputText;
        this.outputText = outputText;
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
