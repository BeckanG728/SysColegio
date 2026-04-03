/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bsager.syscolegio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * @author
 */
@Entity
@Table(name = "pago", catalog = "colegio", schema = "")
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p"),
    @NamedQuery(name = "Pago.findByCodiPago", query = "SELECT p FROM Pago p WHERE p.codiPago = :codiPago"),
    @NamedQuery(name = "Pago.findByMontPago", query = "SELECT p FROM Pago p WHERE p.montPago = :montPago"),
    @NamedQuery(name = "Pago.findByFechPago", query = "SELECT p FROM Pago p WHERE p.fechPago = :fechPago"),
    @NamedQuery(name = "Pago.findByEstadoPago", query = "SELECT p FROM Pago p WHERE p.estadoPago = :estadoPago")})
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codiPago", nullable = false)
    private Integer codiPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "montPago", nullable = false, precision = 8, scale = 2)
    private BigDecimal montPago;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechPago", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechPago;
    @Column(name = "estadoPago")
    private Character estadoPago;
    @JoinColumn(name = "codiMatr", referencedColumnName = "codiMatr", nullable = false)
    @ManyToOne(optional = false)
    private Matricula codiMatr;
    @JoinColumn(name = "codiConc", referencedColumnName = "codiConc", nullable = false)
    @ManyToOne(optional = false)
    private Concepto codiConc;

    public Pago() {
    }

    public Pago(Integer codiPago) {
        this.codiPago = codiPago;
    }

    public Pago(Integer codiPago, BigDecimal montPago, Date fechPago) {
        this.codiPago = codiPago;
        this.montPago = montPago;
        this.fechPago = fechPago;
    }

    public Integer getCodiPago() {
        return codiPago;
    }

    public void setCodiPago(Integer codiPago) {
        this.codiPago = codiPago;
    }

    public BigDecimal getMontPago() {
        return montPago;
    }

    public void setMontPago(BigDecimal montPago) {
        this.montPago = montPago;
    }

    public Date getFechPago() {
        return fechPago;
    }

    public void setFechPago(Date fechPago) {
        this.fechPago = fechPago;
    }

    public Character getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(Character estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Matricula getCodiMatr() {
        return codiMatr;
    }

    public void setCodiMatr(Matricula codiMatr) {
        this.codiMatr = codiMatr;
    }

    public Concepto getCodiConc() {
        return codiConc;
    }

    public void setCodiConc(Concepto codiConc) {
        this.codiConc = codiConc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiPago != null ? codiPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.codiPago == null && other.codiPago != null) || (this.codiPago != null && !this.codiPago.equals(other.codiPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsager.syscolegio.model.Pago[ codiPago=" + codiPago + " ]";
    }

}
