import java.util.ArrayList;
import java.util.Scanner;

/**
 * Simple Procedural BlackJack Game 
 * 
 */
public class BlackJack
{
    // instance variables - accessible by all methods
    ArrayList<Integer> deck = new ArrayList<Integer>();
    Scanner scan = new Scanner(System.in);
    

    /**
     * Constructor for objects of class BlackJack
     */
    public BlackJack()
    {
        initializeDeck();
    }
    
    /**
     * Implements the game mechanics of blackJack for two players
     */
    public void playGame() {
        
        wantToPlay();
        while(wantToPlay==true && playerMoney > 0){ //runs the game if the user wants to play and has money
           playerBet();
           for(int i=0; i<2; i++){//repeats twice to deal the player two cards
              dealPlayer();
           }
           dealDealer();
           displayShowing();
           playerHit(); 
           if(playerBust == false){ //only executes if the player has busted because the dealer automaticaly wins
              dealerHit();
           }
           if(playerBust == false && dealerBust == false){
               //only executes if neither player has busted because if they have the winner would have already been determined
              evaluateHandWinner();
           }
           betReturn();
           playAgain();
           resetGame();
        }
        System.out.println("Thanks for playing.");
    }
    
    
    int playerTotal = 0;
    int dealerTotal=0;
    boolean wantToPlay;
    int betAmt=0;
    int playerMoney=100;
    boolean aceDealt = false;
    int cardDealt;
    int dealerShowing;
    boolean playerBust = false;
    boolean dealerBust = false;
    boolean playerWins;
    String hitOrStay;
    boolean tie=false;
    
