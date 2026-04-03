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
@Table(name = "nivel", catalog = "colegio", schema = "")
@NamedQueries({
    @NamedQuery(name = "Nivel.findAll", query = "SELECT n FROM Nivel n"),
    @NamedQuery(name = "Nivel.findByCodiNive", query = "SELECT n FROM Nivel n WHERE n.codiNive = :codiNive"),
    @NamedQuery(name = "Nivel.findByNombNive", query = "SELECT n FROM Nivel n WHERE n.nombNive = :nombNive")})
public class Nivel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codiNive", nullable = false)
    private Short codiNive;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombNive", nullable = false, length = 30)
    private String nombNive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codiNive")
    private Collection<Grado> gradoCollection;

    public Nivel() {
    }

    public Nivel(Short codiNive) {
        this.codiNive = codiNive;
    }

    public Nivel(Short codiNive, String nombNive) {
        this.codiNive = codiNive;
        this.nombNive = nombNive;
    }

    public Short getCodiNive() {
        return codiNive;
    }

    public void setCodiNive(Short codiNive) {
        this.codiNive = codiNive;
    }

    public String getNombNive() {
        return nombNive;
    }

    public void setNombNive(String nombNive) {
        this.nombNive = nombNive;
    }

    public Collection<Grado> getGradoCollection() {
        return gradoCollection;
    }

    public void setGradoCollection(Collection<Grado> gradoCollection) {
        this.gradoCollection = gradoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiNive != null ? codiNive.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nivel)) {
            return false;
        }
        Nivel other = (Nivel) object;
        if ((this.codiNive == null && other.codiNive != null) || (this.codiNive != null && !this.codiNive.equals(other.codiNive))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsager.syscolegio.model.Nivel[ codiNive=" + codiNive + " ]";
    }

}
