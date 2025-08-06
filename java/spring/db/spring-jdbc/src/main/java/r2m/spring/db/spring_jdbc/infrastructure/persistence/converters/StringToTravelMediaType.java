package r2m.spring.db.spring_jdbc.infrastructure.persistence.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import r2m.spring.db.spring_jdbc.domain.TravelMediaType;

@ReadingConverter
public class StringToTravelMediaType implements Converter<String, TravelMediaType> {

    @Override
    public TravelMediaType convert(String source) {
        return TravelMediaType.valueOf(source);
    }
}
