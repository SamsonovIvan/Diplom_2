import java.util.ArrayList;

public class Order {
    private ArrayList<Object> ingredients;
    private static String bunN200i = "61c0c5a71d1f82001bdaaa6c";
    private static String cowMeteor = "61c0c5a71d1f82001bdaaa70";

    public Order (ArrayList<Object> ingredients){
        this.ingredients = ingredients;
    }

    public static Order getDefaultOrder(){
        ArrayList<Object> ingredients = new ArrayList<>();
        ingredients.add(bunN200i);
        ingredients.add(cowMeteor);
        return new Order(ingredients);
    }

    public static Order getIncorrectOrder(){
        ArrayList<Object> ingredients = new ArrayList<>();
        ingredients.add("qwertyuiop1");
        ingredients.add("asdfghjkl1");
        return new Order(ingredients);
    }

    @Override
    public String toString(){
        return "Order{" +
                "ingredients=" + ingredients +
                '}';
    }

}
