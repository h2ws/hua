import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

abstract class Plan {
    String name;
    long premium;
    long maxCoveragePerClaim;
    long deductible;
    boolean nullValue = true;
    RangeCriterion customerAgeCriterion = new RangeCriterion();
    RangeCriterion customerIncomeCriterion = new RangeCriterion();

    Plan(HashMap<String, ArrayList<Tag>> tags) { //Assign the value of the corresponding position to the variable.
        if (tags.get("NAME") != null) { //It should be exist first then we can use it.
            name = tags.get("NAME").get(0).getValue();
        }else{
            nullValue = false; //If this value does not exist, return false for the subsequent work.
        }
        if (tags.get("PREMIUM") != null) {
            premium = Integer.parseInt(tags.get("PREMIUM").get(0).getValue());
        }else{
            nullValue = false;
        }
        if (tags.get("MAX_COVERAGE_PER_CLAIM") != null) {
            maxCoveragePerClaim = Integer.parseInt(tags.get("MAX_COVERAGE_PER_CLAIM").get(0).getValue());
        }else{
            nullValue = false;
        }
        if (tags.get("DEDUCTIBLE") != null) {
            deductible = Integer.parseInt(tags.get("DEDUCTIBLE").get(0).getValue());
        }else{
            nullValue = false;
        }



        if (tags.get("CUSTOMER.AGE") != null) {
            for (Tag tag: tags.get("CUSTOMER.AGE")) {
                customerAgeCriterion.addCriterion(tag);
            }
        }
        if (tags.get("CUSTOMER.INCOME") != null) {
            for (Tag tag: tags.get("CUSTOMER.INCOME")) {
                customerIncomeCriterion.addCriterion(tag);
            }
        }

    }

    abstract boolean isEligible(Insurable insurable, Date date);

    abstract Insurable getInsuredItem(Customer customer, Database database);

    boolean isEligible(Customer customer, Date currentDate) {
        // Extracting the approximate age of the customer (just based on the calendar years)
        LocalDate localCurrentDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localBirthDate = customer.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long age = localCurrentDate.getYear() - localBirthDate.getYear();
        // Checking if the age is in the range.
        if (!customerAgeCriterion.isInRange(age))
            return false;
        // Checking if the income is in the range.
        return customerIncomeCriterion.isInRange(customer.getIncome());
    }

    String getName() {
        return name;
    }

    long getPremium() {
        return premium;
    }

    long getMaxCoveragePerClaim() {
        return maxCoveragePerClaim;
    }

    //This method is used to confirm if there is null inside the plan
    boolean getNullValue() {
        return nullValue;
    }


    long getDeductible() {
        return deductible;
    }
}
