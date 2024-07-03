# Multi Agent System project

This project implements a Smart Restaurant utilizing 5 BDI agents that communicate with one another, using the [Jade](lib/ReadMe.md) library in Java.

An agent is a program that mimics the thinking process of a human, in order to simulate advanced societies, similar to the real world. The BDI agent is one way of implementing the agent. This approach relies on three main components: 
 * B - Belief: It contains the current information (about the self and the world)
 * D - Desire: It contains the goal that needs to be achieved, but the agent does not know yet how.
 * I - Intention: This represents the bond between desire and plan. Using this, the agent generates an action that will run.

## Description of the Agents

 - ManagerAgent: Manages the overall restaurant operations, assigns tasks to other agents, and handles user requests. (Receives messages from the user)
 - ChefAgent: Receives orders, prepares the food and notifies when the order is prepared.
 - WaiterAgent: Receives orders from the ManagerAgent, delivers food to different tables and cleans after customers.
 - TableAgent: Stores overall information about the restaurant (ex: status of tables).
 - CHeckoutAgent: Stores the current profit and cost of the products.

## User Interaction

The user can interact with the ManagerAgent through text messages to simulate an interaction between the restaurant staff and clients. The different commands the user can send are:
 - "Reserve" - reserves a table in the restaurant, if one is available.
 - "Order x,y" - (x - table number, y - food item) Order a food item from the menu.
 - "Remove order x,y" - (x - table number, y - food item) Remove a food item from the order list.
 - "List order x" - (x - table number) Print the current order
 - "Send order x" - (x - table number) Send the order to be prepared. The food will be automatically delivered to the table.
 - "Pay x" - (x - table number) Pay for a table.
 - "Close" - Closes the restaurant, if it is empty.
 - "Status x" - (x - table number) Prints the status of a specific table.

For more information about the scenarios, check [Scenarios.md](Scenarios.md).

## Run

The project already contains the jade library in the lib folder and cache for [IntelliJ Idea](https://www.jetbrains.com/idea/) to set up the project.

After everything is set, you can simply run [src/Main.java](src/Main.java). A GUI should open. Inside, accessing "AgentPlatforms" - "IP/Name" - "COntainer" should reveal all the agents. Right click on the Manager and select send message to begin the interaction with the system.
 
