package com.parking.service;

import com.parking.entity.Booking;
import com.parking.entity.User;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class ReceiptService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private BookingService bookingService;

    /**
     * Generates PDF receipt for a completed booking belonging to the given user.
     * @return PDF bytes, or empty if booking not found, not completed, or not owned by user
     */
    public Optional<byte[]> generateReceiptPdf(Long bookingId, Long userId) {
        return bookingService.findById(bookingId)
                .filter(b -> b.getUser().getId().equals(userId))
                .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                .map(booking -> {
                    User user = booking.getUser();
                    Context context = new Context();
                    context.setVariable("booking", booking);
                    context.setVariable("user", user);
                    String html = templateEngine.process("user/receipt-pdf", context);
                    try (ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
                        HtmlConverter.convertToPdf(html, pdfOut);
                        return pdfOut.toByteArray();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to generate receipt PDF", e);
                    }
                });
    }
}
