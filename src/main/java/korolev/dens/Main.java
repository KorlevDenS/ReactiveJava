package korolev.dens;


import korolev.dens.generator.*;
import korolev.dens.model.AdmissionCompany;
import korolev.dens.stats.StatsAccumulator;

import java.util.List;
import java.util.Map;

public class Main {

    public static final int SMALL_COLLECTION_SIZE = 5000;
    public static final int MEDIUM_COLLECTION_SIZE = 50000;
    public static final int LARGE_COLLECTION_SIZE = 250000;

    private static void printResults(String method, Map<Integer, Double> results, long duration) {
        System.out.printf("%s: %d ms\n%n", method, duration);
        results.forEach((k, v) -> System.out.printf("%d: %.3f%n", k, v));
        System.out.println("----------------------------------------------");
    }

    static void main() {

        List<AdmissionCompany> admissionCompanies = AdmissionCompanyGenerator.generate(SMALL_COLLECTION_SIZE);

        Map<Integer, Double> results;
        long startTime;
        long endTime;

        System.out.println("AVERAGE PASSING SCORES FOR THE BUDGET BY YEARS");
        System.out.println("----------------------------------------------");

        startTime = System.currentTimeMillis();
        results = StatsAccumulator.calcStatsIterative(admissionCompanies);
        endTime = System.currentTimeMillis();
        printResults("Iterative",  results, endTime - startTime);

        startTime = System.currentTimeMillis();
        results = StatsAccumulator.calcStatsWithStreamApi(admissionCompanies);
        endTime = System.currentTimeMillis();
        printResults("Stream API",  results, endTime - startTime);

        startTime = System.currentTimeMillis();
        results = StatsAccumulator.calcStatsWithCustomCollector(admissionCompanies);
        endTime = System.currentTimeMillis();
        printResults("Custom Collector",  results, endTime - startTime);

    }

}
