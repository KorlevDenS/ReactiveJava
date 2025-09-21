package korolev.dens.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducationalProgram {

    private String title;
    private String description;
    private EducationLevel educationLevel;
    private int budgetPlacesNumber;
    private int contractPlacesNumber;
    private int contractCost;
    private int minimumPassingScore;

    private List<Applicant> applicants;

}
