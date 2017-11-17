package Domain;


import java.io.Serializable;

public class Battle {

    long number;
    private Character player1;
    private Character player2;

    private boolean player1IsReady = false;
    private boolean player2IsReady = false;

    private PartOfBody player1Def;
    private PartOfBody player2Def;

    private PartOfBody player1Hit;
    private PartOfBody player2Hit;


    public Battle(long number,Character player1, Character player2) {
        this.number = number;
        this.player1 = player1;
        this.player2 = player2;
    }


    public void fight(){
        if (!getPlayer1Hit().equals(getPlayer2Def()) && getPlayer1Hit()!= null)
        {
            player1.hit(player2);
            System.out.println("HP Игрока 1 "+player1.getHP());
        }
        if (!getPlayer2Hit().equals(getPlayer1Def())&& getPlayer2Hit()!= null)
        {
            player2.hit(player1);
            System.out.println("HP Игрока 2 "+player2.getHP());
        }

        player1IsReady =false;
        player2IsReady =false;

//        player1.sendMessage(gson.toJson(this));
 //       player2.sendMessage(gson.toJson(this));
    }



    public boolean isRedy(){
        return player1IsReady && player2IsReady;
    }

    public boolean isFinish(){
        if (player1.getHP()<=0 || player2.getHP()<=0)
            return true;
        else return false;
    }


    public void startfight() {
        System.out.println("Бой начался");
        while (true)
        {
            if (isRedy()){
                if (isFinish())
                    break;
                fight();
            }
            setPlayer1IsReady(true);
            setPlayer2IsReady(true);
            player1Def = PartOfBody.HEAD;
            player2Def= PartOfBody.HEAD;

            player1Hit= PartOfBody.BODY;
            player2Hit= PartOfBody.BODY;
        }
        System.out.println("Бой закончен");

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
}
