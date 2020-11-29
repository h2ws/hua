import java.util.ArrayList;

class Database {
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Home> homes = new ArrayList<>();
    private ArrayList<Car> cars = new ArrayList<>();
    private ArrayList<Plan> plans = new ArrayList<>();
    private ArrayList<Contract> contracts = new ArrayList<>();
    private ArrayList<Claim> claims = new ArrayList<>();
    private ArrayList<Company> companys = new ArrayList<>();

    void insertHome(Home home) {
        homes.add(home);
    }

    void insertCar(Car car) {
        cars.add(car);
    }

    void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    void insertPlan(Plan plan) {
        plans.add(plan);
    }

    void insertClaim(Claim claim) {
        claims.add(claim);
    }

    void insertContract(Contract contract) {
        contracts.add(contract);
    }

    void insertCompany(Company company) {
        companys.add(company);
    }

    Plan getPlan(String name) {
        for (Plan plan : plans) {
            if (plan.name.equals(name))
                return plan;
        }
        return null;
    }

    /*
     *  I created this method 'comparePlan', which is used to check if this plan actually exist. the parameter 'name' means the plan name that we input.
     *  There are a lot of files need this function. Because if the plan does not exist, it will generate many errors.
     */
    boolean comparePlan(String name) {
        boolean planExist = false;
        for (Plan plan : plans){
            if(plan.getName().equals(name) && plan.getNullValue() == true){
                planExist = true;
            }
        }
        return planExist;
    }

    Company getCompany(String companyName) {
        for (Company company : companys) {
            if (company.getCompanyName().equals(companyName))
                return company;
        }
        return null;
    }

    Customer getCustomer(String name) {
        for (Customer customer : customers) {
            if (customer.getName().equals(name))
                return customer;
        }
        return null;
    }

    Contract getContract(String name) {
        for (Contract contract : contracts) {
            if (contract.getContractName().equals(name))
                return contract;
        }
        return null;
    }


    Home getHomeByPostalCode(String postalCode) {
	int c = 0;
	Home r = null;
        for (Home home : homes) {
            if (home.getPostalCode().equals(postalCode))
	    {
		    r = home;
		    c++;
	    }
        }
	//if(!( c == 1 || c == 0)) throw new RuntimeException("TEST");
        return r;
    }
    
    Car getCarByPlateNumber2(String plateNumber) {
        for (Car car : cars) {
            if (car.getPlateNumber().equals(plateNumber))
	         return car;
        }
        return null;
    }

    Car getCarByPlateNumber(String plateNumber) {
	int c = 0;
	Car r = null;
        for (Car car : cars) {
            if (car.getPlateNumber().equals(plateNumber))
	    {
		    r = car;
		    c++;
	    }
        }
	if(c>1) throw new RuntimeException("TEST");
        return r;
    }

    long totalWealthAmountByCompany(Company company) {
        long totalAmount = 0;
        for (Company eachCompany : companys) {
	    if(eachCompany.getOwnerName().equals(company.getCompanyName())) {
	        totalAmount += totalWealthAmountByCompany(eachCompany);
	    }
	}
	return totalAmount+company.getValue();
    }

    long totalWealthAmountByCustomer(Customer customer) {
	long totalAmount = 0;
        for (Company company : companys) {
	    if(company.getOwnerName().equals(customer.getName())) {
	        totalAmount += totalWealthAmountByCompany(company);
	    }
	}
        for (Home home : homes) {
	    if(home.getOwnerName().equals(customer.getName())) {
	        totalAmount += home.getValue();
	    }
	}
        for (Car car : cars) {
	    if(car.getOwnerName().equals(customer.getName())) {
	        totalAmount += car.getValue();
	    }
	}
	return totalAmount;
    }


    long totalClaimAmountByCustomer(String customerName) {
        long totalClaimed = 0;
        for (Claim claim : claims) {
            Contract contract = getContract(claim.getContractName());
	    Plan planOf = getPlan(contract.getPlanName());
	    if (planOf !=null && planOf.getNullValue() == true && getContract(claim.getContractName()).getCustomerName().equals(customerName))
                totalClaimed += claim.getAmount();
        }
        return totalClaimed;
    }

    long totalReceivedAmountByCustomer(String customerName) {
        long totalReceived = 0;
        boolean planExist = false;
        for (Claim claim : claims) {
            Contract contract = getContract(claim.getContractName());
            if (contract.getCustomerName().equals(customerName)) {
                for (Plan plan : plans){ //we need to determine if this plan actually exist first. if not then this command should return 0
                    if(plan.getName().equals(contract.getPlanName()) && plan.getNullValue() == true){ //This getNullValue method is used to confirm if there is null inside the plan
                        planExist = true;
                    }
                }
                if (claim.wasSuccessful() && planExist == true) {
                    long deductible = getPlan(contract.getPlanName()).getDeductible();
                    totalReceived += Math.max(0, claim.getAmount() - deductible);
                }
            }
        }
        return totalReceived;
    }
    /*
     *This method is used for the command "PRINT PLAN NUM_CUSTOMERS", which can print how many customers under this certain plan.
     *This method should be used in the file of "PrintCommand"
     */
    long numberOfCustomers(String planName) {
        long totalNumber = 0; // zero customers in the beginning
        boolean planExist = false;
        for (Plan plan : plans){ //Firstly we should check if this plan actually exist.
            if(plan.getName().equals(planName) && plan.getNullValue() == true){
                planExist = true;
            }
        }
        for (Contract contract : contracts) { //check every contracts to find which contract is used for the plan that we need
            if (contract.getPlanName().equals(planName) && planExist == true) { //check if it's the correct plan
                totalNumber++; //add one customer
            }
        }
        return totalNumber; //how many customers under this plan
    }

    /*
     *This method is used for the command "PRINT PLAN TOTAL_PAYED_TO_CUSTOMERS", which can print how much payed by this certain plan.
     *This method should be used in the file of "PrintCommand"
     */
    long totalAmountPayedByCustomer(String planName) {
        long totalPayed = 0; // zero payment in the beginning
        boolean planExist = false;
        for (Plan plan : plans){ //Firstly we should check if this plan actually exist.
            if(plan.getName().equals(planName) && plan.getNullValue() == true){
                planExist = true;
            }
        }
        for (Claim claim : claims) { //check every claims to find which claims has the contract that is used for the plan that we need.
            if (claim.wasSuccessful()) {
                String nameOfContract = claim.getContractName(); //store the name of this contract recently
                if (getContract(nameOfContract).getPlanName().equals(planName) && planExist == true) { //check if this contract is used for the plan that we need.
                    long contractFee = getPlan(planName).deductible;
                    totalPayed += Math.max(0, claim.getAmount() - contractFee); //if yes, calculate the payment of each claim which is belong to this plan
                }

            }
        }
        return totalPayed;
    }

}
