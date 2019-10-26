package com.softwaredevelopmentstuff.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface EmployeeServiceAsync {
    void findAll(AsyncCallback<List<Employee>> async);
}
