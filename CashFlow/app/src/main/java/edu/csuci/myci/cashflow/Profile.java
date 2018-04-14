package edu.csuci.myci.cashflow;

public class Profile {
    private String name;
    //private <????> database;  // TODO: this is where the database class is referenced

    public Profile(String name /*, <???> database*/) {
        this.name = name;

        // TODO: set this.database reference & any final setup for the database
    }

    public String getName() {
        return this.name;
    }

    // TODO: any methods the database might require for setup \ utilization
}
