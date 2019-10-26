package com.softwaredevelopmentstuff.stockwatcher.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.softwaredevelopmentstuff.stockwatcher.client.Employee;
import com.softwaredevelopmentstuff.stockwatcher.client.EmployeeService;
import org.jdbi.v3.core.Jdbi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class EmployeeServiceImpl extends RemoteServiceServlet implements EmployeeService {

    @Override
    public List<Employee> findAll() throws Exception {
        Jdbi jdbi = getJdbi();

        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM employees"))
                .mapToBean(Employee.class)
                .list();
    }

    private Jdbi getJdbi() throws Exception {
        String url = "jdbc:mysql://localhost/test";
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection(url, "root", "password");
        return Jdbi.create(conn);
    }
}
