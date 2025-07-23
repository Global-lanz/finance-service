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

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contract_id")
    private UUID contractId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "frequency")
    @Enumerated(EnumType.STRING)
    private FrequencyEnum frequency;

    @Column(name = "payment_day")
    private Integer paymentDay;

    @Column(name = "week_payment_day")
    @Enumerated(EnumType.STRING)
    private DayOfWeek weekPaymentDay;

    @Column(name = "start_date")
    private LocalDate start;

    @Column(name = "end_date")
    private LocalDate end;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_status")
    private ContractStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    private ContractTypeEnum type;

    @Column(name = "termination_clause")
    private String terminationClause;

    @Column(name = "penalty_fee")
    private BigDecimal penaltyFee;

    @Column(name = "description")
    private String description;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "currency_id")
    private UUID currencyId;

    @Column(name = "company_id")
    private UUID companyId;

    public boolean isRunning() {
        return status == ContractStatusEnum.RUNNING;
    }

    public boolean isTerminated() {
        return status == ContractStatusEnum.TERMINATED;
    }

    public boolean isCancelled() {
        return status == ContractStatusEnum.CANCELLED;
    }
}
