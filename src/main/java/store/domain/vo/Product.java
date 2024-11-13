package store.domain.vo;

public record Product(Name name, Price price) {
    public String getName() {
        return name.value();
    }

    public int getPrice() {
        return price.value();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Product) {
            return ((Product) o).name().equals(name) && ((Product) o).price().equals(price);
        }
        return false;
    }
}
