package korolev.dens.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public List<EducationalProgram> getEducationalPrograms(long delay) {
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return educationalPrograms;
    }

}
