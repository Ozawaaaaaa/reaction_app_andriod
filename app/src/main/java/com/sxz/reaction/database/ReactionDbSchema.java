package com.sxz.reaction.database;

/**
 * Created by Shihao on 12/6/17.
 */

public class ReactionDbSchema {
    public static final class RecordTable{
        public static final String NAME = "records";

        public static final class Cols {
            public static final String USERID = "user_id";
            public static final String DATE = "date";
            public static final String TIME = "time";
            public static final String TYPE = "type";
        }


    }
}
