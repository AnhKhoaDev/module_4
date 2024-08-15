package com.codegym.bai_1.service.impl;

import com.codegym.bai_1.model.Customer;
import com.codegym.bai_1.service.ICustomerService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class CustomerService implements ICustomerService {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.conf.xml")
                    .buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> findAll() {
        String queryStr = "SELECT c FROM Customer AS c";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer findById(Long id) {
        String queryStr = "SELECT c FROM Customer AS c WHERE c.id = :id";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void save(Customer customer) {
        Transaction transaction = null;
        Customer cus;
        if (customer.getId() == null) {
            cus = new Customer();
        }else {
            cus = findById(customer.getId());
        }

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            cus.setName(customer.getName());
            cus.setEmail(customer.getEmail());
            cus.setAddress( customer.getAddress());
            session.saveOrUpdate(cus);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void remove(Long id) {
        Customer customer = findById(id);
        if (customer != null) {
            Transaction transaction = null;
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                session.remove(customer);
                transaction.commit();
            }catch (HibernateException e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }
}
