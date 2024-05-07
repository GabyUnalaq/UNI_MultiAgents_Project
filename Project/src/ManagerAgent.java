import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


public class ManagerAgent extends Agent{
    boolean debug = true;
    String agent_name = "Manager";

    @Override
    protected void setup() {
        // Main loop
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                /*info = perceiveWorld();
                B = updateBeliefs(info);
                D = generateDesires(B, I);
                I = generateIntentions(B, D, I);
                plan = choosePlan(B, I);
                executePlan(plan);*/
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

    }
}
