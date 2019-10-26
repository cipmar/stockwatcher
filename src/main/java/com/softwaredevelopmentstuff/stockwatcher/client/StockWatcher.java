package com.softwaredevelopmentstuff.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {
    private static final int REFRESH_INTERVAL = 5000; // ms

    private VerticalPanel mainPanel = new VerticalPanel();
    private FlexTable stocksFlexTable = new FlexTable();
    private HorizontalPanel addPanel = new HorizontalPanel();
    private TextBox newSymbolTextBox = new TextBox();
    private Button addStockButton = new Button("Add");
    private Label lastUpdatedLabel = new Label();
    private Label errorMsgLabel = new Label();

    private List<String> stocks = new ArrayList<>();
    private StockPriceServiceAsync stockPriceService = GWT.create(StockPriceService.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        stocksFlexTable.setText(0, 0, "Symbol");
        stocksFlexTable.setText(0, 1, "Price");
        stocksFlexTable.setText(0, 2, "Change");
        stocksFlexTable.setText(0, 3, "Remove");

        stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
        stocksFlexTable.addStyleName("watchList");
        stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");

        // Add stock panel
        addPanel.add(newSymbolTextBox);
        addPanel.add(addStockButton);
        addPanel.setStyleName("addPanel");

        // Main panel
        errorMsgLabel.setStyleName("errorMessage");
        errorMsgLabel.setVisible(false);

        mainPanel.add(errorMsgLabel);
        mainPanel.add(stocksFlexTable);
        mainPanel.add(addPanel);
        mainPanel.add(lastUpdatedLabel);

        RootPanel.get("stockList").add(mainPanel);

        newSymbolTextBox.setFocus(true);

        // setup timer to refresh data automatically
        Timer refreshTimer = new Timer() {
            @Override
            public void run() {
                refreshStockData();
            }
        };

        refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

        addStockButton.addClickHandler(clickEvent -> addStock());
        newSymbolTextBox.addKeyDownHandler(keyDownEvent -> {
            if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                addStock();
            }
        });
    }

    private void refreshStockData() {
        AsyncCallback<StockPrice[]> asyncCallback = new AsyncCallback<StockPrice[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                String error = throwable.getMessage();

                if (throwable instanceof DelistedException) {
                    error = "Company " + ((DelistedException) throwable).getSymbol() + " was delisted!";
                }

                errorMsgLabel.setText("Error: " + error);
                errorMsgLabel.setVisible(true);
            }

            @Override
            public void onSuccess(StockPrice[] prices) {
                updateTable(prices);
                errorMsgLabel.setText("");
                errorMsgLabel.setVisible(false);
            }
        };

        stockPriceService.getPrices(stocks.toArray(new String[0]), asyncCallback);
    }

    private void updateTable(StockPrice[] prices) {
        for (StockPrice price : prices) {
            updateTableRow(price);
        }

        DateTimeFormat dtf = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
        lastUpdatedLabel.setText("Last updated: " + dtf.format(new Date()));
    }

    private void updateTableRow(StockPrice price) {
        if (!stocks.contains(price.getSymbol())) {
            return;
        }

        int row = stocks.indexOf(price.getSymbol()) + 1;

        String priceText = NumberFormat.getFormat("#,##0.00").format(price.getPrice());
        NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
        String changeText = changeFormat.format(price.getChange());
        String changePercentText = changeFormat.format(price.getChangePercent());

        stocksFlexTable.setText(row, 1, priceText);
        Label changeLabel = (Label) stocksFlexTable.getWidget(row, 2);
        changeLabel.setText(changeText + " (" + changePercentText + ")");

        String changeStyleName = "noChange";
        if (price.getChangePercent() < 0.1f) {
            changeStyleName = "negativeChange";
        }

        if (price.getChangePercent() > 0.1f) {
            changeStyleName = "positiveChange";
        }
        changeLabel.setStyleName(changeStyleName);
    }

    private void addStock() {
        final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
        newSymbolTextBox.setFocus(true);

        if (!symbol.matches("^[0-9A-Z.]{1,10}$")) {
            Window.alert("Invalid symbol");
            newSymbolTextBox.selectAll();
            return;
        }

        newSymbolTextBox.setText("");

        // Don't add stock if it's already in the table
        if (stocks.contains(symbol)) {
            return;
        }

        // Add symbol to the table
        int row = stocksFlexTable.getRowCount();
        stocks.add(symbol);
        stocksFlexTable.setText(row, 0, symbol);
        stocksFlexTable.setWidget(row, 2, new Label());
        stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
        stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

        // Add remove button
        Button removeStockButton = new Button("x");
        removeStockButton.addStyleDependentName("remove");
        removeStockButton.addClickHandler(clickEvent -> {
            int removeIndex = stocks.indexOf(symbol);
            stocks.remove(removeIndex);
            stocksFlexTable.removeRow(removeIndex + 1);
        });

        stocksFlexTable.setWidget(row, 3, removeStockButton);

        // Get stock data
        refreshStockData();
    }
}
