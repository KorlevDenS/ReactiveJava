package korolev.dens;


import korolev.dens.generator.*;
import korolev.dens.model.AdmissionCompany;

import java.util.List;
import java.util.stream.IntStream;

public class Main {

    static void main() {

        for (int k = 0; k < 20; k++) {
            List<AdmissionCompany> admissionCompanies;
            long startTime = System.currentTimeMillis();
            admissionCompanies = IntStream.range(0, 250000)
                    .parallel().mapToObj((_) -> AdmissionCompanyGenerator.generate()).toList();
            long endTime = System.currentTimeMillis();
            System.out.println("Total time taken: " + (endTime - startTime));
            System.out.println(admissionCompanies.size());
        }


    }

}
