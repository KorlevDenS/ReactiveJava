package korolev.dens.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Applicant {

    private int applicantId;
    private PersonContacts contacts;
    private EntranceTest entranceTest;
    private int pintsNumber;
    private float previousEducationAverageScore;

}
