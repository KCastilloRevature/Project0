package testservices;

import org.junit.*;
import services.Validator;

public class TestValidator {
	@Test
	public void test_InsufficientFundsSuccess() {
		boolean resultFalse = Validator.ifInsufficientFunds(3000, 2000);
		boolean resultTrue = Validator.ifInsufficientFunds(2000, 3000);
		
		Assert.assertEquals(false, resultFalse);
		Assert.assertEquals(true, resultTrue);
	}
}
