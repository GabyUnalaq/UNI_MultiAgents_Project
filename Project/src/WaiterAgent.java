import java.util.*;
import jade.core.behaviours.CyclicBehaviour;

import utils.RestaurantAgent;


public class WaiterAgent extends RestaurantAgent {
    @Override
    protected void setup() {
        init("Waiter");
    }

    protected void interpretMessage(String msg) {
        /* Messages:
            - Send order x-y;z : xyz same as chef, Send it to the chef
            - Order complete: x: x - table num
            - Clean x: x - table num
         */
        if (msg.startsWith("Send order")) {
            sendMessage("Chef", msg.substring(11));
        }
        else if (msg.startsWith("Order complete")) {
            int table_num = Integer.parseInt(msg.substring(15));
            sendMessage("Table", String.format("Deliver %d", table_num));
            System.out.printf("Food is delivered to table %d.%n", table_num);
        }
        else if (msg.startsWith("Clean")) {
            int table_num = Integer.parseInt(msg.substring(6));
            sendMessage("Table", msg);
            System.out.printf("Cleaning table %d.%n", table_num);
        }
        else {
            logError("Invalid message received.");
        }
    }
}