package lanz.global.financeservice.api.request.contract;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lanz.global.financeservice.model.ContractStatusEnum;

import java.io.Serializable;

public record ContractStatusUpdateRequest(
        @Schema(description = "The new status of the contract") @NotNull ContractStatusEnum status) implements Serializable {
}
