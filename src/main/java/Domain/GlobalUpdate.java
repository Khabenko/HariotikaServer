package Domain;

import Net.Command;
import Net.HariotikaMessage;
import Net.ServerWS;
import Net.WsCode;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GlobalUpdate extends Thread {
    private HariotikaMessage hariotikaMessage;
    private Character character;
    private Gson gson;
  //  private int regenerationMP = 1;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public GlobalUpdate() {
        this.start();
        character = new Character();
        gson = new Gson();

        scheduler.scheduleAtFixedRate(new Thread(){
            public void run(){
             //   System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                for (HashMap.Entry<String, Character> pair : ServerWS.getCharacterMap().entrySet()) {
                    character = ServerWS.getCharacterMap().get(pair.getKey() );
                    if (character.getHP()< character.getMaxHP() && !character.isInBattle() && character.getName()!= "Bot") {
                        character.setHP(character.getHP() + character.getHp_perSec());
                        ServerWS.getCharacterMap().get(character.getName()).setHP(character.getHP());
                    }
                    if (character.getMP()< character.getMaxMP() && !character.isInBattle() && character.getName()!= "Bot") {
                        character.setMP(character.getMP() + character.getMp_perSec());
                        ServerWS.getCharacterMap().get(character.getName()).setMP(character.getMP());
                    }

                    if (character.getName()!= "Bot")
                        if (ServerWS.getSessionMap().get(pair.getKey()).isOpen() ) {
                            hariotikaMessage = new HariotikaMessage(Command.Login, WsCode.Success,character);
                            character.sendMessage(gson.toJson(hariotikaMessage));
                            //  ServerWS.getSessionMap().get(character.getName()).getAsyncRemote().sendText("login#1#" + gson.toJson(ServerWS.getCharacterMap().get(character.getName())));

                        } else {
                            //Удаляем сесию, если она закрыта
                        }

                }
            }
        }, 1, 1, TimeUnit.SECONDS);


    }

    @Override
    public void run() {
     while (true) {
         try {
       //    Thread.sleep(1000);
/*
             //HP regeneration
         for (HashMap.Entry<String, Character> pair : ServerWS.getCharacterMap().entrySet()) {
             character = ServerWS.getCharacterMap().get(pair.getKey() );

             if (character.getHP()< character.getMaxHP() && !character.isInBattle() && character.getName()!= "Bot") {
                 character.setHP(character.getHP() + character.getHp_perSec());
                 ServerWS.getCharacterMap().get(character.getName()).setHP(character.getHP());
             }

             if (character.getMP()< character.getMaxMP() && !character.isInBattle() && character.getName()!= "Bot") {
                 character.setMP(character.getMP() + character.getMp_perSec());
                 ServerWS.getCharacterMap().get(character.getName()).setMP(character.getMP());
             }


             //Делаем расслку состония
             if (character.getName()!= "Bot")
             if (ServerWS.getSessionMap().get(pair.getKey()).isOpen() ) {
                 hariotikaMessage = new HariotikaMessage(Command.Login, WsCode.Success,character);
                 character.sendMessage(gson.toJson(hariotikaMessage));
               //  ServerWS.getSessionMap().get(character.getName()).getAsyncRemote().sendText("login#1#" + gson.toJson(ServerWS.getCharacterMap().get(character.getName())));

             } else {
                 //Удаляем сесию, если она закрыта
             }

         }*/
         } catch (Exception e) {
             e.printStackTrace();
        }


     }
    }
}
