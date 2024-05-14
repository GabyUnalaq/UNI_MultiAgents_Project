package utils;

import java.util.LinkedList;


public class Table {
    public enum Status {
        empty, // Table is empty
        occupied, // Table has just been reserved. Waiting for orders
        ordered, // Table has ordered the food
        eating, // The food has arrived
        cleaning; // The table has paid. Waiting for the waiter to clean

        public String toString() {
            if (this == Status.empty)
                return "Empty";
            else if (this == Status.occupied)
                return "Occupied";
            else if (this == Status.ordered)
                return "Ordered";
            else if (this == Status.eating)
                return "Eating";
            else if (this == Status.cleaning)
                return "Cleaning";
            else
                return "Empty";
        }
    }
    Status status;
    int number;
    int profit, cost;
    LinkedList<String> order = new LinkedList<>();

    public Table(int number) {
        this.number = number;
        this.status = Status.empty;
        this.profit = 0;
        this.cost = 0;
    }

    /**
     * Getter for the status
     * @return Status representing the current status
     */
    public Status getStatus() { return status; }

    /**
     * Getter for the table number
     * @return Integer representing the number of table
     */
    public int getNumber() { return number; }

    /**
     * Getter for the cost
     * @return Integer representing the cost of the order
     */
    public int getCost() { return cost; }

    /**
     * Getter for the profit
     * @return Integer representing the profit of the order
     */
    public int getProfit() { return profit; }

    /**
     * Function will advance the status of the current table to the next
     * Order: empty - occupied - ordered - eating - cleaning - empty
     */
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

    /**
     * Reset the table for further usages
     * @param force Boolean that forces the status to empty
     */
    public void resetTable(boolean force) {
        if (force)
            status = Status.empty;
        order.clear();
        profit = 0;
        cost = 0;
    }

    /**
     * Reset the table for further usages
     */
    public void resetTable() {
        order.clear();
        profit = 0;
        cost = 0;
    }

    /**
     * Check if the status of the table is empty
     * @return True if the status is empty
     */
    public boolean isEmpty() { return status == Status.empty; }

    /**
     * Add an item to the order of the table
     * @param item String representing the food item
     */
    public void addOrder(String item) {
        order.add(item);
        profit += Menu.getProfit(item);
        cost += Menu.getCost(item);
    }

    /**
     * Remove an item from the order of the table
     * @param item String representing the food item
     */
    public void removeOrder(String item) {
        if (checkItemInOrder(item)) {
            order.remove(item);
            profit -= Menu.getProfit(item);
            cost -= Menu.getCost(item);
        }
    }

    /**
     * Checks if the order contains the food item
     * @param item String representing the food item
     * @return True if the item exists in the order
     */
    public boolean checkItemInOrder(String item) { return order.contains(item); }

    /**
     * Returns the size of the order
     * @return Integer representing the size
     */
    public int orderSize() { return order.size(); }

    /**
     * Function that returns a String representing the order, for agent communication
     * @return String representing the order of the table to be sent
     */
    public String getOrder() { return String.join(";", order); }

    /**
     * Function that returns a String representing the order, for print
     * @return String representing the order of the table and the cost
     */
    public String toString() {
        return String.format("Order: %s,\nCost: %d",
                String.join(", ", order), profit);
    }
}
