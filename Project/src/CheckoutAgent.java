import java.util.*;
import jade.core.behaviours.CyclicBehaviour;

import utils.RestaurantAgent;


public class CheckoutAgent extends RestaurantAgent {
    int cost, profit;

    @Override
    protected void setup() {
        init("Checkout");

        cost = 0;
        profit = 0;
    }

    protected void interpretMessage(String msg) {
        /* Messages:
            - Add to cost: x: x - sum of money
            - Add to profit: x: x - sum of money
            - Close
         */
        if (msg.startsWith("Add to cost")) {
            cost += Integer.parseInt(msg.substring(12));
        }
        else if (msg.startsWith("Add to profit")) {
            profit += Integer.parseInt(msg.substring(14));
        }
        else if (msg.startsWith("Close")) {
            System.out.println("Restaurant is closed!");
            System.out.printf("Total profit made today: %d\n", profit - cost);
        }
        else {
            logError("Invalid message received.");
        }
    }
}
