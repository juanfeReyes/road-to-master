package com.roadtomaster.financialAsset.infrastructure.persistence;


import com.roadtomaster.financialAsset.infrastructure.persistence.account.AccountTable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
public class RegisterEmbedded {

  @OneToMany(cascade = CascadeType.ALL)
  private Set<AccountTable> accounts;
}
