package Domain;

import Net.Command;
import Net.HariotikaMessage;
import Net.ServerWS;
import Net.WsCode;
import com.google.gson.Gson;
import Domain.Character;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.*;

import static Net.ServerWS.getCharacterMap;


public class Arena extends Thread {
    static HashMap<Integer,PriorityQueue<Domain.Character>> charQueue;
    static HashMap<Long,Battle> battleList;
    HashSet<String> battleOFF;
    private HariotikaMessage hariotikaMessage;
    private Gson gson ;


    public Arena() {
        gson = new Gson();
        this.battleOFF = new HashSet<String>();
        this.charQueue = new HashMap<Integer, PriorityQueue<Character>>();
        this.battleList = new HashMap<Long, Battle>();
        this.gson = new Gson();
        for (int i = 1; i <= 20 ; i++) {
            this.charQueue.put(i, new PriorityQueue<Character>());
        }
        this.start();
        System.out.println("------------Arena created------------");
    }

    public void addToArena(Character character) {

        for (HashMap.Entry<Integer, PriorityQueue<Character>>  pair: charQueue.entrySet()) {
            if(pair.getKey() == character.getLvl()){
                if (!(pair.getValue().contains(character)) && !getCharacterMap().get(character.getName()).isInBattle()) {
                  //  System.out.println("Проверка, естьли в очереди чар "+character.getName()+" "+pair.getValue().contains(character));
                 //   System.out.println("Проверка, персонаж уже в бою "+getCharacterMap().get(character.getName()).isInBattle());
                    pair.getValue().offer(getCharacterMap().get(character.getName()));
                }
                else {
                    for (HashMap.Entry<Long, Battle> battleEntry : battleList.entrySet()) {
                        if (battleEntry.getValue().getPlayer1().getName().equals(character.getName()) || battleEntry.getValue().getPlayer2().getName().equals(character.getName())) {
                            System.out.println("++++++++Возвращяем сущетвующий батл игроку++++++++");
                            hariotikaMessage = new HariotikaMessage(Command.Battle, WsCode.UpdateBattle, battleEntry.getValue());
                            character.sendMessage(gson.toJson(hariotikaMessage));
                            break;
                        }

                     }

                }
            }
        }

    }

    public void cancelRegBattle(Character character){
           charQueue.get(character.getLvl()).remove(character);
        }



    @Override
    public void run() {
        System.out.println("------------Started Arena------------");
             while (true){
                 createBattle();
             }
    }


    public void createBattle() {


        for (HashMap.Entry<Integer, PriorityQueue<Character>>  pair: charQueue.entrySet()) {

           if (pair.getValue().size()>1 && pair.getValue().size()%2==0 ){

               System.out.println("----------Greated Battle-------------LVL = "+pair.getKey());


               long number = new Date().getTime();

               Character player1 = charQueue.get(pair.getKey()).poll();
               System.out.println("Add to battle "+player1.getName());
               Character player2 = charQueue.get(pair.getKey()).poll();
               System.out.println("Add to battle "+player2.getName());

               final Battle battle = new Battle(number,player1,player2);
               player1.setInBattle(true);
               player2.setInBattle(true);
               battleList.put(number, battle);

               Thread thread = new Thread(){
                   public void run(){
                       System.out.println("Thread Running " + battle.getNumber());
                       battle.startfight();
                       System.out.println("Thread Stop " + battle.getNumber());

                   }
               };
               thread.start();
               hariotikaMessage = new HariotikaMessage(Command.Battle, WsCode.UpdateBattle, battle);
              // System.out.println(hariotikaMessage);

               if (player1.getName()!="Bot")
               player1.sendMessage(gson.toJson(hariotikaMessage));
               if (player2.getName()!="Bot")
               player2.sendMessage(gson.toJson(hariotikaMessage));
           }

            }

    }






    public HashMap<Integer, PriorityQueue<Character>> getCharQueue() {
        return charQueue;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void setCharQueue(HashMap<Integer, PriorityQueue<Character>> charQueue) {
        this.charQueue = charQueue;
    }

    public HashMap<Long, Battle> getBattleList() {
        return battleList;
    }

    public void setBattleList(HashMap<Long, Battle> battleList) {
        this.battleList = battleList;
    }

    public HashSet<String> getBattleOFF() {
        return battleOFF;
    }

    public  void setBattleOFF(HashSet<String> battleOFF) {
        this.battleOFF = battleOFF;
    }
}
