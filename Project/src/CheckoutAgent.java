import java.util.*;
import jade.core.behaviours.CyclicBehaviour;

import utils.RestaurantAgent;


public class CheckoutAgent extends RestaurantAgent {
    // Beliefs
    int cost, profit;

    // Desires
    LinkedList<String> desires = new LinkedList<>();
    String last_desire;

    @Override
    protected void setup() {
        init("Checkout");

        cost = 0;
        profit = 0;

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
                    if (current_desire.startsWith("add_cost")) {
                        // get value
                        aux = current_desire.split(":")[1];
                        if (addCost(Integer.parseInt(aux)))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("add_profit")) {
                        // get value
                        aux = current_desire.split(":")[1];
                        if (addProfit(Integer.parseInt(aux)))
                            desires.removeFirst();
                    }
                    else if (current_desire.equals("close")) {
                        if (closeRestaurant())
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
            - Add to cost: x: x - sum of money
            - Add to profit: x: x - sum of money
            - Close
         */
        if (msg.startsWith("Add to cost")) {
            //cost += Integer.parseInt(msg.substring(12));
            desires.add(String.format("add_cost:%s", msg.substring(12)));
        }
        else if (msg.startsWith("Add to profit")) {
            //profit += Integer.parseInt(msg.substring(14));
            desires.add(String.format("add_profit:%s", msg.substring(14)));
        }
        else if (msg.startsWith("Close")) {
            //System.out.println("Restaurant is closed!");
            //System.out.printf("Total profit made today: %d%n", profit - cost);
            desires.add("close");
        }
        else {
            logError("Invalid message received.");
        }
        this.doWake();
    }

    // Plan to add cost
    public boolean addCost(int val) {
        logInfo("Executing plan: add cost " + val);
        cost += val;
        return true;
    }

    // Plan to add profit
    public boolean addProfit(int val) {
        logInfo("Executing plan: add profit " + val);
        profit += val;
        return true;
    }

    // Plan to close
    public boolean closeRestaurant() {
        logInfo("Executing plan: clos restaurant ");
        System.out.println("Restaurant is closed!");
        System.out.printf("Total profit made today: %d%n", profit - cost);
        return true;
    }
}
