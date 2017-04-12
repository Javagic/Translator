package com.ilya.translator.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 08.04.17 17:07.
 */
public class Pair {
    String from;
    String to;

    public Pair(String from, String to) {
        this.from = from;
        this.to = to;
    }
    public Pair(LanguageType from, LanguageType to) {
        this.from = from.shortName;
        this.to = to.shortName;
    }
    public Pair(String pair) {
        this.from = pair.split("-")[0];
        this.to = pair.split("-")[1];
    }

    public Pair(LanguageType type) {
        this.from = type.shortName;
        this.to = type.shortName;
    }
    @Override
    public String toString() {
        return from + "-" + to;
    }

    public static  String pairFrom(LanguageType type){
        return type.shortName+"-"+type.shortName;
    }

    public static  String pairFrom(LanguageType typeFrom,LanguageType typeTo){
        return typeFrom.shortName+"-"+typeTo.shortName;
    }

    public static  String pairFromUpperCase(LanguageType type,LanguageType type2){
        return type.shortName.toUpperCase()+"-"+type2.shortName.toUpperCase();
    }

    public static List<Pair> asList(List<String> list){
        List<Pair> pairs = new ArrayList<>();
        for (String s : list) {
            pairs.add(new Pair(s));
        }
        return pairs;
    }
}