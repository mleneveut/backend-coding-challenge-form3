package tech.form3.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.form3.challenge.entity.Payment;

import java.util.UUID;

/**
 * JPA Repository to handle payments.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}
