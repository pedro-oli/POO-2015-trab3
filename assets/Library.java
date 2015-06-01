package assets;

// java classes
import java.io.*;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Library {
    private List<User> users;
    private List<Book> books;
    private List<Loan> loans;
    private BufferedReader br;
    private Calendar date;

    public Library() {
        this.users = new ArrayList<User>();
        this.books = new ArrayList<Book>();
        this.loans = new ArrayList<Loan>();
        this.br = new BufferedReader(new InputStreamReader(System.in));
        this.date = new GregorianCalendar();
        this.date.setLenient(false);        
    }

    public void go() throws IOException {
        this.updateFromCSV();
        
        int userOption;
        boolean exit = false;
        boolean validOption = false;

        while (!exit) {
            
            // clearing console
            this.clearConsole();
            
            // prints date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            System.out.println("Date: " + sdf.format(this.date.getTime()));
            
            // menu
            this.showMenu();
            try {
                userOption = Integer.parseInt(br.readLine());            
                switch(userOption) {
                    case 1:
                        newUser();
                        break;

                    case 2:
                        newBook();
                        break;
                    
                    case 3:
                        newLoan();
                        break;

                    case 4:
                        returnBook();
                        break;
                            
                    case 5:
                        printUsers();                
                        break;

                    case 6:
                        printBooks();
                        break;
                    
                    case 7:
                        printLoans();
                        break;

                    case 8:
                        this.changeDate();
                        break;

                    case 9:
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid option! Press Enter to try again.");
                        // waits for user to click Enter
                        br.readLine();
                        break;
                }
            }
            catch (Exception e) {
                System.out.println("Invalid option! Press Enter to try again.");
                // waits for user to click Enter
                br.readLine();
            }
        }
    } // "main"
    
    public boolean isAvailable(Book book) {
        String bookName = book.getName();
        
        for (Loan l : this.loans) {
            if ( l.getBookName().equals(bookName) ) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean removeLoanFromCSV(String bookName) {
        try {
            // updating loans
            File fileLoans = new File("assets/loans.txt");
            File tempFile = new File("assets/tmpLoans.txt");

            BufferedReader readerCSV = new BufferedReader(new FileReader(fileLoans));
            BufferedWriter writerCSV = new BufferedWriter(new FileWriter(tempFile));

            String currentLine = null;
            while ( (currentLine = readerCSV.readLine()) != null ) {
                if ( currentLine.trim().contains(bookName) == false ) {
                    writerCSV.write(currentLine);
                    writerCSV.newLine();
                    writerCSV.flush();
                }
            }
            writerCSV.close();
            readerCSV.close();
            System.gc();

            // delete the original file
            if ( fileLoans.delete() == false ) {
                System.out.println("Could not delete file");
                return false;
            }

            // rename the new file to the filename the original file had.
            if ( tempFile.renameTo(fileLoans) == false ) {
                System.out.println("Could not rename file");
                return false;
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean validateAuthor(String bookAuthor) {
        return Pattern.matches("^[A-Z\\ ]+,\\ [A-Z]([a-zA-Z\\ \\.])+", bookAuthor);
    }
    
    public void changeDate() throws IOException {
        // clearing console
        this.clearConsole();

        boolean valid = false;
        while (!valid) {    
            // gets year
            System.out.println("Enter year: (Example: 2013)");
            int inputYear = Integer.parseInt(br.readLine());

            // gets month
            System.out.println("Enter month: (Example: 09)");
            int inputMonth = Integer.parseInt(br.readLine());

            // gets day
            System.out.println("Enter day: (Example: 26)");
            int inputDay = Integer.parseInt(br.readLine());

            // january is month 0
            this.date.set(inputYear, inputMonth - 1, inputDay);

            // validates user input (this.date is set to non-lenient)
            try {
                this.date.getTime();
                valid = true;
            }
            catch (IllegalArgumentException e) {
                this.clearConsole();
                System.out.println("Invalid input! Try again.\n");
                valid = false;
            }
        }
    }

    public void clearConsole() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
    
    public void showMenu() {
        System.out.println();
        System.out.println("--------------------------");
        System.out.println("Options:");
        System.out.println("1 - Register new user.");
        System.out.println("2 - Register new book.");
        System.out.println("3 - Register new loan.");
        System.out.println("4 - Return book.");
        System.out.println("5 - Show all users.");
        System.out.println("6 - Show all books.");
        System.out.println("7 - Show all loans.");
        System.out.println("8 - Change date.");
        System.out.println("9 - Exit.");
        System.out.println("--------------------------");
    }

    public void newUser() throws IOException {
        String userName = "";
        String userType = "";
        
        // gets userName
        boolean valid = false;
        while (!valid) {
            System.out.println("\nEnter name: ");
            userName = br.readLine();
            
            // validates userName
            valid = true;
            for ( User u : this.users ) {
                if ( u.getName().equals(userName) ) {
                    System.out.println("Username Taken! Try again.\n");
                    valid = false;
                }
            }
        }
        
        // gets userType
        valid = false;
        while (!valid) {
            System.out.println("\nEnter type: ('s' for student | 't' for teacher | 'o' for other)");
            userType = br.readLine();
            char c = userType.charAt(0);
            
            // validates userType
            if ( (c != 's') && (c != 't') && (c != 'o') ) {
                this.clearConsole();
                System.out.println("Invalid type! Try again.\n");
            }
            else {
                User newUser = new User(userName, userType);
                this.users.add(newUser);
                
                // writing newUser on users.txt
                FileWriter fw = new FileWriter("assets/users.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(userName + "|" + userType + "\n");
                bw.close();
                
                valid = true;
                
                // waits for user to click Enter
                System.out.println("\nRegistration completed! Press Enter to return to the menu.");
                br.readLine();
            }
        }
    }
    
    public void newBook() throws IOException {
        String bookType = "bookTypeError";
        String bookName = "bookNameError";
        String bookAuthor = "bookAuthorError";
        int bookYear = 2015;
            
        // gets bookType
        boolean valid = false;
        while (!valid) {
            System.out.println("Enter type: ('t' for \"school text\" | 'g' for \"general books/others\")");
            bookType = br.readLine();
            char c = bookType.charAt(0);
            
            // validates bookType
            if ( (c != 't') && (c != 'g') ) {
                this.clearConsole();
                System.out.println("Invalid type! Try again.\n");
            }
            else {
                valid = true; 
            }
        }

        // gets bookName
        System.out.println("Enter name:");
        bookName = br.readLine();

        // gets bookAuthor
        valid = false;
        while (!valid) {
            System.out.println("Enter author: (Format: ENGELS, Friedrich)");
            bookAuthor = br.readLine();
            // validates bookAuthor
            if (this.validateAuthor(bookAuthor) == false) {
                this.clearConsole();
                System.out.println("Invalid format! Try again.\n");
            }
            else {
                valid = true;
            }
        }
        
        // gets bookYear
        valid = false;
        while (!valid) {
            System.out.println("Enter publication year:");
            try {
                bookYear = Integer.parseInt(br.readLine());
                Book newBook = new Book(bookType, bookName, bookAuthor, bookYear, true);
                this.books.add(newBook);

                // writing newBook on books.txt
                FileWriter fw = new FileWriter("assets/books.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(bookType + "|" + bookName + "|" + bookAuthor + "|" + bookYear + "|" + true + "\n");
                bw.close();
                
                valid = true;
            }
            catch (Exception e) {
                this.clearConsole();
                System.out.println("Invalid year! Try again.\n");
            }            
        }
        
        // waits for user to click Enter
        System.out.println("\nRegistration completed! Press Enter to return to the menu.");
        br.readLine();
    }
    
    public void newLoan() throws IOException {
        boolean invalidUser = true;
        boolean suspendedUser = true;
        boolean maxBooks = true;
        String userName = "";
        String userType = "";
        String bookName = "";
        
        // validates user
        while ( (invalidUser == true) || (suspendedUser == true) || (maxBooks == true) ) {
            System.out.println("\nEnter username: ");
            userName = br.readLine();

            // checks if user exists and if they're not suspended
            for ( User u : this.users ) {                
                if ( u.getName().equals(userName) ) {
                    invalidUser = false;

                    // checks if user is suspended
                    if ( u.getSuspension() == 0 ) {
                        suspendedUser = false;
                        userType = u.getType();
                    }
                    
                    // checks if user already has the maximum quantity of books
                    if ( u.incrementBooks() == true ) {
                        maxBooks = false;
                    }                    
                }                
            }
            if (invalidUser) {
                System.out.println("Invalid user! Try again.\n");
            }
            else if (suspendedUser) {
                System.out.println("User is suspended! Try again.\n");
            }
            else if (maxBooks) {
                System.out.println("User can't loan more books! Try again.\n");
            }
        }
        
        // validates book
        boolean invalidBook = true;
        boolean unavailableBook = true;
        boolean userCantLoanThisBook = true;
        while ( (invalidBook == true) || (unavailableBook == true) || (userCantLoanThisBook == true) ) {
            System.out.println("\nEnter book name: ");
            bookName = br.readLine();
            
            // checks if book exists and if is available
            // AND if this type of user can take this type of book
            for ( Book b : this.books) {
                
                if ( b.getName().equals(bookName) ) {
                    invalidBook = false;
                    
                    if ( this.isAvailable(b) ) {
                        unavailableBook = false;
                        
                        if ( (b.getType().equals("school text")) && (userType.equals("other")) ) {
                            System.out.println("User is neither student nor teacher, therefore they can't get a school text!");
                            System.out.println("Try again.\n");
                        }
                        else {
                            userCantLoanThisBook = false;
                        }
                    }
                }
            }
            if (invalidBook) {
                System.out.println("Invalid book! Try again.\n");
            }
            else if (unavailableBook) {
                System.out.println("Book is unavailable! Try again.\n");
            }
        }

        // registers loan
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(this.date.getTime());
        
        Loan newLoan = new Loan(bookName, userName, dateString);
        this.loans.add(newLoan);

        // writing newLoan on loans.txt
        FileWriter fw = new FileWriter("assets/loans.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        
        bw.write(dateString + "|" + bookName + "|" + userName + "\n");
        bw.close();
        
        // waits for user to click Enter
        System.out.println("\nLoan completed! Press Enter to return to the menu.");
        br.readLine();
    }
    
    public void printUsers() throws IOException {
        if (this.users.isEmpty()) {
            System.out.println("There are no users registered yet!");
            // waits for user to click Enter
            System.out.println("\nPress Enter to return to the menu.");
            br.readLine();
            return;
        }
        else {
            for (User u : this.users) {
                System.out.print(u.getType() + "\t ");
                System.out.print(u.getName() + "\t ");
                if ( u.getSuspension() == 0 ) {
                    System.out.print("Not suspended.\n");
                }
                else {
                    System.out.print("Suspended for " + u.getSuspension() + " day(s).\n");
                }
            }
            // waits for user to click Enter
            System.out.println("\nPress Enter to return to the menu.");
            br.readLine();
        }
    }
    
    public void printBooks() throws IOException {
        if (this.books.isEmpty()) {
            System.out.println("There are no books registered yet!");
            // waits for user to click Enter
            System.out.println("\nPress Enter to return to the menu.");
            br.readLine();
        }
        else {
            for (Book b : this.books) {
                // for aesthetical reasons
                if (b.getType().equals("general book/others")) {
                    System.out.print(b.getType() + "\t");
                }
                else {
                    System.out.print(b.getType() + "\t\t");
                }

                System.out.print(b.getName() + "\t");
                System.out.print(b.getAuthor() + "\t");
                System.out.print(b.getYear() + "\n");
            }        

            // waits for user to click Enter
            System.out.println("\nPress Enter to return to the menu.");
            br.readLine();
        }
    }
    
    public void printLoans() throws IOException {
        if (this.loans.isEmpty()) {
            System.out.println("There are no loans registered yet!");
            // waits for user to click Enter
            System.out.println("\nPress Enter to return to the menu.");
            br.readLine();
        }
        else {
            for (Loan l : this.loans) {
                System.out.print(l.getDate() + "\t ");
                System.out.print(l.getUserName() + "\t ");
                System.out.print(l.getBookName() + "\n");
            }        

            // waits for user to click Enter
            System.out.println("\nPress Enter to return to the menu.");
            br.readLine();
        }
    }
        
    public void returnBook() throws IOException {
        // gets bookName
        System.out.println("Enter book name:");
        String bookName = br.readLine();

        // checks if book is in the loans list
        for ( Loan l : this.loans ) {
            if ( l.getBookName().equals(bookName) ) {
                if ( removeLoanFromCSV(bookName) ) {
                    this.loans.remove(l);
                    // waits for user to click Enter
                    System.out.println("\nBook returned! Press Enter to return to the menu.");
                    br.readLine();
                    return;
                }
                else {
                    System.out.println("ERROR! ::removing loan from CSV file ('loans.txt')::");
                    System.out.println("Press Enter to return to the menu.");
                    br.readLine();
                    return;
                }
            }
        }

        // waits for user to click Enter
        System.out.println("\nBook isn't in the loans list! Press Enter to return to the menu.");
        br.readLine();
    }
    
    public void updateFromCSV() {
        try {
            // updating books
            File fileBooks = new File("assets/books.txt");
            // if file doesn't exists, then create it
            if (!fileBooks.exists()) {
                fileBooks.createNewFile();
            }
            BufferedReader booksCSV = new BufferedReader(new FileReader(fileBooks));
            String line = null;
            while ( (line = booksCSV.readLine()) != null) {
                String[] parts = line.split("\\|");
                String bookType = parts[0];
                String bookName = parts[1];
                String bookAuthor = parts[2];
                int bookYear = Integer.parseInt(parts[3]);
                String available = parts[4];

                boolean bookAvailable = false;
                if ( available.equals("true") ) {
                    bookAvailable = true;
                }

                Book newBook = new Book(bookType, bookName, bookAuthor, bookYear, bookAvailable);
                this.books.add(newBook);
            }
        
            // updating users
            File fileUsers = new File("assets/users.txt");
            // if file doesn't exists, then create it
            if (!fileUsers.exists()) {
                fileUsers.createNewFile();
            }
            BufferedReader usersCSV = new BufferedReader(new FileReader(fileUsers));
            line = null;
            while ( (line = usersCSV.readLine()) != null) {
                String[] parts = line.split("\\|");
                String userName = parts[0];
                String userType = parts[1];
                User newUser = new User(userName, userType);
                this.users.add(newUser);
            }
            
            // updating loans
            File fileLoans = new File("assets/loans.txt");
            // if file doesn't exists, then create it
            if (!fileLoans.exists()) {
                fileLoans.createNewFile();
            }
            BufferedReader loansCSV = new BufferedReader(new FileReader(fileLoans));
            line = null;
            while ( (line = loansCSV.readLine()) != null) {
                String[] parts = line.split("\\|");
                
                // gets book
                String bookCSV = parts[0];
                String userCSV = parts[1];
                String dateCSV = parts[2];
                
                // increments how many books the current user has
                for ( User u : this.users ) {
                    if ( u.getName().equals(userCSV) ) {
                        u.incrementBooks();
                    }
                }
                        
                // adds to loans List
                Loan newLoan = new Loan(bookCSV, userCSV, dateCSV);
                this.loans.add(newLoan);
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
