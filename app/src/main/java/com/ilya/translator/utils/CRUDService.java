package com.ilya.translator.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ilya.translator.models.LanguageType;
import com.ilya.translator.models.Pair;
import com.ilya.translator.models.TextEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 09.04.17 18:01.
 */

/**
 * Сервис работы с Базой Данных
 */
public class CRUDService extends SQLiteOpenHelper {

    private static CRUDService instance;

    /**
     * максимальное количество элементов до очистки базы
     */
    private static int MAX_HISTORY_ITEMS_COUNT = 200;

    /**
     * оставляемое количество элементов после очистки базы
     */
    private static int PREFERABLE_HISTORY_ITEMS_COUNT = 40;

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "CRUDService";

    //таблица сущностей текста
    private static final String TABLE_TEXT_ENTITIES = "textEntities";

    //таблица языков
    private static final String TABLE_LANGUAGE_TYPES = "languageTypes";

    //таблица пар языков
    private static final String TABLE_PAIRS = "pairs";

    private static final String KEY_ID = "_id";

    //TABLE_TEXT_ENTITIES
    private static final String KEY_INPUT_LANGUAGE = "inputLanguage";
    private static final String KEY_OUTPUT_LANGUAGE = "outputLanguage";
    private static final String KEY_INPUT_TEXT = "inputText";
    private static final String KEY_OUTPUT_TEXT = "outputText";
    private static final String KEY_IS_MARKED = "isMarked";
    private static final String KEY_POS = "pos";

    //TABLE_LANGUAGE_TYPES
    private static final String KEY_SHORT_NAME = "shortName";
    private static final String KEY_LONG_NAME = "longName";

    //TABLE_PAIRS
    private static final String KEY_FIRST_LANG = "firstLang";
    private static final String KEY_SECOND_LANG = "secondLang";


    public static CRUDService getInstance(Context context) {
        if (instance == null) {
            instance = new CRUDService(context);
            if (instance.getHistoryCount() > MAX_HISTORY_ITEMS_COUNT) {
                instance.removeExtraHistory();
            }
        }
        return instance;
    }

