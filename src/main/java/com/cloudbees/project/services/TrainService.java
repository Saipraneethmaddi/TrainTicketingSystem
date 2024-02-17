package com.cloudbees.project.services;

import com.cloudbees.project.constants.Section;
import com.cloudbees.project.constants.TicketStatus;
import com.cloudbees.project.dto.BookingRequestDto;
import com.cloudbees.project.dto.BookingResponseDto;
import com.cloudbees.project.dto.SeatResponseDto;
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

@Service
public class TrainService {
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final ReceiptRepository receiptRepository;
    private final UserService userService;

    public TrainService(SeatRepository seatRepository, UserRepository userRepository, ReceiptRepository receiptRepository,
                        UserService userService) {
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.receiptRepository = receiptRepository;
        this.userService = userService;
    }

    public BookingResponseDto bookTicket(BookingRequestDto bookingRequestDto) throws CustomException {
        bookingRequestDto.isValid();

        User user = userRepository.findById(bookingRequestDto.getUserId()).orElse(null);
        if(Objects.isNull(user)) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        Seat seat = seatRepository.findAvailableSeat();
        if(Objects.isNull(seat)) return new BookingResponseDto("No seats available!", TicketStatus.NOT_BOOKED);
        seat.setBooked(true); seat.setUser(user);
        seat = seatRepository.save(seat);

        Receipt receipt = new Receipt(seat, user, bookingRequestDto, TicketStatus.BOOKED);
        receipt = receiptRepository.save(receipt);

        return new BookingResponseDto(receipt);
    }

    public List<BookingResponseDto> getTicketDetailsForUser(String userIdString) throws CustomException {
        long userId = userService.userIdFromString(userIdString);
        User user = userRepository.findById(userId).orElse(null);
        if(Objects.isNull(user)) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        List<Receipt> receipts = receiptRepository.findAllByUserAndTicketStatus(user, TicketStatus.BOOKED);
        return receipts.stream().map(BookingResponseDto::new).toList();
    }

    public BookingResponseDto getTicketDetailsFromReceipt(String receiptIdString) throws CustomException{
        long receiptId = getReceiptIdFromString(receiptIdString);

        Receipt receipt = receiptRepository.findById(receiptId).orElse(null);
        if(Objects.isNull(receipt)) throw new CustomException(ErrorCode.RECEIPT_NOT_FOUND);

        return new BookingResponseDto(receipt);
    }

    public List<SeatResponseDto> getBookedSeatsFromSection(String sectionString) throws CustomException {
        if(Objects.isNull(sectionString)) throw new CustomException(ErrorCode.INVALID_SECTION);
        Section section = Section.getSectionFromString(sectionString);
        if(Objects.isNull(section)) throw new CustomException(ErrorCode.SECTION_DOES_NOT_EXIST);

        List<Seat> seats = seatRepository.getBookedSeatsFromSection(section);
        return seats.stream().map(SeatResponseDto::new).toList();
    }

    public BookingResponseDto cancelTicket(String  receiptIdString) throws CustomException {
        long receiptId = getReceiptIdFromString(receiptIdString);

        Receipt receipt = receiptRepository.findByIdAndStatus(receiptId, TicketStatus.BOOKED);
        if(Objects.isNull(receipt)) throw new CustomException(ErrorCode.RECEIPT_NOT_FOUND);

        Seat seat = receipt.getSeat();
        seat.setBooked(false); seat.setUser(null);
        seat = seatRepository.save(seat);

        receipt.setStatus(TicketStatus.CANCELLED); receipt.setSeat(seat);
        receipt = receiptRepository.save(receipt);

        return new BookingResponseDto(receipt);
    }

    public BookingResponseDto modifySeat(String receiptIdString, String seatNumberString) throws CustomException {
        long receiptId = getReceiptIdFromString(receiptIdString);
        Integer seatNumber;
        if(Objects.isNull(seatNumberString) || seatNumberString.isBlank()) seatNumber = null;
        else {
            try {
                seatNumber = Integer.parseInt(seatNumberString);
            } catch (NumberFormatException e) {
                throw new CustomException(ErrorCode.INVALID_SEAT_NUMBER);
            }
        }

        Receipt receipt = receiptRepository.findByIdAndStatus(receiptId, TicketStatus.BOOKED);
        if(Objects.isNull(receipt)) throw new CustomException(ErrorCode.RECEIPT_NOT_FOUND);

        Integer availableSeatCount = seatRepository.countAvailableSeats();
        if(availableSeatCount==0)
            return new BookingResponseDto("No seats available for swapping.", TicketStatus.NOT_BOOKED);

        Seat seat = receipt.getSeat();
        Seat newSeat;
        if(Objects.isNull(seatNumber)) newSeat = seatRepository.findAvailableSeat();
        else newSeat = seatRepository.findById(seatNumber).orElse(null);
        if(Objects.isNull(newSeat)) throw new CustomException(ErrorCode.SEAT_NOT_FOUND);


        seat.setBooked(false); seat.setUser(null);
        newSeat.setBooked(true); newSeat.setUser(receipt.getUser());
        seatRepository.saveAll(List.of(seat, newSeat));

        receipt.setSeat(newSeat);
        receipt = receiptRepository.save(receipt);

        return new BookingResponseDto(receipt);
    }

    private long getReceiptIdFromString(String receiptIdString) throws CustomException{
        if(Objects.isNull(receiptIdString)) throw new CustomException(ErrorCode.INVALID_RECEIPT_ID);
        try {
            return Long.parseLong(receiptIdString);
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.INVALID_RECEIPT_ID);
        }
    }
}
