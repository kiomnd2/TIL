package lambda;

public class AppleHeavyWieghtPredicateImpl implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return apple.getSize() >150;
    }
}
