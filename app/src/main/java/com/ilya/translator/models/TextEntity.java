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
        this.isMarked = textEntity.isMarked;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextEntity that = (TextEntity) o;

        if (isMarked != that.isMarked) return false;
        if (inputLanguage != null ? !inputLanguage.equals(that.inputLanguage) : that.inputLanguage != null)
            return false;
        if (outputLanguage != null ? !outputLanguage.equals(that.outputLanguage) : that.outputLanguage != null)
            return false;
        if (inputText != null ? !inputText.equals(that.inputText) : that.inputText != null)
            return false;
        if (outputText != null ? !outputText.equals(that.outputText) : that.outputText != null)
            return false;
        if (pos != null ? !pos.equals(that.pos) : that.pos != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inputLanguage != null ? inputLanguage.hashCode() : 0;
        result = 31 * result + (outputLanguage != null ? outputLanguage.hashCode() : 0);
        result = 31 * result + (inputText != null ? inputText.hashCode() : 0);
        result = 31 * result + (outputText != null ? outputText.hashCode() : 0);
        result = 31 * result + (isMarked ? 1 : 0);
        result = 31 * result + (pos != null ? pos.hashCode() : 0);
        return result;
    }
}
