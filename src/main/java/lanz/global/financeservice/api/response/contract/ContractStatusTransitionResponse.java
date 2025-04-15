package lanz.global.financeservice.api.response.contract;

import lanz.global.financeservice.model.ContractStatusEnum;

import java.io.Serializable;

public record ContractStatusTransitionResponse(ContractStatusEnum fromStatus,
                                               ContractStatusEnum toStatus) implements Serializable {

}
