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
import javax.validation.constraints.NotNull;

/**
 * @author
 */
@Entity
@Table(name = "aula", catalog = "colegio", schema = "")
@NamedQueries({
    @NamedQuery(name = "Aula.findAll", query = "SELECT a FROM Aula a"),
    @NamedQuery(name = "Aula.findByCodiAula", query = "SELECT a FROM Aula a WHERE a.codiAula = :codiAula"),
    @NamedQuery(name = "Aula.findBySeccion", query = "SELECT a FROM Aula a WHERE a.seccion = :seccion"),
    @NamedQuery(name = "Aula.findByAnio", query = "SELECT a FROM Aula a WHERE a.anio = :anio")})
public class Aula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codiAula", nullable = false)
    private Integer codiAula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seccion", nullable = false)
    private Character seccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date anio;
    @JoinColumn(name = "codiGrad", referencedColumnName = "codiGrad", nullable = false)
    @ManyToOne(optional = false)
    private Grado codiGrad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codiAula")
    private Collection<Matricula> matriculaCollection;

    public Aula() {
    }

    public Aula(Integer codiAula) {
        this.codiAula = codiAula;
    }

    public Aula(Integer codiAula, Character seccion, Date anio) {
        this.codiAula = codiAula;
        this.seccion = seccion;
        this.anio = anio;
    }

    public Integer getCodiAula() {
        return codiAula;
    }

    public void setCodiAula(Integer codiAula) {
        this.codiAula = codiAula;
    }

    public Character getSeccion() {
        return seccion;
    }

    public void setSeccion(Character seccion) {
        this.seccion = seccion;
    }

    public Date getAnio() {
        return anio;
    }

    public void setAnio(Date anio) {
        this.anio = anio;
    }

    public Grado getCodiGrad() {
        return codiGrad;
    }

    public void setCodiGrad(Grado codiGrad) {
        this.codiGrad = codiGrad;
    }

    public Collection<Matricula> getMatriculaCollection() {
        return matriculaCollection;
    }

    public void setMatriculaCollection(Collection<Matricula> matriculaCollection) {
        this.matriculaCollection = matriculaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiAula != null ? codiAula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aula)) {
            return false;
        }
        Aula other = (Aula) object;
        if ((this.codiAula == null && other.codiAula != null) || (this.codiAula != null && !this.codiAula.equals(other.codiAula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsager.syscolegio.model.Aula[ codiAula=" + codiAula + " ]";
    }

}
