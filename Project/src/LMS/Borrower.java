
package LMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Borrower extends Person {
	private final ArrayList<Loan> borrowedBooks; // Those books which are currently borrowed by this borrower
	private final ArrayList<HoldRequest> onHoldBooks; // Those books which are currently requested by this borrower to
														// be on hold

	public Borrower(int id, String name, String address, int phoneNum) // para. cons
	{
		super(id, name, address, phoneNum);

		borrowedBooks = new ArrayList();
		onHoldBooks = new ArrayList();
	}

	// Printing Borrower's Info
	@Override
	public void printInfo() {
		super.printInfo();

		printBorrowedBooks();
		printOnHoldBooks();
	}

	// Printing Book's Info Borrowed by Borrower
	public void printBorrowedBooks() {
		if (!borrowedBooks.isEmpty()) {
			System.out.println("\nBorrowed Books are: ");

			System.out.println("------------------------------------------------------------------------------");
			System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
			System.out.println("------------------------------------------------------------------------------");

			for (int i = 0; i < borrowedBooks.size(); i++) {
				System.out.print(new StringBuilder().append(i).append("-").append("\t\t").toString());
				borrowedBooks.get(i).getBook().printInfo();
				System.out.print("\n");
			}
		} else {
			System.out.println("\nNo borrowed books.");
		}
	}

	// Printing Book's Info kept on Hold by Borrower
	public void printOnHoldBooks() {
		if (!onHoldBooks.isEmpty()) {
			System.out.println("\nOn Hold Books are: ");

			System.out.println("------------------------------------------------------------------------------");
			System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
			System.out.println("------------------------------------------------------------------------------");

			for (int i = 0; i < onHoldBooks.size(); i++) {
				System.out.print(new StringBuilder().append(i).append("-").append("\t\t").toString());
				onHoldBooks.get(i).getBook().printInfo();
				System.out.print("\n");
			}
		} else {
			System.out.println("\nNo On Hold books.");
		}
	}

	// Updating Borrower's Info
	public void updateBorrowerInfo() throws IOException {
		String choice;

		final var sc = new Scanner(System.in);
		final var reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println(new StringBuilder().append("\nDo you want to update ").append(getName())
				.append("'s Name ? (y/n)").toString());
		choice = sc.next();

		updateBorrowerName(choice, reader);

		System.out.println(new StringBuilder().append("\nDo you want to update ").append(getName())
				.append("'s Address ? (y/n)").toString());
		choice = sc.next();

		updateBorrowerAddress(choice, reader);

		System.out.println(new StringBuilder().append("\nDo you want to update ").append(getName())
				.append("'s Phone Number ? (y/n)").toString());
		choice = sc.next();

		updateBorrowerPhoneNumber(choice, sc);

		System.out.println("\nBorrower is successfully updated.");

	}

	private void updateBorrowerPhoneNumber(String choice, Scanner sc) {
		if (!"y".equals(choice)) {
			return;
		}
		System.out.println("\nType New Phone Number: ");
		setPhone(sc.nextInt());
		System.out.println("\nThe phone number is successfully updated.");
	}

	private void updateBorrowerAddress(String choice, BufferedReader reader) throws IOException {
		if (!"y".equals(choice)) {
			return;
		}
		System.out.println("\nType New Address: ");
		setAddress(reader.readLine());
		System.out.println("\nThe address is successfully updated.");
	}

	private void updateBorrowerName(String choice, BufferedReader reader) throws IOException {
		if (!"y".equals(choice)) {
			return;
		}
		System.out.println("\nType New Name: ");
		setName(reader.readLine());
		System.out.println("\nThe name is successfully updated.");
	}

	/*-- Adding and Removing from Borrowed Books---*/
	public void addBorrowedBook(Loan iBook) {
		borrowedBooks.add(iBook);
	}

	public void removeBorrowedBook(Loan iBook) {
		borrowedBooks.remove(iBook);
	}

	/*-------------------------------------------*/

	/*-- Adding and Removing from On Hold Books---*/
	public void addHoldRequest(HoldRequest hr) {
		onHoldBooks.add(hr);
	}

	public void removeHoldRequest(HoldRequest hr) {
		onHoldBooks.remove(hr);
	}

	/*-------------------------------------------*/

	/*-----------Getter FUNCs. ------------------*/
	public ArrayList<Loan> getBorrowedBooks() {
		return borrowedBooks;
	}

	public ArrayList<HoldRequest> getOnHoldBooks() {
		return onHoldBooks;
	}
	/*-------------------------------------------*/
}
