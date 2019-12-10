package sample.qute.rs;

public class Item {
    private String name;
    private int price;

    public Item() {}

    public static Item of(String name, int price) {
        Item item = new Item();
        item.name = name;
        item.price = price;
        return item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
