package com.softwaredevelopmentstuff.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {

    private Panel mainLayout = new HorizontalPanel();

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        mainLayout.add(new StocksPanel().getPanel());
        mainLayout.add(new EmployeesPanel().getPanel());
        RootPanel.get("stockList").add(mainLayout);
    }
}
