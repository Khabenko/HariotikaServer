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
    String log = "";
    ArrayList<PartOfBody> partOfBodies;

    Character winner;

    private Character player1;
    private Character player2;

    private boolean player1IsReady;
    private boolean player2IsReady;

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
        this.partOfBodies = new ArrayList<>();

        for (int i = 0; i <2 ; i++) {
            for (PartOfBody mass : PartOfBody.values()) {
                partOfBodies.add(mass);
            }
        }


    }


    public void fight(){
        HariotikaMessage hariotikaMessage;
        Gson  gson = new Gson();
        int player1DefIndex = 0;
        int player2DefIndex = 0;
        System.out.println("Файт");
        log = "";

        System.out.println(partOfBodies.indexOf(getPlayer2Def()));
        System.out.println("----DEFF-----"+partOfBodies.get(partOfBodies.indexOf(getPlayer2Def())+1));

        if (getPlayer2Def()!= null){
            player1DefIndex = partOfBodies.indexOf(getPlayer1Def());
        }
        if (getPlayer1Def()!=null){
            player2DefIndex = partOfBodies.indexOf(getPlayer2Def());

        }


        if (getPlayer1Hit()!= null && !getPlayer1Hit().equals(partOfBodies.get(player2DefIndex)) && !getPlayer1Hit().equals(partOfBodies.get(player2DefIndex+1)) )
        {

            player1.hit(player2);
            log=player1.getName()+" hitting "+player2.getName()+" to "+ player1Hit+" "+"  \n";
            System.out.println("HP Игрока 1 "+player1.getHP());
        }
        else {
            log=player1.getName()+" hitting "+player2.getName()+" to "+ player1Hit+" "+" but was Block"+"  \n";
        }


        if (getPlayer2Hit()!= null && !getPlayer1Hit().equals(partOfBodies.get(player1DefIndex)) && !getPlayer1Hit().equals(partOfBodies.get(player1DefIndex+1)))
        {
            player2.hit(player1);
            log+=player2.getName()+" hitting "+player1.getName()+" to "+ player2Hit;
            System.out.println("HP Игрока 2 "+player2.getHP());
        }else {
            log+=player2.getName()+" hitting "+player1.getName()+" to "+ player2Hit+" but was Block";
        }

        System.out.println(gson.toJson(this));

        if (isFinish()) {
            finished = true;
            setEXP(player1);
            setEXP(player2);

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
        System.out.println("Бой начался");
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
         //   setPlayer1IsReady(true);
        //    setPlayer2IsReady(true);



        }

        player1.setInBattle(false);
        player2.setInBattle(false);
        System.out.println("В бою p1 "+player1.isInBattle());
        System.out.println("В бою p2 "+player2.isInBattle());
        System.out.println("Бой закончен");
        finished =true;
        Arena.battleList.remove(number);
        System.out.println("Бой удален "+Arena.battleList.containsKey(number));



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




    public void fightWithBot(){

        List<PartOfBody> VALUES = new ArrayList<PartOfBody>();
        VALUES.add(PartOfBody.HEAD);
        VALUES.add(PartOfBody.NECK);
        VALUES.add(PartOfBody.CHEST);
        VALUES.add(PartOfBody.BELLY);
        VALUES.add(PartOfBody.LEGS);
        int SIZE = VALUES.size();
         Random RANDOM = new Random();


         if (player1.getName().equals("Bot")) {
             player1IsReady = true;
             player1Hit = VALUES.get(RANDOM.nextInt(SIZE));
             player1Def = VALUES.get(RANDOM.nextInt(SIZE));
         } else if (player2.getName().equals("Bot")){
             player2IsReady = true;
             player2Hit = VALUES.get(RANDOM.nextInt(SIZE));
             player2Def = VALUES.get(RANDOM.nextInt(SIZE));
         }

    }



   public void setEXP(Character player){
       int exp;
       int lvl;

       System.out.println("SetEXP Player "+player.getName());
       System.out.println("SetEXP Winner "+winner.getName());

        if (winner.getName().equals(player.getName())) {
            exp = player.getMaxHP()-player.getHP();
        }
        else {
            exp = (player.getMaxHP()-player.getHP())/4;
            player.setHP(0);
        }
        player.setExperience(player.getExperience()+exp);

        if (player.getExperience() >= player.getExpnextlvl()){
            exp = player.getExperience()-player.getExpnextlvl();
            lvl = player.getLvl();
            player.setExperience(exp);
            player.setLvl(lvl+1);
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
