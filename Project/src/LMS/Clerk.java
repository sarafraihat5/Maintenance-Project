
package LMS;

public class Clerk extends Staff {

	public static int currentdeskNumber = 0;
	int deskNo; // Desk Number of the Clerk

	public Clerk(int id, String n, String a, int ph, double s, int dk) // para cons.
	{
		super(id, n, a, ph, s);

		deskNo = dk == -1 ? currentdeskNumber : dk;

		currentdeskNumber++;
	}

	// Printing Clerk's Info
	@Override
	public void printInfo() {
		super.printInfo();
		System.out.println("Desk Number: " + deskNo);
	}

} // Clerk's Class Closed