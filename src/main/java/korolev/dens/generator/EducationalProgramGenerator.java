package korolev.dens.generator;

import korolev.dens.model.Applicant;
import korolev.dens.model.EducationLevel;
import korolev.dens.model.EducationalProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class EducationalProgramGenerator {

    private static final int PROGRAMS_NUMBER = 100_000;

    private static final SplittableRandom rnd = new SplittableRandom();
    private static final List<EducationalProgram> educationalProgramsBase = generateEducationalPrograms();

    private EducationalProgramGenerator() {}

    private static List<EducationalProgram> generateEducationalPrograms() {
        ArrayList<EducationalProgram> educationalPrograms = new ArrayList<>();
        for (int i = 0; i < PROGRAMS_NUMBER; i++) {
            educationalPrograms.add(create());
        }
        return educationalPrograms;
    }

    private static EducationalProgram create() {
        int programIndex = rnd.nextInt(RandVals.PROGRAMS_AMOUNT);
        String title = RandVals.programTitle(programIndex);
        String description = RandVals.programDescription(programIndex);
        EducationLevel educationLevel = EducationLevel.values()[rnd.nextInt(EducationLevel.values().length)];
        int budgetPlacesNumber = 5 + rnd.nextInt(46);
        int contractPlacesNumber = 10 + rnd.nextInt(61);
        int contractCost = 100_000 + rnd.nextInt(900_001);
        int minimumPassingScore = 40 + rnd.nextInt(21);
        int concurs = budgetPlacesNumber + rnd.nextInt(budgetPlacesNumber * 9 + 1);
        List<Applicant> applicants = new ArrayList<>();
        for (int i = 0; i < concurs; i++) {
            applicants.add(ApplicantGenerator.generate());
        }
        return new EducationalProgram(title, description, educationLevel, budgetPlacesNumber,
                contractPlacesNumber, contractCost, minimumPassingScore, applicants);
    }

    public static EducationalProgram generate(EducationLevel educationLevel) {
        EducationalProgram ep = educationalProgramsBase.get(rnd.nextInt(educationalProgramsBase.size()));
        ep.setEducationLevel(educationLevel);
        return ep;
    }

}
