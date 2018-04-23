package edu.csuci.myci.cashflow.database;

/**
 * Created by viktoriya on 4/4/18.
 */

public class TransactionDbSchema {
    public static final class TransactionTable{
        public static final String NAME = "Transactions";

        public static final class Cols{
            public static final String IDTRANSACTION = "idTransaction";
            public static final String TITLE = "title";
            public static final String DATE = "date";  //? do we need a separate 1?
            //public static final String CATEGORIES = "categories";
            public static final String AMOUNT = "amount";

        }

    }

    public static final class CategoryTable{
        public static final String NAME = "Categories";

        public static final class Cols{
            public static final String IDCATEGORY = "idCategory";
            public static final String CATEGORYNAME = "categoryName";
            public static final String LIMITAMOUNT = "limits";

        }
    }
    public static final class CategoryTransactionTable{
        public static final String NAME = "Cat_Transaction";

        public static final class Cols{
            public static final String IDCATEGORY = "idCategory";
            public static final String IDTRANSACTION = "idTransaction";

        }
    }

}
