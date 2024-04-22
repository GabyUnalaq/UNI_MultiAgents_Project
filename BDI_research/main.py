class BDI_Agent:

  def __init__(self, beliefs, desires, plans):
    self.beliefs = beliefs  # Dictionary to store beliefs (e.g., {'location': 'room1'})
    self.desires = desires  # List of goals (e.g., ['find_food', 'explore_room2'])
    self.plans = plans  # Dictionary of plans for each desire (e.g., {'find_food': ['go_kitchen', 'search_pantry']})

  def update_beliefs(self, new_information):
    # Update beliefs based on new information (sensor data, messages)
    self.beliefs.update(new_information)

  def select_desire(self):
    # Choose the most important unfulfilled desire based on priorities
    for desire in self.desires:
      if desire not in self.beliefs.keys():  # Check if desire is achieved
        return desire
    return None  # No unfulfilled desires

  def form_intention(self):
    # Select a desire and create an intention based on current beliefs
    desire = self.select_desire()
    if desire:
      return desire, self.plans[desire]  # Intention = (desire, plan)
    return None  # No intention formed

  def execute_plan(self, plan):
    # Execute the given plan (sequence of actions), update beliefs accordingly
    for action in plan:
      if action == 'go_kitchen':
        self.beliefs['location'] = 'kitchen'
      elif action == 'search_pantry':
        if self.beliefs['location'] == 'kitchen':
          self.beliefs['has_food'] = True  # Simulate finding food
        else:
          print("Can't search pantry, not in kitchen!")
      # Simulate action execution (print for simplicity)
      print(f"Taking action: {action}")

  def run(self):
    while True:
      # BDI cycle
      self.update_beliefs(new_information={})  # Replace with actual updates
      intention = self.form_intention()
      if intention:
        desire, plan = intention
        print(f"Current intention: {desire}")
        self.execute_plan(plan)
        if self.beliefs.get('has_food', False):  # Check if food is found
          print("Found food! Stopping...")
          break
      else:
        print("No current intention")


# Example Usage
beliefs = {'location': 'room1'}
desires = ['find_food']
plans = {
  'find_food': ['go_kitchen', 'search_pantry']
}

agent = BDI_Agent(beliefs, desires, plans)
agent.run()
