package com.github.forax.dop;

import static com.github.forax.dop.Data2.*;

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
public class TextToImageAPI2 {
  private final InvoiceRepository invoiceRepository;

  public TextToImageAPI2(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @PostMapping(path="/dalle", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @Transactional
  @Operation(summary = "create an image using Dall-E")
  public ImageResponse createImage(@RequestBody DalleImageRequest imageRequest) {
    var user = imageRequest.user();
    var text = imageRequest.text();
    var imageSize = imageRequest.imageSize();

    var parameter = new DalleEngineParameter(imageSize);
    var price = price(parameter);

    var invoice = new Invoice(user, text, price, LocalDateTime.now());
    invoiceRepository.save(invoice);

    var imagePath = Engines.dall_e(text, imageSize.size(), imageSize.size());

    return new ImageResponse(imagePath);
  }

  @PostMapping(path="/sd", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @Transactional
  @Operation(summary = "create an image using stable diffusion")
  public ImageResponse createImage(@RequestBody SDImageRequest imageRequest) {
    var user = imageRequest.user();
    var text = imageRequest.text();
    var imageSize = imageRequest.imageSize();
    var plms = imageRequest.plms();

    var parameter = new SDEngineParameter(imageSize, plms);
    var price = price(parameter);

    var invoice = new Invoice(user, text, price, LocalDateTime.now());
    invoiceRepository.save(invoice);

    var imagePath = Engines.stable_diffusion(text, imageSize.size(), imageSize.size(), plms);

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