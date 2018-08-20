package za.co.digitalPlatoon.invoiceServices.db;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Mabelesk
 */
@Entity
@Table(name = "LINE_ITEMS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LineItems.findAll", query = "SELECT l FROM LineItems l")
    , @NamedQuery(name = "LineItems.findById", query = "SELECT l FROM LineItems l WHERE l.id = :id")
    , @NamedQuery(name = "LineItems.findByQuantity", query = "SELECT l FROM LineItems l WHERE l.quantity = :quantity")
    , @NamedQuery(name = "LineItems.findByDescription", query = "SELECT l FROM LineItems l WHERE l.description = :description")
    , @NamedQuery(name = "LineItems.findByUnitPrice", query = "SELECT l FROM LineItems l WHERE l.unitPrice = :unitPrice")})
public class LineItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "QUANTITY")
    private Long quantity;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;
    @JoinColumn(name = "INVOICE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Invoice invoiceId;

    @Transient
    private BigDecimal lineItemsTotal;

    public LineItems() {
    }

    public LineItems(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Invoice getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Invoice invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getLineItemsTotal() {
        return lineItemsTotal;
    }

    public void setLineItemsTotal(BigDecimal lineItemsTotal) {
        this.lineItemsTotal = lineItemsTotal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LineItems)) {
            return false;
        }
        LineItems other = (LineItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "za.co.digitalPlatoon.invoiceServices.db.LineItems[ id=" + id + " ]";
    }


}
