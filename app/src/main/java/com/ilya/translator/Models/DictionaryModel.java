package com.ilya.translator.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 08.04.17 20:53.
 */
public class DictionaryModel {
    @SerializedName("def")
    public ArrayList<DefModel> def;


    public class DefModel {

        @SerializedName("text")
        public String text;

        //part of speech
        @SerializedName("pos")
        public String pos;

        @SerializedName("anm")
        public String animate;

        @SerializedName("tr")
        public ArrayList<TrModel> tr;

        public class TrModel{
            @SerializedName("text")
            public String text;

            @SerializedName("mean")
            public ArrayList<TrModel> mean;

            @SerializedName("syn")
            public ArrayList<TrModel> similar;

        }
    }

}
