package tuto.msg;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import javax.swing.*;

public class Second extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    JOptionPane.showMessageDialog(null, "Got message: " + msg.getContent());
                    System.out.println("Got message: " + msg.getContent());
                }
                else {
                    block();
                }
            }
        });
    }
}
