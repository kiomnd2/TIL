# 동작 파라미터화 코드 전달하기

## 람다표현식의 등장
람다 표현식은 java8 부터 등장하게 된 새로운 표현식 입니다. 람다 표현식의 등장 배경엔 자주 바뀌는 요구사항에 대해 효과적인 대응에 따라 등장하게 되었습니다.
동작 파라미터는 아직 어떻게 실행될지 모르는 코드 블록을 정의하게 되며 결과적으로 이 코드 블록에 따라 메서드의 동작이 파라미터화 됩니다.

### 변화하는 요구사항에 대응
변화에 대응하는 코드를 구현하는 것은 어려운 일입니다. 기존 코드가 구체적이면 구체적일수록 변화에 대응하기 힘들며, 적절한 추상화를 통해 우리는 
이 문제를 해결해야 합니다. 

예를들어 보면 우리가 리스트에서 특정 조건에 만족하는 사과를 리턴받길 원한다는 코드를 작성해 봅시다

```java
    public static List<Apple> fillterApplesByColor(List<Apple> inventory, Color color) {
        List<Apple> list= new ArrayList<>();
        
        for (Apple apple: inventory) {
            if (apple.getColor().equals(color)) {
                list.add(apple);
            }
        }
        return list;
    }
```
우리가 특정 색의 사과를 얻길 원한다고 가정 했을 때, 다음과같이 파라미터를 통해 원해는 색깔을 지정하여 필터링받아 사과를 가져올 수 있습니다.

그런데 요구사항이 달라져 정해진 크기보다 큰 사과를 리턴받길 원할 경우 크기에 대한 파리미터를 추가로 받아서 작성해야 합니다

```java

   public static List<Apple> fillterApplesByColorAndSize(List<Apple> inventory, Color color, int size) {
        List<Apple> list= new ArrayList<>();

        for (Apple apple: inventory) {
            if (apple.getColor().equals(color) && apple.getSize() > size) {
                list.add(apple);
            }
        }
        return list;
    }
```
좋은 방법일 순 있지만, 사실 매번 요구사항이 늘어날 때마다 새로운 파라미터를 추가해야하는것은 이 메서드를 사용해야 하는 입장에서도, 구현하는 입장에서도
곤욕스러운 일이 아닐 수 없습니다.

이 문제를 동작 파라미터화를 통해 해결 할 수 있습니다.

### 동작파라미터화
참 또는 거짓을 반환하는 함수를 프리디케이트 하고 합니다. 
```java
public interface ApplePredicate  {
    boolean test(Apple apple);
}


public class AppleRedColorPredicateImpl implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return apple.getColor() == Color.RED;
    }
}

public class AppleHeavyWeightPredicateImpl implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return apple.getSize() >150;
    }
}
```
Predicate를 전략에 따라 다르게 구현함으로써 우리는 위 조건에 따라 filter 메서드가 다르게 동작할 것이라는 것을 예상할 수 있습니다.
우리는 이를 전략패턴이라고 부릅니다.

다시 예제로 돌아가서 실제로 이  프레디케이트 함수를 주입하는것을통해 어떻게 filter를 수행하는지 알아보면

```java
public static List<Apple> filterApple(List<Apple> inventory, ApplePredicate applePredicate) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple: inventory) {
        if (applePredicate.test(apple)) {
            result.add(apple);
        }
    }
    return result;
}

List<Apple> list = filterApple(inventory, new AppleHeavyWieghtPredicateImpl());
List<Apple> list = filterApple(inventory, new AppleRedColorPredicateImpl());
```
프레디케이트라는 코드와 동작을 전달함으로써 이전보다 더 유연한 코드를 얻을 수 있었습니다.


### 복잡한 과정 간소화
프레디케이트를 작성하는데도 단점이 있습니다. 달라지는 동작마다 프레디케이트 인터페이스를 구현하여 인스턴스화 해야하며 이는 꽤 번거로운 작업이 될 수 있습니다.
이를 좀더 간소화 하기 위해 익명클래스를 사용해 봅시다. 

```java
List<Apple> inventory = new ArrayList<>();
filterApple(inventory, new ApplePredicate() {
    @Override
    public boolean test(Apple apple) {
        return apple.getSize() > 150;
    }
});
```
익명클래스를 이용하면 여러 클래스를 구현하는 과정이 조금 줄일 수 있게되지만 여전히 만족스럽지 않습니다.
자바 8에선 람다 표현식이라는 더 간략한 코드 전달법을 제공합니다. 

### 람다표현식
람다표현식을 하면 위 익명클래스의 구현을 더 간단하게 고칠 수 있습니다.
```java
filterApple(inventory, apple -> apple.getSize() > 150);
```
한눈에봐도 코드 길이가 확연하게 줄었으며, 코드의 가독성이 늘어남을 알 수 있습니다.


### 리스트 형식으로 추상화
```java

public interface Predicate<T> {
    boolean test(T t);
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
```
마지막으로 다양한 타입에 대응하기 위해 제네릭을 사용하였습니다. 이제 사과 뿐 아니라 바나나 오렌지 등 다양한 객체를 받을 수 있게 되었습니다.