import java.util.*;
import jade.core.behaviours.CyclicBehaviour;

import utils.RestaurantAgent;
import utils.Menu;

public class ChefAgent extends RestaurantAgent {
    int current_table_order;
    LinkedList<String> food_items_to_do = new LinkedList<>();
    LinkedList<String> food_items_done = new LinkedList<>();
    @Override
    protected void setup() {
        init("Chef");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (!food_items_to_do.isEmpty()) {
                    // Get desire
                    String food_item_to_make = food_items_to_do.getFirst();

                    // Check if valid
                    if (!Menu.checkItem(food_item_to_make)) {
                        food_items_to_do.removeFirst();
                        logError("Invalid food item: " + food_item_to_make);
                    }
                    else {
                        execute_plan(food_item_to_make);

                        food_items_done.add(food_item_to_make);
                        food_items_to_do.removeFirst();
                        logInfo("Made " + food_item_to_make);
                    }
                }
                else if (!food_items_done.isEmpty()) {
                    sendMessage("Waiter", String.format("Order complete %d", current_table_order));
                    food_items_done.clear();
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
        try {
            String[] split = msg.split("-");
            current_table_order = Integer.parseInt(split[0]);
            food_items_to_do.addAll(Arrays.asList(split[1].split(";")));
            this.doWake();

            logInfo("Added order: " + msg);

        } catch (Exception e){
            logError("Invalid message received: " + e.toString());
        }
    }

    private void execute_plan(String food_item_to_make) {
        // Simulate making food
        sleep(1000);
    }
}