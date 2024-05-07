package com.bozdemir.clodious.DAO.impl;

import com.bozdemir.clodious.DAO.FileDAO;
import com.bozdemir.clodious.DAO.UserDAO;
import com.bozdemir.clodious.model.FileMeta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FileDAOImpl implements FileDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    public FileDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<FileMeta> getFileByName(String fileName) throws NoResultException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<FileMeta> criteria = builder.createQuery(FileMeta.class);
            Root<FileMeta> root = criteria.from(FileMeta.class);
            Predicate predicate = builder.equal(root.get("name"), fileName);
            criteria.select(root).where(predicate);
            TypedQuery<FileMeta> query = entityManager.createQuery(criteria);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Boolean saveOrUpdateFile(FileMeta fileMeta) {
        boolean success = true;
        try {
            entityManager.persist(fileMeta);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public Boolean removeFile(FileMeta fileMeta) {
        boolean success = true;
        try {
            fileMeta.setUser(null);
            entityManager.remove(fileMeta);
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
}
