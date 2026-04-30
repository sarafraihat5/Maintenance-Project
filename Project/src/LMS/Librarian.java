
package LMS;

import static LMS.Library.librarian;
import static LMS.Library.persons;

public class Librarian extends Staff {

	public static int currentOfficeNumber = 0;
	int officeNo; // Office Number of the Librarian

	public Librarian(int id, String n, String a, int p, double s, int of) // para cons.
	{
		super(id, n, a, p, s);

		officeNo = of == -1 ? currentOfficeNumber : of;

		currentOfficeNumber++;
	}

	// Printing Librarian's Info
	@Override
	public void printInfo() {
		super.printInfo();
		System.out.println("Office Number: " + officeNo);
	}

	public static boolean addLibrarian(Librarian lib) {
		// One Library can have only one Librarian
		if (librarian == null) {
			librarian = lib;
			persons.add(librarian);
			return true;
		} else {
			System.out.println("\nSorry, the library already has one librarian. New Librarian can't be created.");
		}
		return false;
	}
}