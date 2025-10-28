package r2m.spring.db.spring_jdbc.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Sequence;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "shipment", schema = "logistics")
public class Shipment {

    @Id
    @Sequence(sequence = "shipment_seq_id", schema = "logistics")
    private Integer id;

    private String source;

    private String destination;

    @MappedCollection(idColumn = "shipment_id")
    private Set<Travel> travels;
}
