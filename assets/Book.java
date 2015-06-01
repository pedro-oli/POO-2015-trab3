package assets;

// java classes
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.stream.*;

public class Book {
    private String type;
    private String name;
    private String author;
    private int year;
    private boolean available;

    public Book(String type, String name, String author, int year, boolean available) {
        this.type = type;
        this.name = name;
        this.author = author;
        this.year = year;
        this.available = available;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        if (this.type.charAt(0) == 't') {
            return "school text";
        }
        else {
            return "general book/others";
        }
    }
    
    public String getName() {
        return this.name;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getYear() {
        return this.year;
    }
    
    public boolean isAvailable() {
        return this.available;
    }
}
