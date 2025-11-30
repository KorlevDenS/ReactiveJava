package korolev.dens.stats;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import korolev.dens.model.AdmissionCompany;
import korolev.dens.model.Applicant;
import korolev.dens.model.EducationalProgram;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StatsAccumulator {


    public static Map<Integer, Double> calcStatsWithSpliterator(List<AdmissionCompany> admissionCompanies,
                                                                long delay) {
        return StreamSupport.stream(new CompanySpliterator(admissionCompanies), true)
                .parallel().collect(ParallelAvgScoreCollector.toAvgScoreByYears(delay));
    }

    public static Map<Integer, Double> calcStatsWithSpliterator(List<AdmissionCompany> admissionCompanies) {
        return StreamSupport.stream(new CompanySpliterator(admissionCompanies), true)
                .parallel().collect(ParallelAvgScoreCollector.toAvgScoreByYears());
    }


    public static Map<Integer, Double> calcStatsWithParallelCollector(List<AdmissionCompany> admissionCompanies,
                                                                      long delay) {
        return admissionCompanies.parallelStream().collect(ParallelAvgScoreCollector.toAvgScoreByYears(delay));
    }

    public static Map<Integer, Double> calcStatsWithParallelCollector(List<AdmissionCompany> admissionCompanies) {
        return admissionCompanies.parallelStream().collect(ParallelAvgScoreCollector.toAvgScoreByYears());
    }

    public static Map<Integer, Double> calcStatsWithCustomCollector(List<AdmissionCompany> admissionCompanies,
                                                                    long delay) {
        return admissionCompanies.stream().collect(AverageScoreCollector.toAverageScoreByYears(delay));
    }

    public static Map<Integer, Double> calcStatsWithCustomCollector(List<AdmissionCompany> admissionCompanies) {
        return admissionCompanies.stream().collect(AverageScoreCollector.toAverageScoreByYears());
    }

    public static Map<Integer, Double> calcStatsWithRxJavaSubscriber(List<AdmissionCompany> admissionCompanies) {
        AvgScoreSubscriber scoreSubscriber = new AvgScoreSubscriber();
        Flowable<AdmissionCompany> flowable = Flowable.create(emitter -> {
            for (AdmissionCompany item : admissionCompanies) {
                emitter.onNext(item);
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
        flowable.subscribeOn(Schedulers.computation()).observeOn(Schedulers.io()).subscribe(scoreSubscriber);
        return scoreSubscriber.getFuture().join();
    }

    public static Map<Integer, Double> calcStatsWithEmbeddedRxJava(List<AdmissionCompany> admissionCompanies, long delay) {
        return Observable.fromIterable(admissionCompanies)
                .groupBy(AdmissionCompany::getYear)
                .flatMapSingle(group ->
                        group//.observeOn(Schedulers.computation())
                                .flatMap(company -> Observable.just(company)
                                        .subscribeOn(Schedulers.computation())
                                        .flatMap(comp -> Observable.fromIterable(comp.getEducationalPrograms(delay))
                                                .flatMap(program -> Observable.fromIterable(program.getApplicants())
                                                        .filter(a -> a.getPointsNumber() >= program.getMinimumPassingScore())
                                                        .sorted(Comparator.comparingDouble(Applicant::getPreviousEducationAverageScore).reversed())
                                                        .sorted(Comparator.comparingInt(Applicant::getPointsNumber).reversed())
                                                        .take(program.getBudgetPlacesNumber())
                                                )
                                        )
                                ).map(Applicant::getPointsNumber)
                                .toList()
                                .map(scores -> scores.stream()
                                        .collect(Collectors.averagingInt(Integer::intValue))
                                ).map(avg -> {
                                    assert group.getKey() != null;
                                    return Map.entry(group.getKey(), avg);
                                })
                ).toMap(Map.Entry::getKey, Map.Entry::getValue)
                .blockingGet();
    }

    public static Map<Integer, Double> calcStatsWithEmbeddedRxJava(List<AdmissionCompany> admissionCompanies) {
        return Observable.fromIterable(admissionCompanies)
                .groupBy(AdmissionCompany::getYear)
                .flatMapSingle(group ->
                        group//.observeOn(Schedulers.computation())
                                .flatMap(company -> Observable.just(company)
                                        .subscribeOn(Schedulers.computation())
                                        .flatMap(comp -> Observable.fromIterable(comp.getEducationalPrograms())
                                                .flatMap(program -> Observable.fromIterable(program.getApplicants())
                                                        .filter(a -> a.getPointsNumber() >= program.getMinimumPassingScore())
                                                        .sorted(Comparator.comparingDouble(Applicant::getPreviousEducationAverageScore).reversed())
                                                        .sorted(Comparator.comparingInt(Applicant::getPointsNumber).reversed())
                                                        .take(program.getBudgetPlacesNumber())
                                                )
                                        )
                                ).map(Applicant::getPointsNumber)
                                .toList()
                                .map(scores -> scores.stream()
                                        .collect(Collectors.averagingInt(Integer::intValue))
                                ).map(avg -> {
                                    assert group.getKey() != null;
                                    return Map.entry(group.getKey(), avg);
                                })
                ).toMap(Map.Entry::getKey, Map.Entry::getValue)
                .blockingGet();
    }

    public static Map<Integer, Double> calcStatsWithStreamApi(List<AdmissionCompany> admissionCompanies) {
        Map<Integer, List<AdmissionCompany>> companiesByYear = admissionCompanies.stream()
                .collect(Collectors.groupingBy(
                        AdmissionCompany::getYear,
                        Collectors.toList())
                );
        return companiesByYear.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream().flatMap(
                        ac -> ac.getEducationalPrograms().stream()
                ).flatMap(
                        ep -> ep.getApplicants().stream()
                                .filter(a -> a.getPointsNumber() >= ep.getMinimumPassingScore())
                                .sorted(Comparator.comparingDouble(
                                        Applicant::getPreviousEducationAverageScore
                                ).reversed())
                                .sorted(Comparator.comparingInt(Applicant::getPointsNumber).reversed())
                                .limit(ep.getBudgetPlacesNumber())
                ).collect(Collectors.averagingInt(Applicant::getPointsNumber))
        ));
    }

    public static Map<Integer, Double> calcStatsIterative(List<AdmissionCompany> admissionCompanies) {
        Map<Integer, List<AdmissionCompany>> companiesByYear = new HashMap<>();
        for (AdmissionCompany company : admissionCompanies) {
            int year = company.getYear();
            if (!companiesByYear.containsKey(year)) {
                companiesByYear.put(year, new ArrayList<>());
            }
            companiesByYear.get(year).add(company);
        }
        Map<Integer, Double> results = new HashMap<>();
        for (Map.Entry<Integer, List<AdmissionCompany>> entry : companiesByYear.entrySet()) {
            int year = entry.getKey();
            List<AdmissionCompany> companies = entry.getValue();
            int yearPassedSumScore = 0;
            int yearPassedCount = 0;
            for (AdmissionCompany company : companies) {
                List<EducationalProgram> programs = company.getEducationalPrograms();
                for (EducationalProgram program : programs) {
                    List<Applicant> applicants = program.getApplicants();
                    int passedApplicantsCount = 0;
                    applicants.sort(Comparator.comparingDouble(Applicant::getPreviousEducationAverageScore).reversed());
                    applicants.sort(Comparator.comparingInt(Applicant::getPointsNumber).reversed());
                    int applicantsCount = 0;
                    while (applicantsCount < applicants.size()
                            && passedApplicantsCount < program.getBudgetPlacesNumber()) {
                        Applicant applicant = applicants.get(applicantsCount);
                        if (applicant.getPointsNumber() >= program.getMinimumPassingScore()) {
                            passedApplicantsCount++;
                            yearPassedCount++;
                            yearPassedSumScore += applicant.getPointsNumber();
                        }
                        applicantsCount++;
                    }
                }
            }
            results.put(year, yearPassedSumScore / (double) yearPassedCount);
        }
        return results;
    }

}
