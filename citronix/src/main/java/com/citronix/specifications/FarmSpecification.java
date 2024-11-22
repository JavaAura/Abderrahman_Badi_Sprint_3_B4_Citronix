package com.citronix.specifications;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.citronix.dto.FarmCriteria;
import com.citronix.model.Farm;

public class FarmSpecification {
    public static Specification<Farm> withFilters(FarmCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getId() != null) {
                predicates.add(builder.equal(root.get("id"), criteria.getId()));
            }
            if (criteria.getName() != null) {
                predicates.add(builder.like(root.get("name"), "%" + criteria.getName() + "%"));
            }
            if (criteria.getAddress() != null) {
                predicates.add(builder.like(root.get("address"), "%" + criteria.getAddress() + "%"));
            }
            if (criteria.getSurface() != null) {
                predicates.add(builder.equal(root.get("surface"), criteria.getSurface()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
