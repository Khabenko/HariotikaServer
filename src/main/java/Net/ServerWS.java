package Net;

import Domain.*;
import Domain.Character;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import db.Login;
import db.UpdateDB;
import db.Users;
import org.slf4j.*;

import java.io.IOException;
import java.util.*;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/")
public class ServerWS   {



    private static Map<String,Session> sessionMap = Collections.synchronizedMap(new HashMap<String, Session>());
    private static Map<String,Character> characterMap = Collections.synchronizedMap(new HashMap<String, Character>());
    private static GlobalUpdate globalUpdate = new GlobalUpdate();
    private static Arena arena = new Arena();

    private HariotikaMessage hariotikaMessage;
    private Gson gson = new Gson();
    private Session session;
    private Login login;


    @OnOpen
    public void onOpen(Session peer) throws IOException, InterruptedException {
        System.out.println("Open Connection ..." + peer);
        session = peer;
        session.setMaxTextMessageBufferSize(500000);
        session.setMaxBinaryMessageBufferSize(500000);
      //  session.setMaxIdleTimeout(30000);



    }

    @OnClose
    public void onClose(){
        System.out.println("Close Connection ...");
    }

    @OnMessage
    public void onMessage(String message) throws IOException, InterruptedException {
        parsingHariotikaMessage(message);


    }

    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }


    public void sendMessage(String message) {
        synchronized (session) {
            session.getMessageHandlers().clear();
            this.session.getAsyncRemote().sendText(message);
        }
    }


       public void verifyLogin(HariotikaMessage message){

                   login = new Login(message.getLogin(), message.getPassword());
               if (!message.getLogin().equals("null")){
                   if (login.loginIsPresent() && login.checkPass(message.getPassword())) {
                       sessionMap.put(login.getCharacter().getName(), session);
                       if (!characterMap.containsKey(login.getCharacter().getName())) {
                           characterMap.put(login.getCharacter().getName(), login.getCharacter());
                           System.out.println("Logining to game: "+message.getLogin());
                       }
                       hariotikaMessage = new HariotikaMessage(Command.Login,WsCode.Success,characterMap.get(login.getCharacter().getName()));
                       sendMessage(gson.toJson(hariotikaMessage));
                   }
                   else {
                       hariotikaMessage = new HariotikaMessage(Command.Login,WsCode.Reject);
                       sendMessage(gson.toJson(hariotikaMessage));
                   }
               }
               else if (message.getLogin().equals("null")) {
                   login.createNewUser();
                   sessionMap.put(login.getCharacter().getName(), session);
                   if (!characterMap.containsKey(login.getCharacter().getName()))
                   characterMap.put(login.getCharacter().getName(),login.getCharacter());
                   hariotikaMessage = new HariotikaMessage(Command.Login,WsCode.New,login.getCharacter());
                   System.out.println(hariotikaMessage);
                   sendMessage(gson.toJson(hariotikaMessage));
               }
       }

       public  void updateBattle(HariotikaMessage message ){
           Long number = Long.valueOf(message.getBattle().getNumber());
               String name = message.getCharName();
               PartOfBody wereHit = PartOfBody.valueOf(message.getHit());
               PartOfBody whatDef = PartOfBody.valueOf(message.getDef());
               try {
                   if (arena.getBattleList().containsKey(Long.valueOf(number))) {
                       if (arena.getBattleList().get(Long.valueOf(number)).getPlayer1().getName().equals(name)) {
                           //Мы первый игрок
                           arena.getBattleList().get(number).setPlayer1Defance(message.getPlayerDefance());
                           arena.getBattleList().get(number).setPlayer1Hit(wereHit);
                           arena.getBattleList().get(number).setPlayer1Def(whatDef);
                           arena.getBattleList().get(number).setPlayer1IsReady(true);
                       } else if (arena.getBattleList().get(Long.valueOf(number)).getPlayer2().getName().equals(name)) {
                           arena.getBattleList().get(number).setPlayer2Defance(message.getPlayerDefance());
                           arena.getBattleList().get(number).setPlayer2Hit(wereHit);
                           arena.getBattleList().get(number).setPlayer2Def(whatDef);
                           arena.getBattleList().get(number).setPlayer2IsReady(true);
                       }
                   } else {

                       sendMessage(gson.toJson(new HariotikaMessage(Command.Battle, WsCode.RemoveBattle)));

                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
       }

        private void registrationToBattle(){
           arena.addToArena(login.getCharacter());
           System.out.println("Registration on Battle");
           hariotikaMessage = new HariotikaMessage(Command.Battle,WsCode.Success);
           sendMessage(gson.toJson(hariotikaMessage));

           //add Bot
           Character character = new Character();
           character.setName("Bot");
            character.setMaxHP(60);
            character.setMaxMP(40);
            character.setMP(40);
            character.setHP(60);
            character.setStrength(3);
            character.setAgility(3);
            character.setIntuition(3);
            character.updatePlayerCharacteristics();
           character.setLvl(login.getCharacter().getLvl());
           characterMap.put(character.getName(), character);
           arena.addToArena(character);
       }

        private void cancelRegistrationToBattle(){
        arena.cancelRegBattle(login.getCharacter());
        hariotikaMessage = new HariotikaMessage(Command.Battle,WsCode.Success);
        sendMessage(gson.toJson(hariotikaMessage));
        System.out.println("Регистрация на батл отменена игорком "+login.getCharacter());


    }


    private void parsingHariotikaMessage(String message){
        try {
            hariotikaMessage = gson.fromJson(message,HariotikaMessage.class);
            switch (hariotikaMessage.getCommand()){
                case Login: commandLoginCode(hariotikaMessage);
                    break;
                case Battle: commandBattleCode(hariotikaMessage);
                    break;
                case Characteristic: commandCharacteristicCode(hariotikaMessage);
                    break;
                default:
                    System.out.println("Server sended "+message);
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void commandLoginCode(HariotikaMessage message){

        switch (message.getCode()){
            case Success:
                break;
            case Reject:
                break;
            case New:
                break;
            case Authorithation:
                verifyLogin(message);
                break;
            case UpdateCharacter: {
                // Незабудь проверку добавить баран, чтобы не взломали
                characterMap.put(message.getCharacter().getName(),message.getCharacter());
                characterMap.get(message.getCharacter().getName()).updatePlayerCharacteristics();
                UpdateDB.UpdateDB(characterMap.get(message.getCharacter().getName()));
            }
                break;
            default:
                System.out.println("Invalid WsCode "+message.getCode());
                break;
        }

    }

    private void commandBattleCode(HariotikaMessage message) {

        switch (message.getCode()) {
            case RegistrationToBattle:
                registrationToBattle();
                break;
            case CancelRegistrationToBattle:
                cancelRegistrationToBattle();
                break;
            case UpdateBattle:
                updateBattle(message);
                break;
            default:
                System.out.println("Invalid WsCode " + message.getCode());
                break;
        }
    }

    private void commandCharacteristicCode(HariotikaMessage message){
         Character character = characterMap.get(message.getCharacter().getName());

        if (character.getPointCharacteristics() >0) {
            switch (message.getCode()) {
                case Strength:
                    character.setPointCharacteristics(character.getPointCharacteristics() - 1);
                    character.setStrength(character.getStrength() + 1);
                    break;
                case Agility:
                    character.setPointCharacteristics(character.getPointCharacteristics() - 1);
                    character.setAgility(character.getAgility() + 1);
                    break;
                case Intuition:
                    character.setPointCharacteristics(character.getPointCharacteristics() - 1);
                    character.setIntuition(character.getIntuition() + 1);
                    break;
                case Wisdom:
                    character.setPointCharacteristics(character.getPointCharacteristics() - 1);
                    character.setWisdom(character.getWisdom() + 1);
                    break;
                case Vitality:
                    character.setPointCharacteristics(character.getPointCharacteristics() - 1);
                    character.setVitality(character.getVitality() + 1);
                    break;
                case Intelligence:
                    character.setPointCharacteristics(character.getPointCharacteristics() - 1);
                    character.setIntelligence(character.getIntelligence() + 1);
                    break;
                case Reset:
                    int total = 0 ;
                    total = character.getAgility()+character.getStrength()+character.getIntuition()+character.getWisdom()+character.getPointCharacteristics()+
                    +character.getVitality()+character.getIntelligence();
                    character.setStrength(0);
                    character.setAgility(0);
                    character.setIntuition(0);
                    character.setWisdom(0);
                    character.setVitality(0);
                    character.setIntelligence(0);
                    character.setPointCharacteristics(total);

                    characterMap.put(message.getCharacter().getName(),character);
                    characterMap.get(message.getCharacter().getName()).updatePlayerCharacteristics();
                    UpdateDB.UpdateDB(characterMap.get(message.getCharacter().getName()));
                    break;
                default:
                    System.out.println("Invalid WsCode " + message.getCode());
                    break;

            }

            characterMap.put(message.getCharacter().getName(),character);
            characterMap.get(message.getCharacter().getName()).updatePlayerCharacteristics();
            UpdateDB.UpdateDB(characterMap.get(message.getCharacter().getName()));
        }
        else if (message.getCode() == WsCode.Reset){

            int total = 0 ;
            total = character.getAgility()+character.getStrength()+character.getIntuition()+character.getWisdom()+character.getPointCharacteristics();
            character.setStrength(0);
            character.setAgility(0);
            character.setIntuition(0);
            character.setWisdom(0);
            character.setPointCharacteristics(total);

            characterMap.put(message.getCharacter().getName(),character);
            characterMap.get(message.getCharacter().getName()).updatePlayerCharacteristics();
            UpdateDB.UpdateDB(characterMap.get(message.getCharacter().getName()));

        }


    }




    public static GlobalUpdate getGlobalUpdate() {
        return globalUpdate;
    }

    public static void setGlobalUpdate(GlobalUpdate globalUpdate) {
        ServerWS.globalUpdate = globalUpdate;
    }

    public static Map<String, Character> getCharacterMap() {
        return characterMap;
    }

    public static void setCharacterMap(Map<String, Character> characterMap) {
        ServerWS.characterMap = characterMap;
    }

    public static Map<String, Session> getSessionMap() {
        return sessionMap;
    }

    public static void setSessionMap(Map<String, Session> sessionMap) {
        ServerWS.sessionMap = sessionMap;
    }

    public ServerWS getSoket(){
           return this;
    }
}
