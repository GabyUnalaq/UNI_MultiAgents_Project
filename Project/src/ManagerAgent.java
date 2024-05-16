import utils.RestaurantAgent;

/**
 * The ManagerAgent implement a BDI agent with the
 * purpose of handling requests from the user and
 * redirecting them to other agents.
 * User commands:
 *  - Reserve - Looks to reserve a table
 *  - Order x,y - x: table num, y: food item
 *  - Remove order x,y - x: table num, y: food item
 *  - List order x - x: table num
 *  - Send order x - x: table num
 *  - Pay x - x: table num
 *  - Status x - x: table num
 *  - Close
 *
 * @author Dumitru, Radu.M, Patrania, Tomuta
 */
public class ManagerAgent extends RestaurantAgent {
    @Override
    protected void setup() {
        init("Manager");
    }

    protected void interpretMessage(String msg) {
        /* Messages:
            - Reserve: Reserves one table
            - Order x,y: x - table num, y - command
            - Send order x: x - table num
            - Pay x: x - table num
            - Close: Closes restaurant
            - Free: Restaurant is empty
            - Not free: Restaurant is empty
            - Table x: x - Number of reserved table.
            - Full - All tables are full
            - Status x: x - table num
            - Remove order x,y: x - table num, y - item
            - List order x: x - table num
         */
        if (msg.startsWith("Reserve") || msg.startsWith("Order") || msg.startsWith("Send order") ||
                msg.startsWith("Status") || msg.startsWith("Remove order") ||
                msg.startsWith("List order") || msg.equals("Close")) {
            sendMessage("Table", msg);
        }
        else if (msg.startsWith("Pay")) {
            int table_num = Integer.parseInt(msg.substring(4));

            sendMessage("Table", msg);
            sendMessage("Waiter", String.format("Clean %d", table_num));
        }
        else if (msg.equals("Free")) {
            sendMessage("Checkout", "Close");
            // Closing the restaurant for the day
        }
        else if (msg.equals("Not free")) {
            System.out.println("Could not close, the restaurant is not yet empty.");
        }
        else if (msg.startsWith("Table")) {
            int table_num = Integer.parseInt(msg.substring(6));
            System.out.printf("Please proceed to the following table: %d%n", table_num);
        }
        else if (msg.equals("Full")) {
            System.out.println("Restaurant is full, we are truly sorry.");
        }
        else {
            logError("Invalid message received.");
        }
    }
}
