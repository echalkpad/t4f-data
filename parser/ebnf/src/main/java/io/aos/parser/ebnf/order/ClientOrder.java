package io.aos.parser.ebnf.order;

import java.util.ArrayList;
import java.util.List;

public class ClientOrder {
    public enum BuySell {
        BUY, SELL
    }
    private String accountNo;
    private List<LineItem> lineItems = new ArrayList<LineItem>();
    
    public ClientOrder(List<LineItem> lineItems, String acccountNo) {
        this.lineItems = lineItems;
        this.accountNo = acccountNo;
    }
    
}
