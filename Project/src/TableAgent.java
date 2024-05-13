import java.util.LinkedList;

import utils.RestaurantAgent;
import utils.Table;

import javax.lang.model.util.SimpleElementVisitor14;

public class TableAgent extends RestaurantAgent {
    int tables_num = 5;
    LinkedList<Table> tables = new LinkedList<Table>();

    protected void setup() {
        init("Table");

        // Init tables
        for (int i = 1; i <= tables_num; i++) {
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
            for (int i = 0; i < tables_num; i++) {
                if (tables.get(i).isEmpty()) {
                    //sendMessage("Manager", String.format("Table %d", i+1));
                    logInfo(String.format("Table %d", i+1));
                    tables.get(i).advanceStatus(); // empty -> occupied
                    reserved = true;
                    break;
                }
            }
            if (!reserved) {
                //sendMessage("Manager", "Full");
                logInfo("Restaurant is full");
            }
        }
        else if (msg.startsWith("Order")) {
            String[] split = msg.substring(6).split(",");
            int table_num = Integer.parseInt(split[0]) - 1;
            String item = split[1];

            // Check table
            if (1 <= table_num && table_num <= tables_num &&
                    tables.get(table_num).getStatus() == Table.Status.occupied) {
                tables.get(table_num).addOrder(item);
                System.out.println("TODO");
            }
        }
        else {
            logError("Invalid message received.");
        }
    }
}