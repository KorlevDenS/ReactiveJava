package korolev.dens.generator;

import korolev.dens.model.PersonContacts;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class PersonContactsGenerator {

    private static final int PERSON_CONTACTS_NUMBER= 1_000_000;

    private static final SplittableRandom rnd = new SplittableRandom();
    private static final List<PersonContacts> personContactsBase = generatePersonContacts();

    private  PersonContactsGenerator() {}

    private static PersonContacts create() {
        String firstName = RandVals.firstName();
        String lastName = RandVals.lastName();
        String email = RandVals.email();
        String phone = RandVals.phoneNumber();
        String address = RandVals.address();
        return new PersonContacts(firstName, lastName, email, phone, address);
    }

    private static List<PersonContacts> generatePersonContacts() {
        ArrayList<PersonContacts> personContacts = new ArrayList<>();
        for (int i = 0; i < PERSON_CONTACTS_NUMBER; i++) {
            personContacts.add(create());
        }
        return personContacts;
    }

    public static PersonContacts generate() {
        return personContactsBase.get(rnd.nextInt(personContactsBase.size()));
    }

}
