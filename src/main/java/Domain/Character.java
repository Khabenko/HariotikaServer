package Domain;

import com.google.gson.annotations.Expose;
import db.Users;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.query.Query;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.persistence.*;
import javax.websocket.Session;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import static Net.ServerWS.getSessionMap;

@Entity
@Table(name = "character")
public class Character implements Comparable, Serializable {
    @Transient
    private  Users user;
    @Transient
    private boolean inBattle;
    @Transient
    private BufferedImage avatar;


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "strength")
    private int strength;
    @Column(name = "agility")
    private int agility;
    @Column(name = "intuition")
    private int intuition;
    @Column(name = "vitality")
    private int vitality;
    @Column(name = "intelligence")
    private int intelligence;
    @Column(name = "wisdom")
    private int wisdom;
    @Column(name = "armor")
    private int armor;
    @Column(name = "maxHP")
    private int maxHP;
    @Column(name = "HP")
    private int HP;
    @Column(name = "login")
    private String login;
    @Column(name = "lvl")
    private int lvl;
    @Column(name = "experience")
    private int experience;
    @Column(name = "expnextlvl")
    private int expnextlvl;







    public Character(String name, String login) {
        this.name = name;
        this.login = login;
/*
        try {
            System.out.println("Вычитываем аватарку "+name);
            this.avatar = ImageIO.read(new File("D:\\MyGame\\HariotikaServer\\src\\main\\resources\\avatars\\"+name+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    public Character() {

           }

    public void hit(Character enemy){
        System.out.println("Удар "+getName());
       // int perReductionPhyDamage = (100-(enemy.getArmor()/enemy.getLvl()))/100;
     //   System.out.println(perReductionPhyDamage);
      //  enemy.setHP(enemy.getHP()-(this.strength*perReductionPhyDamage));
        enemy.setHP(enemy.getHP()-this.strength);
    }

    public void sendMessage(String message) {
        try {
            synchronized (getSessionMap().get(this.getName())) {
                if (getSessionMap().get(this.getName()).isOpen()) {
                 //   System.out.println("Игроку " + getName() + " отправленно " + message);
                    getSessionMap().get(this.getName()).getAsyncRemote().sendText(message);
                }
            }
        }
        catch (Exception e)
        {
            if (getSessionMap().get(this.getName()).isOpen() ){
                System.out.println("Ошибка отправки сообщения ироку " + name);
                e.printStackTrace();
            sendMessage(message);
        }
            else
               e.printStackTrace();
        }

       // this.clientSession.getAsyncRemote().sendText(message);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getIntuition() {
        return intuition;
    }

    public void setIntuition(int intuition) {
        this.intuition = intuition;
    }

    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public boolean isInBattle() {
        return inBattle;
    }

    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    public int getExpnextlvl() {
        return expnextlvl;
    }

    public void setExpnextlvl(int expnextlvl) {
        this.expnextlvl = expnextlvl;
    }

    public int compareTo(Object o) {
        return 0;
    }
}
