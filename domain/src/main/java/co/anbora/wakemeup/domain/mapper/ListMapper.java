package co.anbora.wakemeup.domain.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dalgarins on 10/12/17.
 */

public class ListMapper<M, P> implements Mapper<List<M>, List<P>> {

    private final Mapper<M, P> mapper;

    public ListMapper(Mapper<M, P> mapper) {
        this.mapper = mapper;
    }

    @Override public List<P> apply(List<M> models) {
        ArrayList<P> persistences = new ArrayList<>();
        if (models != null && models.size() > 0) {
            for (M model : models) {
                persistences.add(mapper.apply(model));
            }
        }
        return persistences;
    }
}
