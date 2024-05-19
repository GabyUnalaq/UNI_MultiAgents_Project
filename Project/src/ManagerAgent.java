import jade.core.behaviours.CyclicBehaviour;
import utils.RestaurantAgent;

import java.util.LinkedList;

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
    // Beliefs
    String table_agent_name = "Table";
    String checkout_agent_name = "Checkout";

    // Desires
    LinkedList<String> desires = new LinkedList<>();

    @Override
    protected void setup() {
        init("Manager");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (!desires.isEmpty()) {
                    String current_desire = desires.getFirst();
                    logInfo("Current desire: " + current_desire);

                    // Choose action
                    String aux;
                    if (current_desire.startsWith("send_table")) {
                        // get content
                        aux = current_desire.split(":")[1];
                        if (sendTable(aux))
                            desires.removeFirst();
                    }
                    else if(current_desire.startsWith("send_checkout")) {
                        // get content
                        aux = current_desire.split(":")[1];
                        if (sendCheckout(aux))
                            desires.removeFirst();
                    }
                    else {
                        logError("Desire unknown: " + current_desire);
                        desires.removeFirst();
                    }
                }
                else {
                    block();
                }
            }
        });
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
                msg.startsWith("Status") || msg.startsWith("Remove order") || msg.startsWith("Pay") ||
                msg.startsWith("List order") || msg.equals("Close")) {
            desires.add("send_table:" + msg);
        }
        else if (msg.equals("Free")) {
            desires.add("send_checkout:Close");
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

    // Plan to send to table agent
    public boolean sendTable(String content) {
        logInfo("Executing plan: send table: " + content);
        sendMessage(table_agent_name, content);
        return true;
    }

    // Plan to send to checkout agent
    public boolean sendCheckout(String content) {
        logInfo("Executing plan: send checkout: " + content);
        sendMessage(checkout_agent_name, content);
        return true;
    }
}
