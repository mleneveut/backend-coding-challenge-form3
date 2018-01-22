package tech.form3.challenge.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tech.form3.challenge.dto.PaymentDto;
import tech.form3.challenge.entity.Payment;
import tech.form3.challenge.web.PaymentsController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * MapStruct mapper to convert Payments DAO to DTO and reverse.
 */
@Mapper
public interface PaymentMapper {

    static PaymentMapper instance() {
        return Mappers.getMapper(PaymentMapper.class);
    }

    /*
     * PaymentDto -> Payment
     */
    Payment toPayment(PaymentDto source);

    /*
     * Payment -> PaymentDto
     */
    @Mappings({
            @Mapping(target = "links", ignore = true)
    })
    PaymentDto toPaymentDto(Payment source);

    /**
     * Add the HATEOAS links to the target bean.
     *
     * @param source The DAO
     * @param target The DTO where the links will be added
     */
    @AfterMapping
    default void addLinks(Payment source, @MappingTarget PaymentDto target) {
        target.add(linkTo(PaymentsController.class).withRel("payments"));
        target.add(linkTo(methodOn(PaymentsController.class).getById(target.getPaymentId())).withSelfRel());
        target.add(linkTo(methodOn(PaymentsController.class).update(target.getPaymentId(), target)).withRel("update"));
        target.add(linkTo(methodOn(PaymentsController.class).delete(target.getPaymentId())).withRel("delete"));
    }
}