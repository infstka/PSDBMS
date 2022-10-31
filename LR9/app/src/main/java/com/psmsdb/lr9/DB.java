package com.psmsdb.lr9;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String DB_NAME = "LR9_DB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "SimpleTable";

    private static final String ID_COL = "id";
    private static final String FLOAT_COL = "f";
    private static final String TEXT_COL = "t";

    public DB(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //создание
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FLOAT_COL + " REAL," +
                TEXT_COL + " TEXT)";
        db.execSQL(query); //выполняет запрос
    }

    //обновление (если таблица существует - она удалится и пересоздатся)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldT, int newT)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //добавление в таблицу
    public boolean addTable(@Nullable Integer id, @NonNull Float f, @NonNull String t) throws SQLiteConstraintException
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contval = new ContentValues(); //словарь, что содержит набор пар (кл,зн)
            if (id != null)
            {
                contval.put(ID_COL, id); //добавление в словарь объекта
            }
            contval.put(FLOAT_COL, f);
            contval.put(TEXT_COL, t);
            long row = db.insert(TABLE_NAME, null, contval);
            Log.d("LR9", String.format("row = %d", row));
            return row == -1 ? false : true;
        }
        catch (SQLiteConstraintException e)
        {
            Log.d("LR9", "Ошибка");
            return false;
        }
    }

    //обновление таблицы
    public void updateTable(String savedID, String f, String t)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contval = new ContentValues();

        contval.put(FLOAT_COL, f);
        contval.put(TEXT_COL, t);

        db.update(TABLE_NAME, contval, "id=?", new String[]{savedID});
        db.close();
    }

    public void deleteTable(String savedID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{savedID});
        db.close();
    }

    public SimpleTable getTablesRaw(String tableID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        //пишется запрос, как в sql
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + String.format(" WHERE id = %s", tableID), null);
        SimpleTable st = new SimpleTable();

        //перемещает курсор на первую строку в результате запроса
        if(cursor.moveToFirst())
        {
            st.id = cursor.getInt(0);
            st.f = cursor.getFloat(1);
            st.t = cursor.getString(2);
        }
        cursor.close();
        return st;
    }

    public SimpleTable getTable(String tableID)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            //имя таблицы, список имен возвр. полей, возврат полей введенного id (where), значения аргумента, groupby. having. orderby
            Cursor cursor = db.query(TABLE_NAME, new String[]{"id", "f", "t"}, " id = ?",
                    new String[] {tableID}, null, null, null);
            SimpleTable st = new SimpleTable();

            if(cursor.moveToFirst())
            {
                st.id = cursor.getInt(0);
                st.f = cursor.getFloat(1);
                st.t = cursor.getString(2);
            }
            cursor.close();
            return st;
        }
        catch(Exception exc)
        {
            return null;
        }
    }
}
