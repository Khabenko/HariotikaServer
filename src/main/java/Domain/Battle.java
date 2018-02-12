package Domain;


import Net.Command;
import Net.HariotikaMessage;
import Net.ServerWS;
import Net.WsCode;
import com.google.gson.Gson;
import db.UpdateDB;

import java.io.Serializable;
import java.util.*;

import static Net.ServerWS.getCharacterMap;
import static db.UpdateDB.*;

public class Battle {

  //  Battle battle;
    long number;
    boolean finished = false;
    long timer;
    long endBattleTime;
    long startBattleTime;
    int round;


    Character winner;

    private Character player1;
    private Character player2;



    private boolean player1IsReady;
    private boolean player2IsReady;


    private ArrayList<PartOfBody> player1Hitting;
    private ArrayList<PartOfBody> player2Hitting;

    private ArrayList<PartOfBody>  player1Defance;
    private ArrayList<PartOfBody>  player2Defance;


    private RoundLogs player1LogHit;
    private RoundLogs player2LogHit;

    private int player1Damaged;
    private int player2Damaged;

    private PartOfBody player1Def;
    private PartOfBody player2Def;

    private PartOfBody player1Hit;
    private PartOfBody player2Hit;



    public Battle(long number,Character player1, Character player2) {
        this.startBattleTime = new Date().getTime()/1000;
        this.endBattleTime = startBattleTime+30;
        this.timer = endBattleTime -startBattleTime;
        this.number = number;
        this.player1 = player1;
        this.player2 = player2;
        this.player1IsReady = false;
        this.player2IsReady =false;
        this.player1Damaged =0;
        this.player2Damaged =0;
        this.round=0;

        this.player1Defance =  new ArrayList<PartOfBody>();
        this.player2Defance = new ArrayList<PartOfBody>();


    }


    public void fight(){
        HariotikaMessage hariotikaMessage;
        Gson  gson = new Gson();
        System.out.println("Fight");

        if (getPlayer1Hit()!= null && !(player2Defance.contains(getPlayer1Hit())))
        {
            player1LogHit = player1.hit(player2);
            player1Damaged+= player1LogHit.getPlayerDamaged();
            player1LogHit.setHit(getPlayer1Hit());
            System.out.println("HP player 1 "+player1.getHP());
        }
        else {
             this.player1LogHit = new RoundLogs();
             player1LogHit.setEnemyBlock(true);
             player1LogHit.setHit(getPlayer1Hit());
        }

        if (getPlayer2Hit()!= null && !(player1Defance.contains(getPlayer2Hit())))
        {
            player2LogHit = player2.hit(player1);
            player2Damaged+= player2LogHit.getPlayerDamaged();
            player2LogHit.setHit(getPlayer2Hit());
            System.out.println("HP player 2 "+player2.getHP());

        }else {
            this.player2LogHit = new RoundLogs();
            player2LogHit.setEnemyBlock(true);
            player2LogHit.setHit(getPlayer2Hit());
        }

       /// System.out.println(gson.toJson(this));

        if (isFinish()) {
            finished = true;
            setEXP(player1, player1Damaged);
            setEXP(player2, player2Damaged);

        }

            hariotikaMessage = new HariotikaMessage(Command.Battle, WsCode.UpdateBattle, this);


        if (player1.getName()!="Bot")
            player1.sendMessage(gson.toJson(hariotikaMessage));
        if (player2.getName()!="Bot")
            player2.sendMessage(gson.toJson(hariotikaMessage));

        player1IsReady =false;
        player2IsReady =false;
        this.startBattleTime = new Date().getTime()/1000;
        this.endBattleTime = startBattleTime+30;

    }



    public boolean isRedy(){
          return this.player1IsReady && this.player2IsReady;
    }

    public boolean isFinish(){
        if (player1.getHP()<=0 || player2.getHP()<=0) {
           if (player1.getHP()<=0 && player2.getHP()<=0)
            winner = null;
        else if (player1.getHP()<=0)
            winner = player2;
        else if (player2.getHP()<=0)
               winner = player1;
            return true;
        }
        else return false;
    }


