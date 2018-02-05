import Domain.Arena;
import Domain.Battle;
import Domain.Character;
import Domain.PartOfBody;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.HibernateUtil;
import db.HibernateUtil2;
import db.Login;
import db.Users;
import org.hibernate.Session;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.ws.rs.HEAD;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.Date;
import java.util.Random;


public class Main {



    private static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void copy(File source, File dest) throws IOException {
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

    public static void main(String[] args) throws InterruptedException {



        Random r = new Random();
        int Low = 1;
        int High = 101;
        int Result = r.nextInt(High-Low) + Low;



/*
        File avatar = new File("src\\main\\resources\\avatars\\Default.png");
        File newFile = new File("src\\main\\resources\\avatars\\Player.png");

        try {
            copy(avatar,newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

*/







      /*
        try {



            // System.out.println(gson.toJson(serializeStream));

            File f = new File("D:\\MyGame\\HariotikaServer\\src\\main\\resources\\avatars\\Maka.png"); // тестовая картинка 400*300 24 bit (400*300*3=360000)
            BufferedImage img = ImageIO.read(f);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            System.out.println(bytes.length);

            /*
            byte[] bytes = ((DataBufferByte) img.getData().getDataBuffer()).getData();
            System.out.println("data lenght " + bytes.length);


            InputStream in = new ByteArrayInputStream(bytes);
            BufferedImage bImageFromConvert = ImageIO.read(in);

            File outputfile = new File("image1.jpg");
            ImageIO.write(bImageFromConvert, "jpg", outputfile);


            System.out.println(img.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }




        /*
        System.out.println(3%2);

       Arena arena = new Arena();
        Character character = new Character("Maka","Maka");
        Character character2 = new Character("Maka","Maka");
        Character character3 = new Character("Maka","Maka");
        Character character4 = new Character("Maka","Maka");

     character.setLvl(1);
     //   arena.addToArena(character);
        character.setName("Maka2");
    //    arena.addToArena(character);

        System.out.println(arena.getCharQueue().get(1));


        Battle battle = new Battle(1,character,character);
        Gson gson = new Gson();
        Character character1 = new Character();
        character.setLvl(1);
        character.setHP(3);
        character.setStrength(1);
        character.setArmor(1);


         gson = new Gson();

        System.out.println(character.getName());
        System.out.println(gson.toJson(battle.getPlayer1()));
        System.out.println(gson.toJson(battle.getPlayer2()));

        System.out.println(gson.toJson(battle));
        System.out.println(true&&true);





        System.out.println("Maven + Hibernate + PgSQL");
        Session session = HibernateUtil2.getSessionFactory().openSession();
      //  Login login = new Login("Maka","2");
        Users user = new Users();
        user.setLogin("Maka2");
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        Login login = new Login("Serg","1");
        //login.start();
//        login.save();
//        System.out.println(login.login());
       /*
        Session session = HibernateUtil.getSessionFactory().openSession();


        User user = new  User("Maka","2");
   //     User user2 = new  User("Maka3","4");

        String hql = "from User where login = '"+user.getLogin()+"'";
        Query query = session.createQuery(hql);

        if (query.list().isEmpty()) {
            session.beginTransaction();
            System.out.println("++++++++++++++Логин не существует++++++++++++++");
            session.saveOrUpdate(user);
            session.getTransaction().commit();
           // session.close();
        }


        for (int i = 0; i <20 ; i++) {
            Thread.sleep(3000);
            user.setPass(String.valueOf(i));
            System.out.println(user.getLogin());
            System.out.println(user.getPass());
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();

        }




    //  session.close();
*/
    }

}

