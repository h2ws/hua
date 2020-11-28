import java.text.ParseException;

class PrintCommand extends Command {
    private String entityType;
    private String queryType;
    private String queryValue;

    PrintCommand(String[] tokens) {
        super();
        entityType = tokens[1];
        queryType = tokens[2];
        queryValue = tokens[3];
    }

    @Override
    void run(Database database) {
        if (entityType.equals("CUSTOMER"))
            runPrintCustomer(database);
        else if (entityType.equals("PLAN"))
            runPrintPlan(database);
        else {
            throw new BadCommandException("Bad print command.");
        }
    }

    private void runPrintCustomer(Database database) {
        if (queryType.equals("TOTAL_CLAIMED")) {
            System.out.println("Total amount claimed by " + database.getCustomer(queryValue).getName() +
                    " is " + database.totalClaimAmountByCustomer(queryValue));
        } else if (queryType.equals("TOTAL_RECEIVED")) {
            System.out.println("Total amount received by " + database.getCustomer(queryValue).getName() +
                            " is " + database.totalReceivedAmountByCustomer(queryValue));
        } else {
            throw new BadCommandException("Invalid PRINT CUSTOMER command.");
        }
    }

    //TODO
    private void runPrintPlan(Database database) { //this is for the commands by key word "Plan"
        if (queryType.equals("NUM_CUSTOMERS")) {
            System.out.println("Number of customers under " + queryValue +
                    " is " + database.numberOfCustomers(queryValue));
        } else if (queryType.equals("TOTAL_PAYED_TO_CUSTOMERS")) {
            System.out.println("Total amount payed under " + queryValue +
                    " is " + database.totalAmountPayedByCustomer(queryValue));
        } else {
            throw new BadCommandException("Invalid PRINT CUSTOMER command.");
        }
    }
}
