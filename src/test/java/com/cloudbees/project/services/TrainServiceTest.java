package com.cloudbees.project.services;

import com.cloudbees.project.constants.Section;
import com.cloudbees.project.constants.TicketStatus;
import com.cloudbees.project.dto.BookingRequestDto;
import com.cloudbees.project.exceptions.CustomException;
import com.cloudbees.project.models.Receipt;
import com.cloudbees.project.models.Seat;
import com.cloudbees.project.models.User;
import com.cloudbees.project.models.repositories.ReceiptRepository;
import com.cloudbees.project.models.repositories.SeatRepository;
import com.cloudbees.project.models.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TrainServiceTest {

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReceiptRepository receiptRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private TrainService trainService;

    @Test
    public void bookTicketTestWithInvalidRequestBody() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        assertThrows(CustomException.class, () -> trainService.bookTicket(bookingRequestDto));

        bookingRequestDto.setFrom("France");
        assertThrows(CustomException.class, () -> trainService.bookTicket(bookingRequestDto));

        bookingRequestDto.setTo("London");
        assertThrows(CustomException.class, () -> trainService.bookTicket(bookingRequestDto));

        bookingRequestDto.setPricePaid(10);
        assertThrows(CustomException.class, () -> trainService.bookTicket(bookingRequestDto));

        bookingRequestDto.setUserId(1L);
        assertThrows(CustomException.class, () -> trainService.bookTicket(bookingRequestDto));

        bookingRequestDto.setFrom("London");
        assertThrows(CustomException.class, () -> trainService.bookTicket(bookingRequestDto));

        bookingRequestDto.setTo("France");
        assertThrows(CustomException.class, () -> trainService.bookTicket(bookingRequestDto));
    }

    @Test
    public void bookTicketWithInvalidUserNotFound() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto("London", "France", 1L, 20);
        doReturn(Optional.empty()).when(userRepository).findById(1L);

        assertThrows(CustomException.class, () -> trainService.bookTicket(bookingRequestDto));
    }

    @Test
    public void bookTicketWhenNoSeatsAvailable() throws CustomException{
        BookingRequestDto bookingRequestDto = new BookingRequestDto("London", "France", 1L, 20);
        User user = new User(); user.setId(1L); user.setEmail("saipraneeth.maddi@gmail.com");
        user.setFirstName("Sai Praneeth"); user.setLastName("Maddi");
        doReturn(Optional.of(user)).when(userRepository).findById(1L);
        doReturn(null).when(seatRepository).findAvailableSeat();

        assertEquals(trainService.bookTicket(bookingRequestDto).getStatus(), TicketStatus.NOT_BOOKED.getStatus());
    }

    @Test
    public void bookTicket() throws CustomException{
        BookingRequestDto bookingRequestDto = new BookingRequestDto("London", "France", 1L, 20);
        User user = new User(); user.setId(1L); user.setEmail("saipraneeth.maddi@gmail.com");
        user.setFirstName("Sai Praneeth"); user.setLastName("Maddi");
        Seat seat = new Seat(); seat.setNumber(1); seat.setSection(Section.A); seat.setBooked(true); seat.setUser(user);
        Receipt receipt = new Receipt(seat, user, bookingRequestDto, TicketStatus.BOOKED);
        doReturn(Optional.of(user)).when(userRepository).findById(1L);
        doReturn(seat).when(seatRepository).findAvailableSeat();
        doReturn(seat).when(seatRepository).save(any());
        doReturn(receipt).when(receiptRepository).save(Mockito.any());

        assertEquals(trainService.bookTicket(bookingRequestDto).getStatus(), TicketStatus.BOOKED.getStatus());
    }

    @Test
    public void getTicketDetailsForUserWhenUserIdIsInvalid() {
        assertThrows(CustomException.class, () -> trainService.getTicketDetailsForUser(""));
    }

    @Test
    public void getTicketDetailsForUserWhenUserNotFound() throws CustomException {
        String userIdString = "1";
        doReturn(1L).when(userService).userIdFromString(userIdString);
        doReturn(Optional.empty()).when(userRepository).findById(1L);

        assertThrows(CustomException.class, () -> trainService.getTicketDetailsForUser(userIdString));
    }

    @Test
    public void getTicketDetailsForUser() throws CustomException {
        String userIdString = "1";
        User user = new User(); user.setId(1L); user.setEmail("saipraneeth.maddi@gmail.com");
        user.setFirstName("Sai Praneeth"); user.setLastName("Maddi");
        doReturn(1L).when(userService).userIdFromString(userIdString);
        doReturn(Optional.of(user)).when(userRepository).findById(1L);
        doReturn(List.of()).when(receiptRepository).findAllByUserAndTicketStatus(user, TicketStatus.BOOKED);

        assertEquals(trainService.getTicketDetailsForUser(userIdString).size(), 0);
    }

    @Test
    public void getTicketDetailsForReceiptWhenReceiptIdIsInvalid() {
        assertThrows(CustomException.class, () -> trainService.getTicketDetailsFromReceipt(""));
    }

    @Test
    public void getTicketDetailsForReceiptWhenReceiptIsNotFound() {
        String receiptIdString = "1";
        doReturn(Optional.empty()).when(receiptRepository).findById(1L);

        assertThrows(CustomException.class, () -> trainService.getTicketDetailsFromReceipt(receiptIdString));
    }

    @Test
    public void getTicketDetailsForReceipt() throws CustomException {
        String receiptIdString = "1";
        User user = new User(); user.setId(1L); user.setEmail("saipraneeth.maddi@gmail.com");
        user.setFirstName("Sai Praneeth"); user.setLastName("Maddi");
        Seat seat = new Seat(); seat.setNumber(1); seat.setSection(Section.A); seat.setBooked(true); seat.setUser(user);
        Receipt receipt = new Receipt(); receipt.setSeat(seat); receipt.setUser(user); receipt.setId(1L);
        receipt.setStatus(TicketStatus.BOOKED); receipt.setFromStation("London"); receipt.setToStation("France");
        receipt.setAmountPaid(20);
        doReturn(Optional.of(receipt)).when(receiptRepository).findById(1L);

        assertEquals(trainService.getTicketDetailsFromReceipt(receiptIdString).getReceiptId(), 1L);
    }

    @Test
    public void getBookedSeatsFromSectionWhenSectionIsInvalid() {
        assertThrows(CustomException.class, () -> trainService.getBookedSeatsFromSection("C"));
    }

    @Test
    public void getBookedSeatsFromSection() throws CustomException {
        String section = "A";
        doReturn(List.of()).when(seatRepository).getBookedSeatsFromSection(Section.A);

        assertEquals(trainService.getBookedSeatsFromSection(section).size(), 0);
    }

    @Test
    public void cancelTicketWhenReceiptIdIsInvalid() {
        assertThrows(CustomException.class, () -> trainService.cancelTicket(""));
    }

    @Test
    public void cancelTicketWhenReceiptIsNotFound() {
        String receiptIdString = "1";
        doReturn(null).when(receiptRepository).findByIdAndStatus(1L, TicketStatus.BOOKED);

        assertThrows(CustomException.class, () -> trainService.cancelTicket(receiptIdString));
    }

    @Test
    public void cancelTicket() throws CustomException {
        String receiptIdString = "1";
        User user = new User(); user.setId(1L); user.setEmail("saipraneeth.maddi@gmail.com");
        user.setFirstName("Sai Praneeth"); user.setLastName("Maddi");
        Seat seat = new Seat(); seat.setNumber(1); seat.setSection(Section.A); seat.setBooked(true); seat.setUser(user);
        Receipt receipt = new Receipt(); receipt.setSeat(seat); receipt.setUser(user); receipt.setId(1L);
        receipt.setStatus(TicketStatus.BOOKED); receipt.setFromStation("London"); receipt.setToStation("France");
        receipt.setAmountPaid(20);
        doReturn(receipt).when(receiptRepository).findByIdAndStatus(1L, TicketStatus.BOOKED);
        doReturn(seat).when(seatRepository).save(seat);
        doReturn(receipt).when(receiptRepository).save(receipt);

        assertEquals(trainService.cancelTicket(receiptIdString).getStatus(), TicketStatus.CANCELLED.getStatus());
    }

    @Test
    public void modifySeatWhenReceiptIdIsInvalid() {
        assertThrows(CustomException.class, () -> trainService.modifySeat("", ""));
    }

    @Test
    public void modifySeatWhenSeatNumberIsInvalid() {
        assertThrows(CustomException.class, () -> trainService.modifySeat("1", "a"));
    }

    @Test
    public void modifySeatWhenReceiptNotFound() {
        String receiptIdString = "1"; String seatNumber = "";
        doReturn(null).when(receiptRepository).findByIdAndStatus(1L, TicketStatus.BOOKED);

        assertThrows(CustomException.class, () -> trainService.modifySeat(receiptIdString, seatNumber));

    }

    @Test
    public void modifySeatWhenNoSeatsAreAvailable() throws CustomException {
        String receiptIdString = "1"; String seatNumber = "";
        User user = new User(); user.setId(1L); user.setEmail("saipraneeth.maddi@gmail.com");
        user.setFirstName("Sai Praneeth"); user.setLastName("Maddi");
        Seat seat = new Seat(); seat.setNumber(1); seat.setSection(Section.A); seat.setBooked(true); seat.setUser(user);
        Receipt receipt = new Receipt(); receipt.setSeat(seat); receipt.setUser(user); receipt.setId(1L);
        receipt.setStatus(TicketStatus.BOOKED); receipt.setFromStation("London"); receipt.setToStation("France");
        receipt.setAmountPaid(20);
        doReturn(receipt).when(receiptRepository).findByIdAndStatus(1L, TicketStatus.BOOKED);
        doReturn(0).when(seatRepository).countAvailableSeats();

        assertEquals(trainService.modifySeat(receiptIdString, seatNumber).getStatus(), TicketStatus.NOT_BOOKED.getStatus());
    }

    @Test
    public void modifySeatWhenSeatNumberProvidedAndSeatNotFound() {
        String receiptIdString = "1"; String seatNumber = "2";
        User user = new User(); user.setId(1L); user.setEmail("saipraneeth.maddi@gmail.com");
        user.setFirstName("Sai Praneeth"); user.setLastName("Maddi");
        Seat seat = new Seat(); seat.setNumber(1); seat.setSection(Section.A); seat.setBooked(true); seat.setUser(user);
        Receipt receipt = new Receipt(); receipt.setSeat(seat); receipt.setUser(user); receipt.setId(1L);
        receipt.setStatus(TicketStatus.BOOKED); receipt.setFromStation("London"); receipt.setToStation("France");
        receipt.setAmountPaid(20);
        doReturn(receipt).when(receiptRepository).findByIdAndStatus(1L, TicketStatus.BOOKED);
        doReturn(1).when(seatRepository).countAvailableSeats();
        doReturn(Optional.empty()).when(seatRepository).findById(2);

        assertThrows(CustomException.class, () -> trainService.modifySeat(receiptIdString, seatNumber));
    }

    @Test
    public void modifySeatWhenSeatNumberProvided() throws CustomException {
        String receiptIdString = "1"; String seatNumber = "2";
        User user = new User(); user.setId(1L); user.setEmail("saipraneeth.maddi@gmail.com");
        user.setFirstName("Sai Praneeth"); user.setLastName("Maddi");
        Seat seat = new Seat(); seat.setNumber(1); seat.setSection(Section.A); seat.setBooked(true); seat.setUser(user);
        Receipt receipt = new Receipt(); receipt.setSeat(seat); receipt.setUser(user); receipt.setId(1L);
        receipt.setStatus(TicketStatus.BOOKED); receipt.setFromStation("London"); receipt.setToStation("France");
        receipt.setAmountPaid(20);
        doReturn(receipt).when(receiptRepository).findByIdAndStatus(1L, TicketStatus.BOOKED);
        doReturn(1).when(seatRepository).countAvailableSeats();
        doReturn(Optional.of(seat)).when(seatRepository).findById(2);
        doReturn(List.of()).when(seatRepository).saveAll(any());
        doReturn(receipt).when(receiptRepository).save(any());

        assertEquals(trainService.modifySeat(receiptIdString, seatNumber).getSeatNumber(), 1);
    }

    @Test
    public void modifySeatWhenSeatNumberNotProvided() throws CustomException {
        String receiptIdString = "1"; String seatNumber = "";
        User user = new User(); user.setId(1L); user.setEmail("saipraneeth.maddi@gmail.com");
        user.setFirstName("Sai Praneeth"); user.setLastName("Maddi");
        Seat seat = new Seat(); seat.setNumber(1); seat.setSection(Section.A); seat.setBooked(true); seat.setUser(user);
        Receipt receipt = new Receipt(); receipt.setSeat(seat); receipt.setUser(user); receipt.setId(1L);
        receipt.setStatus(TicketStatus.BOOKED); receipt.setFromStation("London"); receipt.setToStation("France");
        receipt.setAmountPaid(20);
        doReturn(receipt).when(receiptRepository).findByIdAndStatus(1L, TicketStatus.BOOKED);
        doReturn(1).when(seatRepository).countAvailableSeats();
        doReturn(seat).when(seatRepository).findAvailableSeat();
        doReturn(List.of()).when(seatRepository).saveAll(any());
        doReturn(receipt).when(receiptRepository).save(any());

        assertEquals(trainService.modifySeat(receiptIdString, seatNumber).getSeatNumber(), 1);
    }

}
