package korolev.dens.stats;

import korolev.dens.model.AdmissionCompany;
import korolev.dens.model.Applicant;
import korolev.dens.model.EducationalProgram;

import java.util.*;
import java.util.stream.Collectors;

public class StatsAccumulator {

    public static Map<Integer, Double> calcStatsWithCustomCollector(List<AdmissionCompany> admissionCompanies) {
        return admissionCompanies.stream().collect(AverageScoreCollector.toAverageScoreByYears());
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
