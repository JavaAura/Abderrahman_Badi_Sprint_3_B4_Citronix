package com.citronix.util;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.citronix.model.Tree;

@Service
public class TreeServiceHelper {

    public Tree calculateTreeAgeAndAnnualProductivity(Tree tree) {
        int age = LocalDate.now().getYear() - tree.getPlantedAt().getYear();
        tree.setAge(age);

        if (age < 3) {
            tree.setAnnualProductivity(2.5D);
        }
        if (age <= 10) {
            tree.setAnnualProductivity(12D);
        }
        if (age > 10) {
            tree.setAnnualProductivity(20D);
        }

        return tree;
    }
}