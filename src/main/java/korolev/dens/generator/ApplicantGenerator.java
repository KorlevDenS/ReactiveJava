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
        EntranceTest entranceTest = EntranceTest.values()[rnd.nextInt(EntranceTest.values().length)];
        int pintsNumber;
        if (
                entranceTest == EntranceTest.OLYMPIAD ||
                entranceTest == EntranceTest.SOCIAL_BENEFIT ||
                entranceTest == EntranceTest.RECOMMENDATION_LETTER
        ) {
            pintsNumber = 100 + rnd.nextInt(11);
        } else {
            pintsNumber = rnd.nextInt(111);
        }
        float previousEducationAverageScore = 3.0F + rnd.nextFloat(2.0F);
        return new Applicant(applicantId, contacts, entranceTest, pintsNumber, previousEducationAverageScore);
    }

    public static Applicant generate() {
        return applicantsBase.get(rnd.nextInt(applicantsBase.size()));
    }

}
