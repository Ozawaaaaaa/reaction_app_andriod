package com.sxz.reaction;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.sxz.reaction.database.ReactionLab;
import com.sxz.reaction.model.Record;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ReactionDatabaseTest {

    private ReactionLab mLab;

    @Before
    public void setUp(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        mLab = ReactionLab.get(appContext);
        mLab.clearAll();
    }
    @Test
    public void DatabaseRecordAddAndQuery() throws Exception {
        String userID = "123";
        Record newRecord = new Record();
        newRecord.setTime((float)10.3);
        newRecord.setDate(new Date());
        newRecord.setType(Record.Type.AUDITORY);
        newRecord.setUserID(userID);
        mLab.addRecord(newRecord);
        List<Record> records = mLab.getRecordsBy("123", Record.Type.AUDITORY);

        assertEquals(records.size(), 1);

        Record testRecord = records.get(0);

        assertEquals(newRecord.getDate(), testRecord.getDate());
        assertEquals(newRecord.getTime(), testRecord.getTime(), 0.01);
        assertEquals(newRecord.getUserID(), testRecord.getUserID());
        assertEquals(newRecord.getType(), testRecord.getType());
    }

    @Test
    public void DatabaseRecordQueryByUser() throws Exception {
        String userID1 = "123";
        String userID2 = "456";
        Record newRecord = new Record();
        newRecord.setTime((float)10.3);
        newRecord.setDate(new Date());
        newRecord.setType(Record.Type.AUDITORY);
        newRecord.setUserID(userID1);
        mLab.addRecord(newRecord);

        Record newRecord1 = new Record();
        newRecord1.setTime((float)10.3);
        newRecord1.setDate(new Date());
        newRecord1.setType(Record.Type.AUDITORY);
        newRecord1.setUserID(userID2);
        mLab.addRecord(newRecord1);

        Record newRecord2 = new Record();
        newRecord2.setTime((float)10.3);
        newRecord2.setDate(new Date());
        newRecord2.setType(Record.Type.AUDITORY);
        newRecord2.setUserID(userID2);
        mLab.addRecord(newRecord2);

        List<Record> user1Records = mLab.getRecordsBy(userID1, Record.Type.AUDITORY);
        List<Record> user2Records = mLab.getRecordsBy(userID2, Record.Type.AUDITORY);

        assertEquals(user1Records.size(), 1);
        assertEquals(user2Records.size(), 2);

    }

    @Test
    public void DatabaseRecordQueryByType() throws Exception {
        String userID1 = "123";
        Record newRecord = new Record();
        newRecord.setTime((float)10.3);
        newRecord.setDate(new Date());
        newRecord.setType(Record.Type.AUDITORY);
        newRecord.setUserID(userID1);
        mLab.addRecord(newRecord);

        Record newRecord1 = new Record();
        newRecord1.setTime((float)10.3);
        newRecord1.setDate(new Date());
        newRecord1.setType(Record.Type.VISUAL);
        newRecord1.setUserID(userID1);
        mLab.addRecord(newRecord1);

        Record newRecord2 = new Record();
        newRecord2.setTime((float)10.3);
        newRecord2.setDate(new Date());
        newRecord2.setType(Record.Type.VISUAL);
        newRecord2.setUserID(userID1);
        mLab.addRecord(newRecord2);

        List<Record> user1AuritoryRecords = mLab.getRecordsBy(userID1, Record.Type.AUDITORY);
        List<Record> user1VisualRecords = mLab.getRecordsBy(userID1, Record.Type.VISUAL);

        assertEquals(user1AuritoryRecords.size(), 1);
        assertEquals(user1VisualRecords.size(), 2);
    }
}
