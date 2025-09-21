package korolev.dens.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmissionCompany {

    private int year;
    private String universityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private EducationLevel educationLevel;
    private AdmissionCommission commission;

    private List<EducationalProgram> educationalPrograms;

}
