package com.sxz.reaction.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shihao on 12/6/17.
 */

public class ReactionBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 4;
    private static final String DATABASE_NAME = "reactionBase.db";

    public ReactionBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ ReactionDbSchema.RecordTable.NAME +"(" +
            "_id integer primary key autoincrement, "+
                ReactionDbSchema.RecordTable.Cols.DATE+" REAL, "+
                ReactionDbSchema.RecordTable.Cols.TIME+" REAL, "+
                ReactionDbSchema.RecordTable.Cols.USERID+" TEXT,"+
                ReactionDbSchema.RecordTable.Cols.TYPE+" INTEGER"+
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReactionDbSchema.RecordTable.NAME);
        onCreate(sqLiteDatabase);
    }
}
