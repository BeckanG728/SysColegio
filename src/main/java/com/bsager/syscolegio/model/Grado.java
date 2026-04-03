/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bsager.syscolegio.model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author
 */
@Entity
@Table(name = "grado", catalog = "colegio", schema = "")
@NamedQueries({
    @NamedQuery(name = "Grado.findAll", query = "SELECT g FROM Grado g"),
    @NamedQuery(name = "Grado.findByCodiGrad", query = "SELECT g FROM Grado g WHERE g.codiGrad = :codiGrad"),
    @NamedQuery(name = "Grado.findByNombGrad", query = "SELECT g FROM Grado g WHERE g.nombGrad = :nombGrad")})
public class Grado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codiGrad", nullable = false)
    private Short codiGrad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombGrad", nullable = false, length = 30)
    private String nombGrad;
    @JoinColumn(name = "codiNive", referencedColumnName = "codiNive", nullable = false)
    @ManyToOne(optional = false)
    private Nivel codiNive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codiGrad")
    private Collection<Aula> aulaCollection;

    public Grado() {
    }

    public Grado(Short codiGrad) {
        this.codiGrad = codiGrad;
    }

    public Grado(Short codiGrad, String nombGrad) {
        this.codiGrad = codiGrad;
        this.nombGrad = nombGrad;
    }

    public Short getCodiGrad() {
        return codiGrad;
    }

    public void setCodiGrad(Short codiGrad) {
        this.codiGrad = codiGrad;
    }

    public String getNombGrad() {
        return nombGrad;
    }

    public void setNombGrad(String nombGrad) {
        this.nombGrad = nombGrad;
    }

    public Nivel getCodiNive() {
        return codiNive;
    }

    public void setCodiNive(Nivel codiNive) {
        this.codiNive = codiNive;
    }

    public Collection<Aula> getAulaCollection() {
        return aulaCollection;
    }

    public void setAulaCollection(Collection<Aula> aulaCollection) {
        this.aulaCollection = aulaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiGrad != null ? codiGrad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grado)) {
            return false;
        }
        Grado other = (Grado) object;
        if ((this.codiGrad == null && other.codiGrad != null) || (this.codiGrad != null && !this.codiGrad.equals(other.codiGrad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsager.syscolegio.model.Grado[ codiGrad=" + codiGrad + " ]";
    }

}
