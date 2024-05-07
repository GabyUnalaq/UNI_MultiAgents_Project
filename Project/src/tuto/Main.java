package tuto;

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

        ContainerController cc = rt.createMainContainer(p);
        AgentController ac;
        try {
            ac = cc.createNewAgent("FirstAgent1", "tuto.FirstAgent", null);
            ac.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
