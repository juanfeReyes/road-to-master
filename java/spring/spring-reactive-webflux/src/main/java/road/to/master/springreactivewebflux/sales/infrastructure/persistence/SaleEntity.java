package road.to.master.springreactivewebflux.sales.infrastructure.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Data
@Table(name = "sales")
public class SaleEntity implements Persistable<String> {

  @Id
  private String id;

  @Column
  private Double amount;

  @Column
  private String description;

  @Column
  private String origin;

  @Override
  public boolean isNew() {
    var validNew = Objects.isNull(id);
    id = validNew ? UUID.randomUUID().toString() : id;
    return validNew;
  }
}
