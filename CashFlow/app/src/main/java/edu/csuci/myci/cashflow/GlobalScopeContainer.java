package edu.csuci.myci.cashflow;

import java.util.List;




// although not initially intended to be such, this is now a non-instanced (static) object
// to hold some application-wide data
// just include GlobalScopeContainer & reference as GlobalScopeContainer.variable
public class GlobalScopeContainer {
    private GlobalScopeContainer() {};  // stop instantiation by hiding constructor

    public static final int TRANSACTION_BUFFER_SIZE = 16;

    public static Profile activeProfile;               // currently active profile
    public static List<String> profileList;            // list of profiles (databases)
    public static Transaction[] transactionBuffer;     // latest set of transactions pulled from the database
}
