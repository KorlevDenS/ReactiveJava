package korolev.dens.generator;

import korolev.dens.model.AdmissionCommission;
import korolev.dens.model.PersonContacts;

public class AdmissionCommissionGenerator {

    private AdmissionCommissionGenerator() {}

    public static AdmissionCommission generate() {
        PersonContacts chairman = PersonContactsGenerator.generate();
        String address = RandVals.address();
        String phone = RandVals.phoneNumber();
        String email = RandVals.email();
        return new AdmissionCommission(chairman, address, phone, email);
    }

}
