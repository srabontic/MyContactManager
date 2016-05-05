package com.example.user.mycontactmanager;

/**
 * Created by User on 27-03-2016.
 */
public class Person {
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String email;
    private String date;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Person(){
        super();
    }
    public Person(String firstName, String lastName, String phoneNo,String email, String date) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.date = date;
    }
}
