package korolev.dens.model;

import lombok.Getter;

@Getter
public enum EducationLevel {

    BACHELOR("Bachelor"),
    MASTER("Master"),
    POSTGRADUATE("Postgraduate"),;

    private final String title;

    EducationLevel(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "EducationLevel{" +
                "title='" + title + '\'' +
                '}';
    }
}
