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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author
 */
@Entity
@Table(name = "alumno", catalog = "colegio", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"numeDocu"})})
@NamedQueries({
    @NamedQuery(name = "Alumno.findAll", query = "SELECT a FROM Alumno a"),
    @NamedQuery(name = "Alumno.findByCodiAlum", query = "SELECT a FROM Alumno a WHERE a.codiAlum = :codiAlum"),
    @NamedQuery(name = "Alumno.findByNumeDocu", query = "SELECT a FROM Alumno a WHERE a.numeDocu = :numeDocu"),
    @NamedQuery(name = "Alumno.findByAppaAlum", query = "SELECT a FROM Alumno a WHERE a.appaAlum = :appaAlum"),
    @NamedQuery(name = "Alumno.findByApmaAlum", query = "SELECT a FROM Alumno a WHERE a.apmaAlum = :apmaAlum"),
    @NamedQuery(name = "Alumno.findByNombAlum", query = "SELECT a FROM Alumno a WHERE a.nombAlum = :nombAlum"),
    @NamedQuery(name = "Alumno.findByCorrAlum", query = "SELECT a FROM Alumno a WHERE a.corrAlum = :corrAlum"),
    @NamedQuery(name = "Alumno.findByCelAlum", query = "SELECT a FROM Alumno a WHERE a.celAlum = :celAlum"),
    @NamedQuery(name = "Alumno.findBySexoAlum", query = "SELECT a FROM Alumno a WHERE a.sexoAlum = :sexoAlum"),
    @NamedQuery(name = "Alumno.findByActiAlum", query = "SELECT a FROM Alumno a WHERE a.actiAlum = :actiAlum")})
public class Alumno implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codiAlum", nullable = false)
    private Integer codiAlum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "numeDocu", nullable = false, length = 8)
    private String numeDocu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "appaAlum", nullable = false, length = 50)
    private String appaAlum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "apmaAlum", nullable = false, length = 50)
    private String apmaAlum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombAlum", nullable = false, length = 50)
    private String nombAlum;
    @Size(max = 60)
    @Column(name = "corrAlum", length = 60)
    private String corrAlum;
    @Size(max = 9)
    @Column(name = "celAlum", length = 9)
    private String celAlum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sexoAlum", nullable = false)
    private Character sexoAlum;
    @Column(name = "actiAlum")
    private Boolean actiAlum;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codiAlum")
    private Collection<Matricula> matriculaCollection;

    public Alumno() {
    }

    public Alumno(Integer codiAlum) {
        this.codiAlum = codiAlum;
    }

    public Alumno(Integer codiAlum, String numeDocu, String appaAlum, String apmaAlum, String nombAlum, Character sexoAlum) {
        this.codiAlum = codiAlum;
        this.numeDocu = numeDocu;
        this.appaAlum = appaAlum;
        this.apmaAlum = apmaAlum;
        this.nombAlum = nombAlum;
        this.sexoAlum = sexoAlum;
    }

    public Integer getCodiAlum() {
        return codiAlum;
    }

    public void setCodiAlum(Integer codiAlum) {
        this.codiAlum = codiAlum;
    }

    public String getNumeDocu() {
        return numeDocu;
    }

    public void setNumeDocu(String numeDocu) {
        this.numeDocu = numeDocu;
    }

    public String getAppaAlum() {
        return appaAlum;
    }

    public void setAppaAlum(String appaAlum) {
        this.appaAlum = appaAlum;
    }

    public String getApmaAlum() {
        return apmaAlum;
    }

    public void setApmaAlum(String apmaAlum) {
        this.apmaAlum = apmaAlum;
    }

    public String getNombAlum() {
        return nombAlum;
    }

    public void setNombAlum(String nombAlum) {
        this.nombAlum = nombAlum;
    }

    public String getCorrAlum() {
        return corrAlum;
    }

    public void setCorrAlum(String corrAlum) {
        this.corrAlum = corrAlum;
    }

    public String getCelAlum() {
        return celAlum;
    }

    public void setCelAlum(String celAlum) {
        this.celAlum = celAlum;
    }

    public Character getSexoAlum() {
        return sexoAlum;
    }

    public void setSexoAlum(Character sexoAlum) {
        this.sexoAlum = sexoAlum;
    }

    public Boolean getActiAlum() {
        return actiAlum;
    }

    public void setActiAlum(Boolean actiAlum) {
        this.actiAlum = actiAlum;
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
        hash += (codiAlum != null ? codiAlum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumno)) {
            return false;
        }
        Alumno other = (Alumno) object;
        if ((this.codiAlum == null && other.codiAlum != null) || (this.codiAlum != null && !this.codiAlum.equals(other.codiAlum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsager.syscolegio.model.Alumno[ codiAlum=" + codiAlum + " ]";
    }

}
