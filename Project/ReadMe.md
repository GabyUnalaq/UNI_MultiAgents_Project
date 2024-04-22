Theme: Smart Restaurant

Agents:
 - ManagerAgent: Manages the overall restaurant operations, assigns tasks to other agents, and handles user requests. (Receives messages from the user)
 - ChefAgent: Receives orders from the ManagerAgent, prepares food, and informs the ManagerAgent when finished.
 - WaiterAgent: Receives orders from the ManagerAgent, delivers food to customers, and takes new orders from the user.
 - TableAgent: Represents a table in the restaurant, keeps track of its occupancy and orders.
 - KitchenDisplayAgent: Displays the current food orders for the ChefAgent.

User Interaction: The user can interact with the ManagerAgent through text messages (simulated) to:
 - Make a reservation for a table.
 - Order food from the menu.