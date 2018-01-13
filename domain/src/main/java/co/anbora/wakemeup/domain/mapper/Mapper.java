package co.anbora.wakemeup.domain.mapper;

import java.util.function.Function;

/**
 * Created by dalgarins on 10/12/17.
 */

public interface Mapper<T, R> extends Function<T, R> {
}
