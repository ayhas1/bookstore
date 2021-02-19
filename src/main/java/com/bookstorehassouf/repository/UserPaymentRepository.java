package com.bookstorehassouf.repository;

import com.bookstorehassouf.model.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPaymentRepository extends JpaRepository<UserPayment, Long> {

}
