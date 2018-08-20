/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.digitalPlatoon.invoiceServices.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.digitalPlatoon.invoiceServices.db.Invoice;
import za.co.digitalPlatoon.invoiceServices.db.LineItems;

import java.util.List;

/**
 *
 * @author Mabelesk
 */
public interface LineItemsRepo extends JpaRepository<LineItems, Long> {
    List<LineItems> findByInvoiceId(Invoice invoice);
}
