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
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 4, time = 1, timeUnit = TimeUnit.SECONDS)
public class StatsBenchmark {

    public final long DELAY = 5;

    @Param({"10000"})
    private int collectionSize;

    private List<AdmissionCompany> admissionCompanies;


    @Setup(Level.Invocation)
    public void setup() {
        admissionCompanies = AdmissionCompanyGenerator.generate(collectionSize);
    }

    @Benchmark
    public void calcWithStreamApi(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithStreamApi(admissionCompanies)
        );
    }

    @Benchmark
    public void calcIterative(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsIterative(admissionCompanies)
        );
    }

    @Benchmark
    public void calcWithCustomCollector(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithCustomCollector(admissionCompanies)
        );
    }

    @Benchmark
    public void delayCalcWithCustomCollector(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithCustomCollector(admissionCompanies, DELAY)
        );
    }

    @Benchmark
    public void calcWithParallelCollector(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithParallelCollector(admissionCompanies)
        );
    }

    @Benchmark
    public void delayCalcWithParallelCollector(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithParallelCollector(admissionCompanies, DELAY)
        );
    }

    @Benchmark
    public void calcWithSpliterator(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithSpliterator(admissionCompanies)
        );
    }

    @Benchmark
    public void delayCalcWithSpliterator(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithSpliterator(admissionCompanies, DELAY)
        );
    }

    @Benchmark
    public void calcWithRxJavaSubscriber(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithRxJavaSubscriber(admissionCompanies)
        );
    }

    @Benchmark
    public void calcWithEmbeddedRxJava(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithEmbeddedRxJava(admissionCompanies)
        );
    }

    @Benchmark
    public void delayCalcWithEmbeddedRxJava(Blackhole bh) {
        bh.consume(
                StatsAccumulator.calcStatsWithEmbeddedRxJava(admissionCompanies, DELAY)
        );
    }

}
