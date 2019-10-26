package com.softwaredevelopmentstuff.stockwatcher.client;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;

public class Employee implements Serializable {
    private String no;
    private String firstName;
    private String lastName;

    public Employee() {
    }

    @ColumnName("emp_no")
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @ColumnName("first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @ColumnName("last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "no='" + no + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
