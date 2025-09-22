package korolev.dens.generator;

import korolev.dens.model.AdmissionCommission;
import korolev.dens.model.AdmissionCompany;
import korolev.dens.model.EducationLevel;
import korolev.dens.model.EducationalProgram;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class AdmissionCompanyGenerator {

    private static final int MAX_PROGRAMS_IN_COMPANY = 80;

    private static final SplittableRandom rnd = new SplittableRandom();

    private AdmissionCompanyGenerator(){}

    public static AdmissionCompany generate() {
        String universityName = RandVals.university();
        LocalDate startDate = RandVals.juneDay();
        LocalDate endDate = RandVals.augustDay();
        EducationLevel educationLevel = EducationLevel.values()[rnd.nextInt(EducationLevel.values().length)];
        AdmissionCommission commission = AdmissionCommissionGenerator.generate();
        int programsNum = 1 + rnd.nextInt(MAX_PROGRAMS_IN_COMPANY);
        List<EducationalProgram> educationalPrograms = new ArrayList<>();
        for (int  i = 0; i < programsNum; i++) {
            educationalPrograms.add(EducationalProgramGenerator.generate(educationLevel));
        }
        return new AdmissionCompany(startDate.getYear(), universityName, startDate, endDate, educationLevel,
                commission, educationalPrograms);
    }

}
