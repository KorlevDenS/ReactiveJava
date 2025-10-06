package korolev.dens.stats;

import korolev.dens.generator.AdmissionCompanyGenerator;
import korolev.dens.model.AdmissionCompany;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class StatsBenchmark {

    public final long DELAY = 5;

    @Param({"150000"})
    private int collectionSize;

    private List<AdmissionCompany> admissionCompanies;


    @Setup(Level.Invocation)
    public void setup() {
        admissionCompanies = AdmissionCompanyGenerator.generate(collectionSize);
    }

    @Benchmark
    public void trivialCalc(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithCustomCollector(admissionCompanies)
        );
    }

//    @Benchmark
//    public void delayTrivialCalc(Blackhole bh) {
//        bh.consume(
//                StatsAccumulator.calcStatsWithCustomCollector(admissionCompanies, DELAY)
//        );
//    }

    @Benchmark
    public void calcWithParallelCollector(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithParallelCollector(admissionCompanies)
        );
    }

//    @Benchmark
//    public void delayCalcWithParallelCollector(Blackhole bh) {
//        bh.consume(
//                StatsAccumulator.calcStatsWithParallelCollector(admissionCompanies, DELAY)
//        );
//    }

}
