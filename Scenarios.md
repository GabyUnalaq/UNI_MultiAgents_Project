# Scenarios

The code implements multiple scenarios, that will be described in here. This presents the communication flow between different agents, depending on the scenario triggered by the user.

* Scenario 1: Reservation
   - User -> Manager: "Reserve"
   - Manager -> Table: "reserve"
   - TableAgent: find a suitable table
   - Table -> Manager: "Table <x>"
   - Manager -> User: You can take table <x> / Full

* Scenario 2: Order food
   - User - Manager: "Order 1,Water"
   - Manager -> Table: "Order 1,Water"
   - Table: Remembers order and sum of money
   - User - Manager: "Order 1,Pasta"
   - Manager -> Table: "Order 1,Pasta"
   - Table: Remembers order and sum of money
   - User - Manager: "Order 1,Pizza"
   - Manager -> Table: "Order 1,Pizza"
   - Table: Remembers order and sum of money
   - User -> Manager: "Remove order 1,Water"
   - Manager -> Table: "Remove order 1,Water"
   - Table: Removes item from the order
   - User -> Manager: "List order 1"
   - Manager -> Table: "List order 1"
   - Table: Prints current order
   - User - Manager: "Send order 1"
   - Manager -> Table: "Send order 1"
   - Table -> Waiter: "Send order 1-Pasta;Pizza"
   - Table -> Checkout: "Add to cost x" - for each
   - Waiter -> Chef: "1-Pasta;Pizza"
   - Chef: prepares Pasta
   - Chef: prepares Pizza
   - Chef -> Waiter: "Order complete 1"
   - Waiter -> Table: "Deliver 1"
   - Waiter: Delivers food to table

* Scenario 3: Pay for the food
   - User - Manager: "Pay 1"
   - Manager -> Table: "Pay 1"
   - Table -> Checkout: "Add to profit x"
   - Manager -> Waiter: "Clean 1"
   - Waiter: Cleans table
   - Waiter -> Table: "Clean 1" - table = free

* Scenario 4: Close restaurant
   - User - Manager: "Close"
   - Manager -> Table: "Close"
   - Table writes the tables that are not empty
   - Table -> Manager: "Free" / "Not free"
   - Manager -> Checkout: "Close"
   - Checkout writes profit and cost!

* Scenario 5: Status
   - User - Manager: "Status 1"
   - Manager -> Table: "Status 1"
   - Table writes status of table 1.

## Messages understood by each agent

* Commands:
   * Manager:
	 - Reserve: Reserves one table
	 - Order x,y: x - table num, y - command
	 - Send order x: x - table num
	 - Pay x: x - table num
	 - Close: Closes restaurant
	 - Free: Restaurant is empty
	 - Not free: Restaurant is empty
	 - Table x: x - Number of reserved table.
	 - Full - All tables are full
	 - Status x: x - table num
	 - Remove order x,y: x - table num, y - item
	 - List order x: x - table num
	
   * Table:
	 - Reserve: Check if there are tables empty
	 - Order x,y: x - table num, y - item
	 - Remove order x,y: x - table num, y - item
	 - List order x: x - table num
	 - Send order x: x - table num
	 - Deliver x: x - table num
	 - Pay x: x - table num
	 - Clean x: x - table num
	 - Close: Checks if the restaurant can be closed.
	 - Status x: x - table num
	 
   * Chef:
	 - x-y;z : x - table num, y,z - food items
	 
   * Waiter:
	 - Send order x-y;z : xyz same, Send it to the chef
	 - Order complete x: x - table num
	 - Clean x: x - table num
	 
   * Checkout:
	 - Add to cost x: x - sum of money
	 - Add to profit x: x - sum of money
	 - Close
	 
