package r2m.spring.db.spring_jpa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "shipment", schema = "logistics")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipment_id_gen")
    @SequenceGenerator(name = "shipment_id_gen", sequenceName = "shipment_seq_id", schema = "logistics", allocationSize = 1)
    public Integer id;

    public String source;

    public String destination;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public Set<Travel> travels = new HashSet<>();
}
