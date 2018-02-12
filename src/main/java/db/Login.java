package db;


import Domain.Character;
import Net.ServerWS;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class Login  {

    private Session session;
    private Users user;
    private Character character;


    public Login(String userLogin, String pass) {

       this.session = HibernateUtil2.getSessionFactory().openSession();;
       this.user = new Users();
       this.user.setLogin(userLogin);
       this.user.setPass(pass);
    }
/*
    @Override
    public void run() {
        session = HibernateUtil2.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
    }

*/
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }


    public  boolean loginIsPresent(){
        System.out.println(user.getLogin());
        String hql = "from Users where login = '"+user.getLogin()+"'";
        Query query = session.createQuery(hql);
        boolean isLogin = query.list().isEmpty() ;
             return !isLogin;
    }

    public  boolean checkPass(String pass){
        String hql = "from Users where login = '"+user.getLogin()+"'";
        Query query = session.createQuery(hql);
        List<Users> users = query.list();
        if (loginIsPresent()) {
           user = users.get(0);
            System.out.println("++++++++++++++Логин верифицирован++++++++++++++");
            if (user.getPass().toString().equals(pass)) {
                System.out.println("++++++++++++++Пароль верифицирован++++++++++++++");
                hql = "from Character WHERE login='"+user.getLogin()+"'";
                query = session.createQuery(hql);
                List<Character> characters =  query.list();
                character=characters.get(0);
                character.updatePlayerCharacteristics();
                return true;
            }
            else
                return false;
        }
        return false;
    }


    public void save(Object object){
        session.beginTransaction();
        session.saveOrUpdate(object);
        session.getTransaction().commit();
    }

    public  void createNewUser(){
        String hql = "from Users";
        Query query = session.createQuery(hql);
        user.setLogin("Player"+query.list().size());
        user.setPass("null");
        save(user);
        createNewChar();
    }

    public  void createNewChar(){
        character = new Character(user.getLogin(),user.getLogin());
        character.setLvl(1);
        character.setHP(60);
        character.setMaxHP(60);
        character.setStrength(3);
        character.setAgility(3);
        character.setIntuition(3);
        character.setIntelligence(4);
        character.setVitality(6);
        save(character);
        character.updatePlayerCharacteristics();



        File avatar = new File("C:\\Avatars\\Default.png");
        File newFile = new File("C:\\Avatars\\"+character.getName()+".png");
        try {
            copy(avatar,newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public static void copy(File source, File dest) throws IOException {
        System.out.println("++++++++++++++++++++++++++++++++++"+source.getAbsolutePath());
        FileInputStream is = new FileInputStream(source);
        try {
            FileOutputStream os = new FileOutputStream(dest);
            try {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } finally {
                os.close();
            }
        } finally {
            is.close();
        }
    }

}
