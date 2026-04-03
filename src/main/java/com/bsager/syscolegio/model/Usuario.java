/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bsager.syscolegio.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author
 */
@Entity
@Table(name = "usuario", catalog = "colegio", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"logiUsua"})})
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByCodiUsua", query = "SELECT u FROM Usuario u WHERE u.codiUsua = :codiUsua"),
    @NamedQuery(name = "Usuario.findByLogiUsua", query = "SELECT u FROM Usuario u WHERE u.logiUsua = :logiUsua"),
    @NamedQuery(name = "Usuario.findByPassUsua", query = "SELECT u FROM Usuario u WHERE u.passUsua = :passUsua"),
    @NamedQuery(name = "Usuario.findByActiUsua", query = "SELECT u FROM Usuario u WHERE u.actiUsua = :actiUsua")})
public class Usuario implements Serializable {

    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codiUsua", nullable = false)
    private Integer codiUsua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "logiUsua", nullable = false, length = 30)
    private String logiUsua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "passUsua", nullable = false, length = 64)
    private String passUsua;
    @Column(name = "actiUsua")
    private Boolean actiUsua;

    public Usuario() {
    }

    public Usuario(Integer codiUsua) {
        this.codiUsua = codiUsua;
    }

    public Usuario(Integer codiUsua, String logiUsua, String passUsua) {
        this.codiUsua = codiUsua;
        this.logiUsua = logiUsua;
        this.passUsua = passUsua;
    }

    public Integer getCodiUsua() {
        return codiUsua;
    }

    public void setCodiUsua(Integer codiUsua) {
        this.codiUsua = codiUsua;
    }

    public String getLogiUsua() {
        return logiUsua;
    }

    public void setLogiUsua(String logiUsua) {
        this.logiUsua = logiUsua;
    }

    public String getPassUsua() {
        return passUsua;
    }

    public void setPassUsua(String passUsua) {
        this.passUsua = passUsua;
    }

    public Boolean getActiUsua() {
        return actiUsua;
    }

    public void setActiUsua(Boolean actiUsua) {
        this.actiUsua = actiUsua;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiUsua != null ? codiUsua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.codiUsua == null && other.codiUsua != null) || (this.codiUsua != null && !this.codiUsua.equals(other.codiUsua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsager.syscolegio.model.Usuario[ codiUsua=" + codiUsua + " ]";
    }

}
