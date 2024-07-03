import java.util.*;
import jade.core.behaviours.CyclicBehaviour;

import utils.RestaurantAgent;


/**
 * The WaiterAgent implement a BDI agent with the
 * purpose of moving around the restaurant. This
 * includes taking the order to the chef, the
 * food to the table and cleaning the table.
 *
 * @author Dumitru, Radu.M, Patrania, Tomuta
 */
public class WaiterAgent extends RestaurantAgent {
    // Beliefs
    String location; // values: table_x, kitchen
    boolean has_food;

    // Desires
    LinkedList<String> desires = new LinkedList<>();
    String last_desire;

    @Override
    protected void setup() {
        init("Waiter");
        location = "table_0";
        has_food = false;

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
                    if (current_desire.startsWith("location")) {
                        // get target location
                        aux = current_desire.split(":")[1];
                        if (walk(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("order")) {
                        // get order details
                        aux = current_desire.split(":")[1];
                        if (sendOrderToChef(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("deliver")) {
                        // get table number
                        aux = current_desire.split(":")[1];
                        if (deliverOrder(Integer.parseInt(aux)))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("clean")) {
                        // get table number
                        aux = current_desire.split(":")[1];
                        if (cleanTable(Integer.parseInt(aux)))
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
            - Send order x-y;z : xyz same as chef, Send it to the chef
            - Order complete: x: x - table num
            - Clean x: x - table num
         */
        if (msg.startsWith("Send order")) {
            desires.add(String.format("order:%s", msg.substring(11)));
        }
        else if (msg.startsWith("Order complete")) {
            desires.add(String.format("deliver:%s", msg.substring(15)));
        }
        else if (msg.startsWith("Clean")) {
            desires.add(String.format("clean:%s", msg.substring(6)));
        }
        else {
            logError("Invalid message received.");
        }
        this.doWake();
    }

    // Plan to change location
    public boolean walk(String target) {
        logInfo("Executing plan: walk to " + target);
        if (!location.equals(target)) {
            // Simulate walking
            sleep(500);

            location = target;
        }
        return true;
    }

    // Plan to send order to chef
    public boolean sendOrderToChef(String order) {
        logInfo("Executing plan: get to chef " + order);
        if (!location.equals("chef")) {
            desires.addFirst("location:chef");
        }
        else {
            sendMessage("Chef", order);
            return true;
        }
        return false;
    }

    // Plan to deliver food to table
    public boolean deliverOrder(int table_num) {
        logInfo("Executing plan: deliver order to table " + table_num);
        if (!has_food && !location.equals("chef")) {
            desires.addFirst("location:chef");
        }
        else if (!has_food) {
            has_food = true;
        }
        if (!location.equals(String.format("table_%d", table_num))) {
            desires.addFirst(String.format("location:table_%d", table_num));
        }
        else {
            System.out.printf("Food is delivered to table %d.%n", table_num);
            sendMessage("Table", String.format("Deliver %d", table_num));
            has_food = false;
            return true;
        }
        return false;
    }

    // Plan to clean the table
    public boolean cleanTable(int table_num) {
        logInfo("Executing plan: clean table " + table_num);
        if (!location.equals(String.format("table_%d", table_num))) {
            desires.addFirst(String.format("location:table_%d", table_num));
        }
        else {
            System.out.printf("Cleaning table %d.%n", table_num);

            // Simulate cleaning
            sleep(500);

            sendMessage("Table", String.format("Clean %d", table_num));

            return true;
        }
        return false;
    }
}