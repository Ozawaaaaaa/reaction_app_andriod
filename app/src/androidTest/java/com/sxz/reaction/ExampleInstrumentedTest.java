package com.sxz.reaction;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.sxz.reaction.database.ReactionLab;
import com.sxz.reaction.model.Record;

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
public class ExampleInstrumentedTest {
    @Test
    public void DatabaseRecordAdd() throws Exception {

        Context appContext = InstrumentationRegistry.getTargetContext();
        ReactionLab lab = ReactionLab.get(appContext);

        Record newRecord = new Record();
        newRecord.setTime(new Float(10.3));
        newRecord.setDate(new Date());
        newRecord.setUserName("charlesxsh");

        lab.addRecord(newRecord);

        List<Record> records = lab.getRecords();

        assertEquals(records.size(), 1);

        Record testRecord = records.get(0);

        assertEquals(newRecord.getDate(), testRecord.getDate());
        assertEquals(newRecord.getTime(), testRecord.getTime());
        assertEquals(newRecord.getUserName(), testRecord.getUserName());
    }
}
