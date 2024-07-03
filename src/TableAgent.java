import java.util.LinkedList;

import jade.core.behaviours.CyclicBehaviour;
import utils.RestaurantAgent;
import utils.Table;
import utils.Menu; // Belief


/**
 * The WaiterAgent implement a BDI agent with the
 * purpose of moving around the restaurant. This
 * includes taking the order to the chef, the
 * food to the table and cleaning the table.
 *
 * @author Dumitru, Radu.M, Patrania, Tomuta
 */
public class TableAgent extends RestaurantAgent {
    // Beliefs
    int total_number_of_tables = 5;
    LinkedList<Table> tables = new LinkedList<>();

    // Desires
    LinkedList<String> desires = new LinkedList<>();
    String last_desire;

    protected void setup() {
        init("Table");

        // Init tables
        for (int i = 0; i < total_number_of_tables; i++) {
            tables.add(new Table(i));
        }

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (!desires.isEmpty()) {
                    String current_desire = desires.getFirst();
                    logInfo("Current desire: " + current_desire);

                    // Check desire
                    if (current_desire.equals(last_desire)) {
                        logError("Desire is not achievable: " + current_desire);
                        desires.removeFirst();
                        last_desire = "";
                        return;
                    }

                    // Choose action
                    String aux;
                    if (current_desire.equals("reserve")) {
                        if (reserveTable())
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("order")) {
                        aux = current_desire.split(":")[1];
                        if (orderFood(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("remove")) {
                        aux = current_desire.split(":")[1];
                        if (removeFood(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("list")) {
                        aux = current_desire.split(":")[1];
                        if (listOrder(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("send")) {
                        aux = current_desire.split(":")[1];
                        if (sendOrder(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("deliver")) {
                        aux = current_desire.split(":")[1];
                        if (deliverOrder(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("pay")) {
                        aux = current_desire.split(":")[1];
                        if (payOrder(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("clean")) {
                        aux = current_desire.split(":")[1];
                        if (cleanTable(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("status")) {
                        aux = current_desire.split(":")[1];
                        if (statusTable(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.equals("close")) {
                        if (closeRestaurant())
                            desires.removeFirst();
                    }
                    else {
                        logError("Desire unknown: " + current_desire);
                        desires.removeFirst();
                    }

                    last_desire = current_desire;
                }
                else {
                    block();
                }
            }
        });
    }

    protected void interpretMessage(String msg) {
        /* Messages:
            - Reserve: Check if there are tables empty
            - Order x,y: x - table num, y - command
            - Remove x,y: x - table num, y - command
            - Send order x: Sends the order to chef
            - Deliver: Delivers the food to table
            - Pay x: x - table num
            - Clean x: x - table num
            - Close: Checks if the restaurant can be closed.
            - Status x: x - table num
         */
        if (msg.startsWith("Reserve")) {
            desires.add("reserve");
        }
        else if (msg.startsWith("Order")) {
            desires.add(String.format("order:%s", msg.substring(6)));
        }
        else if (msg.startsWith("Remove")) {
            desires.add("remove:" + msg.substring(7));
        }
        else if (msg.startsWith("List order")) {
            desires.add("list:" + msg.substring(11));
        }
        else if (msg.startsWith("Send order")) {
            desires.add("send:" + msg.substring(11));
        }
        else if (msg.startsWith("Deliver")) {
            desires.add("deliver:" + msg.substring(8));
        }
        else if (msg.startsWith("Pay")) {
            desires.add("pay:" + msg.substring(4));
        }
        else if (msg.startsWith("Clean")) {
            desires.add("clean:" + msg.substring(6));
        }
        else if (msg.startsWith("Status")) {
            desires.add("status:" + msg.substring(7));
        }
        else if (msg.equals("Close")) {
            desires.add("close");
        }
        else {
            logError("Invalid message received.");
        }
    }

    // Plan to reserve a table
    public boolean reserveTable() {
        logInfo("Executing plan: Reserve table.");
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

        return true;
    }

    // Plan to order the food
    public boolean orderFood(String content) {
        String[] split = content.split(",");
        int table_num = Integer.parseInt(split[0]);
        String item = split[1];
        logInfo("Executing plan: order food for table " + table_num + " " + item);

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

        return true;
    }

    // Plan to remove a food item
    public boolean removeFood(String content) {
        String[] split = content.split(",");
        int table_num = Integer.parseInt(split[0]);
        String item = split[1];
        logInfo("Executing plan: remove food item from table " + table_num + " " + item);

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

        return true;
    }

    // Plan to list order for a table
    public boolean listOrder(String content) {
        int table_num = Integer.parseInt(content);
        logInfo("Executing plan: list order for table " + table_num);

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

        return true;
    }

    // Plan to send order for a table
    public boolean sendOrder(String content) {
        int table_num = Integer.parseInt(content);
        logInfo("Executing plan: send order for table " + table_num);

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
            sendMessage("Waiter", String.format("Send order %s-%s", table_num, tables.get(table_num).getOrder()));

            // Send the cost of the order to the Checkout
            sendMessage("Checkout", String.format("Add to cost %d", tables.get(table_num).getCost()));

            tables.get(table_num).advanceStatus(); // occupied -> ordered
            logInfo(String.format("Sent the order to the Waiter for table %d.", table_num));
        }

        return true;
    }

    // Plan to deliver food to a table
    public boolean deliverOrder(String content) {
        int table_num = Integer.parseInt(content);
        logInfo("Executing plan: deliver food to table " + table_num);

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

        return true;
    }

    // Plan to pay for a table
    public boolean payOrder(String content) {
        int table_num = Integer.parseInt(content);
        logInfo("Executing plan: pay for table " + table_num);

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
            sendMessage("Waiter", "Clean " + table_num);

            tables.get(table_num).advanceStatus(); // eating -> cleaning
            logInfo(String.format("Table %d has finished eating and has payed.", table_num));
        }

        return true;
    }

    // Plan to clean a table
    public boolean cleanTable(String content) {
        int table_num = Integer.parseInt(content);
        logInfo("Executing plan: clean table " + table_num);

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

        return true;
    }

    // Plan to show status to a table
    public boolean statusTable(String content) {
        int table_num = Integer.parseInt(content);
        logInfo("Executing plan: show status of table " + table_num);

        // Check if table number is valid
        if (0 > table_num || table_num >= total_number_of_tables) {
            logError("Table number is invalid. Could not get status.");
        }
        else {
            System.out.printf("Table %d has the following status: %s.%n",
                    table_num, tables.get(table_num).getStatus().toString());
        }

        return true;
    }

    // Plan to close the restaurant
    public boolean closeRestaurant() {
        logInfo("Executing plan: closing..");
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

        return true;
    }
}