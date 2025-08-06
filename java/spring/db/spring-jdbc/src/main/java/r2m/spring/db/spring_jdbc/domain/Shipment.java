package r2m.spring.db.spring_jdbc.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Sequence;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;

@Data
@Table(name = "shipment", schema = "logistics")
public class Shipment {

    @Id
    @Sequence(sequence = "shipment_seq_id", schema = "logistics")
    public Integer id;

    public String source;

    public String destination;

    @MappedCollection(idColumn = "shipment_id")
    public Set<Travel> travels;
}
