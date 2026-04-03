/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bsager.syscolegio.controller;

import com.bsager.syscolegio.controller.exceptions.IllegalOrphanException;
import com.bsager.syscolegio.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bsager.syscolegio.model.Alumno;
import com.bsager.syscolegio.model.Aula;
import com.bsager.syscolegio.model.Matricula;
import com.bsager.syscolegio.model.Pago;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author
 */
public class MatriculaJpaController implements Serializable {

    public MatriculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Matricula matricula) {
        if (matricula.getPagoCollection() == null) {
            matricula.setPagoCollection(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno codiAlum = matricula.getCodiAlum();
            if (codiAlum != null) {
                codiAlum = em.getReference(codiAlum.getClass(), codiAlum.getCodiAlum());
                matricula.setCodiAlum(codiAlum);
            }
            Aula codiAula = matricula.getCodiAula();
            if (codiAula != null) {
                codiAula = em.getReference(codiAula.getClass(), codiAula.getCodiAula());
                matricula.setCodiAula(codiAula);
            }
            Collection<Pago> attachedPagoCollection = new ArrayList<Pago>();
            for (Pago pagoCollectionPagoToAttach : matricula.getPagoCollection()) {
                pagoCollectionPagoToAttach = em.getReference(pagoCollectionPagoToAttach.getClass(), pagoCollectionPagoToAttach.getCodiPago());
                attachedPagoCollection.add(pagoCollectionPagoToAttach);
            }
            matricula.setPagoCollection(attachedPagoCollection);
            em.persist(matricula);
            if (codiAlum != null) {
                codiAlum.getMatriculaCollection().add(matricula);
                codiAlum = em.merge(codiAlum);
            }
            if (codiAula != null) {
                codiAula.getMatriculaCollection().add(matricula);
                codiAula = em.merge(codiAula);
            }
            for (Pago pagoCollectionPago : matricula.getPagoCollection()) {
                Matricula oldCodiMatrOfPagoCollectionPago = pagoCollectionPago.getCodiMatr();
                pagoCollectionPago.setCodiMatr(matricula);
                pagoCollectionPago = em.merge(pagoCollectionPago);
                if (oldCodiMatrOfPagoCollectionPago != null) {
                    oldCodiMatrOfPagoCollectionPago.getPagoCollection().remove(pagoCollectionPago);
                    oldCodiMatrOfPagoCollectionPago = em.merge(oldCodiMatrOfPagoCollectionPago);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Matricula matricula) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Matricula persistentMatricula = em.find(Matricula.class, matricula.getCodiMatr());
            Alumno codiAlumOld = persistentMatricula.getCodiAlum();
            Alumno codiAlumNew = matricula.getCodiAlum();
            Aula codiAulaOld = persistentMatricula.getCodiAula();
            Aula codiAulaNew = matricula.getCodiAula();
            Collection<Pago> pagoCollectionOld = persistentMatricula.getPagoCollection();
            Collection<Pago> pagoCollectionNew = matricula.getPagoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pago pagoCollectionOldPago : pagoCollectionOld) {
                if (!pagoCollectionNew.contains(pagoCollectionOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoCollectionOldPago + " since its codiMatr field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codiAlumNew != null) {
                codiAlumNew = em.getReference(codiAlumNew.getClass(), codiAlumNew.getCodiAlum());
                matricula.setCodiAlum(codiAlumNew);
            }
            if (codiAulaNew != null) {
                codiAulaNew = em.getReference(codiAulaNew.getClass(), codiAulaNew.getCodiAula());
                matricula.setCodiAula(codiAulaNew);
            }
            Collection<Pago> attachedPagoCollectionNew = new ArrayList<Pago>();
            for (Pago pagoCollectionNewPagoToAttach : pagoCollectionNew) {
                pagoCollectionNewPagoToAttach = em.getReference(pagoCollectionNewPagoToAttach.getClass(), pagoCollectionNewPagoToAttach.getCodiPago());
                attachedPagoCollectionNew.add(pagoCollectionNewPagoToAttach);
            }
            pagoCollectionNew = attachedPagoCollectionNew;
            matricula.setPagoCollection(pagoCollectionNew);
            matricula = em.merge(matricula);
            if (codiAlumOld != null && !codiAlumOld.equals(codiAlumNew)) {
                codiAlumOld.getMatriculaCollection().remove(matricula);
                codiAlumOld = em.merge(codiAlumOld);
            }
            if (codiAlumNew != null && !codiAlumNew.equals(codiAlumOld)) {
                codiAlumNew.getMatriculaCollection().add(matricula);
                codiAlumNew = em.merge(codiAlumNew);
            }
            if (codiAulaOld != null && !codiAulaOld.equals(codiAulaNew)) {
                codiAulaOld.getMatriculaCollection().remove(matricula);
                codiAulaOld = em.merge(codiAulaOld);
            }
            if (codiAulaNew != null && !codiAulaNew.equals(codiAulaOld)) {
                codiAulaNew.getMatriculaCollection().add(matricula);
                codiAulaNew = em.merge(codiAulaNew);
            }
            for (Pago pagoCollectionNewPago : pagoCollectionNew) {
                if (!pagoCollectionOld.contains(pagoCollectionNewPago)) {
                    Matricula oldCodiMatrOfPagoCollectionNewPago = pagoCollectionNewPago.getCodiMatr();
                    pagoCollectionNewPago.setCodiMatr(matricula);
                    pagoCollectionNewPago = em.merge(pagoCollectionNewPago);
                    if (oldCodiMatrOfPagoCollectionNewPago != null && !oldCodiMatrOfPagoCollectionNewPago.equals(matricula)) {
                        oldCodiMatrOfPagoCollectionNewPago.getPagoCollection().remove(pagoCollectionNewPago);
                        oldCodiMatrOfPagoCollectionNewPago = em.merge(oldCodiMatrOfPagoCollectionNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = matricula.getCodiMatr();
                if (findMatricula(id) == null) {
                    throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Matricula matricula;
            try {
                matricula = em.getReference(Matricula.class, id);
                matricula.getCodiMatr();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pago> pagoCollectionOrphanCheck = matricula.getPagoCollection();
            for (Pago pagoCollectionOrphanCheckPago : pagoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Matricula (" + matricula + ") cannot be destroyed since the Pago " + pagoCollectionOrphanCheckPago + " in its pagoCollection field has a non-nullable codiMatr field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Alumno codiAlum = matricula.getCodiAlum();
            if (codiAlum != null) {
                codiAlum.getMatriculaCollection().remove(matricula);
                codiAlum = em.merge(codiAlum);
            }
            Aula codiAula = matricula.getCodiAula();
            if (codiAula != null) {
                codiAula.getMatriculaCollection().remove(matricula);
                codiAula = em.merge(codiAula);
            }
            em.remove(matricula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Matricula> findMatriculaEntities() {
        return findMatriculaEntities(true, -1, -1);
    }

    public List<Matricula> findMatriculaEntities(int maxResults, int firstResult) {
        return findMatriculaEntities(false, maxResults, firstResult);
    }

    private List<Matricula> findMatriculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Matricula.class));
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

    public Matricula findMatricula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Matricula.class, id);
        } finally {
            em.close();
        }
    }

    public int getMatriculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Matricula> rt = cq.from(Matricula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
