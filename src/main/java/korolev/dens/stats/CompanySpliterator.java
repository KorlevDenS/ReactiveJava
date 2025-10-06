package korolev.dens.stats;

import korolev.dens.model.AdmissionCompany;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class CompanySpliterator implements Spliterator<AdmissionCompany> {

    private final List<AdmissionCompany> elements;
    private int currentIndex;

    public CompanySpliterator(List<AdmissionCompany> elements) {
        this.elements = elements;
        this.currentIndex = 0;
    }

    @Override
    public boolean tryAdvance(Consumer<? super AdmissionCompany> action) {
        if (currentIndex < elements.size()) {
            action.accept(elements.get(currentIndex));
            currentIndex++;
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<AdmissionCompany> trySplit() {
        int currentSize = elements.size() - currentIndex;
        if (currentSize < 2) {
            return null;
        }
        int splitIndex = currentIndex + currentSize / 2;
        CompanySpliterator newSpliterator = new CompanySpliterator(elements.subList(currentIndex, splitIndex));
        currentIndex = splitIndex;
        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return elements.size() - currentIndex;
    }

    @Override
    public int characteristics() {
        return ORDERED|SIZED|SUBSIZED|NONNULL|IMMUTABLE;
    }

}
