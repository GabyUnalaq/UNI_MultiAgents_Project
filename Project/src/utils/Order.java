package utils;

public class Order {
    int table;
    String content;

    public Order(int table, String content) {
        this.table = table;
        this.content = content;
    }

    public int getTable() {
        return table;
    }

    public String getContent() {
        return content;
    }
}
