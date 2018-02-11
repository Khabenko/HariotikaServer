package Domain;

import java.util.ArrayList;

public class RoundLogs{


    private int playerDamaged;
    private boolean enemyBlock;
    private boolean enemyDodge;
    private boolean enemyParry;
    private boolean playerCounterattack;
    private boolean playerCritkal;
    private PartOfBody hit;

    public RoundLogs() {
        this.enemyDodge = true;
        this.enemyParry = true;
    }

    public int getPlayerDamaged() {
        return playerDamaged;
    }

    public void setPlayerDamaged(int playerDamaged) {
        this.playerDamaged = playerDamaged;
    }

    public boolean isEnemyDodge() {
        return enemyDodge;
    }

    public void setEnemyDodge(boolean enemyDodge) {
        this.enemyDodge = enemyDodge;
    }

    public boolean isEnemyParry() {
        return enemyParry;
    }

    public void setEnemyParry(boolean enemyParry) {
        this.enemyParry = enemyParry;
    }

    public boolean isPlayerCounterattack() {
        return playerCounterattack;
    }

    public void setPlayerCounterattack(boolean playerCounterattack) {
        this.playerCounterattack = playerCounterattack;
    }

    public boolean isPlayerCritkal() {
        return playerCritkal;
    }

    public void setPlayerCritkal(boolean playerCritkal) {
        this.playerCritkal = playerCritkal;
    }

    public boolean isEnemyBlock() {
        return enemyBlock;
    }

    public void setEnemyBlock(boolean enemyBlock) {
        this.enemyBlock = enemyBlock;
    }

    public PartOfBody getHit() {
        return hit;
    }

    public void setHit(PartOfBody hit) {
        this.hit = hit;
    }
}
