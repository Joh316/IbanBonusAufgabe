import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class erzeugeIbanTest {
	
	@Test
	public void countryLetterShouldBeUpperCase() {
		assertEquals("DE40123456780000123456", IbanBerechnen.erzeugeIban("de", "12345678", "123456"));
			
	}
}
