package com.bozdemir.clodious.DAO.impl;

import com.bozdemir.clodious.DAO.FileDAO;
import com.bozdemir.clodious.DAO.UserDAO;
import com.bozdemir.clodious.model.FileData;
import com.bozdemir.clodious.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<FileData> getFileByName(String fileName) throws NoResultException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<FileData> criteria = builder.createQuery(FileData.class);
            Root<FileData> root = criteria.from(FileData.class);
            Predicate predicate = builder.equal(root.get("name"), fileName);
            criteria.select(root).where(predicate);
            TypedQuery<FileData> query = entityManager.createQuery(criteria);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Optional<FileData> getFileById(UUID fileId) throws NoResultException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<FileData> criteria = builder.createQuery(FileData.class);
            Root<FileData> root = criteria.from(FileData.class);
            Predicate predicate = builder.equal(root.get("id"), fileId);
            criteria.select(root).where(predicate);
            TypedQuery<FileData> query = entityManager.createQuery(criteria);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Boolean saveOrUpdateFile(FileData fileData) {
        boolean success = true;
        try {
            entityManager.persist(fileData);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public Boolean removeFile(FileData fileData) {
        boolean success = true;
        try {
            fileData.setUser(null);
            entityManager.remove(fileData);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public List<FileData> getAllFilesByUserName(String userName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileData> criteria = builder.createQuery(FileData.class);
        Root<FileData> root = criteria.from(FileData.class);
        Join<FileData, User> user = root.join("user");
        Predicate predicate = builder.equal(user.get("username"), userName);

        criteria.select(root).where(predicate);
        TypedQuery<FileData> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public List<FileData> getAllFilesByUser(User user) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileData> criteria = builder.createQuery(FileData.class);
        Root<FileData> file = criteria.from(FileData.class);
        Predicate predicate = builder.equal(file.get("user"), user);
        criteria.select(file).where(predicate);
        TypedQuery<FileData> query = entityManager.createQuery(criteria);
        return query.getResultList();
    }
}
