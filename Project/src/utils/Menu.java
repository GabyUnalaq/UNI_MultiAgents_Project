package utils;

import java.util.ArrayList;
import java.util.Objects;

class FoodItem {
    String name; // Name of dish
    int profit; // Sell price
    int cost; // Cost to make

    public FoodItem(String name, int profit, int cost){
        this.name = name;
        this.cost = cost;
        this.profit = profit;
    }
}

public class Menu {
    static ArrayList<FoodItem> list = new ArrayList<>() {
        {
            // Aperitive
            add(new FoodItem("Salam", 5, 2));
            add(new FoodItem("Pateu", 3, 1));
            add(new FoodItem("Paine", 2, 1));
            add(new FoodItem("Paine prajita", 7, 1));

            // Meniu principal
            add(new FoodItem("Pasta", 50, 20));
            add(new FoodItem("Pizza", 65, 30));
            add(new FoodItem("Sarmale", 40, 20));
            add(new FoodItem("Burger", 65, 32));
            add(new FoodItem("Vita angus", 130, 67));
            add(new FoodItem("Platou de carne", 199, 150));

            // Garnituri
            add(new FoodItem("Cartofi prajiti", 20, 5));
            add(new FoodItem("Orez cu legume", 22, 5));

            // Sosuri
            add(new FoodItem("Mujdei", 4, 2));
            add(new FoodItem("Ketchup", 4, 2));

            // Deserturi
            add(new FoodItem("Clatite cu gem", 20, 15));
            add(new FoodItem("Clatite cu ciocolata", 25, 19));
            add(new FoodItem("Clatite cu banane", 22, 13));

            // Bauturi
            add(new FoodItem("Coca cola", 5, 1));
            add(new FoodItem("Apa Hera", 50, 2));
            add(new FoodItem("Limonada", 30, 5));
            add(new FoodItem("Limonada cu ghimbir", 300, 6));
            //add(new FoodItem("Bere cu manageru", 999, 1));
        }
    };

    /**
     * Function checks if the food item exists in the menu
     * @param item String representing the food item
     * @return True if the item exists in the Menu
     */
    public static boolean checkItem(String item) {
        for (FoodItem fi : Menu.list) {
            if (Objects.equals(fi.name, item))
                return true;
        }
        return false;
    }

    /**
     * Getter for cost (Money needed to produce the item)
     * @param item String representing the food item
     * @return Integer representing the cost of the item
     */
    public static int getCost(String item) {
        for (FoodItem fi : Menu.list) {
            if (Objects.equals(fi.name, item))
                return fi.cost;
        }
        return 0;
    }

    /**
     * Getter for profit (Amount paid by the client)
     * @param item String representing the food item
     * @return Integer representing the profit of the item
     */
    public static int getProfit(String item) {
        for (FoodItem fi : Menu.list) {
            if (Objects.equals(fi.name, item))
                return fi.profit;
        }
        return 0;
    }
}
