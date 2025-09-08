package lanz.global.financeservice.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lanz.global.financeservice.api.config.Rules;
import lanz.global.financeservice.api.request.contract.ContractRequest;
import lanz.global.financeservice.api.request.contract.ContractStatusUpdateRequest;
import lanz.global.financeservice.api.request.contract.GetContractParams;
import lanz.global.financeservice.api.response.contract.ContractResponse;
import lanz.global.financeservice.api.response.contract.ContractStatusTransitionResponse;
import lanz.global.financeservice.api.response.invoice.InvoiceResponse;
import lanz.global.financeservice.model.Contract;
import lanz.global.financeservice.model.ContractStatusTransition;
import lanz.global.financeservice.model.Invoice;
import lanz.global.financeservice.service.ContractService;
import lanz.global.financeservice.service.InvoiceService;
import lanz.global.libraryservice.converter.component.ServiceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/finance/contract")
@RequiredArgsConstructor
public class ContractApi {

    private final ContractService contractService;
    private final ServiceConverter serviceConverter;
    private final InvoiceService invoiceService;

    @PostMapping
    @RolesAllowed(Rules.CREATE_CONTRACT)
    @ApiOperation(value = "Create new Contract", notes = "The endpoint creates a contract")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Contract created"), @ApiResponse(code = 400, message = "Bad request")})
    public ResponseEntity<ContractResponse> createContract(@Valid @RequestBody ContractRequest request) {
        Contract contract = contractService.createContract(request);

        return ResponseEntity.ok(serviceConverter.convert(contract, ContractResponse.class));
    }

    @PutMapping("/{contractId}")
    @RolesAllowed(Rules.UPDATE_CONTRACT)
    @ApiOperation(value = "Update contract by ID", notes = "The endpoint updates an existing contract")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Contract updated"), @ApiResponse(code = 404, message = "Contract not found"), @ApiResponse(code = 400, message = "Bad request")})
    public ResponseEntity<ContractResponse> updateContract(@PathVariable UUID contractId, @Valid @RequestBody ContractRequest request) {
        Contract contract = contractService.updateContract(contractId, request);

        return ResponseEntity.ok(serviceConverter.convert(contract, ContractResponse.class));
    }

    @GetMapping("/{contractId}")
    @RolesAllowed(Rules.GET_CONTRACT)
    @ApiOperation(value = "Find contract by ID", notes = "The endpoint retrieves the contract data")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Contract data"), @ApiResponse(code = 404, message = "Not found")})
    public ResponseEntity<ContractResponse> findContractById(@PathVariable UUID contractId) {
        Contract contract = contractService.findContractById(contractId);

        return ResponseEntity.ok(serviceConverter.convert(contract, ContractResponse.class));
    }

    @GetMapping
    @RolesAllowed(Rules.LIST_CONTRACTS)
    @ApiOperation(value = "Find contracts", notes = "The endpoint retrieves a list of contracts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of contracts")})
    public ResponseEntity<List<ContractResponse>> findAllContracts() {
        List<Contract> contracts = contractService.findAllContracts();

        return ResponseEntity.ok(serviceConverter.convertList(contracts, ContractResponse.class));
    }

    @GetMapping("/search")
    @RolesAllowed(Rules.LIST_CONTRACTS)
    @ApiOperation(value = "Find contracts", notes = "The endpoint retrieves a list of contracts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of contracts")})
    public ResponseEntity<Page<ContractResponse>> findAllContracts(@ModelAttribute GetContractParams params) {
        Page<Contract> page = contractService.findAllContracts(params);

        return ResponseEntity.ok(page.map(contract -> serviceConverter.convert(contract, ContractResponse.class)));
    }

    @DeleteMapping("/{contractId}")
    @RolesAllowed(Rules.DELETE_CONTRACT)
    @ApiOperation(value = "Delete contract by ID", notes = "The endpoint deletes a contract")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List of contracts")})
    public ResponseEntity<Void> deleteContractById(@PathVariable UUID contractId) {
        contractService.deleteContractById(contractId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{contractId}/status")
    @RolesAllowed(Rules.UPDATE_CONTRACT_STATUS)
    @ApiOperation(value = "Updates the status of the contract", notes = "The endpoint updates the status of the contract following some rules")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Contract status updated"), @ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Contract not found")})
    public ResponseEntity<ContractResponse> updateContractStatus(@PathVariable UUID contractId, @Valid @RequestBody ContractStatusUpdateRequest request) {
        Contract contract = contractService.updateContractStatus(contractId, request);

        return ResponseEntity.ok(serviceConverter.convert(contract, ContractResponse.class));
    }

    @GetMapping("/status/transition")
    @RolesAllowed(Rules.UPDATE_CONTRACT_STATUS)
    @ApiOperation(value = "List of allowed status transitions", notes = "The endpoint retrieves the list of the status transitions that a contract can have")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Transition allowed"), @ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Contract not found")})
    public ResponseEntity<List<ContractStatusTransitionResponse>> findAllContractStatusTransitions() {
        List<ContractStatusTransition> transitions = contractService.findAllContractStatusTransitions();

        return ResponseEntity.ok(serviceConverter.convertList(transitions, ContractStatusTransitionResponse.class));
    }

    @GetMapping("/{contractId}/invoice")
    @RolesAllowed(Rules.LIST_INVOICES)
    @ApiOperation(value = "List invoices from a contract", notes = "The endpoint retrieves the list of the invoices of the contract")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Invoices"), @ApiResponse(code = 404, message = "Contract not found")})
    public ResponseEntity<List<InvoiceResponse>> findInvoicesByContractId(@PathVariable UUID contractId) {
        List<Invoice> invoices = invoiceService.findInvoicesByContractId(contractId);

        return ResponseEntity.ok(serviceConverter.convertList(invoices, InvoiceResponse.class));
    }

}
