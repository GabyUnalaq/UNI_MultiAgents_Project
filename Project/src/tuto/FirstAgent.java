package tuto;

import jade.core.Agent;
import jade.Boot;

public class FirstAgent extends Agent {
    /*
    public static void main(String[] args) {
        Boot.main(args);
    }
    */

    @Override
    protected void setup() {
        System.out.println("Hello world. I am first agent.");
        //doDelete();
    }
}
