/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bsager.syscolegio.controller;

import com.bsager.syscolegio.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bsager.syscolegio.model.Matricula;
import com.bsager.syscolegio.model.Concepto;
import com.bsager.syscolegio.model.Pago;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author
 */
public class PagoJpaController implements Serializable {

    public PagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Matricula codiMatr = pago.getCodiMatr();
            if (codiMatr != null) {
                codiMatr = em.getReference(codiMatr.getClass(), codiMatr.getCodiMatr());
                pago.setCodiMatr(codiMatr);
            }
            Concepto codiConc = pago.getCodiConc();
            if (codiConc != null) {
                codiConc = em.getReference(codiConc.getClass(), codiConc.getCodiConc());
                pago.setCodiConc(codiConc);
            }
            em.persist(pago);
            if (codiMatr != null) {
                codiMatr.getPagoCollection().add(pago);
                codiMatr = em.merge(codiMatr);
            }
            if (codiConc != null) {
                codiConc.getPagoCollection().add(pago);
                codiConc = em.merge(codiConc);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getCodiPago());
            Matricula codiMatrOld = persistentPago.getCodiMatr();
            Matricula codiMatrNew = pago.getCodiMatr();
            Concepto codiConcOld = persistentPago.getCodiConc();
            Concepto codiConcNew = pago.getCodiConc();
            if (codiMatrNew != null) {
                codiMatrNew = em.getReference(codiMatrNew.getClass(), codiMatrNew.getCodiMatr());
                pago.setCodiMatr(codiMatrNew);
            }
            if (codiConcNew != null) {
                codiConcNew = em.getReference(codiConcNew.getClass(), codiConcNew.getCodiConc());
                pago.setCodiConc(codiConcNew);
            }
            pago = em.merge(pago);
            if (codiMatrOld != null && !codiMatrOld.equals(codiMatrNew)) {
                codiMatrOld.getPagoCollection().remove(pago);
                codiMatrOld = em.merge(codiMatrOld);
            }
            if (codiMatrNew != null && !codiMatrNew.equals(codiMatrOld)) {
                codiMatrNew.getPagoCollection().add(pago);
                codiMatrNew = em.merge(codiMatrNew);
            }
            if (codiConcOld != null && !codiConcOld.equals(codiConcNew)) {
                codiConcOld.getPagoCollection().remove(pago);
                codiConcOld = em.merge(codiConcOld);
            }
            if (codiConcNew != null && !codiConcNew.equals(codiConcOld)) {
                codiConcNew.getPagoCollection().add(pago);
                codiConcNew = em.merge(codiConcNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pago.getCodiPago();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getCodiPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            Matricula codiMatr = pago.getCodiMatr();
            if (codiMatr != null) {
                codiMatr.getPagoCollection().remove(pago);
                codiMatr = em.merge(codiMatr);
            }
            Concepto codiConc = pago.getCodiConc();
            if (codiConc != null) {
                codiConc.getPagoCollection().remove(pago);
                codiConc = em.merge(codiConc);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pago findPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
