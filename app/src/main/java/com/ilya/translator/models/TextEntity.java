package com.ilya.translator.models;

import android.os.Parcelable;
/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 05.04.17 22:18.
 */

public class TextEntity implements Parcelable {
    public long id;
    public String inputLanguage;
    public String outputLanguage;
    public String inputText;
    public String outputText;
    public boolean isMarked;
    public String pos;//part of speech

    public TextEntity() {
    }


    public TextEntity(int id, String inputLanguage, String outputLanguage, String inputText, String outputText, String pos) {
        this.id = id;
        this.inputLanguage = inputLanguage;
        this.outputLanguage = outputLanguage;
        this.inputText = inputText;
        this.outputText = outputText;
        this.pos = pos;
    }

    public TextEntity(TextEntity textEntity) {
        this.id = textEntity.id;
        this.inputLanguage = textEntity.inputLanguage;
        this.outputLanguage = textEntity.outputLanguage;
        this.inputText = textEntity.inputText;
        this.outputText = textEntity.outputText;
        this.pos = textEntity.pos;
    }


    protected TextEntity(android.os.Parcel in) {
        id = in.readLong();
        inputLanguage = in.readString();
        outputLanguage = in.readString();
        inputText = in.readString();
        outputText = in.readString();
        isMarked = Boolean.parseBoolean(in.readString());
        pos = in.readString();
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
        parcel.writeLong(id);
        parcel.writeString(inputLanguage);
        parcel.writeString(outputLanguage);
        parcel.writeString(inputText);
        parcel.writeString(outputText);
        parcel.writeString(String.valueOf(isMarked));
        parcel.writeString(pos);
    }

}
