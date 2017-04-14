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

  // Contacts Table Columns names
  private static final String KEY_ID = "_id";
  private static final String KEY_INPUT_LANGUAGE = "inputLanguage";
  private static final String KEY_OUTPUT_LANGUAGE = "outputLanguage";
  private static final String KEY_INPUT_TEXT = "inputText";
  private static final String KEY_OUTPUT_TEXT = "outputText";
  private static final String KEY_IS_MARKED = "isMarked";


  public static CRUDService getInstance(Context context) {
    if (instance == null) {
      instance = new CRUDService(context);

    }
    return instance;
  }

  private CRUDService(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    getWritableDatabase();
  }

  // Creating Tables
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TEXT_ENTITIES + "("
        + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
        + KEY_INPUT_LANGUAGE  + " TEXT,"
        + KEY_OUTPUT_LANGUAGE  + " TEXT,"
        + KEY_INPUT_TEXT + " TEXT,"
        +  KEY_OUTPUT_TEXT + " TEXT,"
        +  KEY_IS_MARKED + " TEXT"
        +")";
    db.execSQL(CREATE_CONTACTS_TABLE);
  }

  // Upgrading database
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXT_ENTITIES);

    // Create tables again
    onCreate(db);
  }

  public long addTextEntity(TextEntity textEntity) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_OUTPUT_LANGUAGE, textEntity.outputLanguage);
    values.put(KEY_INPUT_LANGUAGE,textEntity.inputLanguage);
    values.put(KEY_INPUT_TEXT, textEntity.outputText);
    values.put(KEY_OUTPUT_TEXT, textEntity.inputText);
    values.put(KEY_IS_MARKED, Boolean.toString(textEntity.isMarked));

    long a =db.insertOrThrow(TABLE_TEXT_ENTITIES, null, values);
    db.close();
    return a;
  }

  TextEntity getTextEntity(int id) {
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.query(TABLE_TEXT_ENTITIES, new String[] {
        KEY_ID,
        KEY_INPUT_LANGUAGE,
        KEY_OUTPUT_LANGUAGE,
        KEY_INPUT_TEXT,
        KEY_OUTPUT_TEXT,
        KEY_IS_MARKED
    }, KEY_ID + "=?",

        new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null)
      cursor.moveToFirst();

    TextEntity textEntity = new TextEntity(Integer.parseInt(cursor.getString(0)),
        cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));

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
        textEntity.id =Integer.parseInt(cursor.getString(0));
        textEntity.inputLanguage = cursor.getString(1);
        textEntity.outputLanguage = cursor.getString(2);
        textEntity.inputText = cursor.getString(3);
        textEntity.outputText = cursor.getString(4);
        textEntity.isMarked = Boolean.valueOf(cursor.getString(5));
        contactList.add(textEntity);
      } while (cursor.moveToNext());
    }

    return contactList;
  }

  public int updateContact(TextEntity textEntity) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_INPUT_LANGUAGE, textEntity.inputLanguage);
    values.put(KEY_OUTPUT_LANGUAGE, textEntity.outputText);
    values.put(KEY_INPUT_TEXT, textEntity.inputText);
    values.put(KEY_OUTPUT_TEXT, textEntity.outputText);
    values.put(KEY_IS_MARKED, textEntity.isMarked);

    return db.update(TABLE_TEXT_ENTITIES, values, KEY_ID + " = ?",
        new String[] { String.valueOf(textEntity.id) });
  }

  public void deleteTextEntity(TextEntity contact) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_TEXT_ENTITIES, KEY_ID + " = ?",
        new String[] { String.valueOf(contact.id) });
    db.close();
  }

  public int getTextEntitiesCount() {
    String countQuery = "SELECT  * FROM " + TABLE_TEXT_ENTITIES;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();

    return cursor.getCount();
  }
  }
