import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

class Company extends Insurable {
    private String companyName;

    static final String inputTag = "COMPANY";

    Company(HashMap<String, ArrayList<Tag>> tags) throws ParseException {
        super(tags);
        companyName = tags.get("COMPANY_NAME").get(0).getValue();
    }

    public String getCompanyName() {
        return companyName;
    }

    public static String getInputTag() {
        return inputTag;
    }
}
