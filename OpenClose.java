interface DiscountStrategy {
    double apply(double price);
}

class SeasonalDiscount implements DiscountStrategy {
    public double apply(double price) { return price * 0.9; } // 10% off
}

class DiscountService {
    public double calculate(double price, DiscountStrategy strategy) {
        return strategy.apply(price);
    }
}

public class OpenClose {
    public static void main(String[] args) {
        DiscountService service = new DiscountService();
        double originalPrice = 100.0;

        DiscountStrategy seasonalDiscount = new SeasonalDiscount();
        double discountedPrice = service.calculate(originalPrice, seasonalDiscount);

        System.out.println("Original Price: " + originalPrice);
        System.out.println("Discounted Price: " + discountedPrice);
    }
}