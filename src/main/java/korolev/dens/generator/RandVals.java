package korolev.dens.generator;

import korolev.dens.Main;
import net.datafaker.Faker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class RandVals {

    private static final int VALS_AMOUNT = 10_000;
    public static final int PROGRAMS_AMOUNT = 100;
    private static final SplittableRandom rnd = new SplittableRandom();
    private static final Faker faker = new Faker();

    private static final List<String> firstNames = genFirstNames();
    private static final List<String> lastNames = genLastNames();
    private static final List<String> emails = genEmails();
    private static final List<String> phoneNumbers = genPhoneNumbers();
    private static final List<String> addresses = genAddresses();
    private static final List<String> universities = genUniversities();
    private static final List<String> programTitles = readProgramTitles();
    private static final List<String> programDescriptions = readProgramDescriptions();
    private static final List<LocalDate> juneDays = genJuneDays();
    private static final List<LocalDate> augustDays = genAugustDays();

    private RandVals() {}

    private static List<LocalDate> genJuneDays() {
        List<LocalDate> juneDays = new ArrayList<>();
        for (int year = 2000; year <= 2025; year++) {
            for (int day = 1; day <= 30; day++) {
                juneDays.add(LocalDate.of(year, 6, day));
            }
        }
        return juneDays;
    }

    private static List<LocalDate> genAugustDays() {
        List<LocalDate> juneDays = new ArrayList<>();
        for (int year = 2000; year <= 2025; year++) {
            for (int day = 1; day <= 31; day++) {
                juneDays.add(LocalDate.of(year, 8, day));
            }
        }
        return juneDays;
    }

    private static List<String> genUniversities() {
        List<String> universities = new ArrayList<>();
        for (int i = 0; i < VALS_AMOUNT; i++) {
            universities.add(faker.educator().university());
        }
        return universities;
    }

    private static List<String> readProgramDescriptions() {
        URL url = Main.class.getClassLoader().getResource("programs.csv");
        Path path;
        try {
            assert url != null;
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            List<String> programs = Files.readAllLines(path);
            return programs.stream().map(p -> p.split(";", 2)[1]).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> readProgramTitles() {
        URL url = Main.class.getClassLoader().getResource("programs.csv");
        Path path;
        try {
            assert url != null;
            path = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            List<String> programs = Files.readAllLines(path);
            return programs.stream().map(p -> p.split(";", 2)[0]).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> genAddresses() {
        List<String> addresses = new ArrayList<>();
        for (int i = 0; i < VALS_AMOUNT; i++) {
            addresses.add(faker.address().fullAddress());
        }
        return addresses;
    }

    private static List<String> genPhoneNumbers() {
        List<String> phoneNumbers = new ArrayList<>();
        for (int i = 0; i < VALS_AMOUNT; i++) {
            phoneNumbers.add(faker.phoneNumber().phoneNumber());
        }
        return phoneNumbers;
    }

    private static List<String> genFirstNames() {
        List<String> firstNames = new ArrayList<>();
        for (int i = 0; i < VALS_AMOUNT; i++) {
            firstNames.add(faker.name().firstName());
        }
        return firstNames;
    }

    private static List<String> genLastNames() {
        List<String> lastNames = new ArrayList<>();
        for (int i = 0; i < VALS_AMOUNT; i++) {
            lastNames.add(faker.name().lastName());
        }
        return lastNames;
    }

    private static List<String> genEmails() {
        List<String> emails = new ArrayList<>();
        for (int i = 0; i < VALS_AMOUNT; i++) {
            emails.add(faker.internet().emailAddress());
        }
        return emails;
    }

    public static String lastName() {
        return lastNames.get(rnd.nextInt(VALS_AMOUNT));
    }

    public static String firstName() {
        return firstNames.get(rnd.nextInt(VALS_AMOUNT));
    }

    public static String email() {
        return emails.get(rnd.nextInt(VALS_AMOUNT));
    }

    public static String phoneNumber() {
        return phoneNumbers.get(rnd.nextInt(VALS_AMOUNT));
    }

    public static String address() {
        return addresses.get(rnd.nextInt(VALS_AMOUNT));
    }

    public static String university() {
        return universities.get(rnd.nextInt(VALS_AMOUNT));
    }

    public static String programTitle(int index) {
        return programTitles.get(index);
    }

    public static String programDescription(int index) {
        return programDescriptions.get(index);
    }

    public static LocalDate juneDay() {
        return juneDays.get(rnd.nextInt(juneDays.size()));
    }

    public static LocalDate augustDay() {
        return augustDays.get(rnd.nextInt(augustDays.size()));
    }
}
