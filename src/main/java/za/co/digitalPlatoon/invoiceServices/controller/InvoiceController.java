package za.co.digitalPlatoon.invoiceServices.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import za.co.digitalPlatoon.invoiceServices.db.Invoice;
import za.co.digitalPlatoon.invoiceServices.db.LineItems;
import za.co.digitalPlatoon.invoiceServices.repo.InvoiceRepo;
import za.co.digitalPlatoon.invoiceServices.repo.LineItemsRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class InvoiceController {
    private static final Logger logger = Logger.getLogger(InvoiceController.class);
    @Autowired
    private InvoiceRepo invoiceRepo;
    @Autowired
    private LineItemsRepo lineItemsRepo;

    //add invoice
    @RequestMapping(value = "invoices", method = RequestMethod.POST)
    public ResponseEntity<Invoice> addInvoice(
        @RequestBody Invoice invoice,
        UriComponentsBuilder ucBuilder) {
        try {

            logger.info(
                "\n===============================================================\n "
                    + "We are doing invoice save for invoice with client "
                    + " [" + invoice.getClient() + "] AT [" + new Date() + "]"
                    + "\n=============================================================== ");

            invoice = invoiceRepo.save(invoice);

            logger.info(
                "\n===============================================================\n "
                    + "Invoice for client "
                    + " [" + invoice.getClient() + "] saved successful AT [" + new Date() + "]"
                    + "\n=============================================================== ");

        } catch (Exception e) {
            logger.info(
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee\n"
                    + "Exception caught : " + e.getMessage()
                    + "\neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee ");
            e.printStackTrace();
        }
        return new ResponseEntity<>(invoice, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoicesList = new ArrayList<>();
        try {
            invoicesList = invoiceRepo.findAll();
            if (invoicesList.isEmpty()) {
                logger.info(
                    "\n===============================================================\n"
                        + "NO Invoice(s) FOUND Query returned [" + invoicesList.size() + "].\n"
                        + "\n===============================================================");
            } else {
                logger.info(
                    "\n===============================================================\n"
                        + "Invoice(s) FOUND Query returned [" + invoicesList.size() + "].\n"
                        + "\n===============================================================");

                for (Invoice invoice : invoicesList) {
                    BigDecimal subTotal = BigDecimal.ZERO;
                    BigDecimal vat = BigDecimal.ZERO;
                    BigDecimal total = BigDecimal.ZERO;
                    BigDecimal lineItemsTotal = BigDecimal.ZERO;

                    List<LineItems> lineItemsList = new ArrayList<>();
                    lineItemsList = lineItemsRepo.findByInvoiceId(invoice);

                    logger.info(
                        "\n===============================================================\n"
                            + "Invoice [" + invoice.getId() + "] have [" + lineItemsList.size() + "] line items.\n"
                            + "\n===============================================================");

                    for (LineItems lineItems : lineItemsList) {
                        //lineItem = quantity * unitPrice
                        Long quantity = lineItems.getQuantity();
                        BigDecimal unitPrice = lineItems.getUnitPrice();

                        BigDecimal lineTotal = new BigDecimal(quantity).multiply(unitPrice);
                        lineItems.setLineItemsTotal(lineTotal);
                        subTotal = subTotal.add(lineItems.getLineItemsTotal());
                    }

                    subTotal.setScale(2, RoundingMode.HALF_UP);
                    invoice.setSubTotal(subTotal);

                    vat = subTotal.multiply(new BigDecimal(invoice.getVatRate()).divide(new BigDecimal(100)));
                    vat.setScale(2, RoundingMode.HALF_UP);
                    invoice.setVat(vat);

                    total = subTotal.add(vat);
                    total.setScale(2, RoundingMode.HALF_UP);
                    invoice.setTotal(total);
                }
            }
        } catch (Exception e) {
            logger.info(
                "\neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee\n"
                    + "Exception caught." + e.getMessage()
                    + "\neeeeeeedeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee ");
            e.printStackTrace();
        }
        return new ResponseEntity<>(invoicesList,
            HttpStatus.OK);
    }

    @RequestMapping(value = "invoice/{invoiceId}", method = RequestMethod.GET)
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable String invoiceId) {
        Invoice invoice = new Invoice();
        try {
            invoice = invoiceRepo.findOne(new Long(invoiceId));
            if (invoice == null) {
                logger.info(
                    "\n===============================================================\n"
                        + "NO Invoice FOUND for invoice with ID [" + invoiceId + "].\n"
                        + "\n===============================================================");
            } else {
                logger.info(
                    "\n===============================================================\n"
                        + "Invoice FOUND for invoice with ID [" + invoiceId + "].\n"
                        + "\n===============================================================");

                BigDecimal subTotal = BigDecimal.ZERO;
                BigDecimal vat = BigDecimal.ZERO;
                BigDecimal total = BigDecimal.ZERO;

                List<LineItems> lineItemsList = new ArrayList<>();
                lineItemsList = lineItemsRepo.findByInvoiceId(invoice);

                for (LineItems lineItems : lineItemsList) {
                    Long quantity = lineItems.getQuantity();
                    BigDecimal unitPrice = lineItems.getUnitPrice();

                    BigDecimal lineTotal = new BigDecimal(quantity).multiply(unitPrice);
                    lineItems.setLineItemsTotal(lineTotal);
                    subTotal = subTotal.add(lineItems.getLineItemsTotal());
                }

                subTotal.setScale(2, RoundingMode.HALF_UP);
                invoice.setSubTotal(subTotal);

                vat = subTotal.multiply(new BigDecimal(invoice.getVatRate()).divide(new BigDecimal(100)));
                vat.setScale(2, RoundingMode.HALF_UP);
                invoice.setVat(vat);
                total = subTotal.add(vat);
                invoice.setTotal(total);
                total.setScale(2, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            logger.info(
                "\neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee\n"
                    + "Exception caught." + e.getMessage()
                    + "\neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee ");
            e.printStackTrace();
        }
        return new ResponseEntity<>(invoice,
            HttpStatus.OK);
    }

}
