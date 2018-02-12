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
import java.util.Random;
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
    @Column(name = "pointCharacteristics")
    private int pointCharacteristics;



    @Transient
    private int maxMP;
    @Transient
    private int MP;

    @Transient
    private int hp_perSec;
    @Transient
    private int mp_perSec;
    @Transient
    private int phy_attack;
    @Transient
    private int decreasePower_Crit;
    @Transient
    private int decreasePersen_Crit;
    @Transient
    private int  evesion;
    @Transient
    private int  decreaseEnemyEvesion;
    @Transient
    private int armor_penetration;
    @Transient
    private int chance_criticalPhyAttack;
    @Transient
    private int power_criticalPhyAttack;
    @Transient
    private int chance_counterattack;
    @Transient
    private int chance_parry;

    @Transient
    private int magic_armor;
    @Transient
    private int magic_attack;
    @Transient
    private int chance_criticalMagicAttack;










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

    public void updatePlayerCharacteristics(){
        System.out.println("Updated characteristics");

        this.phy_attack = this.getStrength() +this.getAgility()+this.getIntuition();
        System.out.println("Phy attack "+phy_attack);

        this.mp_perSec = (int) (1+intelligence*0.2);
        System.out.println("MP per Sec "+mp_perSec);

        this.hp_perSec = 1+(int)(this.strength*0.02);
        System.out.println("Hp per Sec "+this.hp_perSec);

        this.maxMP = intelligence*4;
        System.out.println("MAX MP "+this.maxMP);

        this.armor = this.strength*3;
        System.out.println("Armor "+this.armor);

        this.decreasePersen_Crit = (int) (this.strength*0.1);
        System.out.println("Decrease Persen_Crit "+this.decreasePersen_Crit);

        this.decreasePower_Crit = (int) (this.strength*0.8);
        System.out.println("Decrease Power_Crit "+this.decreasePower_Crit);

        this.evesion = (int) (5+this.agility*0.7);
        System.out.println("Evesion "+this.evesion);

        this.decreaseEnemyEvesion = (int) (this.agility*0.3);
        System.out.println("Decrease Enemy Evesion "+this.decreaseEnemyEvesion);

        this.armor_penetration = (int) (this.agility);
        System.out.println("Armor penetration "+this.armor_penetration);


        this.chance_criticalPhyAttack = (int) (5+this.intuition*0.4);
        System.out.println("Chance criticalPhyAttack "+this.chance_criticalPhyAttack);

        this.power_criticalPhyAttack = (int) (this.intuition*2);
        System.out.println("Power criticalPhyAttack "+this.power_criticalPhyAttack);

        this.chance_counterattack = (int) (this.intuition*0.3);
        System.out.println("Chance counterattack "+this.chance_counterattack);

        this.chance_parry = (int) (5+this.intuition*0.3);
        System.out.println("Chance parry "+this.chance_parry);

    }


    public RoundLogs hit(Character enemy){
        RoundLogs roundLogs = new RoundLogs();
        System.out.println(getName()+"--------Hit--------"+enemy.getName());
        int damage = phy_attack - (enemy.armor-this.armor_penetration);
        System.out.println("Damage "+damage);
        int critChance = this.chance_criticalPhyAttack-enemy.getDecreasePersen_Crit();
        System.out.println("Crit chance "+critChance);
        int powercrit = this.power_criticalPhyAttack - enemy.getDecreasePower_Crit();
        System.out.println("Power crit "+powercrit);
        int enemyEvasion = enemy.getEvesion() - this.decreaseEnemyEvesion;


        if (damage<0) damage=0;
         if (critChance<0) critChance =0;
         if (powercrit<1) powercrit=1;

        boolean enemyDodge = enemyEvasion <= random(1,101);
        boolean enemyParry = enemy.getChance_parry() <= random(1,101);

        boolean critacalHit = critChance>= random(1,101);
        boolean counterattack = chance_counterattack >= random(1,101);

        roundLogs.setEnemyDodge(enemyDodge);
        roundLogs.setEnemyParry(enemyParry);
        roundLogs.setPlayerCritkal(critacalHit);
        roundLogs.setPlayerCounterattack(counterattack);



        if (enemyDodge){
            if (enemyParry){
                if (critacalHit){
                    //Крит прошел
                    damage = damage*powercrit;
                }
                enemy.setHP(enemy.getHP()-damage);

            }
            else {
                System.out.println(enemy.getName()+" parried ");
            }
        }
        else {
            System.out.println(enemy.getName()+" evaged "+enemyEvasion);
        }

         roundLogs.setPlayerDamaged(damage);
         return roundLogs;


       // int perReductionPhyDamage = (100-(enemy.getArmor()/enemy.getLvl()))/100;
     //   System.out.println(perReductionPhyDamage);
      //  enemy.setHP(enemy.getHP()-(this.strength*perReductionPhyDamage));
      //  enemy.setHP(enemy.getHP()-this.strength);
    }

    public int random(int low, int high){

        Random r = new Random();
        int Low = low;
        int High = high;
        int result = r.nextInt(High-Low) + Low;

        return result;
    }

    public void sendMessage(String message) {

        try {
            synchronized (getSessionMap().get(this.getName())) {
                if (getSessionMap().get(this.getName()).isOpen()) {
                    getSessionMap().get(this.getName()).getMessageHandlers().clear();
                   //System.out.println("Игроку " + getName() + " отправленно " + message);
                   // getSessionMap().get(this.getName()).getAsyncRemote().sendText(message);
                      getSessionMap().get(this.getName()).getBasicRemote().sendText(message);
                }
            }
        }
        catch (Exception e)
        {
            if (getSessionMap().get(this.getName()).isOpen() ){
                getSessionMap().get(this.getName()).getMessageHandlers().clear();
                System.out.println("Ошибка отправки cообщения ироку " + name);
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

    public int getHp_perSec() {
        return hp_perSec;
    }

    public void setHp_perSec(int hp_perSec) {
        this.hp_perSec = hp_perSec;
    }

    public int getMp_perSec() {
        return mp_perSec;
    }

    public void setMp_perSec(int mp_perSec) {
        this.mp_perSec = mp_perSec;
    }

    public int getPhy_attack() {
        return phy_attack;
    }

    public void setPhy_attack(int phy_attack) {
        this.phy_attack = phy_attack;
    }


    public int getDecreasePower_Crit() {
        return decreasePower_Crit;
    }

    public void setDecreasePower_Crit(int decreasePower_Crit) {
        this.decreasePower_Crit = decreasePower_Crit;
    }

    public int getDecreasePersen_Crit() {
        return decreasePersen_Crit;
    }

    public void setDecreasePersen_Crit(int decreasePersen_Crit) {
        this.decreasePersen_Crit = decreasePersen_Crit;
    }

    public int getEvesion() {
        return evesion;
    }

    public void setEvesion(int evesion) {
        this.evesion = evesion;
    }

    public int getDecreaseEnemyEvesion() {
        return decreaseEnemyEvesion;
    }

    public void setDecreaseEnemyEvesion(int decreaseEnemyEvesion) {
        this.decreaseEnemyEvesion = decreaseEnemyEvesion;
    }

    public int getArmor_penetration() {
        return armor_penetration;
    }

    public void setArmor_penetration(int armor_penetration) {
        this.armor_penetration = armor_penetration;
    }

    public int getChance_criticalPhyAttack() {
        return chance_criticalPhyAttack;
    }

    public void setChance_criticalPhyAttack(int chance_criticalPhyAttack) {
        this.chance_criticalPhyAttack = chance_criticalPhyAttack;
    }

    public int getPower_criticalPhyAttack() {
        return power_criticalPhyAttack;
    }

    public void setPower_criticalPhyAttack(int power_criticalPhyAttack) {
        this.power_criticalPhyAttack = power_criticalPhyAttack;
    }

    public int getChance_counterattack() {
        return chance_counterattack;
    }

    public void setChance_counterattack(int chance_counterattack) {
        this.chance_counterattack = chance_counterattack;
    }

    public int getChance_parry() {
        return chance_parry;
    }

    public void setChance_parry(int chance_parry) {
        this.chance_parry = chance_parry;
    }

    public int getPointCharacteristics() {
        return pointCharacteristics;
    }

    public void setPointCharacteristics(int pointCharacteristics) {
        this.pointCharacteristics = pointCharacteristics;
    }

    public int getMaxMP() {
        return maxMP;
    }

    public void setMaxMP(int maxMP) {
        this.maxMP = maxMP;
    }

    public int getMP() {
        return MP;
    }

    public void setMP(int MP) {
        this.MP = MP;
    }
}
