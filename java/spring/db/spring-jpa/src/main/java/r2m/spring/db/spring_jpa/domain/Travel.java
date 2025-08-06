package r2m.spring.db.spring_jpa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@Table(name = "travel", schema = "logistics")
@EqualsAndHashCode(exclude = "shipment") // required as @Data has circular ref due to bidirectional @OneToMany
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String address;

    private LocalDateTime arrivalDate;

    @Enumerated(EnumType.STRING)
    private TravelMediaType mediaType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shipment_id", foreignKey = @ForeignKey(name = "kf_shipment_id"))
    private Shipment shipment;
}
