package com.github.forax.dop;

import static com.github.forax.dop.Data4.*;

import com.github.forax.dop.db.Invoice;
import com.github.forax.dop.db.InvoiceRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//@RestController  // uncomment and comment the RestController annotation of the other TextToImageAPI?
@RequestMapping("/api")
public class TextToImageAPI4 {
  private final InvoiceRepository invoiceRepository;

  public TextToImageAPI4(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @PostMapping(path="/dalle", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @Transactional
  @Operation(summary = "create an image using Dall-E")
  public ImageResponse createImage(@RequestBody DalleImageRequest imageRequest) {
    return createImage((ImageRequest) imageRequest);
  }

  @PostMapping(path="/sd", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @Transactional
  @Operation(summary = "create an image using stable diffusion")
  public ImageResponse createImage(@RequestBody SDImageRequest imageRequest) {
    return createImage((ImageRequest) imageRequest);
  }

  private ImageResponse createImage(ImageRequest imageRequest) {
    var user = imageRequest.user();
    var text = imageRequest.text();

    var parameter = parameter(imageRequest);
    var price = price(parameter);

    var invoice = new Invoice(user, text, price, LocalDateTime.now());
    invoiceRepository.save(invoice);

    var imagePath = imageToText(text, parameter);

    return new ImageResponse(imagePath);
  }

  @GetMapping(path= "/invoice", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "get all invoices")
  public List<InvoiceResponse> getAllInvoices() {
    return invoiceRepository.findAll().stream()
        .map(invoice -> new InvoiceResponse(invoice.userName, invoice.message, invoice.price, invoice.dateTime))
        .toList();
  }
}