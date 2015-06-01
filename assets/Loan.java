package assets;

// java classes
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.stream.*;

public class Loan {
    private String bookName;
    private String userName;
    private String date;

    public Loan(String bookName, String userName, String date) {
        this.bookName = bookName;
        this.userName = userName;
        this.date = date;
    }

    public String getBookName() {
        return this.bookName;
    }

    public String getUserName() {
        return this.userName;
    }
        
    public String getDate() {
        return this.date;
    }
}