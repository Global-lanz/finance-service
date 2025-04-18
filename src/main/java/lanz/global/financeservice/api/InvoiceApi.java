package lanz.global.financeservice.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lanz.global.financeservice.api.config.Rules;
import lanz.global.financeservice.api.request.payment.PaymentRequest;
import lanz.global.financeservice.api.response.invoice.PaymentResponse;
import lanz.global.financeservice.model.Payment;
import lanz.global.financeservice.service.InvoiceService;
import lanz.global.financeservice.util.converter.ServiceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/finance/invoice")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvoiceApi {

    private final InvoiceService invoiceService;
    private final ServiceConverter serviceConverter;

    @RolesAllowed(Rules.UPDATE_PAYMENT)
    @PutMapping("/{invoiceId}/payment")
    @ApiOperation(value = "Create invoice payment", notes = "The endpoint creates the payment record for the given invoice ID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Payment created"), @ApiResponse(code = 404, message = "Invoice not found")})
    public ResponseEntity<PaymentResponse> updatePayment(@PathVariable("invoiceId") UUID invoiceId, @Valid @RequestBody PaymentRequest request) {
        Payment payment = invoiceService.updatePayment(invoiceId, request);

        return ResponseEntity.ok(serviceConverter.convert(payment, PaymentResponse.class));
    }

    @RolesAllowed(Rules.GET_PAYMENT)
    @GetMapping("/{invoiceId}/payment")
    @ApiOperation(value = "Get invoice payment", notes = "The endpoint retrieves the payment record for the given invoice ID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Invoice"), @ApiResponse(code = 404, message = "Invoice not found")})
    public ResponseEntity<PaymentResponse> getPaymentByInvoiceId(@PathVariable("invoiceId") UUID invoiceId) {
        Payment payment = invoiceService.getPaymentByInvoiceId(invoiceId);

        return ResponseEntity.ok(serviceConverter.convert(payment, PaymentResponse.class));
    }

    @RolesAllowed(Rules.DELETE_PAYMENT)
    @GetMapping("/{invoiceId}/payment")
    @ApiOperation(value = "Delete invoice payment", notes = "The endpoint deletes the payment record for the given invoice ID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Invoice"), @ApiResponse(code = 404, message = "Invoice not found")})
    public ResponseEntity<PaymentResponse> deletePaymentByInvoiceId(@PathVariable("invoiceId") UUID invoiceId) {
        invoiceService.deletePaymentBy(invoiceId);
        return ResponseEntity.noContent().build();
    }

}
