package r2m.spring.db.spring_jdbc.infrastructure.persistence.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import r2m.spring.db.spring_jdbc.domain.TravelMediaType;

@WritingConverter
public class TravelMediaTypeToStringConverter implements Converter<TravelMediaType, String> {

    @Override
    public String convert(TravelMediaType value) {
        return value.name();
    }
}