    public static CRUDService getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    private CRUDService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TEXT_TABLE = "CREATE TABLE " + TABLE_TEXT_ENTITIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
                + KEY_INPUT_LANGUAGE + " TEXT,"
                + KEY_OUTPUT_LANGUAGE + " TEXT,"
                + KEY_INPUT_TEXT + " TEXT,"
                + KEY_OUTPUT_TEXT + " TEXT,"
                + KEY_IS_MARKED + " TEXT,"
                + KEY_POS + " TEXT"
                + ")";
        db.execSQL(CREATE_TEXT_TABLE);
        String CREATE_LANGUAGE_TABLE = "CREATE TABLE " + TABLE_LANGUAGE_TYPES + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
                + KEY_SHORT_NAME + " TEXT,"
                + KEY_LONG_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_LANGUAGE_TABLE);
        String CREATE_PAIRS_TABLE = "CREATE TABLE " + TABLE_PAIRS + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
                + KEY_FIRST_LANG + " TEXT,"
                + KEY_SECOND_LANG + " TEXT"
                + ")";
        db.execSQL(CREATE_PAIRS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXT_ENTITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGE_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAIRS);
        onCreate(db);
    }

    public long addTextEntity(TextEntity textEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OUTPUT_LANGUAGE, textEntity.outputLanguage);
        values.put(KEY_INPUT_LANGUAGE, textEntity.inputLanguage);
        values.put(KEY_INPUT_TEXT, textEntity.inputText);
        values.put(KEY_OUTPUT_TEXT, textEntity.outputText);
        values.put(KEY_IS_MARKED, Boolean.toString(textEntity.isMarked));
        values.put(KEY_POS, textEntity.pos);
        long a = db.insertOrThrow(TABLE_TEXT_ENTITIES, null, values);
        db.close();
        return a;
    }


    public List<TextEntity> getHistory() {
        List<TextEntity> textEntities = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_TEXT_ENTITIES + " where " + KEY_INPUT_TEXT + " is not null and " + KEY_INPUT_TEXT + " <> '' ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TextEntity textEntity = new TextEntity();
                textEntity.id = Integer.parseInt(cursor.getString(0));
                textEntity.inputLanguage = cursor.getString(1);
                textEntity.outputLanguage = cursor.getString(2);
                textEntity.inputText = cursor.getString(3);
                textEntity.outputText = cursor.getString(4);
                textEntity.isMarked = Boolean.valueOf(cursor.getString(5));
                textEntity.pos = cursor.getString(6);
                textEntities.add(textEntity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.reverse(textEntities);
        return textEntities;
    }

    public List<TextEntity> getFavorites() {
        List<TextEntity> textEntities = new ArrayList<TextEntity>();
        String selectQuery = "SELECT  * FROM " + TABLE_TEXT_ENTITIES + " where " + KEY_INPUT_TEXT + " is not null and " + KEY_INPUT_TEXT + " <> '' " + " AND " + KEY_IS_MARKED + " = 'true'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TextEntity textEntity = new TextEntity();
                textEntity.id = Integer.parseInt(cursor.getString(0));
                textEntity.inputLanguage = cursor.getString(1);
                textEntity.outputLanguage = cursor.getString(2);
                textEntity.inputText = cursor.getString(3);
                textEntity.outputText = cursor.getString(4);
                textEntity.isMarked = Boolean.valueOf(cursor.getString(5));
                textEntity.pos = cursor.getString(6);
                textEntities.add(textEntity);
            } while (cursor.moveToNext());
        }
        Collections.reverse(textEntities);
        return textEntities;
    }

    public int updateTextEntity(TextEntity textEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INPUT_LANGUAGE, textEntity.inputLanguage);
        values.put(KEY_OUTPUT_LANGUAGE, textEntity.outputLanguage);
        values.put(KEY_INPUT_TEXT, textEntity.inputText);
        values.put(KEY_OUTPUT_TEXT, textEntity.outputText);
        values.put(KEY_IS_MARKED, String.valueOf(textEntity.isMarked));
        values.put(KEY_POS, textEntity.pos);

        return db.update(TABLE_TEXT_ENTITIES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(textEntity.id)});
    }


    public void addLanguageTypes(List<LanguageType> languageTypes) {

        SQLiteDatabase db = this.getWritableDatabase();
        for (LanguageType languageType : languageTypes) {
            ContentValues values = new ContentValues();
            values.put(KEY_SHORT_NAME, languageType.shortName);
            values.put(KEY_LONG_NAME, languageType.longName);
            db.insertOrThrow(TABLE_LANGUAGE_TYPES, null, values);
        }
        db.close();
    }

    public List<LanguageType> getLanguageTypeList() {
        List<LanguageType> languageList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LANGUAGE_TYPES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LanguageType languageType = new LanguageType();
                languageType.id = Integer.parseInt(cursor.getString(0));
                languageType.shortName = cursor.getString(1);
                languageType.longName = cursor.getString(2);
                languageList.add(languageType);
            } while (cursor.moveToNext());
        }

        return languageList;
    }

    public List<TextEntity> searchHistory(String inputText) throws SQLException {
        if (inputText.isEmpty()) {
            return getHistory();
        }
        String selectQuery = " select * from " + TABLE_TEXT_ENTITIES + " where " + KEY_INPUT_TEXT + " like  '%"
                + inputText
                + "%' or " + KEY_OUTPUT_TEXT + " like '%"
                + inputText + "%'";
        return searchTextEntity(selectQuery);
    }

    public List<TextEntity> searchFavorites(String inputText) throws SQLException {
        if (inputText.isEmpty()) {
            return getFavorites();
        }

        String selectQuery = " select * from " + TABLE_TEXT_ENTITIES + " where (" + KEY_INPUT_TEXT + " like  '%"
                + inputText
                + "%' or " + KEY_OUTPUT_TEXT + " like '%"
                + inputText + "%' ) and " + KEY_IS_MARKED + " = 'true'";
        return searchTextEntity(selectQuery);
    }


    public void addPairs(List<Pair> pairs) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Pair pair : pairs) {
            ContentValues values = new ContentValues();
            values.put(KEY_FIRST_LANG, pair.from);
            values.put(KEY_SECOND_LANG, pair.to);
            db.insertOrThrow(TABLE_PAIRS, null, values);
        }
        db.close();
    }

    public List<Pair> getPairs() {
        List<Pair> pairs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PAIRS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Pair pair = new Pair();
                pair.id = Integer.parseInt(cursor.getString(0));
                pair.from = cursor.getString(1);
                pair.to = cursor.getString(2);
                pairs.add(pair);
            } while (cursor.moveToNext());
        }

        return pairs;
    }


    public int getHistoryCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TEXT_ENTITIES + " where " + KEY_IS_MARKED + " = 'false'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void removeAllHistory() {
        String query = "DELETE FROM " + TABLE_TEXT_ENTITIES +
                " WHERE " + KEY_IS_MARKED + " = 'false'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        cursor.close();
    }

    public void removeAllLanguages() {
        String query = "DELETE FROM " + TABLE_LANGUAGE_TYPES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public void removeAllPairs() {
        String query = "DELETE FROM " + TABLE_PAIRS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public void deleteTextEntity(TextEntity textEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEXT_ENTITIES, KEY_ID + " = ?",
                new String[]{String.valueOf(textEntity.id)});
        db.close();
    }


    private List<TextEntity> searchTextEntity(String selectQuery) {
        ArrayList<TextEntity> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            TextEntity textEntity = new TextEntity();
            textEntity.id = Integer.parseInt(cursor.getString(0));
            textEntity.inputLanguage = cursor.getString(1);
            textEntity.outputLanguage = cursor.getString(2);
            textEntity.inputText = cursor.getString(3);
            textEntity.outputText = cursor.getString(4);
            textEntity.isMarked = Boolean.valueOf(cursor.getString(5));
            textEntity.pos = cursor.getString(6);
            list.add(textEntity);
        }
        return list;
    }

    /**
     * удаление элементов при превышшении лимита хранимых объектов
     */
    private void removeExtraHistory() {
        String query = "DELETE FROM " + TABLE_TEXT_ENTITIES +
                " WHERE " + KEY_ID +
                " IN (SELECT " + KEY_ID + " FROM " + TABLE_TEXT_ENTITIES +
                " ORDER BY " + KEY_ID + " ASC LIMIT " + (MAX_HISTORY_ITEMS_COUNT - PREFERABLE_HISTORY_ITEMS_COUNT) + " ) " +
                " AND " + KEY_IS_MARKED + " = 'false'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        cursor.close();
    }

}
