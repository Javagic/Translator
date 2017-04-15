package com.ilya.translator.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ilya.translator.models.LanguageType;
import com.ilya.translator.models.Pair;
import com.ilya.translator.models.TextEntity;
import com.ilya.translator.service.TranslatorService;

import org.apache.commons.codec.language.bm.Lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 09.04.17 18:01.
 */
public class CRUDService extends SQLiteOpenHelper {

    private static CRUDService instance;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "CRUDService";

    // Contacts table name
    private static final String TABLE_TEXT_ENTITIES = "textEntities";

    private static final String TABLE_LANGUAGE_TYPES = "languageTypes";

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


    public static CRUDService getInstance(Context context) {
        if (instance == null) {
            instance = new CRUDService(context);

        }
        return instance;
    }

    private CRUDService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
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
                + KEY_LONG_NAME+ " TEXT"
                + ")";
        db.execSQL(CREATE_LANGUAGE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXT_ENTITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGE_TYPES);

        // Create tables again
        onCreate(db);
    }

    public long addTextEntity(TextEntity textEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OUTPUT_LANGUAGE, textEntity.outputLanguage);
        values.put(KEY_INPUT_LANGUAGE, textEntity.inputLanguage);
        values.put(KEY_INPUT_TEXT, textEntity.outputText);
        values.put(KEY_OUTPUT_TEXT, textEntity.inputText);
        values.put(KEY_IS_MARKED, Boolean.toString(textEntity.isMarked));
        values.put(KEY_POS, textEntity.pos);


        long a = db.insertOrThrow(TABLE_TEXT_ENTITIES, null, values);
        db.close();
        return a;
    }

    TextEntity getTextEntity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TEXT_ENTITIES, new String[]{
                        KEY_ID,
                        KEY_INPUT_LANGUAGE,
                        KEY_OUTPUT_LANGUAGE,
                        KEY_INPUT_TEXT,
                        KEY_OUTPUT_TEXT,
                        KEY_IS_MARKED,
                        KEY_POS
                }, KEY_ID + "=?",

                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TextEntity textEntity = new TextEntity(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        return textEntity;
    }

    public List<TextEntity> getHistory() {
        List<TextEntity> contactList = new ArrayList<TextEntity>();
        String selectQuery = "SELECT  * FROM " + TABLE_TEXT_ENTITIES;

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
                contactList.add(textEntity);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public List<TextEntity> getFavorites() {
        List<TextEntity> contactList = new ArrayList<TextEntity>();
        String selectQuery = "SELECT  * FROM " + TABLE_TEXT_ENTITIES +" WHERE isMarked = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] { "true" });

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
                contactList.add(textEntity);
            } while (cursor.moveToNext());
        }

        return contactList;
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

    public void deleteTextEntity(TextEntity contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEXT_ENTITIES, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.id)});
        db.close();
    }

    public int getTextEntitiesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TEXT_ENTITIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
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
}
