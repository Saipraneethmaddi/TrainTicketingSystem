package com.cloudbees.project.services;

import com.cloudbees.project.dto.BookingRequestDto;
import com.cloudbees.project.dto.BookingResponseDto;
import com.cloudbees.project.exceptions.CustomException;
import com.cloudbees.project.exceptions.ErrorCode;
import com.cloudbees.project.models.Receipt;
import com.cloudbees.project.models.Seat;
import com.cloudbees.project.models.User;
import com.cloudbees.project.models.repositories.ReceiptRepository;
import com.cloudbees.project.models.repositories.SeatRepository;
import com.cloudbees.project.models.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TrainService {
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final ReceiptRepository receiptRepository;

    public TrainService(SeatRepository seatRepository, UserRepository userRepository, ReceiptRepository receiptRepository) {
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.receiptRepository = receiptRepository;
    }

    public BookingResponseDto bookTicket(BookingRequestDto bookingRequestDto) throws CustomException {
        bookingRequestDto.isValid();

        User user = userRepository.findById(bookingRequestDto.getUserId()).orElse(null);
        if(Objects.isNull(user)) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        Seat seat = seatRepository.findAvailableSeat();
        if(Objects.isNull(seat)) return new BookingResponseDto("No seats available!");
        seat.setBooked(true); seat.setUser(user);
        seat = seatRepository.save(seat);

        Receipt receipt = new Receipt(seat, user, bookingRequestDto);
        receipt = receiptRepository.save(receipt);

        return new BookingResponseDto(receipt);
    }

    public List<BookingResponseDto> getTicketDetailsForUser(Long userId) throws CustomException {
        if(Objects.isNull(userId)) throw new CustomException(ErrorCode.NULL_USER_ID);

        User user = userRepository.findById(userId).orElse(null);
        if(Objects.isNull(user)) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        List<Receipt> receipts = receiptRepository.findAllByUser(user);
        return receipts.stream().map(BookingResponseDto::new).toList();
    }
}
