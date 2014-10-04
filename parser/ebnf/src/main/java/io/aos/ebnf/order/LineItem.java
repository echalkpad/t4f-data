package io.aos.ebnf.order;

public class LineItem {
    private final String security;
    private final int quantity;
    private final ClientOrder.BuySell bs;
    private final int price;

    public LineItem(String security, int quantity, ClientOrder.BuySell bs, int price) {
        this.security = security;
        this.quantity = quantity;
        this.bs = bs;
        this.price = price;
    }

}
