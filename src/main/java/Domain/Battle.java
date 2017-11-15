package Domain;

import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;

import java.util.ArrayList;

public class Battle {
    int number;
    private Character player1;
    private Character player2;

    private boolean player1IsReady = false;
    private boolean player2IsReady = false;

    private PartOfBody player1Def;
    private PartOfBody player2Def;

    private PartOfBody player1Hit;
    private PartOfBody player2Hit;


    public Battle(Character player1, Character player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Battle fight(){
        if (!getPlayer1Hit().equals(getPlayer2Def()) && getPlayer1Hit()!= null)
        {
            player1.hit(player2);
        }
        if (!getPlayer2Hit().equals(getPlayer1Def())&& getPlayer2Hit()!= null)
        {
            player2.hit(player1);
        }

        return this;
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


}
