package utils;

import java.lang.Thread;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public abstract class RestaurantAgent extends Agent {
    String agent_name;
    boolean debug = true;

    public void init(String agent_name) {
        this.agent_name = agent_name;

        // Behaviour: receive messages
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(agent_name + ": got message " + content);

                    interpretMessage(content);
                } else {
                    block();
                }
            }
        });
    }

    protected abstract void interpretMessage(String msg);

    protected void sendMessage(String target, String msg) {
        ACLMessage msgToSend = new ACLMessage(ACLMessage.REQUEST);
        msgToSend.addReceiver(getAID(target));
        msgToSend.setContent(msg);
        send(msgToSend);
    }

    protected void logInfo(String info) {
        if (debug)
            System.out.printf("[INFO]: %s: %s\n", agent_name, info);
    }

    protected void logError(String err) {
        System.out.printf("[ERROR]: %s: %s\n", agent_name, err);
    }

    protected void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
