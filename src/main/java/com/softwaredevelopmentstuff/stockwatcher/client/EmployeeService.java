package com.softwaredevelopmentstuff.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("employeeService")
public interface EmployeeService extends RemoteService {
    List<Employee> findAll() throws Exception;
}
