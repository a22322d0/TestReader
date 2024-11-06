package com.example.testreader.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

public class ComicDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "comicReader.db";
    private static final int DATABASE_VERSION = 1;

    // 表名和列名
    public static final String TABLE_COMICS = "comics";
    public static final String COLUMN_COMIC_ID = "comic_id";
    public static final String COLUMN_COMIC_NAME = "comic_name";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_SOURCE = "source";
    public static final String COLUMN_COVER_IMAGE = "cover_image";

    public static final String TABLE_CHAPTERS = "chapters";
    public static final String COLUMN_CHAPTER_ID = "chapter_id";
    public static final String COLUMN_CHAPTER_NAME = "chapter_name";
    public static final String COLUMN_COMIC_REF_ID = "comic_ref_id";
    public static final String COLUMN_ORDER = "chapter_order";

    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_HISTORY_ID = "history_id";
    public static final String COLUMN_CHAPTER_REF_ID = "chapter_ref_id";
    public static final String COLUMN_PAGE_NUMBER = "page_number";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_FAVORITE_ID = "favorite_id";

    // 构造方法
    public ComicDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建 comics 表
        String CREATE_COMICS_TABLE = "CREATE TABLE " + TABLE_COMICS + "("
                + COLUMN_COMIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COMIC_NAME + " TEXT,"
                + COLUMN_AUTHOR + " TEXT,"
                + COLUMN_SOURCE + " TEXT,"
                + COLUMN_COVER_IMAGE + " TEXT"
                + ")";
        db.execSQL(CREATE_COMICS_TABLE);

        // 创建 chapters 表
        String CREATE_CHAPTERS_TABLE = "CREATE TABLE " + TABLE_CHAPTERS + "("
                + COLUMN_CHAPTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CHAPTER_NAME + " TEXT,"
                + COLUMN_COMIC_REF_ID + " INTEGER,"
                + COLUMN_ORDER + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_COMIC_REF_ID + ") REFERENCES " + TABLE_COMICS + "(" + COLUMN_COMIC_ID + ")"
                + ")";
        db.execSQL(CREATE_CHAPTERS_TABLE);

        // 创建 history 表
        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COMIC_REF_ID + " INTEGER,"
                + COLUMN_CHAPTER_REF_ID + " INTEGER,"
                + COLUMN_PAGE_NUMBER + " INTEGER,"
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_COMIC_REF_ID + ") REFERENCES " + TABLE_COMICS + "(" + COLUMN_COMIC_ID + "),"
                + "FOREIGN KEY(" + COLUMN_CHAPTER_REF_ID + ") REFERENCES " + TABLE_CHAPTERS + "(" + COLUMN_CHAPTER_ID + ")"
                + ")";
        db.execSQL(CREATE_HISTORY_TABLE);

        // 创建 favorites 表
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COMIC_REF_ID + " INTEGER,"
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_COMIC_REF_ID + ") REFERENCES " + TABLE_COMICS + "(" + COLUMN_COMIC_ID + ")"
                + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAPTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    // 插入新的 Comic
    public void insertComic(Comic comic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMIC_NAME, comic.getName());
        values.put(COLUMN_AUTHOR, comic.getAuthor());
        values.put(COLUMN_SOURCE, comic.getSource());
        values.put(COLUMN_COVER_IMAGE, comic.getCoverImage());

        db.insert(TABLE_COMICS, null, values);
        db.close();
    }

    // 获取所有 Comics
    public List<Comic> getAllComics() {
        List<Comic> comicList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_COMICS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Comic comic = new Comic(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_COMIC_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_COMIC_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_COVER_IMAGE))
                );
                comicList.add(comic);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return comicList;
    }

    // 插入新的 Chapter
    public void insertChapter(Chapter chapter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAPTER_NAME, chapter.getName());
        values.put(COLUMN_COMIC_REF_ID, chapter.getComicId());
        values.put(COLUMN_ORDER, chapter.getOrder());

        db.insert(TABLE_CHAPTERS, null, values);
        db.close();
    }

    // 获取某漫画的所有 Chapters
    public List<Chapter> getChaptersByComicId(int comicId) {
        List<Chapter> chapterList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CHAPTERS + " WHERE " + COLUMN_COMIC_REF_ID + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(comicId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Chapter chapter = new Chapter(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CHAPTER_NAME)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_COMIC_REF_ID)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER))
                );
                chapterList.add(chapter);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return chapterList;
    }

    // 保存阅读进度
    public void saveReadingProgress(ReadingProgress progress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMIC_REF_ID, progress.getComicId());
        values.put(COLUMN_CHAPTER_REF_ID, progress.getChapterId());
        values.put(COLUMN_PAGE_NUMBER, progress.getPageNumber());

        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    // 获取阅读进度
    @SuppressLint("Range")
    public ReadingProgress getReadingProgress(int comicId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HISTORY + " WHERE " + COLUMN_COMIC_REF_ID + " = ? ORDER BY " + COLUMN_TIMESTAMP + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(comicId)});

        ReadingProgress progress = null;
        if (cursor != null && cursor.moveToFirst()) {
            progress = new ReadingProgress(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_HISTORY_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_COMIC_REF_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_REF_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PAGE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP))
            );
            cursor.close();
        }
        db.close();
        return progress;
    }
}

