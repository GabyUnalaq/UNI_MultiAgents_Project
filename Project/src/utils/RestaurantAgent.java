package utils;

import java.lang.Thread;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


/**
 * The RestaurantAgent is an abstract agent that
 * provides an easy-to-use interface between the
 * developed agents and the jade Agent.
 *
 * @author Dumitru, Radu.M, Patrania, Tomuta
 */
public abstract class RestaurantAgent extends Agent {
    String agent_name; // Each agent's unique identifier
    boolean debug = true; // Show all info on the console
    boolean isolate_agent = false; // Used to disable communication - for testing purposes

    public void init(String agent_name) {
        this.agent_name = agent_name;

        // Behaviour: receive messages
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    String content = msg.getContent();

                    logInfo("got message " + content);
                    try {
                        interpretMessage(content);
                    }
                    catch (Exception e) {
                        logError("Unexpected error occurred while interpreting message: " + e.toString());
                    }
                } else {
                    block();
                }
            }
        });
    }

    protected abstract void interpretMessage(String msg);

    protected void sendMessage(String target, String msg) {
        if (isolate_agent) {
            System.out.println("Sending msg \"" + msg + "\" to " + target);
            return;
        }
        ACLMessage msgToSend = new ACLMessage(ACLMessage.REQUEST);
        msgToSend.addReceiver(getAID(target));
        msgToSend.setContent(msg);
        send(msgToSend);
    }

    protected void logInfo(String info) {
        if (debug)
            System.out.printf("[INFO]: %s: %s%n", agent_name, info);
    }

    protected void logError(String err) {
        System.out.printf("[ERROR]: %s: %s%n", agent_name, err);
    }

    protected void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
