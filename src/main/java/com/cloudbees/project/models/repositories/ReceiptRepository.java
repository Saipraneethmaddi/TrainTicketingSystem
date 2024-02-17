package com.cloudbees.project.models.repositories;

import com.cloudbees.project.constants.TicketStatus;
import com.cloudbees.project.models.Receipt;
import com.cloudbees.project.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
    @Query("SELECT r FROM Receipt r WHERE r.user=?1 AND r.status=?2")
    List<Receipt> findAllByUserAndTicketStatus(User user, TicketStatus status);

    @Query("SELECT r FROM Receipt r WHERE r.id=?1 AND r.status=?2")
    Receipt findByIdAndStatus(Long id, TicketStatus status);
}
