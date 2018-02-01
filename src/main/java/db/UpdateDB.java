package db;

import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class UpdateDB {

    private static Session session = HibernateUtil2.getSessionFactory().openSession();;;

    public static void  UpdateDB(Object object){

            session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();


    }

    public static int nextLevelEXP(int plauerLVL){
        String hql = "from table_lvl where lvl = "+plauerLVL+"";
        Query query = session.createQuery(hql);
        List<table_lvl> lvl = query.list();
        return lvl.get(0).experience;
    }
}
