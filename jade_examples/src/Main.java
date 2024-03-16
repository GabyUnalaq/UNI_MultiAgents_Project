import jade.core.Agent;
import examples.hello.HelloWorldAgent;

public class Main extends Agent {
    public static void main(String[] args) {
        HelloWorldAgent ag = new HelloWorldAgent();
        ag.setup();
    }
}
