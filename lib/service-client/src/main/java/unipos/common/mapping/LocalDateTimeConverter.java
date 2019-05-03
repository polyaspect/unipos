package unipos.common.mapping;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.joda.time.LocalDateTime;

/**
 * Created by Dominik on 04.12.2015.
 */
public class LocalDateTimeConverter implements CustomConverter {
    @Override
    public Object convert(Object destination, Object source,
                          Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        return source;
    }
}
