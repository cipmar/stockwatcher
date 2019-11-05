package com.softwaredevelopmentstuff.stockwatcher.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.List;

class EmployeesPanel {
    private Panel employeesPanel = new VerticalPanel();
    private Button loadEmployeesButton = new Button("Load employees");
    private FlexTable employeesTable = new FlexTable();

    private EmployeeServiceAsync employeeService = GWT.create(EmployeeService.class);

    EmployeesPanel() {
        // Load employees button
        employeesPanel.add(loadEmployeesButton);
        employeesPanel.add(employeesTable);

        // load employees click handler
        loadEmployeesButton.addClickHandler(clickEvent -> {
            employeeService.findAll(new AsyncCallback<List<Employee>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    // TODO
                }

                @Override
                public void onSuccess(List<Employee> employees) {
                    updateTable(employees);
                }
            });
        });
    }

    private void updateTable(List<Employee> employees) {
        employeesTable.setText(0, 0, "Employee No");
        employeesTable.setText(0, 1, "First Name");
        employeesTable.setText(0, 2, "Last Name");
        employeesTable.getRowFormatter().addStyleName(0, "watchListHeader");
        employeesTable.addStyleName("watchList");

        for (int i = 0; i < employees.size(); i++) {
            int row = i + 1;
            employeesTable.setText(row, 0, employees.get(i).getNo());
            employeesTable.setText(row, 1, employees.get(i).getFirstName());
            employeesTable.setText(row, 2, employees.get(i).getLastName());
        }
    }

    Panel getPanel() {
        return employeesPanel;
    }
}
