import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();

        // Set parameters
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.GUI, "true");
        p.setParameter(Profile.CONTAINER_NAME, "Restaurant");

        ContainerController cc = rt.createMainContainer(p);
        AgentController ac;
        try {
            // Create and start Manager
            ac = cc.createNewAgent("Manager", "ManagerAgent", null);
            ac.start();

            ac = cc.createNewAgent("Chef", "ChefAgent", null);
            ac.start();

            ac = cc.createNewAgent("Waiter", "WaiterAgent", null);
            ac.start();

            ac = cc.createNewAgent("Table", "TableAgent", null);
            ac.start();

            ac = cc.createNewAgent("Checkout", "CheckoutAgent", null);
            ac.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
