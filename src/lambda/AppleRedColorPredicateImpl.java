package lambda;

public class AppleRedColorPredicateImpl implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return apple.getColor() == Color.RED;
    }
}
