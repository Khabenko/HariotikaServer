package Net;

import Domain.*;
import Domain.Character;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import db.Login;
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
                           System.out.println("В игру зашол "+message.getLogin());
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
           ArrayList<PartOfBody> playerDeanceList = message.getPlayerDefance();
           try {

           if (arena.getBattleList().get(Long.valueOf(number)).getPlayer1().getName().equals(name)){
               //Мы первый игрок
               arena.getBattleList().get(number).setPlayer1Defance(message.getPlayerDefance());
               arena.getBattleList().get(number).setPlayer1Hit(wereHit);
               arena.getBattleList().get(number).setPlayer1Def(whatDef);
               arena.getBattleList().get(number).setPlayer1IsReady(true);
           }
           else if (arena.getBattleList().get(Long.valueOf(number)).getPlayer2().getName().equals(name)) {
               arena.getBattleList().get(number).setPlayer2Defance(message.getPlayerDefance());
               arena.getBattleList().get(number).setPlayer2Hit(wereHit);
               arena.getBattleList().get(number).setPlayer2Def(whatDef);
               arena.getBattleList().get(number).setPlayer2IsReady(true);
              }
           }
           catch (Exception e){
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
           character.setHP(character.getHP());
           character.setStrength(character.getStrength());
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
            default:
                System.out.println("Invalid WsCode "+message.getCode());
                break;
        }

    }

    private void commandBattleCode(HariotikaMessage message){

        switch (message.getCode()){
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
                System.out.println("Invalid WsCode "+message.getCode());
                break;
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
