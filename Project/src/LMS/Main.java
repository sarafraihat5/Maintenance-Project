
package LMS;

// Including Header Files.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
	// Clearing Required Area of Screen
	public static void clrscr() {
		for (int i = 0; i < 20; i++) {
			System.out.println();
		}
	}

	// Asking for Input as Choice
	public static int takeInput(int min, int max) {
		String choice;
		final var input = new Scanner(System.in);

		while (true) {
			System.out.println("\nEnter Choice: ");

			choice = input.next();

			if ((!choice.matches(".*[a-zA-Z]+.*"))
					&& (Integer.parseInt(choice) > min && Integer.parseInt(choice) < max)) {
				return Integer.parseInt(choice);
			} else {
				System.out.println("\nInvalid Input.");
			}
		}

	}

	// Functionalities of all Persons
	public static void allFunctionalities(Person person, int choice) throws IOException {
		final var lib = Library.getInstance();

		final var scanner = new Scanner(System.in);
		int input = 0;

		switch (choice) {
		case 1 -> lib.searchForBooks();
		case 2 -> {
			final var books = lib.searchForBooks();
			if (books != null) {
				input = takeInput(-1, books.size());

				final var b = books.get(input);

				if ("Clerk".equals(person.getClass().getSimpleName())
						|| "Librarian".equals(person.getClass().getSimpleName())) {
					final var bor = lib.findBorrower();

					if (bor != null) {
						b.makeHoldRequest(bor);
					}
				} else {
					b.makeHoldRequest((Borrower) person);
				}
			}
		}
		case 3 -> {
			if ("Clerk".equals(person.getClass().getSimpleName())
					|| "Librarian".equals(person.getClass().getSimpleName())) {
				final var bor = lib.findBorrower();

				if (bor != null) {
					bor.printInfo();
				}
			} else {
				person.printInfo();
			}
		}
		case 4 -> {
			if ("Clerk".equals(person.getClass().getSimpleName())
					|| "Librarian".equals(person.getClass().getSimpleName())) {
				final var bor = lib.findBorrower();

				if (bor != null) {
					final double totalFine = lib.computeFine2(bor);
					System.out.println("\nYour Total Fine is : Rs " + totalFine);
				}
			} else {
				final double totalFine = lib.computeFine2((Borrower) person);
				System.out.println("\nYour Total Fine is : Rs " + totalFine);
			}
		}
		case 5 -> {
			final var books = lib.searchForBooks();
			if (books != null) {
				input = takeInput(-1, books.size());
				books.get(input).printHoldRequests();
			}
		}
		case 6 -> {
			final var books = lib.searchForBooks();
			if (books != null) {
				input = takeInput(-1, books.size());
				final var b = books.get(input);

				final var bor = lib.findBorrower();

				if (bor != null) {
					b.issueBook(bor, (Staff) person);
				}
			}
		}
		case 7 -> {
			final var bor = lib.findBorrower();
			if (bor != null) {
				bor.printBorrowedBooks();
				final var loans = bor.getBorrowedBooks();

				if (!loans.isEmpty()) {
					input = takeInput(-1, loans.size());
					final var l = loans.get(input);

					l.getBook().returnBook(bor, l, (Staff) person);
				} else {
					System.out.println(new StringBuilder().append("\nThis borrower ").append(bor.getName())
							.append(" has no book to return.").toString());
				}
			}
		}
		case 8 -> {
			final var bor = lib.findBorrower();
			if (bor != null) {
				bor.printBorrowedBooks();
				final var loans = bor.getBorrowedBooks();

				if (!loans.isEmpty()) {
					input = takeInput(-1, loans.size());

					loans.get(input).renewIssuedBook(new java.util.Date());
				} else {
					System.out.println(new StringBuilder().append("\nThis borrower ").append(bor.getName())
							.append(" has no issued book which can be renewed.").toString());
				}
			}
		}
		case 9 -> lib.createPerson('b');
		case 10 -> {
			final var bor = lib.findBorrower();
			if (bor != null) {
				bor.updateBorrowerInfo();
			}
		}
		case 11 -> {
			final var reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("\nEnter Title:");
			final var title = reader.readLine();
			System.out.println("\nEnter Subject:");
			final var subject = reader.readLine();
			System.out.println("\nEnter Author:");
			final var author = reader.readLine();
			lib.createBook(title, subject, author);
		}
		case 12 -> {
			final var books = lib.searchForBooks();
			if (books != null) {
				input = takeInput(-1, books.size());

				lib.removeBookfromLibrary(books.get(input));
			}
		}
		case 13 -> {
			final var books = lib.searchForBooks();
			if (books != null) {
				input = takeInput(-1, books.size());

				books.get(input).changeBookInfo();
			}
		}
		case 14 -> {
			final var clerk = lib.findClerk();
			if (clerk != null) {
				clerk.printInfo();
			}
		}
		}

		// Functionality Performed.
		System.out.println("\nPress any key to continue..\n");
		scanner.next();
	}

	/*-------------------------------------MAIN---------------------------------------------------*/

	public static void main(String[] args) {
		final var admin = new Scanner(System.in);

		// -------------------INTERFACE---------------------------//

		final var lib = Library.getInstance();

		// Setting some by default information like name of library ,fine, deadline and
		// limit of hold request
		lib.setFine(20);
		lib.setRequestExpiry(7);
		lib.setReturnDeadline(5);
		lib.setName("FAST Library");

		// Making connection with Database.
		final var con = lib.makeConnection();

		if (con == null) // Oops can't connnect !
		{
			System.out.println("\nError connecting to Database. Exiting.");
			return;
		}

		try {

			lib.populateLibrary(con); // Populating Library with all Records

			boolean stop = false;
			while (!stop) {
				clrscr();

				// FRONT END //
				System.out.println("--------------------------------------------------------");
				System.out.println("\tWelcome to Library Management System");
				System.out.println("--------------------------------------------------------");

				System.out.println("Following Functionalities are available: \n");
				System.out.println("1- Login");
				System.out.println("2- Exit");
				System.out.println("3- Admininstrative Functions"); // Administration has access only

				System.out.println("-----------------------------------------\n");

				int choice = 0;

				choice = takeInput(0, 4);

				switch (choice) {
				case 3 -> {
					System.out.println("\nEnter Password: ");
					final var aPass = admin.next();
					if ("lib".equals(aPass)) {
						while (true) // Way to Admin Portal
						{
							clrscr();

							System.out.println("--------------------------------------------------------");
							System.out.println("\tWelcome to Admin's Portal");
							System.out.println("--------------------------------------------------------");
							System.out.println("Following Functionalities are available: \n");

							System.out.println("1- Add Clerk");
							System.out.println("2- Add Librarian");
							System.out.println("3- View Issued Books History");
							System.out.println("4- View All Books in Library");
							System.out.println("5- Logout");

							System.out.println("---------------------------------------------");

							choice = takeInput(0, 6);

							if (choice == 5) {
								break;
							}

							switch (choice) {
							case 1 -> lib.createPerson('c');
							case 2 -> lib.createPerson('l');
							case 3 -> lib.viewHistory();
							case 4 -> lib.viewAllBooks();
							}

							System.out.println("\nPress any key to continue..\n");
							admin.next();
						}
					} else {
						System.out.println("\nSorry! Wrong Password.");
					}
				}
				case 1 -> {
					final var person = lib.login();
					if (person == null) {
					}

					else if ("Borrower".equals(person.getClass().getSimpleName())) {
						while (true) // Way to Borrower's Portal
						{
							clrscr();

							System.out.println("--------------------------------------------------------");
							System.out.println("\tWelcome to Borrower's Portal");
							System.out.println("--------------------------------------------------------");
							System.out.println("Following Functionalities are available: \n");
							System.out.println("1- Search a Book");
							System.out.println("2- Place a Book on hold");
							System.out.println("3- Check Personal Info of Borrower");
							System.out.println("4- Check Total Fine of Borrower");
							System.out.println("5- Check Hold Requests Queue of a Book");
							System.out.println("6- Logout");
							System.out.println("--------------------------------------------------------");

							choice = takeInput(0, 7);

							if (choice == 6) {
								break;
							}

							allFunctionalities(person, choice);
						}
					}

					else if ("Clerk".equals(person.getClass().getSimpleName())) {
						while (true) // Way to Clerk's Portal
						{
							clrscr();

							System.out.println("--------------------------------------------------------");
							System.out.println("\tWelcome to Clerk's Portal");
							System.out.println("--------------------------------------------------------");
							System.out.println("Following Functionalities are available: \n");
							System.out.println("1- Search a Book");
							System.out.println("2- Place a Book on hold");
							System.out.println("3- Check Personal Info of Borrower");
							System.out.println("4- Check Total Fine of Borrower");
							System.out.println("5- Check Hold Requests Queue of a Book");
							System.out.println("6- Check out a Book");
							System.out.println("7- Check in a Book");
							System.out.println("8- Renew a Book");
							System.out.println("9- Add a new Borrower");
							System.out.println("10- Update a Borrower's Info");
							System.out.println("11- Logout");
							System.out.println("--------------------------------------------------------");

							choice = takeInput(0, 12);

							if (choice == 11) {
								break;
							}

							allFunctionalities(person, choice);
						}
					}

					else if ("Librarian".equals(person.getClass().getSimpleName())) {
						while (true) // Way to Librarian Portal
						{
							clrscr();

							System.out.println("--------------------------------------------------------");
							System.out.println("\tWelcome to Librarian's Portal");
							System.out.println("--------------------------------------------------------");
							System.out.println("Following Functionalities are available: \n");
							System.out.println("1- Search a Book");
							System.out.println("2- Place a Book on hold");
							System.out.println("3- Check Personal Info of Borrower");
							System.out.println("4- Check Total Fine of Borrower");
							System.out.println("5- Check Hold Requests Queue of a Book");
							System.out.println("6- Check out a Book");
							System.out.println("7- Check in a Book");
							System.out.println("8- Renew a Book");
							System.out.println("9- Add a new Borrower");
							System.out.println("10- Update a Borrower's Info");
							System.out.println("11- Add new Book");
							System.out.println("12- Remove a Book");
							System.out.println("13- Change a Book's Info");
							System.out.println("14- Check Personal Info of Clerk");
							System.out.println("15- Logout");
							System.out.println("--------------------------------------------------------");

							choice = takeInput(0, 16);

							if (choice == 15) {
								break;
							}

							allFunctionalities(person, choice);
						}
					}
				}
				default -> stop = true;
				}

				System.out.println("\nPress any key to continue..\n");
				final var scanner = new Scanner(System.in);
				scanner.next();
			}

			// Loading back all the records in database
			lib.fillItBack(con);
		} catch (Exception e) {
			System.out.println("\nExiting...\n");
		} // System Closed!

	} // Main Closed

} // Class closed.
