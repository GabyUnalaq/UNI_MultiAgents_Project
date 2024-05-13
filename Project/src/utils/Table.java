package utils;

import java.util.LinkedList;


public class Table {
    public enum Status {
        empty,
        occupied,
        ordered,
        eating,
        cleaning
    }
    Status status;
    int number;
    int profit, cost;
    LinkedList<String> order = new LinkedList<String>();

    public Table(int number) {
        this.number = number;
        this.status = Status.empty;
        this.profit = 0;
        this.cost = 0;
    }

    public Status getStatus() { return status; }

    public int getNumber() { return number; }

    public void advanceStatus() {
        if (status == Status.empty)
            status = Status.occupied;
        else if (status == Status.occupied)
            status = Status.ordered;
        else if (status == Status.ordered)
            status = Status.eating;
        else if (status == Status.eating)
            status = Status.cleaning;
        else
            status = Status.empty;
    }

    public boolean isEmpty() {
        return status == Status.empty;
    }

    public void addOrder(String item) {
        order.add(item);
        profit += Menu.getProfit(item);
        cost += Menu.getCost(item);
    }

    public void removeOrder(String item) {
        if (order.contains(item)) {
            order.remove(item);
            profit -= Menu.getProfit(item);
            cost -= Menu.getCost(item);
        }
    }

    public String getOrder() {
        return String.join(";", order);
    }

    public String toString() {
        return String.format("Order: %s,\nCost: %d", String.join(", ", order), profit);
    }
}