    /**
     * Asks the player if they would like to play Black Jack and creates a variable to be used to decide
     * whether or not to continue the game.
     */
    public void wantToPlay(){
        System.out.println("Would you like to play Black Jack? " + "Y/N");
        Scanner play = new Scanner(System.in);
        String yesOrNo = play.next();
        if(yesOrNo.equals("Y")){
            wantToPlay = true;
        }
        else{
            wantToPlay = false;
        }
    }
    /**
     * Asks the user if they would like to play at the end of a round.
     * Only works if the player has money with which they can bet.
     */
    public void playAgain(){
        if(playerMoney>0){
           System.out.println("Would you like to play again? " + "Y/N");
           Scanner play = new Scanner(System.in);
           String yesOrNo = play.next();
           if(yesOrNo.equals("Y")){
               wantToPlay = true;
           }
           else{
               wantToPlay = false;
           }
        }
        else{
            System.out.println("You ran out of money!");
            wantToPlay=false;
        }
    }
    /**
     * Deals a card to the player and adds it to the total.
     * If an 11 is dealt, the method remembers that so if the player's total
     * exceeds 21 then the eleven is turned into a 1 by subtracting 10.
     * If an ace has not been dealt and the players total is greater than 21 
     * it remembers that the player has busted.
     */
    public void dealPlayer(){
        cardDealt = dealCard();
        if(cardDealt == 11){//records whether an ace has been dealt
            aceDealt = true;
            System.out.println("You were dealt an ace worth 11");
        }
        playerTotal = playerTotal + cardDealt;
        if(playerTotal > 21 && aceDealt == true){//changes the value if the player holds one and the change is necessary
            playerTotal = playerTotal - 10;
            System.out.println("Your ace's value was changed from 11 to 1");
            aceDealt=false;
        }
        else if(playerTotal > 21){ //records whether the player has busted
            playerBust = true;
        }
        
    }
    /**
     * Deals two cards to the dealer. Two variables are used to hold the value
     * displayed to the user as well as the true total of the dealer's hand.
     */
    public void dealDealer(){
        dealerTotal = dealerTotal + dealCard();
        //adds first card dealt to the dealers total as the face down card
        dealerShowing = dealCard();
        //sets a new value for what the dealer is shoiwng
        dealerTotal = dealerTotal + dealerShowing;
        //records the dealers total by adding first card and second card
    }
    /**
     * Asks the player if they would like to hit. If they do, the user will
     * continue to have the option to hit until they either choose to stop
     * or bust.
     */
    public void playerHit(){
        System.out.println("Would you like to hit or stay? H/S"); 
        Scanner hit = new Scanner(System.in);
        hitOrStay = hit.next();
        while(hitOrStay.equals("H")){
             //while the player continues to hit they are dealt a new card, evaluates the winner if they bust
            dealPlayer();
              if (playerTotal <= 21){
                System.out.println("Your total is now " + playerTotal);
                System.out.println("Would you like to hit or stay? H/S"); 
                Scanner hitAgain = new Scanner(System.in);
                hitOrStay = hitAgain.next();
            }
            else{
                System.out.println("Your total is now " + playerTotal);
                hitOrStay = "S"; 
                playerWins = false;
                evaluateHandWinner();
            }
        }
    }
    /**
     * If the player has not busted, the dealer is dealt new cards until 
     * the dealer's total is equal to or greater than 17. 
     * If the dealer busts, it stores that as a boolean and evaluates the 
     * hand winner.
     */
    public void dealerHit(){
        if(playerTotal <= 21) {
           while(dealerTotal < 17){
            dealerShowing = dealerShowing + dealCard();
            dealerTotal = dealerShowing + dealerTotal;
           }
           if(dealerTotal > 21){
            dealerBust = true;
            evaluateHandWinner();
           }
        }
    }
    /**
     * Recieves desired bet amount and uses it if the player has enough money
     * but asks for a new value if the player doesn't have enough money.
     */
    public void playerBet(){
        betAmt = getBet();
        if (betAmt <= playerMoney){
         playerMoney = playerMoney - betAmt;
         System.out.println("You bet " + betAmt + " dollars");
         System.out.println("You have " +playerMoney+ " dollars left");
        }
        else{
          System.out.println("You do not have enough money to bet this amount.");
          betAmt=0;
          playerBet();
        }
        
    }
    /**
     * Displays the player's hand total and the total of the cards the dealer
     * is showing. 
     */
    public void displayShowing(){
        System.out.println("Your total is " + playerTotal);
        System.out.println("The dealer is showing " + dealerShowing);
    }
    /**
     * Runs through the possible outcomes of the blackjack hand and sets playerWins to true or false 
     * depending on the outcome. Prints the outcome determined to the console. If the hand is a tie 
     * the boolean variable tie is set to true.
     */
    public void evaluateHandWinner() {
        if(playerBust == false && playerTotal > dealerTotal && playerTotal !=21){
            System.out.println("Your final total is " + playerTotal);
            System.out.println("The dealer's total is " + dealerTotal);
            System.out.println("You win!");
            playerWins=true;
        }
        if(playerTotal == 21 && dealerTotal != 21 && dealerBust==false){
            System.out.println("You have 21! You win!");
            playerWins=true;
        }
        if(dealerTotal == 21 && playerTotal != 21 && playerBust==false){
            System.out.println("The dealer has 21! You lose!");
            playerWins=false;
        }
        if(dealerBust == false && dealerTotal > playerTotal && dealerTotal !=21){
            System.out.println("Your final total is " + playerTotal);
            System.out.println("The dealer's total is " + dealerTotal);
            System.out.println("You lose!");
            playerWins=false;
        }
        if(playerBust == true && dealerBust != true){
            System.out.println("You busted! You lose!");
            playerWins=false;
        }
        if(dealerBust == true && playerBust != true){
            System.out.println("The dealer busted! You win!");
            playerWins=true;
        }
        if(playerTotal == dealerTotal){
            tie = true;
        }
    }
    /**
     * After the winner of the hand has been determined, betReturn() tells the player what
     * their prize or loss is and tells them how much money they are left with.
     */
    public void betReturn(){
        if(playerWins == true && tie==false){
            playerMoney = playerMoney + (2*betAmt);
            System.out.println("You won the hand and got twice your bet in return!");
            System.out.println("You now have " + playerMoney + " dollars");
        }
        if(playerWins ==false && tie == false){
            System.out.print("You lost the hand...and your bet.");
            System.out.println("You now have " + playerMoney + " dollars.");
        }
        if(tie==true){
            playerMoney = playerMoney + betAmt;
            System.out.println("You tied the dealer! Your bet has been returned");
            System.out.println("You now have " + playerMoney + " dollars.");
        }
    }
    /**
     * Sets all variables to their initial values for the next hand of BlackJack
     */
    public void resetGame(){
        resetDeck();
        playerTotal = 0;
        dealerTotal = 0;
        betAmt = 0;
        aceDealt = false;
        dealerShowing = 0;
        playerBust = false;
        dealerBust = false;
        tie = false;
    }
    
    
    
    
    /**
     * Used to get the bet amount (int) from the user
     * Further, caputures the return key being pressed so further scan events work.
     * @return: Bet Amount
     */
    public int getBet() {
            int amount = 0;
            System.out.println("What amount would you like to bet?");
            amount = scan.nextInt();
            
            //Need to capture return key being pressed
            System.out.println("");
            String trash = scan.nextLine(); //clears scanner input screen
            
            return amount;
    }
    
    /**
     * Uses a list of card values.  Where the ACE is set to 11 to poplulate an arraylist
     * with the correct distribution of cards and values
     */
    public void initializeDeck() {
        //List of card values, ACE set at 11 to start
        int[] values = {2, 3, 4 ,5 ,6, 7, 8, 9, 10, 10, 10, 10, 11};
        
        /*
         * For each suit (clubs, spades, hearts, diamonds) loop through
         * and add the value to the deck
         */
        for(int i = 0; i < 4; i++) {
            for (int num : values) {
                deck.add(num);
            }
        }
        
    }
    
    /**
     * Randomly deals a card from the deck (based on deck size)
     * @return:  int from possible card values.  Ace will be 11
     */
    public int dealCard() {
        //Get a random card from the deck
        int location;
        int deckLength = deck.size();
        //Use Math.random to get a random card from the deck
        location =(int)(Math.random()*deckLength);
        
        //Return and remove the specified card
        return deck.remove(location);
    }
    
    /**
     * Creates a new (empty) arrayList to hold card values.  It will then use the initialize
     * deck method to get 52 cards with the correct numeric distribution
     */
    public void resetDeck() {
        //Clears all cards from the deck.  Creates a new deck
        deck = new ArrayList<Integer>();
        initializeDeck();
    }
    
    
}
