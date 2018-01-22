package tech.form3.challenge.mapper;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import tech.form3.challenge.dto.PaymentDto;
import tech.form3.challenge.entity.Payment;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2018-01-22T12:05:32+0100",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_152 (Oracle Corporation)"
)
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment toPayment(PaymentDto source) {
        if ( source == null ) {
            return null;
        }

        Payment payment = new Payment();

        payment.setPaymentId( source.getPaymentId() );
        payment.setType( source.getType() );
        payment.setVersion( source.getVersion() );
        payment.setOrganisationId( source.getOrganisationId() );
        Map<String, Object> map = source.getAttributes();
        if ( map != null ) {
            payment.setAttributes( new HashMap<String, Object>( map ) );
        }
        else {
            payment.setAttributes( null );
        }

        return payment;
    }

    @Override
    public PaymentDto toPaymentDto(Payment source) {
        if ( source == null ) {
            return null;
        }

        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setPaymentId( source.getPaymentId() );
        paymentDto.setType( source.getType() );
        paymentDto.setVersion( source.getVersion() );
        paymentDto.setOrganisationId( source.getOrganisationId() );
        Map<String, Object> map = source.getAttributes();
        if ( map != null ) {
            paymentDto.setAttributes( new HashMap<String, Object>( map ) );
        }
        else {
            paymentDto.setAttributes( null );
        }

        addLinks( source, paymentDto );

        return paymentDto;
    }
}
