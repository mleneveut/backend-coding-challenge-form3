package tech.form3.challenge.database;

import cucumber.api.java8.En;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.form3.challenge.dto.PaymentDto;
import tech.form3.challenge.entity.Payment;
import tech.form3.challenge.repository.PaymentRepository;
import tech.form3.challenge.service.PaymentService;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class PaymentServiceAndRepositoryStepDefs implements En {

    private static final UUID PAYMENT_ID = UUID.fromString("09a8fe0d-e239-4aff-8098-7923eadd0b98");

    private Payment mockDao;
    private PaymentDto mockDto;;
    private Payment dao;

    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;

    public PaymentServiceAndRepositoryStepDefs() {

        Given("^the mocks are injected$",
                () -> MockitoAnnotations.initMocks(this)
        );

        Given("^the mockDto is setup$", () -> {
            setupMockDto();
        });

        Given("^the mockDao is setup$", () -> {
            setupMockDao();
            mockDao.setPaymentId(PAYMENT_ID);
        });

        Given("^the mockDao for creation is setup$", () -> {
            setupMockDao();
        });

        Given("^the findOne is mocked to return the mockDao$",
                () -> when(paymentRepository.findOne(eq(PAYMENT_ID))).thenReturn(mockDao)
        );

        Given("^the findOne is mocked to return null$",
                () -> when(paymentRepository.findOne(eq(PAYMENT_ID))).thenReturn(null)
        );

        Given("^the save is mocked to return a mockDao with new id$",
                () -> {
                    mockDao.setPaymentId(PAYMENT_ID);
                    when(paymentRepository.save(any(Payment.class))).thenReturn(mockDao);
                }
        );

        Given("^the save is mocked to return the mockDao$",
                () -> when(paymentRepository.save(any(Payment.class))).thenReturn(mockDao)
        );

        Given("^the findAll is mocked to return a list of the mockDao$",
                () -> when(paymentRepository.findAll()).thenReturn(Collections.singletonList(mockDao))
        );

        When("^we get the payment by its id$",
                () -> dao = paymentService.getById(PAYMENT_ID)
        );

        When("^we get the payments$",
                () -> dao = paymentService.findAll().get(0)
        );

        When("^we save a payment$",
                () -> dao = paymentService.create(mockDao)
        );

        When("^we update a payment$",
                () -> dao = paymentService.edit(mockDto)
        );

        When("^we delete a payment$",
                () -> paymentService.delete(PAYMENT_ID)
        );

        When("^we find the payment in database$",
                () -> dao = paymentRepository.findOne(PAYMENT_ID)
        );

        When("^we find all the payments in database$",
                () -> dao = paymentRepository.findAll().get(0)
        );

        When("^we save a payment in database$",
                () -> dao = paymentRepository.save(mockDao)
        );

        When("^we delete a payment in database$",
                () -> paymentRepository.delete(PAYMENT_ID)
        );

        Then("the dao and mockDao are equals",
                () -> assertThat(dao).isEqualTo(mockDao)
        );

        Then("the returned dao is null",
                () -> assertThat(dao).isEqualTo(null)
        );

        Then("the delete has been called once",
                () -> verify(paymentRepository, times(1)).delete(PAYMENT_ID)
        );

    }
    private void setupMockDto() {
        mockDto = PaymentDto.builder()
                .paymentId(PAYMENT_ID)
                .organisationId(UUID.fromString("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"))
                .type("Payment")
                .version(1)
                .attributes(setupAttributes())
                .build();
    }

    private void setupMockDao() {
        mockDao = Payment.builder()
                .organisationId(UUID.fromString("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"))
                .type("Payment")
                .version(1)
                .attributes(setupAttributes())
                .build();
    }

    private Map<String, Object> setupAttributes() {
        Map<String, Object> attrs = new LinkedHashMap<>();
        attrs.put("amount", Double.valueOf("100.21"));
        Map<String, Object> beneficiaryParty = new LinkedHashMap<>();
        beneficiaryParty.put("account_name", "W Owens");
        beneficiaryParty.put("account_number", "31926819");
        attrs.put("beneficiary_party", beneficiaryParty);

        return attrs;
    }
}
