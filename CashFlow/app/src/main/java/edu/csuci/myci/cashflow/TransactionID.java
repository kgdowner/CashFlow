package edu.csuci.myci.cashflow;

import java.time.LocalDate;

/**
 * An identifier that contains the transaction date, and the index for that day.
 */

public class TransactionID {
    private LocalDate date;
    private short dailyIndex;

    public TransactionID() {
        // set the date to the current date
        this.date = LocalDate.now();  // FIXME: AS complaining about sdk versions?

        // FIXME: get the daily index from the database ??
        // for now daily index is initialized as 0
        this.dailyIndex = 0;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public short getDailyIndex() {
        return this.dailyIndex;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDailyIndex(short index) {
        if(index >= 0) {
            this.dailyIndex = index;
        }

        // FIXME: throw error if < 0  ?
    }
}
