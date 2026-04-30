
package LMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Book {

	static int currentIdNumber = 0; // This will be unique for every book, since it will be incremented when
									// everytime
	private final int bookID; // ID given by a library to a book to make it distinguishable from other books
	private String title; // Title of a book
	private String subject; // Subject to which a book is related!
	private String author; // Author of book!
	private boolean isIssued; // this will be true if the book is currently issued to some borrower.
	private final HoldRequestOperations holdRequestsOperations = new HoldRequestOperations();
	// when a book is created

	public Book(int id, String t, String s, String a, boolean issued) // Parameterise cons.
	{
		currentIdNumber++;
		bookID = id == -1 ? currentIdNumber : id;

		title = t;
		subject = s;
		author = a;
		isIssued = issued;

	}

	// printing all hold req on a book.
	public void printHoldRequests() {
		if (!holdRequestsOperations.holdRequests.isEmpty()) {
			System.out.println("\nHold Requests are: ");

			System.out.println(
					"---------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("No.\t\tBook's Title\t\t\tBorrower's Name\t\t\tRequest Date");
			System.out.println(
					"---------------------------------------------------------------------------------------------------------------------------------------");

			for (int i = 0; i < holdRequestsOperations.holdRequests.size(); i++) {
				System.out.print(new StringBuilder().append(i).append("-").append("\t\t").toString());
				holdRequestsOperations.holdRequests.get(i).print();
			}
		} else {
			System.out.println("\nNo Hold Requests.");
		}
	}

	// printing book's Info
	public void printInfo() {
		System.out.println(new StringBuilder().append(title).append("\t\t\t").append(author).append("\t\t\t")
				.append(subject).toString());
	}

	// changign Info of a Book
	public void changeBookInfo() throws IOException {
		final var scanner = new Scanner(System.in);
		String input;

		final var reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("\nUpdate Author? (y/n)");
		input = scanner.next();

		if ("y".equals(input)) {
			System.out.println("\nEnter new Author: ");
			author = reader.readLine();
		}

		System.out.println("\nUpdate Subject? (y/n)");
		input = scanner.next();

		if ("y".equals(input)) {
			System.out.println("\nEnter new Subject: ");
			subject = reader.readLine();
		}

		System.out.println("\nUpdate Title? (y/n)");
		input = scanner.next();

		if ("y".equals(input)) {
			System.out.println("\nEnter new Title: ");
			title = reader.readLine();
		}

		System.out.println("\nBook is successfully updated.");

	}

	/*------------Getter FUNCs.---------*/

	public String getTitle() {
		return title;
	}

	public String getSubject() {
		return subject;
	}

	public String getAuthor() {
		return author;
	}

	public boolean getIssuedStatus() {
		return isIssued;
	}

	public void setIssuedStatus(boolean s) {
		isIssued = s;
	}

	public int getID() {
		return bookID;
	}

	public ArrayList<HoldRequest> getHoldRequests() {
		return holdRequestsOperations.holdRequests;
	}

	/*-----------------------------------*/

	// Setter Static Func.
	public static void setIDCount(int n) {
		currentIdNumber = n;
	}

	// -------------------------------------------------------------------//

	// Placing book on Hold
	public void placeBookOnHold(Borrower bor) {
		final var hr = new HoldRequest(bor, this, new Date());

		holdRequestsOperations.addHoldRequest(hr); // Add this hold request to holdRequests queue of this book
		bor.addHoldRequest(hr); // Add this hold request to that particular borrower's class as well

		System.out.println(new StringBuilder().append("\nThe book ").append(title)
				.append(" has been successfully placed on hold by borrower ").append(bor.getName()).append(".\n")
				.toString());
	}

	// Request for Holding a Book
	public void makeHoldRequest(Borrower borrower) {
		boolean makeRequest = true;

		// If that borrower has already borrowed that particular book. Then he isn't
		// allowed to make request for that book. He will have to renew the issued book
		// in order to extend the return deadline.
		for (int i = 0; i < borrower.getBorrowedBooks().size(); i++) {
			if (borrower.getBorrowedBooks().get(i).getBook() == this) {
				System.out.println(
						new StringBuilder().append("\n").append("You have already borrowed ").append(title).toString());
				return;
			}
		}

		// If that borrower has already requested for that particular book. Then he
		// isn't allowed to make the same request again.
		for (int i = 0; i < holdRequestsOperations.holdRequests.size(); i++) {
			if ((holdRequestsOperations.holdRequests.get(i).getBorrower() == borrower)) {
				makeRequest = false;
				break;
			}
		}

		if (makeRequest) {
			placeBookOnHold(borrower);
		} else {
			System.out.println("\nYou already have one hold request for this book.\n");
		}
	}

	// Getting Info of a Hold Request
	public void serviceHoldRequest(HoldRequest hr) {
		holdRequestsOperations.removeHoldRequest();
		hr.getBorrower().removeHoldRequest(hr);
	}

	// Issuing a Book
	public void issueBook(Borrower borrower, Staff staff) {
		// First deleting the expired hold requests
		final var today = new Date();

		final var hRequests = holdRequestsOperations.holdRequests;

		hRequests.forEach(hr -> {
			// Remove that hold request which has expired
			long days = ChronoUnit.DAYS.between(today.toInstant(), hr.getRequestDate().toInstant());
			days = 0 - days;

			if (days > Library.getInstance().getHoldRequestExpiry()) {
				holdRequestsOperations.removeHoldRequest();
				hr.getBorrower().removeHoldRequest(hr);
			}
		});

		if (isIssued) {
			System.out.println(
					new StringBuilder().append("\nThe book ").append(title).append(" is already issued.").toString());
			System.out.println("Would you like to place the book on hold? (y/n)");

			final var sc = new Scanner(System.in);
			final var choice = sc.next();

			if ("y".equals(choice)) {
				makeHoldRequest(borrower);
			}
		}

		else {
			if (!holdRequestsOperations.holdRequests.isEmpty()) {
				boolean hasRequest = false;

				for (int i = 0; i < holdRequestsOperations.holdRequests.size() && !hasRequest; i++) {
					if (holdRequestsOperations.holdRequests.get(i).getBorrower() == borrower) {
						hasRequest = true;
					}

				}

				if (hasRequest) {
					// If this particular borrower has the earliest request for this book
					if (holdRequestsOperations.holdRequests.get(0).getBorrower() == borrower) {
						serviceHoldRequest(holdRequestsOperations.holdRequests.get(0));
					} else {
						System.out.println(
								"\nSorry some other users have requested for this book earlier than you. So you have to wait until their hold requests are processed.");
						return;
					}
				} else {
					System.out.println(
							"\nSome users have already placed this book on request and you haven't, so the book can't be issued to you.");

					System.out.println("Would you like to place the book on hold? (y/n)");

					final var sc = new Scanner(System.in);
					final var choice = sc.next();

					if ("y".equals(choice)) {
						makeHoldRequest(borrower);
					}

					return;
				}
			}

			// If there are no hold requests for this book, then simply issue the book.
			setIssuedStatus(true);

			final var iHistory = new Loan(borrower, this, staff, null, new Date(), null, false);

			Library.getInstance().addLoan(iHistory);
			borrower.addBorrowedBook(iHistory);

			System.out.println(new StringBuilder().append("\nThe book ").append(title)
					.append(" is successfully issued to ").append(borrower.getName()).append(".").toString());
			System.out.println("\nIssued by: " + staff.getName());
		}
	}

	// Returning a Book
	public void returnBook(Borrower borrower, Loan l, Staff staff) {
		l.getBook().setIssuedStatus(false);
		l.setReturnedDate(new Date());
		l.setReceiver(staff);

		borrower.removeBorrowedBook(l);

		l.payFine();

		System.out.println(new StringBuilder().append("\nThe book ").append(l.getBook().getTitle())
				.append(" is successfully returned by ").append(borrower.getName()).append(".").toString());
		System.out.println("\nReceived by: " + staff.getName());
	}

} // Book Class Closed