    public void startfight() {
        System.out.println("Battle started");
        Gson gson = new Gson();
        while (!isFinish())
        {
            HariotikaMessage hariotikaMessage = new HariotikaMessage(Command.Battle,WsCode.UpdateTimer);
            hariotikaMessage.setTimer(timer);
            if (player1.getName()!="Bot")
                player1.sendMessage(gson.toJson(hariotikaMessage));
            if (player2.getName()!="Bot")
                player2.sendMessage(gson.toJson(hariotikaMessage));

            runTimer();
            fightWithBot();
            try {
                Thread.sleep(1000);
                System.out.print("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isRedy()){
                fight();

               if (isFinish())
                    break;
            }

        }




        player1.setInBattle(false);
        player2.setInBattle(false);
       // System.out.println("В бою p1 "+player1.isInBattle());
       // System.out.println("В бою p2 "+player2.isInBattle());
        System.out.println("Battle finish");
        finished =true;
        Arena.battleList.remove(number);
        System.out.println("Battle deleted "+Arena.battleList.containsKey(number));



    }

    public boolean isPlayer1IsReady() {
        return player1IsReady;
    }

    public void setPlayer1IsReady(boolean player1IsReady) {
        this.player1IsReady = player1IsReady;
    }

    public boolean isPlayer2IsReady() {
        return player2IsReady;
    }

    public void setPlayer2IsReady(boolean player2IsReady) {
        this.player2IsReady = player2IsReady;
    }

    public PartOfBody getPlayer1Def() {
        return player1Def;
    }

    public void setPlayer1Def(PartOfBody player1Def) {
        this.player1Def = player1Def;
    }

    public PartOfBody getPlayer2Def() {
        return player2Def;
    }

    public void setPlayer2Def(PartOfBody player2Def) {
        this.player2Def = player2Def;
    }

    public Character getPlayer1() {
        return player1;
    }

    public void setPlayer1(Character player1) {
        this.player1 = player1;
    }

    public Character getPlayer2() {
        return player2;
    }

    public void setPlayer2(Character player2) {
        this.player2 = player2;
    }

    public PartOfBody getPlayer1Hit() {
        return player1Hit;
    }

    public void setPlayer1Hit(PartOfBody player1Hit) {
        this.player1Hit = player1Hit;
    }

    public PartOfBody getPlayer2Hit() {
        return player2Hit;
    }

    public void setPlayer2Hit(PartOfBody player2Hit) {
        this.player2Hit = player2Hit;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public ArrayList<PartOfBody> getPlayer1Defance() {
        return player1Defance;
    }

    public void setPlayer1Defance(ArrayList<PartOfBody> player1Defance) {
        this.player1Defance = player1Defance;
    }

    public ArrayList<PartOfBody> getPlayer2Defance() {
        return player2Defance;
    }

    public void setPlayer2Defance(ArrayList<PartOfBody> player2Defance) {
        this.player2Defance = player2Defance;
    }

    public void fightWithBot(){

        List<PartOfBody> VALUES = new ArrayList<PartOfBody>();
        VALUES.add(PartOfBody.HEAD);
        VALUES.add(PartOfBody.NECK);
        VALUES.add(PartOfBody.CHEST);
        VALUES.add(PartOfBody.BELLY);
        VALUES.add(PartOfBody.LEGS);
        VALUES.add(PartOfBody.HEAD);
        int SIZE = VALUES.size()-1;
         Random RANDOM = new Random();


         if (player1.getName().equals("Bot")) {
             player1Defance.clear();
             player1IsReady = true;
             player1Hit = VALUES.get(RANDOM.nextInt(SIZE));
             int random = RANDOM.nextInt(SIZE);
             player1Defance.add(VALUES.get(random));
             player1Defance.add(VALUES.get(random+1));
         }
         else if (player2.getName().equals("Bot")){
             player2Defance.clear();
             player2IsReady = true;
             player2Hit = VALUES.get(RANDOM.nextInt(SIZE));
          //   player2Def = VALUES.get(RANDOM.nextInt(SIZE));

             int random = RANDOM.nextInt(SIZE);
             player2Defance.add(VALUES.get(random));
             player2Defance.add(VALUES.get(random+1));

         }

    }


    public PartOfBody randomPartOfBody(){
        List<PartOfBody> VALUES = new ArrayList<PartOfBody>();
        VALUES.add(PartOfBody.HEAD);
        VALUES.add(PartOfBody.NECK);
        VALUES.add(PartOfBody.CHEST);
        VALUES.add(PartOfBody.BELLY);
        VALUES.add(PartOfBody.LEGS);
        VALUES.add(PartOfBody.HEAD);
        int SIZE = VALUES.size()-1;
        Random RANDOM = new Random();
        int random = RANDOM.nextInt(SIZE);
        return VALUES.get(random);

    }



   public void setEXP(Character player, int playerDamaged){
       int exp;
       int lvl;


        if(winner !=null && winner.getName().equals(player.getName())) {
            exp = playerDamaged;
        }
        else {
            player.setHP(0);
            exp = playerDamaged/4;

        }
        player.setExperience(player.getExperience()+exp);

        if (player.getExperience() >= player.getExpnextlvl()){
            exp = player.getExperience()-player.getExpnextlvl();
            lvl = player.getLvl();
            player.setExperience(exp);
            player.setLvl(lvl+1);
            player.setPointCharacteristics(player.getPointCharacteristics()+5);
            player.setExpnextlvl(nextLevelEXP(player.getLvl()));
        }


        if (!player.getName().equals("Bot"))
        UpdateDB(player);
   }


   public void runTimer(){
         Date currentTime = new Date();
     //  player1.sendMessage("Timer#"+timer);
    //   player2.sendMessage("Timer#"+timer);
      // System.out.println("Timer#"+timer);
       timer=endBattleTime-currentTime.getTime()/1000;

       if (new Date().getTime()/1000 >=endBattleTime) {
           List<PartOfBody> VALUES = new ArrayList<PartOfBody>();
           VALUES.add(PartOfBody.HEAD);
           VALUES.add(PartOfBody.NECK);
           VALUES.add(PartOfBody.CHEST);
           VALUES.add(PartOfBody.BELLY);
           VALUES.add(PartOfBody.LEGS);
           int SIZE = VALUES.size();
           Random RANDOM = new Random();

           if (!player1IsReady) {
               player1IsReady = true;
               player1Hit = VALUES.get(RANDOM.nextInt(SIZE));
               player1Def = VALUES.get(RANDOM.nextInt(SIZE));
           } else if (!player2IsReady){
               player2IsReady = true;
               player2Hit = VALUES.get(RANDOM.nextInt(SIZE));
               player2Def = VALUES.get(RANDOM.nextInt(SIZE));

           }


       }


   }


}
