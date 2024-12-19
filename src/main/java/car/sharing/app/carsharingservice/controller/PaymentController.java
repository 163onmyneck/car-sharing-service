package car.sharing.app.carsharingservice.controller;

import car.sharing.app.carsharingservice.dto.payment.PaymentDto;
import car.sharing.app.carsharingservice.dto.payment.PaymentRequestDto;
import car.sharing.app.carsharingservice.service.payment.PaymentService;
import java.net.MalformedURLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/payments")
@RestController()
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentDto> getAllPayments(@PathVariable Long id) {
        return paymentService.getAllPaymentsByUserId(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto createPayment(@RequestBody PaymentRequestDto paymentDto)
            throws MalformedURLException {
        return paymentService.createPayment(paymentDto);
    }

    @GetMapping("/success")
    public String success(PaymentDto paymentDto) {
        return "Success! \n Your payment details: \n " + paymentDto;
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "Cancel!";
    }
}
