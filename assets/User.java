package assets;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.stream.*;

public class User {
    private String name;
    private String type;
    private int suspension;
    private int books;

    public User(String name, String type) {
        this.name = name;
        this.type = type;
        this.suspension = 0;
        this.books = 0;
    }
    
    public boolean incrementBooks() {
        if (this.type.charAt(0) == 's') {
            if ( this.books < 4) {
                this.books++;
                return true;
            }
        }
        else if (this.type.charAt(0) == 't') {
            if ( this.books < 6) {
                this.books++;
                return true;
            }
        }
        else {
            if ( this.books < 2) {
                this.books++;
                return true;
            }
        }
        return false;
    }
    
    public boolean decrementBooks() {
        if ( this.books > 0 ) {
            this.books--;
            return true;
        }
        else {
            return false;
        }
    }
    
    public int hasHowManyBooks() {
        return this.books;
    }
    
    public int getSuspension() {
        return this.suspension;
    }
    
    public String getName() {
        return this.name;
    }

    public String getType() {
        if (this.type.charAt(0) == 's') {
            return "student";
        }
        else if (this.type.charAt(0) == 't') {
            return "teacher";
        }
        else {
            return "other";
        }
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setSuspension(int days) {
        this.suspension = days;
    }

    public void setType(String type) {
        this.type = type;
    }

}
