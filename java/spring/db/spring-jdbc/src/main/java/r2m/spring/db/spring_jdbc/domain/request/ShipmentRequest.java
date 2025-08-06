package r2m.spring.db.spring_jdbc.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShipmentRequest {

    public String source;

    public String destination;
}
