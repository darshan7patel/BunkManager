package com.example.bunkmanager;

import android.provider.BaseColumns;

public class CardContract {

    private CardContract() {}

    public static final class CardEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "SubjectList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ATTEND = "attend";
        public static final String COLUMN_BUNK = "bunk";
    }
}
