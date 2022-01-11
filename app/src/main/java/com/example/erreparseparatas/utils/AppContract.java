package com.example.erreparseparatas.utils;

import android.provider.BaseColumns;

public class AppContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AppContract() {}

    /* Inner class that defines the table contents */
    public static class DataEntry implements BaseColumns {
        public static final String TABLE_NAME = "Data";

        public static final String ID = "IdUser";
        public static final String MES = "Mes";
        public static final String CN = "CN";
        public static final String CMU = "CMU";
        public static final String IMU = "IMU";
        public static final String RCU = "RCU";
        public static final String CAU = "CAU";
        public static final String VU = "VU";
        public static final String CEP = "CEP";
        public static final String CAP = "CAP";
        public static final String IAP = "IAP";
    }
}
