package app.cleaningmaintenanceservices.model;

public class MDService {

    public int id;
    public String image;
    public MDTranslation translation = new MDTranslation();
    public boolean isSelected = false;

    @Override
    public String toString() {
        return translation.title;
    }
}
