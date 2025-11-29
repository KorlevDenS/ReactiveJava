package korolev.dens;


import korolev.dens.generator.*;
import korolev.dens.stats.StatsBenchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Map;

public class Main {

    private static void printResults(String method, Map<Integer, Double> results, long duration) {
        System.out.printf("%s: %d ms\n%n", method, duration);
        results.forEach((k, v) -> System.out.printf("%d: %.3f%n", k, v));
        System.out.println("----------------------------------------------");
    }

    static void main() throws RunnerException {

        new Runner(new OptionsBuilder()
                .include(StatsBenchmark.class.getSimpleName())
                .forks(1)
                .build()
        ).run();

    }

}
