package com.sxz.reaction.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sxz.reaction.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shihao on 12/6/17.
 */

public class ReactionLab {
    private static ReactionLab mReactionLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ReactionLab get(Context context){
        if (mReactionLab == null){
             mReactionLab = new ReactionLab(context);
        }
        return mReactionLab;
    }

    private ReactionLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ReactionBaseHelper(mContext).getWritableDatabase();
    }

    public List<Record> getRecordsBy(String userID, int type){
        List<Record> records = new ArrayList<>();
        RecordCursorWrapper cursor = queryRecords(
                ReactionDbSchema.RecordTable.Cols.USERID + "=? and "+
                        ReactionDbSchema.RecordTable.Cols.TYPE + "= ?"
                , new String[]{ userID, Integer.toString(type) });
        while(cursor.moveToNext()){
            records.add( cursor.getRecord() );
        }
        cursor.close();
        return records;
    }

    public void addRecord(Record record){
        this.mDatabase.insert(ReactionDbSchema.RecordTable.NAME, null, getContentValues(record));
    }

    private RecordCursorWrapper queryRecords(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(ReactionDbSchema.RecordTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new RecordCursorWrapper(cursor);
    }

    private ContentValues getContentValues(Record record){
        ContentValues values = new ContentValues();
        values.put(ReactionDbSchema.RecordTable.Cols.DATE, record.getDate().getTime());
        values.put(ReactionDbSchema.RecordTable.Cols.TIME, record.getTime());
        values.put(ReactionDbSchema.RecordTable.Cols.USERID, record.getUserID());
        values.put(ReactionDbSchema.RecordTable.Cols.TYPE, record.getType());
        return values;
    }

    public void clearAll(){
        mDatabase.execSQL("delete from "+ReactionDbSchema.RecordTable.NAME);
    }
}
