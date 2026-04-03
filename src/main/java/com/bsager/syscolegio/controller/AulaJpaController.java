/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bsager.syscolegio.controller;

import com.bsager.syscolegio.controller.exceptions.IllegalOrphanException;
import com.bsager.syscolegio.controller.exceptions.NonexistentEntityException;
import com.bsager.syscolegio.model.Aula;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bsager.syscolegio.model.Grado;
import com.bsager.syscolegio.model.Matricula;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author
 */
public class AulaJpaController implements Serializable {

    public AulaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Aula aula) {
        if (aula.getMatriculaCollection() == null) {
            aula.setMatriculaCollection(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado codiGrad = aula.getCodiGrad();
            if (codiGrad != null) {
                codiGrad = em.getReference(codiGrad.getClass(), codiGrad.getCodiGrad());
                aula.setCodiGrad(codiGrad);
            }
            Collection<Matricula> attachedMatriculaCollection = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionMatriculaToAttach : aula.getMatriculaCollection()) {
                matriculaCollectionMatriculaToAttach = em.getReference(matriculaCollectionMatriculaToAttach.getClass(), matriculaCollectionMatriculaToAttach.getCodiMatr());
                attachedMatriculaCollection.add(matriculaCollectionMatriculaToAttach);
            }
            aula.setMatriculaCollection(attachedMatriculaCollection);
            em.persist(aula);
            if (codiGrad != null) {
                codiGrad.getAulaCollection().add(aula);
                codiGrad = em.merge(codiGrad);
            }
            for (Matricula matriculaCollectionMatricula : aula.getMatriculaCollection()) {
                Aula oldCodiAulaOfMatriculaCollectionMatricula = matriculaCollectionMatricula.getCodiAula();
                matriculaCollectionMatricula.setCodiAula(aula);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
                if (oldCodiAulaOfMatriculaCollectionMatricula != null) {
                    oldCodiAulaOfMatriculaCollectionMatricula.getMatriculaCollection().remove(matriculaCollectionMatricula);
                    oldCodiAulaOfMatriculaCollectionMatricula = em.merge(oldCodiAulaOfMatriculaCollectionMatricula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Aula aula) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aula persistentAula = em.find(Aula.class, aula.getCodiAula());
            Grado codiGradOld = persistentAula.getCodiGrad();
            Grado codiGradNew = aula.getCodiGrad();
            Collection<Matricula> matriculaCollectionOld = persistentAula.getMatriculaCollection();
            Collection<Matricula> matriculaCollectionNew = aula.getMatriculaCollection();
            List<String> illegalOrphanMessages = null;
            for (Matricula matriculaCollectionOldMatricula : matriculaCollectionOld) {
                if (!matriculaCollectionNew.contains(matriculaCollectionOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaCollectionOldMatricula + " since its codiAula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codiGradNew != null) {
                codiGradNew = em.getReference(codiGradNew.getClass(), codiGradNew.getCodiGrad());
                aula.setCodiGrad(codiGradNew);
            }
            Collection<Matricula> attachedMatriculaCollectionNew = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionNewMatriculaToAttach : matriculaCollectionNew) {
                matriculaCollectionNewMatriculaToAttach = em.getReference(matriculaCollectionNewMatriculaToAttach.getClass(), matriculaCollectionNewMatriculaToAttach.getCodiMatr());
                attachedMatriculaCollectionNew.add(matriculaCollectionNewMatriculaToAttach);
            }
            matriculaCollectionNew = attachedMatriculaCollectionNew;
            aula.setMatriculaCollection(matriculaCollectionNew);
            aula = em.merge(aula);
            if (codiGradOld != null && !codiGradOld.equals(codiGradNew)) {
                codiGradOld.getAulaCollection().remove(aula);
                codiGradOld = em.merge(codiGradOld);
            }
            if (codiGradNew != null && !codiGradNew.equals(codiGradOld)) {
                codiGradNew.getAulaCollection().add(aula);
                codiGradNew = em.merge(codiGradNew);
            }
            for (Matricula matriculaCollectionNewMatricula : matriculaCollectionNew) {
                if (!matriculaCollectionOld.contains(matriculaCollectionNewMatricula)) {
                    Aula oldCodiAulaOfMatriculaCollectionNewMatricula = matriculaCollectionNewMatricula.getCodiAula();
                    matriculaCollectionNewMatricula.setCodiAula(aula);
                    matriculaCollectionNewMatricula = em.merge(matriculaCollectionNewMatricula);
                    if (oldCodiAulaOfMatriculaCollectionNewMatricula != null && !oldCodiAulaOfMatriculaCollectionNewMatricula.equals(aula)) {
                        oldCodiAulaOfMatriculaCollectionNewMatricula.getMatriculaCollection().remove(matriculaCollectionNewMatricula);
                        oldCodiAulaOfMatriculaCollectionNewMatricula = em.merge(oldCodiAulaOfMatriculaCollectionNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = aula.getCodiAula();
                if (findAula(id) == null) {
                    throw new NonexistentEntityException("The aula with id " + id + " no longer exists.");
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
            Aula aula;
            try {
                aula = em.getReference(Aula.class, id);
                aula.getCodiAula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Matricula> matriculaCollectionOrphanCheck = aula.getMatriculaCollection();
            for (Matricula matriculaCollectionOrphanCheckMatricula : matriculaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Aula (" + aula + ") cannot be destroyed since the Matricula " + matriculaCollectionOrphanCheckMatricula + " in its matriculaCollection field has a non-nullable codiAula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Grado codiGrad = aula.getCodiGrad();
            if (codiGrad != null) {
                codiGrad.getAulaCollection().remove(aula);
                codiGrad = em.merge(codiGrad);
            }
            em.remove(aula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Aula> findAulaEntities() {
        return findAulaEntities(true, -1, -1);
    }

    public List<Aula> findAulaEntities(int maxResults, int firstResult) {
        return findAulaEntities(false, maxResults, firstResult);
    }

    private List<Aula> findAulaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aula.class));
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

    public Aula findAula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aula.class, id);
        } finally {
            em.close();
        }
    }

    public int getAulaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Aula> rt = cq.from(Aula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
