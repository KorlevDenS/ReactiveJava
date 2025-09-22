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

    @Override
    public String toString() {
        return "EducationalProgram{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", educationLevel=" + educationLevel +
                ", budgetPlacesNumber=" + budgetPlacesNumber +
                ", contractPlacesNumber=" + contractPlacesNumber +
                ", contractCost=" + contractCost +
                ", minimumPassingScore=" + minimumPassingScore +
                ", applicants (count)=" + applicants.size() +
                '}';
    }
}
