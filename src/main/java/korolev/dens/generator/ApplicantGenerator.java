package korolev.dens.generator;

import korolev.dens.model.Applicant;
import korolev.dens.model.EntranceTest;
import korolev.dens.model.PersonContacts;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class ApplicantGenerator {

    private static final int APPLICANTS_NUMBER = 1_000_000;

    private static final SplittableRandom rnd = new SplittableRandom();
    private static final List<Applicant> applicantsBase = generateApplicants();

    private ApplicantGenerator() {
    }

    private static List<Applicant> generateApplicants() {
        List<Applicant> applicants = new ArrayList<>();
        for (int i = 0; i < APPLICANTS_NUMBER; i++) {
            applicants.add(create());
        }
        return applicants;
    }

    private static Applicant create() {
        int applicantId = 10_000_000 + rnd.nextInt(90_000_000);
        PersonContacts contacts = PersonContactsGenerator.generate();
        EntranceTest entranceTest;
        int chance =  rnd.nextInt(100);
        if (chance < 80) {
            entranceTest = EntranceTest.values()[rnd.nextInt(2)];
        } else if (chance < 97) {
            entranceTest = EntranceTest.PORTFOLIO;
        } else {
            entranceTest = EntranceTest.values()[3 + rnd.nextInt(3)];
        }
        int pointsNumber;
        if (
                entranceTest == EntranceTest.OLYMPIAD ||
                entranceTest == EntranceTest.SOCIAL_BENEFIT ||
                entranceTest == EntranceTest.RECOMMENDATION_LETTER
        ) {
            pointsNumber = 85 + rnd.nextInt(26);
        } else {
            pointsNumber = rnd.nextInt(101);
        }
        float previousEducationAverageScore = 3.0F + rnd.nextFloat(2.0F);
        return new Applicant(applicantId, contacts, entranceTest, pointsNumber, previousEducationAverageScore);
    }

    public static Applicant generate() {
        return applicantsBase.get(rnd.nextInt(applicantsBase.size()));
    }

}
