package fr.imt_atlantique.frappe.specifications;

import fr.imt_atlantique.frappe.entities.CreditTransfer;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CreditTransferSpecification {
    public static Specification<CreditTransfer> hasUniversity(String university) {
        return (root, query, criteriaBuilder) ->
                university == null ? null : criteriaBuilder.equal(root.get("university"), university);
    }

    public static Specification<CreditTransfer> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                country == null ? null : criteriaBuilder.equal(root.get("country"), country);
    }

    public static Specification<CreditTransfer> startDateAfter(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate);
    }

    public static Specification<CreditTransfer> endDateBefore(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate);
    }

}
