package app.cleaningmaintenanceservices.model;

public class MDService {

    public int id;
    public String image;
    public String service_type;
    public int companies_count;
    public MDTranslation translation = new MDTranslation();

    @Override
    public String toString() {
        return translation.title;
    }
}
