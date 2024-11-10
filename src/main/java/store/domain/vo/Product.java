package store.domain.vo;

public record Product(Name name, Price price) {
    @Override
    public boolean equals(Object o) {
        if (o instanceof Product) {
            return ((Product) o).name().equals(name) && ((Product) o).price() == price;
        }
        return false;
    }
}
