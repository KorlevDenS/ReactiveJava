package korolev.dens.stats;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observable;
import korolev.dens.model.AdmissionCompany;
import korolev.dens.model.Applicant;
import lombok.Getter;
import org.reactivestreams.Subscription;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AvgScoreSubscriber implements FlowableSubscriber<AdmissionCompany> {

    private Subscription subscription;
    private final Map<Integer, List<Integer>> passedByYears = new ConcurrentHashMap<>();
    @Getter
    private final CompletableFuture<Map<Integer, Double>> future = new CompletableFuture<>();

    @Override
    public void onSubscribe(@NonNull Subscription s) {
        this.subscription = s;
        this.subscription.request(10);
    }

    @Override
    public void onNext(AdmissionCompany item) {
        List<Integer> avgS = Observable.fromIterable(item.getEducationalPrograms())
                .flatMap(p -> Observable.fromIterable(p.getApplicants())
                                .filter(a -> a.getPointsNumber() >= p.getMinimumPassingScore())
                                .sorted(Comparator.comparingDouble(Applicant::getPreviousEducationAverageScore).reversed())
                                .sorted(Comparator.comparingInt(Applicant::getPointsNumber).reversed())
                                .take(p.getBudgetPlacesNumber())
                ).map(Applicant::getPointsNumber).toList().blockingGet();
        if (!passedByYears.containsKey(item.getYear())) {
            passedByYears.put(item.getYear(), new CopyOnWriteArrayList<>());
        }
        passedByYears.get(item.getYear()).addAll(avgS);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println("Error: " + throwable.getMessage());
        future.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        Map<Integer, Double> result =
                Observable.fromIterable(passedByYears.entrySet())
                        .map(entry -> {
                            List<Integer> values = entry.getValue();
                            double avg = values.stream()
                                    .mapToDouble(Integer::doubleValue)
                                    .average()
                                    .orElse(0.0);
                            return new AbstractMap.SimpleEntry<>(entry.getKey(), avg);
                        }).toMap(
                                AbstractMap.SimpleEntry::getKey,
                                AbstractMap.SimpleEntry::getValue
                        ).blockingGet();
        future.complete(result);
    }

}
