package r2m.spring.db.spring_jdbc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "travel", schema = "logistics")
public class Travel {

    @Id
    private UUID id;

    private String address;

    @Column("arrivaldate")
    private LocalDateTime arrivalDate;

    @Column("mediatype")
    private TravelMediaType mediaType;
}
