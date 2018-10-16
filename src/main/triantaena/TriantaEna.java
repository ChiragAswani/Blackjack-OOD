package triantaena;

import com.blackjack.cards.Deck;
import com.blackjack.cards.Card;
import com.blackjack.cards.PlayingDeckInterface;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Dictionary;
import java.util.*;

public final class TriantaEna {

    public static int findMaxHandAndStay(Dictionary [] playerData){
        int maxHandAndStay = 0;
        for (int i = 0; i < playerData.length; i++){
            if (((Integer)playerData[i].get("currentHandTotal") > maxHandAndStay) && ((String)playerData[i].get("currentStatus")).equals("stay")){
                maxHandAndStay = (Integer)playerData[i].get("currentHandTotal");
            }
        }
        return maxHandAndStay;
    }

    public static void main(String[] args) {
        PlayingDeckInterface playingDeck = new Deck(); //initialized 2 decks in blackjack so this works
        playingDeck.shuffle();
        System.out.println("How many players are playing? 2-9");
        Scanner sc = new Scanner(System.in);
        Integer numPlayers = sc.nextInt();
        while (!(2 <= numPlayers && 9 >= numPlayers)){
            System.out.println("Not a valid amount of players. Choose 2-9");
            numPlayers = sc.nextInt();
        }
        Dictionary[] playerData = new Dictionary[numPlayers];
        Integer startingbalance = 100;
        for (int player = 0; player < numPlayers; player++){
            Dictionary dict = new Hashtable();
            ArrayList<Card> cards = new ArrayList<Card>();
            dict.put("playerName", "player" + Integer.toString(player));
            dict.put("currentBet", 0);
            dict.put("currentHand", cards);
            dict.put("currentHandTotal", 0);
            dict.put("currentStatus", "default");
            if (player == 0){
                dict.put("currentBalance", startingbalance*3);
                dict.put("isBanker", true);
            } else {
                dict.put("currentBalance", startingbalance);
                dict.put("isBanker", false);
            }
            playerData[player] = dict;
        }
        int rounds = 1;
        while(true){
            System.out.println("Welcome to Round " + Integer.toString(rounds));
            for (int i = 0; i < playerData.length; i++){
                Card c = playingDeck.draw();
                Object currentHand = playerData[i].get("currentHand");
                ArrayList tempCurrentHand = (ArrayList) currentHand;
                tempCurrentHand.add(c);

                playerData[i].put("currentHand", tempCurrentHand);
                playerData[i].put("currentHandTotal", c.value());
            }
            for (int i = 0; i < playerData.length; i++){
                if ((Boolean)playerData[i].get("isBanker")){
                    System.out.println(playerData[i].get("playerName").toString() + " is the Banker. Cards: " + playerData[i].get("currentHand") + " Balance: " + playerData[i].get("currentBalance"));
                    break;
                }

            }

            for (int i = 0; i < playerData.length; i++){
                if ((Boolean) playerData[i].get("isBanker")){
                    playerData[i].put("currentStatus", "play");
                    continue;
                }
                System.out.println(playerData[i].get("playerName").toString() + " place your bet or fold. Cards: " + playerData[i].get("currentHand") + " Balance: " + playerData[i].get("currentBalance"));
                String decision = sc.next();
                if (decision.equals("fold")){
                    playerData[i].put("currentStatus", "fold");
                } else {
                    //Card 2 Default
                    Card c = playingDeck.draw();
                    ArrayList tempCurrentHand = (ArrayList) playerData[i].get("currentHand");
                    tempCurrentHand.add(c);
                    Integer tempCurrentHandTotal = (Integer) playerData[i].get("currentHandTotal");
                    tempCurrentHandTotal += c.value();
                    playerData[i].put("currentStatus", "play");
                    playerData[i].put("currentHand", tempCurrentHand);
                    playerData[i].put("currentBet", Integer.parseInt(decision));
                    playerData[i].put("currentHandTotal", tempCurrentHandTotal);

                    //Card 3 Default
                    Card c2 = playingDeck.draw();
                    ArrayList tempCurrentHand2 = (ArrayList) playerData[i].get("currentHand");
                    tempCurrentHand2.add(c2);
                    Integer tempCurrentHandTotal2 = (Integer) playerData[i].get("currentHandTotal");
                    tempCurrentHandTotal2 += c2.value();
                    playerData[i].put("currentStatus", "play");
                    playerData[i].put("currentHand", tempCurrentHand2);
                    playerData[i].put("currentBet", Integer.parseInt(decision));
                    playerData[i].put("currentHandTotal", tempCurrentHandTotal2);
                }
            }

            for (int i = 0; i < playerData.length; i++){
                if ((Boolean) playerData[i].get("isBanker")){
                    playerData[i].put("currentStatus", "play");
                    continue;
                }
                if (playerData[i].get("currentStatus").toString().equals("fold")){
                    continue;
                }
                System.out.println(playerData[i].get("playerName").toString() + "'s hand " + playerData[i].get("currentHand") + " Value: " +playerData[i].get("currentHandTotal"));
                while(playerData[i].get("currentStatus").toString().equals("play")){
                    if((Integer) playerData[i].get("currentHandTotal") > 31){
                        System.out.println(playerData[i].get("playerName").toString() + " Busted!");
                        playerData[i].put("currentStatus", "bust");
                        continue;
                    }
                    System.out.println((playerData[i].get("playerName").toString()) + " Hit or Stay?");
                    String hitOrStay = sc.next();
                    if (hitOrStay.equals("stay")){
                        playerData[i].put("currentStatus", "stay");
                    } else {
                        Card c2 = playingDeck.draw();
                        ArrayList tempCurrentHand2 = (ArrayList) playerData[i].get("currentHand");
                        tempCurrentHand2.add(c2);
                        Integer tempCurrentHandTotal2 = (Integer) playerData[i].get("currentHandTotal");
                        tempCurrentHandTotal2 += c2.value();
                        playerData[i].put("currentStatus", "play");
                        playerData[i].put("currentHand", tempCurrentHand2);
                        playerData[i].put("currentHandTotal", tempCurrentHandTotal2);
                        System.out.println(playerData[i].get("playerName").toString() + "'s hand " + playerData[i].get("currentHand") + " Value: " +playerData[i].get("currentHandTotal"));
                    }
                }

            }
            //if everyone busted or folded
            int countFoldedOrBusted = 0;
            for (int i = 0; i < playerData.length; i++){
                String currentStatus = playerData[i].get("currentStatus").toString();
                if(currentStatus.equals("bust") || currentStatus.equals("fold")){
                    countFoldedOrBusted += 1;
                }
            }
            if (countFoldedOrBusted == playerData.length-1){
                System.out.println("Since everyone either busted or folded, dealer wins");
            }

            //Dealer's Turn
            for (int i = 0; i < playerData.length; i++){
                if ((Boolean)playerData[i].get("isBanker")){
                    System.out.println(playerData[i].get("playerName").toString() + "'s hand " + playerData[i].get("currentHand") + " Value: " +playerData[i].get("currentHandTotal"));
                    while(playerData[i].get("currentStatus").toString().equals("play")){
                        if((Integer) playerData[i].get("currentHandTotal") > 31){
                            System.out.println(playerData[i].get("playerName").toString() + " Busted!");
                            playerData[i].put("currentStatus", "bust");
                            break;
                        } else if ((Integer) playerData[i].get("currentHandTotal") < 27 && (findMaxHandAndStay(playerData)) > (Integer) playerData[i].get("currentHandTotal")){
                            Card c2 = playingDeck.draw();
                            ArrayList tempCurrentHand2 = (ArrayList) playerData[i].get("currentHand");
                            tempCurrentHand2.add(c2);
                            Integer tempCurrentHandTotal2 = (Integer) playerData[i].get("currentHandTotal");
                            tempCurrentHandTotal2 += c2.value();
                            playerData[i].put("currentStatus", "play");
                            playerData[i].put("currentHand", tempCurrentHand2);
                            playerData[i].put("currentHandTotal", tempCurrentHandTotal2);
                            System.out.println(playerData[i].get("playerName").toString() + "'s hand " + playerData[i].get("currentHand") + " Value: " +playerData[i].get("currentHandTotal"));
                        } else {
                            System.out.println(playerData[i].get("playerName").toString() + " Stays!");
                            playerData[i].put("currentStatus", "stay");
                            break;
                        }
                    }
                }
            }
            Dictionary bankerData = new Hashtable();
            for (int i = 0; i < playerData.length; i++){
                if((Boolean)playerData[i].get("isBanker")){
                    bankerData = playerData[i];
                }
            }
            //if Dealer Busts: players who "stay" win
            if (bankerData.get("currentStatus").toString().equals("bust")){
                for (int i = 0; i < playerData.length; i++) {
                    if (playerData[i].get("currentStatus").toString().equals("stay")) {
                        System.out.println(playerData[i].get("playerName") + " beats dealer");
                        Integer playerBet = (Integer) playerData[i].get("currentBet");
                        Integer playerGain = (Integer) playerData[i].get("currentBalance") + playerBet;
                        Integer bankerLoss = (Integer) bankerData.get("currentBalance") - playerBet;
                        playerData[i].put("currentBalance", playerGain);
                        bankerData.put("currentBalance", bankerLoss);
                    }
                }
            } else { //determine winner
                for (int i = 0; i < playerData.length; i++){
                    if ((Boolean) playerData[i].get("isBanker")) continue;
                    Integer playerBet = (Integer) playerData[i].get("currentBet");
                    Integer playerLoss = (Integer) playerData[i].get("currentBalance") - playerBet;
                    Integer playerGain = (Integer) playerData[i].get("currentBalance") + playerBet;
                    Integer bankerGain = (Integer) bankerData.get("currentBalance") + playerBet;
                    Integer bankerLoss = (Integer) bankerData.get("currentBalance") - playerBet;
                    if(playerData[i].get("currentStatus").toString().equals("stay")){
                        if ((Integer) bankerData.get("currentHandTotal") > (Integer) playerData[i].get("currentHandTotal")){
                            System.out.println(playerData[i].get("playerName") + " loses to dealer");
                            playerData[i].put("currentBalance", playerLoss);
                            bankerData.put("currentBalance", bankerGain);
                        } else if ((Integer) bankerData.get("currentHandTotal") < (Integer) playerData[i].get("currentHandTotal")){
                            System.out.println(playerData[i].get("playerName") + " beats dealer");
                            playerData[i].put("currentBalance", playerGain);
                            bankerData.put("currentBalance", bankerLoss);
                        } else {
                            System.out.println(playerData[i].get("playerName") + " ties with dealer");
                        }
                    } else { //player == busts
                        System.out.println(playerData[i].get("playerName") + " loses to dealer");
                        playerData[i].put("currentBalance", playerLoss);
                        bankerData.put("currentBalance", bankerGain);
                    }
                }
            }
            for (int i = 0; i < playerData.length; i++){
                if ((Boolean) playerData[i].get("isBanker")){
                    playerData[i] = bankerData;
                }
            }

            int max = 0;
            Dictionary newBanker = new Hashtable();
            for (int i = 0; i < playerData.length; i++){
                if ((Integer)playerData[i].get("currentBalance") > max){
                    max = (Integer)playerData[i].get("currentBalance");
                    newBanker = playerData[i];
                }
            }
            System.out.println(newBanker.get("playerName").toString() + " is the new banker");
            for (int i = 0; i < playerData.length; i++){
                if ((Boolean) playerData[i].get("isBanker")){
                    playerData[i].put("isBanker", false);
                }
            }
            for (int i = 0; i < playerData.length; i++){
                if (playerData[i].get("playerName").equals(newBanker.get("playerName"))){
                    newBanker.put("isBanker", true);
                    playerData[i] = newBanker;
                    break;
                }
            }

            System.out.println("Object Round Summary");
            for (int i = 0; i < playerData.length; i++){
                System.out.println(playerData[i].toString());
            }
            for (int i = 0; i < playerData.length; i++){
                playerData[i].put("currentStatus", "default");
                playerData[i].put("currentHand", new ArrayList<Card>());
                playerData[i].put("currentBet", 0);
                playerData[i].put("currentHandTotal", 0);
            }
            rounds +=1;
        }

    }
}
