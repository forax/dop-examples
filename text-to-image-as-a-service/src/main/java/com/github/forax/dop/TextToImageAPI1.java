package com.github.forax.dop;

import com.github.forax.dop.db.Invoice;
import com.github.forax.dop.db.InvoiceRepository;

import com.github.forax.dop.Data1.*;

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
/*
@RestController
@RequestMapping("/api")
public class TextToImageAPI1 {
  private final InvoiceRepository invoiceRepository;

  public TextToImageAPI1(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @PostMapping(path="/dalle", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @Transactional
  @Operation(summary = "create an image using Dall-E")
  public ImageResponse createImage(@RequestBody DalleImageRequest imageRequest) {
    var user = imageRequest.user();
    var text = imageRequest.text();
    var imageSize = imageRequest.imageSize();

    var price = 1_000;

    var invoice = new Invoice(user, text, price, LocalDateTime.now());
    invoiceRepository.save(invoice);

    var imageURI = Engines.dall_e(text, imageSize.size(), imageSize.size());

    return new ImageResponse(imageURI);
  }

  @PostMapping(path="/sd", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @Transactional
  @Operation(summary = "create an image using stable diffusion")
  public ImageResponse createImage(@RequestBody SDImageRequest imageRequest) {
    var user = imageRequest.user();
    var text = imageRequest.text();
    var imageSize = imageRequest.imageSize();
    var plms = imageRequest.plms();

    var price = (plms? 25 : 0) + switch(imageSize) {
      case small -> 125;
      case medium -> 400;
      case big -> 800;
    };

    var invoice = new Invoice(user, text, price, LocalDateTime.now());
    invoiceRepository.save(invoice);

    var imageURI = Engines.stable_diffusion(text, imageSize.size(), imageSize.size(), plms);

    return new ImageResponse(imageURI);
  }


  @GetMapping(path= "/invoice", produces = APPLICATION_JSON_VALUE)
  @Operation(summary = "get all invoices")
  public List<InvoiceResponse> getAllInvoices() {
    return invoiceRepository.findAll().stream()
        .map(invoice -> new InvoiceResponse(invoice.userName, invoice.message, invoice.price, invoice.dateTime))
        .toList();
  }
}*/
