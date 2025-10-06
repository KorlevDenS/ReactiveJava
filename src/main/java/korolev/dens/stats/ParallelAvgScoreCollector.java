package korolev.dens.stats;

import korolev.dens.model.AdmissionCompany;
import korolev.dens.model.Applicant;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ParallelAvgScoreCollector implements Collector<AdmissionCompany,
        Map<Integer, List<Integer>>,
        Map<Integer, Double>> {

    private long delay = 0;

    public ParallelAvgScoreCollector(long delay) {
        this.delay = delay;
    }

    public ParallelAvgScoreCollector() {}

    public static ParallelAvgScoreCollector toAvgScoreByYears() {
        return new ParallelAvgScoreCollector();
    }

    public static ParallelAvgScoreCollector toAvgScoreByYears(long delay) {
        return new ParallelAvgScoreCollector(delay);
    }

    @Override
    public Supplier<Map<Integer, List<Integer>>> supplier() {
        return ConcurrentHashMap::new;
    }

    @Override
    public BiConsumer<Map<Integer, List<Integer>>, AdmissionCompany> accumulator() {
        return (map, company) -> {
            if (!map.containsKey(company.getYear())) {
                map.put(company.getYear(), new CopyOnWriteArrayList<>());
            }
            map.get(company.getYear()).addAll(
                    (this.delay > 0 ? company.getEducationalPrograms(this.delay) : company.getEducationalPrograms())
                            .parallelStream().flatMap(
                            ep -> ep.getApplicants().parallelStream()
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
            map2.entrySet().parallelStream().forEach(entry -> {
                if (map1.containsKey(entry.getKey())) {
                    map1.get(entry.getKey()).addAll(entry.getValue());
                } else {
                    map1.put(entry.getKey(), entry.getValue());
                }
            });
            return map1;
        };
    }

    @Override
    public Function<Map<Integer, List<Integer>>, Map<Integer, Double>> finisher() {
        return map -> map.entrySet().parallelStream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().parallelStream()
                        .collect(Collectors.averagingInt(Integer::intValue))
        ));
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }

}
