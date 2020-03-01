package All;

//import org.testng.annotations.ITestAnnotation;
//import org.junit.jupiter.api.Test;

//import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;

public class AddressTest
{
    @Test
	public void testAddressParseNonNull()
    {
		//Address address = Address.parse("");
        //assertNotNull("Address.parse should not return null", address);
    }

    @Test
    public void quickMaths(){

        double skaleringsprodukt = (4*2) + (-3*2);
        System.out.println(skaleringsprodukt + "sk");
        double length = Math.sqrt((Math.pow(4,2) + Math.pow(-3,2)) * (Math.pow(2,2) + Math.pow(2,2)));
        System.out.println(length + "le");
        double compute = skaleringsprodukt/length;
        System.out.println(compute + "compute");
        double angle = Math.toDegrees(Math.acos(compute));
        System.out.println(angle);


    }
}
