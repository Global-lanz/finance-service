package lanz.global.financeservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "contract_status_transition")
public class ContractStatusTransition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contract_status_transition_id")
    private UUID contractStatusTransitionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", columnDefinition = "contract_status")
    private ContractStatusEnum fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", columnDefinition = "contract_status")
    private ContractStatusEnum toStatus;

}
