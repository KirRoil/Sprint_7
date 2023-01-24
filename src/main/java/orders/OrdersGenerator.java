package orders;

public class OrdersGenerator {
    public static Orders getDefault() {
        return new Orders("Петр",
                "Смирнов",
                "ул. Ленина, д. 1",
                "Боровицкая",
                "+7 963 963 96 39",
                5,
                "2021-01-01",
                "Тест коммент"
        );
    }
}
