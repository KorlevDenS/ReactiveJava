package korolev.dens.stats;

import korolev.dens.model.AdmissionCompany;
import korolev.dens.model.Applicant;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AverageScoreCollector implements Collector<AdmissionCompany,
        Map<Integer, List<Integer>>,
        Map<Integer, Double>> {

    public static AverageScoreCollector toAverageScoreByYears() {
        return new AverageScoreCollector();
    }

    @Override
    public Supplier<Map<Integer, List<Integer>>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<Integer, List<Integer>>, AdmissionCompany> accumulator() {
        return (map, company) -> {
            if (!map.containsKey(company.getYear())) {
                map.put(company.getYear(), new ArrayList<>());
            }
            map.get(company.getYear()).addAll(
                    company.getEducationalPrograms().stream().flatMap(
                            ep -> ep.getApplicants().stream()
                                    .filter(a -> a.getPointsNumber() >= ep.getMinimumPassingScore())
                                    .sorted(Comparator.comparingDouble(
                                            Applicant::getPreviousEducationAverageScore
                                    ).reversed())
                                    .sorted(Comparator.comparingInt(Applicant::getPointsNumber).reversed())
                                    .limit(ep.getBudgetPlacesNumber())
                    ).map(Applicant::getPointsNumber).toList()
            );
        };
    }

    @Override
    public BinaryOperator<Map<Integer, List<Integer>>> combiner() {
        return (map1, map2) -> {
            map2.forEach((key, value) -> {
                if (map1.containsKey(key)) {
                    map1.get(key).addAll(value);
                } else {
                    map1.put(key, value);
                }
            });
            return map1;
        };
    }

    @Override
    public Function<Map<Integer, List<Integer>>, Map<Integer, Double>> finisher() {
        return map -> map.entrySet().stream().collect(Collectors.toMap(
           Map.Entry::getKey,
           entry -> entry.getValue().stream()
                   .collect(Collectors.averagingInt(Integer::intValue))
        ));
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }
}
