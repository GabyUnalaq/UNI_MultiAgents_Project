import utils.RestaurantAgent;

/**
 * The ManagerAgent implement a BDI agent with the
 * purpose of handling requests from the user and
 * redirecting them to other agents.
 * Examples of requests:
 *  - "Reserve" - Will reserve one table. If none available, will not reserve
 *  - "Order 1,Pasta"
 *
 * @author  Dumitru, Radu.M, Patrania, Tomuta
 */
public class ManagerAgent extends RestaurantAgent {
    @Override
    protected void setup() {
        init("Manager");
    }

    protected void interpretMessage(String msg) {
        if (msg.startsWith("Reserve")) {
            // Handle table reservation
            handleReservation(msg);
        } else if (msg.startsWith("order")) {
            // Handle food order
            handleOrder(msg);
        }
    }

    private void handleReservation(String content) {
        // Extract table number from message
        int tableNumber = Integer.parseInt(content.substring(8));

        sendMessage("table", "occupy");

        System.out.println("Manager: Table " + tableNumber + " reserved!");
    }

    private void handleOrder(String content) {
        // Extract table number and food item from message
        String[] parts = content.substring(5).split(",");
        int tableNumber = Integer.parseInt(parts[0]);
        String foodItem = parts[1];

        // Send order message to ChefAgent
        sendMessage("chef", tableNumber + "," + foodItem);

        System.out.println("Manager: Order sent to ChefAgent: " + content.substring(5));
    }
}
