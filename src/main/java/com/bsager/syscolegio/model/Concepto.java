/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bsager.syscolegio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author
 */
@Entity
@Table(name = "concepto", catalog = "colegio", schema = "")
@NamedQueries({
    @NamedQuery(name = "Concepto.findAll", query = "SELECT c FROM Concepto c"),
    @NamedQuery(name = "Concepto.findByCodiConc", query = "SELECT c FROM Concepto c WHERE c.codiConc = :codiConc"),
    @NamedQuery(name = "Concepto.findByNombConc", query = "SELECT c FROM Concepto c WHERE c.nombConc = :nombConc"),
    @NamedQuery(name = "Concepto.findByMontoConc", query = "SELECT c FROM Concepto c WHERE c.montoConc = :montoConc")})
public class Concepto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codiConc", nullable = false)
    private Short codiConc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombConc", nullable = false, length = 30)
    private String nombConc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "montoConc", nullable = false, precision = 8, scale = 2)
    private BigDecimal montoConc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codiConc")
    private Collection<Pago> pagoCollection;

    public Concepto() {
    }

    public Concepto(Short codiConc) {
        this.codiConc = codiConc;
    }

    public Concepto(Short codiConc, String nombConc, BigDecimal montoConc) {
        this.codiConc = codiConc;
        this.nombConc = nombConc;
        this.montoConc = montoConc;
    }

    public Short getCodiConc() {
        return codiConc;
    }

    public void setCodiConc(Short codiConc) {
        this.codiConc = codiConc;
    }

    public String getNombConc() {
        return nombConc;
    }

    public void setNombConc(String nombConc) {
        this.nombConc = nombConc;
    }

    public BigDecimal getMontoConc() {
        return montoConc;
    }

    public void setMontoConc(BigDecimal montoConc) {
        this.montoConc = montoConc;
    }

    public Collection<Pago> getPagoCollection() {
        return pagoCollection;
    }

    public void setPagoCollection(Collection<Pago> pagoCollection) {
        this.pagoCollection = pagoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiConc != null ? codiConc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Concepto)) {
            return false;
        }
        Concepto other = (Concepto) object;
        if ((this.codiConc == null && other.codiConc != null) || (this.codiConc != null && !this.codiConc.equals(other.codiConc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsager.syscolegio.model.Concepto[ codiConc=" + codiConc + " ]";
    }

}
