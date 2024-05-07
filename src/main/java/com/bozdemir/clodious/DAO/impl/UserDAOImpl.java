package com.bozdemir.clodious.DAO.impl;

import com.bozdemir.clodious.DAO.UserDAO;
import com.bozdemir.clodious.model.User;
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
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws NoResultException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);
            Predicate predicate = builder.equal(root.get("username"), username);
            criteria.select(root).where(predicate);
            TypedQuery<User> query = entityManager.createQuery(criteria);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Boolean saveOrUpdateUser(User user) {
        boolean success = true;
        try {
            entityManager.persist(user);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public Boolean removeUser(User user) {
        boolean success = true;
        try {
            entityManager.remove(user);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public Boolean existsByUsername(String username) throws NoResultException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);
            Predicate predicate = builder.equal(root.get("username"), username);
            criteria.select(root).where(predicate);
            TypedQuery<User> query = entityManager.createQuery(criteria);
            return query.getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Boolean existsByEmail(String email) throws NoResultException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);
            Predicate predicate = builder.equal(root.get("email"), email);
            criteria.select(root).where(predicate);
            TypedQuery<User> query = entityManager.createQuery(criteria);
            return query.getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }

    }
}
