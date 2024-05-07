import java.util.*;
import java.lang.Thread;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import utils.Order;

public class ChefAgent extends Agent {
    boolean debug = true;
    String agent_name = "Chef";

    LinkedList<Order> orders = new LinkedList<Order>();
    LinkedList<String> beliefs = new LinkedList<String>();
    String intention = "";

    @Override
    protected void setup() {
        beliefs.add("Pasta");

        // Main loop
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (!orders.isEmpty()) {
                    // Get desire
                    Order order_to_make = orders.getFirst();
                    String food_item = order_to_make.getContent();

                    // Get intention
                    if (!beliefs.contains(food_item)) {
                        intention = "make_" + food_item;

                        // Execute plan
                        execute_plan(intention);
                    }

                    // Check if desire met
                    if (beliefs.contains(food_item)) {
                        beliefs.remove(food_item);
                        orders.remove(order_to_make);

                        // Send message to other agent

                        if (debug)
                            System.out.println("Chef: Found " + food_item);
                    }
                }
                else {
                    block();
                }
            }
        });

        // Get messages behaviour
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage acl_msg = receive();
                if (acl_msg != null) {
                    String msg = acl_msg.getContent();
                    if (debug)
                        System.out.println(agent_name + " got message: " + msg);
                    interpretMsg(msg);
                }
                else
                    block();
            }
        });
    }

    private void interpretMsg(String msg) {
        // Update beliefs

        // Format: "<Table_num>,<Food_item>"
        String[] msg_split = msg.split(",");
        int table_num = Integer.parseInt(msg_split[0]);
        // Food_item = msg_split[1]

        orders.add(new Order(table_num, msg_split[1]));

        // Start behaviour if blocked
        this.doWake();

        if (debug)
            System.out.println("Chef: Added order: " + msg);
    }

    private void execute_plan(String intention) {
        if (intention.startsWith("make_")) {
            String food_item = intention.substring(5);
            try {
                if (make_food(food_item))
                    beliefs.add(food_item);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean make_food(String food) throws InterruptedException {
        if (debug)
            System.out.println("Chef: Making " + food);

        // Simulate making food
        Thread.sleep(1000);
        boolean success_in_making_food = true;

        return success_in_making_food;
    }
}
