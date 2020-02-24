import java.util.Scanner;

public class IbanBerechnen {

	private final static int KONTO_DIGIT = 10;
	private final static int KONST = 98;

	public static void main(String[] args) {

		boolean abbruch = false;

		Scanner sc = new Scanner(System.in); // Create a Scanner object

		while (!abbruch) {

			// Clear screen
			System.out.print("\033\143");

			// Laenderkennung
			System.out.println("Geben Sie einen zweistellingen Länderkürzel an z.B. 'DE':");
			String laenderkennung = sc.nextLine();
			while (!laenderkennung.matches("(\\b[a-zA-Z]{2}\\b)")) {
				System.out.println("Nur zwei Buchstaben erlaubt! Bitte erneut versuchen.");
				laenderkennung = sc.nextLine();
			}

			// BLZ
			System.out.println("Geben Sie eine achtstellige BLZ an:");
			String blz = sc.nextLine();
			while (!blz.matches("(\\b[0-9]{8}\\b)")) {
				System.out.println("Nur achtstellige Nummern erlaubt! Bitte erneut versuchen.");
				blz = sc.nextLine();
			}

			// Kontonummer
			System.out.println("Geben Sie eine Konto Nummer an:");
			String kontonummer = sc.nextLine();
			while (!kontonummer.matches("(\\b[0-9]{1,10}\\b)")) {
				System.out.println("Nur Nummern bis zu 10 Stellen erlaubt! Bitte erneut versuchen.");
				kontonummer = sc.nextLine();
			}

			// Erzeuge IBAN
			String iban = erzeugeIban(laenderkennung, blz, kontonummer);

			// Ausgabe des Ergebnisses
			System.out.println("Ihre IBAN: " + iban);

			// neue Berechnung?
			System.out.println("\nWollen Sie eine weitere Berechnung durchführen? J/N");
			String doItagain = sc.nextLine();
			while (!doItagain.matches("(\\b[j,n,J,N]{1}\\b)")) {
				System.out.println("Nur J oder N erlaubt! Bitte erneut versuchen.");
				doItagain = sc.nextLine();
			}

			if (doItagain.toUpperCase().equals("J")) {
				abbruch = false;
			} else {
				abbruch = true;
			}

		}

		System.out.println("Danke und auf Wiedersehen.");
		sc.close();

	}

	public static String erzeugeIban(String laenderkennung, String blz, String nummer) {

		// Kontonummer mit führenden Nullen auf 10 Stellen auffüllen
		String normKtn = formatMitNullenLinks(nummer, KONTO_DIGIT);

		// erzeuge BBAN (BLZ + aufgefüllte Kontonummer)
		String bban = blz + normKtn;

		// Berechnen der Laenderkennung
		String contryCode = calcCountryCode(laenderkennung);

		// contryCode an BBAN anhaengen
		String bbanPlus = bban + contryCode;

		// berechne IBAN Prüfzahl
		String strCheckNumber = calcCheckNumber(bbanPlus);

		// make IBAN
		String iban = laenderkennung.toUpperCase() + strCheckNumber + bban;

		return iban;
	}

	private static String calcCheckNumber(String bbanPlus) {

		String myBbanPlus = bbanPlus;
		long modLastNumbers = 0;

		while (myBbanPlus.length() >= 0) {

			String neunStellen = myBbanPlus.substring(0, 9);

			long longNine = Long.parseLong(neunStellen);

			long modNine = longNine % 97;

			// Modulo Wert an restlichen bban Wert vorn ansetzen
			myBbanPlus = modNine + myBbanPlus.substring(9);

			if (myBbanPlus.length() < 9) {

				long lastNumbers = Long.parseLong(myBbanPlus);

				modLastNumbers = lastNumbers % 97;

				break;
			}

		}

		long longCheckNumber = KONST - modLastNumbers;

		String strCheckNumber = Long.toString(longCheckNumber);

		// führende Null anfügen falls einstellig
		if (strCheckNumber.length() < 2) {
			strCheckNumber = formatMitNullenLinks(strCheckNumber, 2);
		}

		return strCheckNumber;
	}

	private static String calcCountryCode(String laenderkennung) {

		int intCode = 0;
		String strCode = "";
		String countryCodeUpper = laenderkennung.toUpperCase();

		for (int i = 0; i < countryCodeUpper.length(); i++) {

			char c = countryCodeUpper.charAt(i);

			// aus A wird 9+1(Stelle im Alphabet)=10
			intCode = 9 + c - 'A' + 1;
			strCode = strCode + intCode;
		}

		strCode = strCode + "00";
		return strCode;
	}

	public static String formatMitNullenLinks(String nummer, int len) {
		//int value;

		// String nach int umwandeln
		// value = Integer.parseInt(nummer);

		// int nach String umwandeln
		// String result = String.valueOf(value);

		while (nummer.length() < len) {
			nummer = "0" + nummer;
		}
		return nummer;
	}
}
