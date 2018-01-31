package db;

import org.hibernate.Session;

public class UpdateDB {

    private static Session session = HibernateUtil2.getSessionFactory().openSession();;;

    public static void  UpdateDB(Object object){

            session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();

    }
}
