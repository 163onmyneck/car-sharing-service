package car.sharing.app.carsharingservice.repository.rental;

import car.sharing.app.carsharingservice.model.Rental;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder {
    private Specification<Rental> specification;

    public SpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    public SpecificationBuilder with(Specification<Rental> newSpec) {
        if (newSpec != null) {
            this.specification = this.specification == null 
                ? Specification.where(newSpec) 
                : this.specification.and(newSpec);
        }
        return this;
    }

    public Specification<Rental> build() {
        return this.specification;
    }
}
