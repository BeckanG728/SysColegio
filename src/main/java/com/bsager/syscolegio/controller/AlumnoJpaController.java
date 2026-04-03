
package com.bsager.syscolegio.controller;

import com.bsager.syscolegio.controller.exceptions.IllegalOrphanException;
import com.bsager.syscolegio.controller.exceptions.NonexistentEntityException;
import com.bsager.syscolegio.model.Alumno;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bsager.syscolegio.model.Matricula;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author
 */
public class AlumnoJpaController implements Serializable {

    public AlumnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alumno alumno) {
        if (alumno.getMatriculaCollection() == null) {
            alumno.setMatriculaCollection(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Matricula> attachedMatriculaCollection = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionMatriculaToAttach : alumno.getMatriculaCollection()) {
                matriculaCollectionMatriculaToAttach = em.getReference(matriculaCollectionMatriculaToAttach.getClass(), matriculaCollectionMatriculaToAttach.getCodiMatr());
                attachedMatriculaCollection.add(matriculaCollectionMatriculaToAttach);
            }
            alumno.setMatriculaCollection(attachedMatriculaCollection);
            em.persist(alumno);
            for (Matricula matriculaCollectionMatricula : alumno.getMatriculaCollection()) {
                Alumno oldCodiAlumOfMatriculaCollectionMatricula = matriculaCollectionMatricula.getCodiAlum();
                matriculaCollectionMatricula.setCodiAlum(alumno);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
                if (oldCodiAlumOfMatriculaCollectionMatricula != null) {
                    oldCodiAlumOfMatriculaCollectionMatricula.getMatriculaCollection().remove(matriculaCollectionMatricula);
                    oldCodiAlumOfMatriculaCollectionMatricula = em.merge(oldCodiAlumOfMatriculaCollectionMatricula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getCodiAlum());
            Collection<Matricula> matriculaCollectionOld = persistentAlumno.getMatriculaCollection();
            Collection<Matricula> matriculaCollectionNew = alumno.getMatriculaCollection();
            List<String> illegalOrphanMessages = null;
            for (Matricula matriculaCollectionOldMatricula : matriculaCollectionOld) {
                if (!matriculaCollectionNew.contains(matriculaCollectionOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaCollectionOldMatricula + " since its codiAlum field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Matricula> attachedMatriculaCollectionNew = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionNewMatriculaToAttach : matriculaCollectionNew) {
                matriculaCollectionNewMatriculaToAttach = em.getReference(matriculaCollectionNewMatriculaToAttach.getClass(), matriculaCollectionNewMatriculaToAttach.getCodiMatr());
                attachedMatriculaCollectionNew.add(matriculaCollectionNewMatriculaToAttach);
            }
            matriculaCollectionNew = attachedMatriculaCollectionNew;
            alumno.setMatriculaCollection(matriculaCollectionNew);
            alumno = em.merge(alumno);
            for (Matricula matriculaCollectionNewMatricula : matriculaCollectionNew) {
                if (!matriculaCollectionOld.contains(matriculaCollectionNewMatricula)) {
                    Alumno oldCodiAlumOfMatriculaCollectionNewMatricula = matriculaCollectionNewMatricula.getCodiAlum();
                    matriculaCollectionNewMatricula.setCodiAlum(alumno);
                    matriculaCollectionNewMatricula = em.merge(matriculaCollectionNewMatricula);
                    if (oldCodiAlumOfMatriculaCollectionNewMatricula != null && !oldCodiAlumOfMatriculaCollectionNewMatricula.equals(alumno)) {
                        oldCodiAlumOfMatriculaCollectionNewMatricula.getMatriculaCollection().remove(matriculaCollectionNewMatricula);
                        oldCodiAlumOfMatriculaCollectionNewMatricula = em.merge(oldCodiAlumOfMatriculaCollectionNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alumno.getCodiAlum();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
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
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getCodiAlum();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Matricula> matriculaCollectionOrphanCheck = alumno.getMatriculaCollection();
            for (Matricula matriculaCollectionOrphanCheckMatricula : matriculaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Matricula " + matriculaCollectionOrphanCheckMatricula + " in its matriculaCollection field has a non-nullable codiAlum field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(alumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
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

    public Alumno findAlumno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
