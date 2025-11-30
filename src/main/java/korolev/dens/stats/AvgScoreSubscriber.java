package korolev.dens.stats;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import korolev.dens.model.AdmissionCompany;
import korolev.dens.model.Applicant;
import lombok.Getter;
import org.reactivestreams.Subscription;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class AvgScoreSubscriber implements FlowableSubscriber<AdmissionCompany> {

    private Subscription subscription;
    private final Map<Integer, List<Double>> passedByYears = new ConcurrentHashMap<>();
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
                .flatMap(program -> Observable.just(program)
                        .subscribeOn(Schedulers.computation())
                        .flatMap(p -> Observable.fromIterable(p.getApplicants())
                                .filter(a -> a.getPointsNumber() >= p.getMinimumPassingScore())
                                .sorted(Comparator.comparingDouble(Applicant::getPreviousEducationAverageScore).reversed())
                                .sorted(Comparator.comparingInt(Applicant::getPointsNumber).reversed())
                                .take(p.getBudgetPlacesNumber())
                        )
                ).map(Applicant::getPointsNumber).toList().blockingGet();

        if (!passedByYears.containsKey(item.getYear())) {
            passedByYears.put(item.getYear(), new CopyOnWriteArrayList<>());
        }
//        if (item % 2 == 0) {
//            sum += item;
//            count++;
//        }
        // Запрашиваем следующий элемент (по одному)
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println("Error: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {

        //double avg = count == 0 ? 0 : (double) sum / count;
        //future.complete(avg);
    }

}
