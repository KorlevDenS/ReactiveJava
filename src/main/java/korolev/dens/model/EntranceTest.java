package korolev.dens.model;

import lombok.Getter;

@Getter
public enum EntranceTest {

    ENTRANCE_EXAM("Entrance exam"),
    STATE_EXAM("State exam"),
    PORTFOLIO("Portfolio"),
    OLYMPIAD("Olympiad"),
    SOCIAL_BENEFIT("Social Benefit"),
    RECOMMENDATION_LETTER("Recommendation letter");

    private final String title;

    EntranceTest(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "EntranceTest{" +
                "title='" + title + '\'' +
                '}';
    }
}
