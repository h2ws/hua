import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

class CarPlan extends Plan {
    static final String inputTag = "CAR_PLAN";
    RangeCriterion mileageCriterion = new RangeCriterion();

    CarPlan(HashMap<String, ArrayList<Tag>> tags) {
        super(tags);

        if (tags.get("CAR.MILEAGE") != null) {
            for (Tag tag : tags.get("CAR.MILEAGE")) {
                mileageCriterion.addCriterion(tag);
            }
        }
    }

    @Override
    boolean isEligible(Insurable insurable, Date date) {
        if (!(insurable instanceof Car))
            return false;
        Car car = (Car) insurable;
        return mileageCriterion.isInRange(car.getMileage());
    }

    @Override
    Insurable getInsuredItem(Customer customer, Claim claim, Database database) {
        Insurable car = database.getCarByPlateNumber(claim.getInsurableId());
	if ( car == null ) return null;
	if ( !car.getOwnerName().equals(customer.getName()) ) return null;
	return car;
    }
}
