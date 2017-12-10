package com.sxz.reaction.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.sxz.reaction.model.Record;

import java.util.Date;

/**
 * Created by Shihao on 12/6/17.
 */

public class RecordCursorWrapper extends CursorWrapper {

    public RecordCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Record getRecord(){
        Record record = new Record();
        String userID = getString(getColumnIndex(ReactionDbSchema.RecordTable.Cols.USERID));
        Long date = getLong(getColumnIndex(ReactionDbSchema.RecordTable.Cols.DATE));
        Float time = getFloat(getColumnIndex(ReactionDbSchema.RecordTable.Cols.Time));

        record.setUserID(userID);
        record.setDate(new Date(date));
        record.setTime(time);
        return record;
    }
}
