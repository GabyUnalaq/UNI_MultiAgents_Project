import java.util.*;
import jade.core.behaviours.CyclicBehaviour;

import utils.RestaurantAgent;
import utils.Menu; // Belief

public class ChefAgent extends RestaurantAgent {
    // Desires
    LinkedList<String> desires = new LinkedList<>();

    @Override
    protected void setup() {
        init("Chef");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (!desires.isEmpty()) {
                    String current_desire = desires.getFirst();
                    logInfo("Current desire: " + current_desire);

                    // Choose action
                    String aux;
                    if (current_desire.startsWith("cook")) {
                        // get value
                        aux = current_desire.split(":")[1];
                        if (cookFood(aux))
                            desires.removeFirst();
                    }
                    else if (current_desire.startsWith("finish_order")) {
                        // get value
                        aux = current_desire.split(":")[1];
                        if (finishOrder(Integer.parseInt(aux)))
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
            - x-y;z : x - table num, y,z - food items
         */
        String[] split = msg.split("-");
        int table_num = Integer.parseInt(split[0]);
        for (String food_item : split[1].split(";")) {
            desires.add(String.format("cook:%s", food_item));
        }
        desires.addLast(String.format("finish_order:%d", table_num));
        this.doWake();
    }

    // Plan to cook
    private boolean cookFood(String food_item) {
        logInfo("Executing plan: cook " + food_item);
        // Check if valid
        if (!Menu.checkItem(food_item)) {
            logError("Invalid food item: " + food_item);
        }
        else {
            // Simulate making food
            sleep(1000);
        }
        return true;
    }

    // Plan to finish the current order
    private boolean finishOrder(int table_num) {
        logInfo("Executing plan: finish order for table " + table_num);
        sendMessage("Waiter", String.format("Order complete %d", table_num));
        return true;
    }
}