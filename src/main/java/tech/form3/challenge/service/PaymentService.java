package tech.form3.challenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.form3.challenge.dto.PaymentDto;
import tech.form3.challenge.entity.Payment;
import tech.form3.challenge.mapper.PaymentMapper;
import tech.form3.challenge.repository.PaymentRepository;

import java.util.List;
import java.util.UUID;

/**
 * Business service to handle payments.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment getById(UUID id) {
        return paymentRepository.findOne(id);
    }

    @Transactional
    public Payment create(Payment resource) {
        return paymentRepository.save(resource);
    }

    @Transactional
    public Payment edit(PaymentDto dto) {
        Payment dao = paymentRepository.findOne(dto.getPaymentId());
        //check if the item to update exists
        if (dao != null) {
            dao = PaymentMapper.instance().toPayment(dto);
            return paymentRepository.save(dao);
        } else {
            return null;
        }
    }

    @Transactional
    public void delete(UUID id) {
        paymentRepository.delete(id);
    }
}
