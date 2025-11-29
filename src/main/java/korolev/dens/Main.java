package korolev.dens;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import korolev.dens.generator.*;
import korolev.dens.model.AdmissionCompany;
import korolev.dens.stats.StatsAccumulator;
import korolev.dens.stats.StatsBenchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static void printResults(String method, Map<Integer, Double> results, long duration) {
        System.out.printf("%s: %d ms\n%n", method, duration);
        results.forEach((k, v) -> System.out.printf("%d: %.3f%n", k, v));
        System.out.println("----------------------------------------------");
    }

    public static List<Double> rxTest(List<List<Integer>> someLists) {
        return Observable.fromIterable(someLists)
                .flatMap(someList -> Observable
                        .fromIterable(someList)
                        .subscribeOn(Schedulers.computation())
                        .filter(x -> x % 2 == 0)
                        .map(Integer::doubleValue)
                        .toList()
                        .toObservable()
                ).map(filtered -> filtered.stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(Double.NaN)
                )
                .toList()
                .blockingGet();
    }


    static void main() throws RunnerException {

        List<AdmissionCompany> admissionCompanies = AdmissionCompanyGenerator.generate(1000);

        printResults("RxJava", StatsAccumulator.calcStatsWithEmbeddedRxJava(admissionCompanies), 1);

        printResults("StreamApi", StatsAccumulator.calcStatsWithStreamApi(admissionCompanies), 1);

        List<List<Integer>> input = List.of(
                List.of(10, 200, 30, 40),
                List.of(150, 90, 80, 300),
                List.of(5, 12, 25, 35)
        );

        System.out.println(rxTest(input));

//        new Runner(new OptionsBuilder()
//                .include(StatsBenchmark.class.getSimpleName())
//                .forks(1)
//                .build()
//        ).run();

    }

}
