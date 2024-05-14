import java.util.LinkedList;

import utils.RestaurantAgent;
import utils.Table;
import utils.Menu;

public class TableAgent extends RestaurantAgent {
    int total_number_of_tables = 5;
    LinkedList<Table> tables = new LinkedList<>();

    protected void setup() {
        init("Table");

        // Init tables
        for (int i = 0; i < total_number_of_tables; i++) {
            tables.add(new Table(i));
        }
    }

    protected void interpretMessage(String msg) {
        /* Messages:
            - Reserve: Check if there are tables empty
            - Order x,y: x - table num, y - command
            - Send order: Sends the order to chef
            - Deliver: Delivers the food to table
            - Pay x: x - table num
            - Clean x: x - table num
            - Close: Checks if the restaurant can be closed.
            - Status x: x - table num
         */
        if (msg.startsWith("Reserve")) {
            boolean reserved = false;
            for (int i = 0; i < total_number_of_tables; i++) {
                if (tables.get(i).isEmpty()) {
                    sendMessage("Manager", String.format("Table %d", i));
                    logInfo(String.format("Table %d", i));
                    tables.get(i).advanceStatus(); // empty -> occupied
                    reserved = true;
                    break;
                }
            }
            if (!reserved) {
                sendMessage("Manager", "Full");
                logInfo("Restaurant is full");
            }
        }
        else if (msg.startsWith("Order")) {
            String[] split = msg.substring(6).split(",");
            int table_num = Integer.parseInt(split[0]);
            String item = split[1];

            // Check if table number is valid
            if (0 > table_num || table_num >= total_number_of_tables) {
                logError("Table number is invalid. Could not order");
            }
            // Check status of table
            else if (tables.get(table_num).getStatus() != Table.Status.occupied) {
                logError("Table is not occupied. Could not order");
            }
            // Check if item is valid
            else if (!Menu.checkItem(item)) {
                logError(String.format("Food item %s is not in the Menu", item));
            }
            else {
                tables.get(table_num).addOrder(item);
                logInfo(String.format("Item added to tables %d order: %s", table_num, item));
            }
        }
        else if (msg.startsWith("Remove")) {
            String[] split = msg.substring(7).split(",");
            int table_num = Integer.parseInt(split[0]);
            String item = split[1];

            // Check if table number is valid
            if (0 > table_num || table_num >= total_number_of_tables) {
                logError("Table number is invalid. Could not remove order");
            }
            // Check status of table
            else if (tables.get(table_num).getStatus() != Table.Status.occupied) {
                logError("Table is not occupied. Could not remove order");
            }
            // Check if item is valid
            else if (!Menu.checkItem(item)) {
                logError(String.format("Food item %s is not in the Menu", item));
            }
            // Check if item is in order
            else if (!tables.get(table_num).checkItemInOrder(item)) {
                logError(String.format("Food item %s is not in the order", item));
            }
            else {
                tables.get(table_num).removeOrder(item);
                logInfo(String.format("Item removed from tables %d order: %s", table_num, item));
            }
        }
        else if (msg.startsWith("List order")) {
            int table_num = Integer.parseInt(msg.substring(11));

            // Check if table number is valid
            if (0 > table_num || table_num >= total_number_of_tables) {
                logError("Table number is invalid. Could not list order");
            }
            // Check if table is empty
            else if (tables.get(table_num).isEmpty()) {
                logError(String.format("Table %d is empty.", table_num));
            }
            else {
                // Print the order to the console
                System.out.println(tables.get(table_num).toString());
            }
        }
        else if (msg.startsWith("Send order")) {
            int table_num = Integer.parseInt(msg.substring(11));

            // Check if table number is valid
            if (0 > table_num || table_num >= total_number_of_tables) {
                logError("Table number is invalid. Could not send order.");
            }
            // Check status of table
            else if (tables.get(table_num).getStatus() != Table.Status.occupied) {
                logError("Table is not occupied. Could not send order.");
            }
            // Check order contains at least one food item
            else if (tables.get(table_num).orderSize() == 0) {
                logError("Order is empty. Could not send order.");
            }
            else {
                // Send the order to the Waiter
                sendMessage("Waiter", String.format("%s-%s", msg, tables.get(table_num).getOrder()));

                // Send the cost of the order to the Checkout
                sendMessage("Checkout", String.format("Add to cost %d", tables.get(table_num).getCost()));

                tables.get(table_num).advanceStatus(); // occupied -> ordered
                logInfo(String.format("Sent the order to the Waiter for table %d.", table_num));
            }
        }
        else if (msg.startsWith("Deliver")) {
            int table_num = Integer.parseInt(msg.substring(8));

            // Check if table number is valid
            if (0 > table_num || table_num >= total_number_of_tables) {
                logError("Table number is invalid. Could not deliver.");
            }
            // Check status of table
            else if (tables.get(table_num).getStatus() != Table.Status.ordered) {
                logError("Table has not ordered. Could not deliver food.");
            }
            else {
                tables.get(table_num).advanceStatus(); // ordered -> eating
                logInfo(String.format("Table %d is now eating the food.", table_num));
            }
        }
        else if (msg.startsWith("Pay")) {
            int table_num = Integer.parseInt(msg.substring(4));

            // Check if table number is valid
            if (0 > table_num || table_num >= total_number_of_tables) {
                logError("Table number is invalid. Could not deliver.");
            }
            // Check status of table
            else if (tables.get(table_num).getStatus() != Table.Status.eating) {
                logError("Table has not eaten yet. Could not pay the food.");
            }
            else {
                // Send the profit of the order to the Checkout
                sendMessage("Checkout", String.format("Add to profit %d", tables.get(table_num).getProfit()));

                tables.get(table_num).advanceStatus(); // eating -> cleaning
                logInfo(String.format("Table %d has finished eating and has payed.", table_num));
            }
        }
        else if (msg.startsWith("Clean")) {
            int table_num = Integer.parseInt(msg.substring(6));

            // Check if table number is valid
            if (0 > table_num || table_num >= total_number_of_tables) {
                logError("Table number is invalid. Could not clean.");
            }
            // Check status of table
            else if (tables.get(table_num).getStatus() != Table.Status.cleaning) {
                logError("Table is not waiting to be cleaned. Could not clean.");
            }
            else {
                tables.get(table_num).advanceStatus(); // cleaning -> empty
                tables.get(table_num).resetTable(); // reset the order, cost and profit
                logInfo(String.format("Table %d cleaned and ready for new clients.", table_num));
            }
        }
        else if (msg.startsWith("Close")) {
            boolean free = true;
            for (int i = 0; i < total_number_of_tables; i++) {
                if (!tables.get(i).isEmpty()) {
                    free = false;
                    logInfo(String.format("Table %d is not empty yet.", i+1));
                }
            }

            if (free)
                sendMessage("Manager", "Free");
            else
                sendMessage("Manager", "Not free");
        }
        else if (msg.startsWith("Status")) {
            int table_num = Integer.parseInt(msg.substring(7));

            // Check if table number is valid
            if (0 > table_num || table_num >= total_number_of_tables) {
                logError("Table number is invalid. Could not get status.");
            }
            else {
                System.out.printf("Table %d has the following status: %s.%n",
                        table_num, tables.get(table_num).getStatus().toString());
            }
        }
        else {
            logError("Invalid message received.");
        }
    }
}