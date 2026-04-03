/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bsager.syscolegio.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author
 */
@Entity
@Table(name = "matricula", catalog = "colegio", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"codiAlum", "codiAula"})})
@NamedQueries({
    @NamedQuery(name = "Matricula.findAll", query = "SELECT m FROM Matricula m"),
    @NamedQuery(name = "Matricula.findByCodiMatr", query = "SELECT m FROM Matricula m WHERE m.codiMatr = :codiMatr"),
    @NamedQuery(name = "Matricula.findByFechMatr", query = "SELECT m FROM Matricula m WHERE m.fechMatr = :fechMatr"),
    @NamedQuery(name = "Matricula.findByEstadoMatr", query = "SELECT m FROM Matricula m WHERE m.estadoMatr = :estadoMatr")})
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codiMatr", nullable = false)
    private Integer codiMatr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechMatr", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechMatr;
    @Column(name = "estadoMatr")
    private Character estadoMatr;
    @JoinColumn(name = "codiAlum", referencedColumnName = "codiAlum", nullable = false)
    @ManyToOne(optional = false)
    private Alumno codiAlum;
    @JoinColumn(name = "codiAula", referencedColumnName = "codiAula", nullable = false)
    @ManyToOne(optional = false)
    private Aula codiAula;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codiMatr")
    private Collection<Pago> pagoCollection;

    public Matricula() {
    }

    public Matricula(Integer codiMatr) {
        this.codiMatr = codiMatr;
    }

    public Matricula(Integer codiMatr, Date fechMatr) {
        this.codiMatr = codiMatr;
        this.fechMatr = fechMatr;
    }

    public Integer getCodiMatr() {
        return codiMatr;
    }

    public void setCodiMatr(Integer codiMatr) {
        this.codiMatr = codiMatr;
    }

    public Date getFechMatr() {
        return fechMatr;
    }

    public void setFechMatr(Date fechMatr) {
        this.fechMatr = fechMatr;
    }

    public Character getEstadoMatr() {
        return estadoMatr;
    }

    public void setEstadoMatr(Character estadoMatr) {
        this.estadoMatr = estadoMatr;
    }

    public Alumno getCodiAlum() {
        return codiAlum;
    }

    public void setCodiAlum(Alumno codiAlum) {
        this.codiAlum = codiAlum;
    }

    public Aula getCodiAula() {
        return codiAula;
    }

    public void setCodiAula(Aula codiAula) {
        this.codiAula = codiAula;
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
        hash += (codiMatr != null ? codiMatr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.codiMatr == null && other.codiMatr != null) || (this.codiMatr != null && !this.codiMatr.equals(other.codiMatr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsager.syscolegio.model.Matricula[ codiMatr=" + codiMatr + " ]";
    }

}
