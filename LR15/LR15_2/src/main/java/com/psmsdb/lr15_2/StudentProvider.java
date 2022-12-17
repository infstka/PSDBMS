package com.psmsdb.lr15_2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.psmsdb.lr15_2.database.DB;

public class StudentProvider extends ContentProvider {

    static final String AUTHORITY = "com.psmsdb.providers.StudentsList";
    static final String PATH = "student";

    static final String STUDENT_NAME = "NAME";
    static final String STUDENT_ID = "IDSTUDENT";
    static final String STUDENT_TABLE = "STUDENT_TABLE";

    public static final Uri STUDENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);


    static final String STUDENT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH;

    static final String STUDENT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PATH;


    static final int URI_STUDENTS = 1;

    static final int URI_STUDENTS_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, URI_STUDENTS);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", URI_STUDENTS_ID);
    }

    DB DB;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        DB = new DB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_STUDENTS:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = STUDENT_NAME + " ASC";
                }
                break;
            case URI_STUDENTS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = STUDENT_ID + " = " + id;
                } else {
                    selection = selection + " AND " + STUDENT_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = DB.getWritableDatabase();
        Cursor cursor = db.query(STUDENT_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                STUDENT_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)) {
            case URI_STUDENTS:
                return STUDENT_CONTENT_TYPE;
            case URI_STUDENTS_ID:
                return STUDENT_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (uriMatcher.match(uri) != URI_STUDENTS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = DB.getWritableDatabase();
        long rowID = db.insert(STUDENT_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(STUDENT_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_STUDENTS:
                break;
            case URI_STUDENTS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = STUDENT_ID + " = " + id;
                } else {
                    selection = selection + " AND " + STUDENT_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = DB.getWritableDatabase();
        int cnt = db.delete(STUDENT_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_STUDENTS:
                break;
            case URI_STUDENTS_ID:
                String id = uri.getLastPathSegment();
                Toast.makeText(getContext(), "URI_CONTACTS_ID, " + id, Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(selection)) {
                    selection = STUDENT_ID + " = " + id;
                } else {
                    selection = selection + " AND " + STUDENT_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = DB.getWritableDatabase();
        int cnt = db.update(STUDENT_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
