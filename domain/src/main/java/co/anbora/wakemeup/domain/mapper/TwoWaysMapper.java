package co.anbora.wakemeup.domain.mapper;

/**
 * Created by dalgarins on 10/12/17.
 */

public interface TwoWaysMapper<T, R> extends Mapper<T, R> {

    T inverseMap(R map);

}
