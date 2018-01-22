package tech.form3.challenge.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java8.En;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.form3.challenge.dto.PaymentDto;
import tech.form3.challenge.entity.Payment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentMapperStepDefs implements En {

    private PaymentDto dto;
    private Payment dao;
    private ObjectMapper mapper = new ObjectMapper();

    public PaymentMapperStepDefs() {
        Given("^the RequestContextHolder is set$", () -> {
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        });

        Given("^the dao is setup$", () -> {
            Map<String, Object> attrs = new LinkedHashMap<>();
            attrs.put("amount", Double.valueOf("100.21"));
            Map<String, Object> beneficiaryParty = new LinkedHashMap<>();
            beneficiaryParty.put("account_name", "W Owens");
            beneficiaryParty.put("account_number", "31926819");
            attrs.put("beneficiary_party", beneficiaryParty);

            dao = Payment.builder()
                    .paymentId(UUID.fromString("09a8fe0d-e239-4aff-8098-7923eadd0b98"))
                    .organisationId(UUID.fromString("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"))
                    .type("Payment")
                    .version(1)
                    .attributes(attrs)
                    .build();
        });

        Given("^the dto is setup$", () -> {
            Map<String, Object> attrs = new LinkedHashMap<>();
            attrs.put("amount", Double.valueOf("100.21"));
            Map<String, Object> beneficiaryParty = new LinkedHashMap<>();
            beneficiaryParty.put("account_name", "W Owens");
            beneficiaryParty.put("account_number", "31926819");
            attrs.put("beneficiary_party", beneficiaryParty);

            dto = PaymentDto.builder()
                    .paymentId(UUID.fromString("09a8fe0d-e239-4aff-8098-7923eadd0b98"))
                    .organisationId(UUID.fromString("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"))
                    .type("Payment")
                    .version(1)
                    .attributes(attrs)
                    .build();
        });

        When("^the dto is mapped from the dao$",
                () -> dto = PaymentMapper.instance().toPaymentDto(dao)
        );

        When("^the dao is mapped from the dto$",
                () -> dao = PaymentMapper.instance().toPayment(dto)
        );

        Then("^the dto and dao have the same values$",
                () -> {
                    assertThat(dto.getPaymentId()).isEqualTo(dao.getPaymentId());
                    assertThat(dto.getOrganisationId()).isEqualTo(dao.getOrganisationId());
                    assertThat(dto.getType()).isEqualTo(dao.getType());
                    assertThat(dto.getVersion()).isEqualTo(dao.getVersion());
                    assertThat(dto.getAttributes()).isEqualTo(dao.getAttributes());
                }
        );
    }
}
