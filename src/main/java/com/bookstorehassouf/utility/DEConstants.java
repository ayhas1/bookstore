package com.bookstorehassouf.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DEConstants {

    public final static String DE = "DE";

    public final static Map<String, String> mapOfDEStates = new HashMap<String, String>() {
        {

            put("BW","Baden-Württemberg");
            put("BY","Bayern");
            put("BB","Brandenburg");
            put("HB","Hamburg");
            put("HH","Hessen");
            put("MV","Mecklenburg-Vorpommern");
            put("NI","Niedersachsen");
            put("NRW","Nordrhein-Westfalen");
            put("RP","Rheinland-Pfalz");
            put("SL","Saarland");
            put("SN","Sachsen");
            put("ST","Sachsen-Anhalt");
            put("SH","schleswig-Holstein");
            put("TH","Thüringen");
        }
    };

    public final static List<String> listOfDEStatesCode = new ArrayList<>(mapOfDEStates.keySet());
    public final static List<String> listOfDEStatesName = new ArrayList<>(mapOfDEStates.values());
}
