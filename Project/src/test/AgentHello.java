package test;

import jade.core.Agent;

/**
 * A simple agent that display a text
 *
 * @author emmanueladam
 *
 * <a href="https://github.com/EmmanuelADAM/jade/blob/english/helloworldSolo/AgentHello.java">Source link</a>
 */
public class AgentHello extends Agent {
    /**
     * this main launch JADE plateforme and asks it to create an agent
     */
    public static void main(String[] args) {
        String[] jadeArgs = new String[2];
        StringBuilder sbAgents = new StringBuilder();
        sbAgents.append("myFirstAgent:test.AgentHello").append(";");
        jadeArgs[0] = "-gui";
        jadeArgs[1] = sbAgents.toString();
        jade.Boot.main(jadeArgs);
    }

    /**
     * agent set-up
     */
    @Override
    protected void setup() {
        String texteHello = "Hello everybody and especially you !";

        System.out.printf("From agent " + getLocalName() + " : " + texteHello);
        System.out.printf("My address is " + getAID());
        //agent asks to be removed from the platform
        doDelete();
    }

    // 'clean-up' of the agent
    @Override
    protected void takeDown() {
        System.out.printf("Me, Agent " + getLocalName() + " I leave the platform ! ");
    }
}