package lambda;

import java.util.ArrayList;
import java.util.List;

public class LambdaMain {

    public static List<Apple> fillterApplesByColor(List<Apple> inventory, Color color) {
        List<Apple> list= new ArrayList<>();

        for (Apple apple: inventory) {
            if (Color.RED.equals(color)) {
                list.add(apple);
            }
        }
        return list;
    }

    public static List<Apple> fillterApplesByColorAndSize(List<Apple> inventory, Color color, int size) {
        List<Apple> list= new ArrayList<>();

        for (Apple apple: inventory) {
            if (apple.getColor().equals(color) && apple.getSize() > size) {
                list.add(apple);
            }
        }
        return list;
    }


    public static List<Apple> filterApple(List<Apple> inventory, ApplePredicate applePredicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory) {
            if (applePredicate.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T e: list) {
            if (p.test(e)) {
                result.add(e);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Apple> inventory = new ArrayList<>();
        filterApple(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return apple.getSize() > 150;
            }
        });

        filterApple(inventory, apple -> apple.getSize() > 150);


    }
}
