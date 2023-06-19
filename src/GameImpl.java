
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

import enums.Color;
import exceptions.IllegalMovementException;
import exceptions.IncorrectTurnOrderException;
import exceptions.InvalidCardException;
import exceptions.InvalidPieceException;

public class GameImpl implements Game {

    private Spot[][] board;
    private Player bluePlayer;
    private Player redPlayer;
    private Player currentPlayer;
    private boolean isGameOver;
    private Scanner scanner;
    private Card tableCard;
    private Card[] deck;
    
    Piece[] pieces;
    
    public GameImpl(){
        deck = Card.createCards();
        this.redPlayer = new Player("PlayerRed", Color.RED, deck);
        this.bluePlayer = new Player("PlayerBlue", Color.BLUE, deck);
        tableCard = deck[0];
        initializeBoard();
    }

    public GameImpl(String nameRedPlayer, String nameBluePlayer){
        deck = Card.createCards();
        this.redPlayer = new Player(nameRedPlayer, Color.RED, deck);
        this.bluePlayer = new Player(nameBluePlayer, Color.BLUE, deck);
        currentPlayer = redPlayer;
        tableCard = deck[0];
        initializeBoard();
        playGame();
    }

    public GameImpl(String nameRedPlayer, String nameBluePlayer, Card [] customDeck){
        deck = Arrays.copyOf(customDeck, customDeck.length);
        Collections.shuffle(Arrays.asList(deck));

        tableCard = deck[0];

        Card[] playerCards = Arrays.copyOfRange(deck, 1, 6);

        this.redPlayer = new Player(nameRedPlayer, Color.RED, playerCards);
        this.bluePlayer = new Player(nameBluePlayer, Color.BLUE, playerCards);
        initializeBoard();
    }

    /**
     * Método que devolve a cor da posição do tabuleiro. Se possui uma cor, significa que é um templo. Caso contrário, é um espaço normal
     * @param position Posição do tabuleiro
     * @return O enum Color que representa a cor da posição
     */
    public Color getSpotColor(Position position){
        return board[position.getRow()][position.getCol()].getColor();
    }

    /**
     * Método que devolve a peça que está na posição do tabuleiro
     * @param position Posição do tabuleiro
     * @return Um objeto Piece que representa a peça na posição indicada. Se não tiver peça, devolve null
     */
    public Piece getPiece(Position position){
        return board[position.getRow()][position.getCol()].getPiece();
    }

    /**
     * Método que devolve a carta que está na mesa, que será substituída após a próxima jogada
     * @return Um objeto Card que representa a carta na mesa
     */
    public Card getTableCard(){
        return tableCard;
    }

    /**
     * Método que devolve as informações sobre o jogador com as peças vermelhas
     * @return Um objeto Player que representa o jogador vermelho
     */
    public Player getRedPlayer(){
        return redPlayer;
    }

    /**
     * Método que devolve as informações sobre o jogador com as peças azuis
     * @return Um objeto Player que representa o jogador azul
     */
    public Player getBluePlayer(){
        return bluePlayer;
    };

    /**
     * Método que move uma peça
     * @param piece A peça que irá mover
     * @param card A carta de movimento que será usada
     * @param position A posição da carta para onde a peça irá se mover
     * @exception IncorrectTurnOrderException Caso não seja a vez de um jogador fazer um movimento
     * @exception IllegalMovementException Caso uma peça seja movida para fora do tabuleiro ou para uma posição onde já tem uma peça da mesma cor
     * @exception InvalidCardException Caso uma carta que não está na mão do jogador seja usada
     * @exception InvalidPieceException Caso uma peça que não está no tabuleiro seja usada
     */
    public void makeMove(Card card, Position cardMove, Position currentPos) throws IncorrectTurnOrderException, IllegalMovementException, InvalidCardException, InvalidPieceException {
        // if (!board[cardMove.getRow()][cardMove.getCol()].isValid()){
        //     throw new IllegalMovementException("Movimento fora do tabuleiro");
        // }

        // if (board[cardMove.getRow()][cardMove.getCol()].isOccupied()){
        //     throw new IllegalMovementException("A posição já está ocupada");
        // }

        // if (!validMove(card, cardMove, currentPos)){
        //     throw new IllegalMovementException("Movimento inválido para a carta");
        // }

        Spot currentSpot = board[currentPos.getRow()][currentPos.getCol()];

        Spot destinationSpot = board[cardMove.getRow()][cardMove.getCol()];

        // if (currentPlayer.getPieceColor() != currentSpot.getPiece().getColor()){
        //     throw new IncorrectTurnOrderException("Não é a vez do jogador fazer um movimento.");
        // }

        // if (destinationSpot.isOccupied() && destinationSpot.getPiece().getColor() == currentSpot.getPiece().getColor()){
        //     throw new IllegalMovementException("Posição já ocupada por peça de mesma cor");
        // }

        destinationSpot.occupySpot(currentSpot.getPiece());
        currentSpot.releaseSpot();


        currentPlayer.swapCard(card, tableCard);

        this.tableCard = card;


        currentPlayer = (currentPlayer == bluePlayer) ? redPlayer : bluePlayer;
        

        


    }

    /**
     * Método que confere se um jogador de uma determinada cor venceu o jogo. Critérios de vitória:
     * — Derrotou a peça de mestre adversária
     * — Posicionou o seu mestre na posição da base adversária
     * @param color Cor das peças do jogador que confere a condição de vitória
     * @return Um booleano true para caso esteja em condições de vencer e false caso contrário
     */
    public boolean checkVictory(Color color){
        Player player = (color == color.RED) ? redPlayer : bluePlayer;
        Player opponent = (player.getPieceColor() == color.RED) ? bluePlayer: redPlayer;
        

        Spot opponentTemple = getTempleSpot(opponent.getPieceColor());

        Piece piece = opponentTemple.getPiece();

        if (piece != null && piece.isMaster() && piece.getColor() == player.getPieceColor()){
            return true;
        }
        
        Spot masSpot = null;

        for (int row = 0; row < 5; row++){
            for (int col = 0; col < 5; col++ ){
                masSpot = board[row][col];
                Piece auxPiece = masSpot.getPiece();
                if (auxPiece != null && auxPiece.isMaster() && auxPiece.getColor() == opponent.getPieceColor()){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Método que imprime o tabuleiro no seu estado atual
     * OBS: Esse método é opcional não será utilizado na correção, mas serve para acompanhar os resultados parciais do jogo
     */
    // public void printBoard(){
    //     System.out.println("   0  1  2  3  4");
    //     System.out.println("----------------");
    //     for (int i = 0; i < 5; i++){
    //         System.out.print(i + "|");
    //         for (int j = 0; j < 5; j++){
    //             Spot s = board[i][j];
    //             if(s.isOccupied()){
    //                 Piece piece = s.getPiece();
    //                 System.out.print(piece + " ");
    //             }
    //             else {
    //                 System.out.print("  ");
    //             }
    //         }
    //         System.out.println("|" + i);
    //     }

    //      System.out.println("----------------");
    //      System.out.println(" 0  1  2  3  4");

    // }

    public void printBoard(){

      System.out.println("Tabuleiro:");

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Spot spot = board[row][col];
                Piece piece = spot.getPiece();

                if (piece != null) {
                    System.out.print(piece.toString() + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }

        System.out.println();
    }

    public void initializeBoard(){
        board = new Spot[5][5];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (row == 0) {
                    board[row][col] = new Spot(new Piece(Color.BLUE, false), new Position(row, col));
                } else if (row == 4) {
                    board[row][col] = new Spot(new Piece(Color.RED, false), new Position(row, col));
                }else{
                    board[row][col] = new Spot(new Position(row, col));

                }

            }
        }

        board[0][2] = new Spot(new Piece(Color.BLUE, true), new Position(0, 2));
        board[4][2] = new Spot(new Piece(Color.RED, true), new Position(4, 2));
    }

    private void capturePiece(Position position){
        Spot spot = board[position.getRow()][position.getRow()];
        Piece piece = spot.getPiece();

        if(piece != null && piece.isMaster()){
            //vitoria
        }
        //spot.occupySpot(piece);
        spot.releaseSpot();
        //newSpot.occupySpot(piece);
    }


private Spot getPieceSpot(Piece piece) {
    for (int row = 0; row < board.length; row++) {
        for (int col = 0; col < board[row].length; col++) {
            Spot spot = board[row][col];
            if (spot != null && spot.getPiece() == piece) {
                return spot;
            }
        }
    }
    return null;

 }


//  private boolean isOccupiedBySameColor(Position position, Color color) {
//     Piece piece = board[position.getRow()][position.getCol()].getPiece();
//     return piece != null && piece.getColor() == color;
// }

public boolean validMove(Card card, Position cardMove, Position currPosition){
    Spot currentSpot = board[currPosition.getRow()][currPosition.getCol()];

    if (currentSpot == null && !currentSpot.isOccupied()){
        throw new InvalidPieceException("Peça inválida");
    }

    if(!board[cardMove.getRow()][cardMove.getCol()].isValid()){
        throw new IllegalMovementException("Posição inválida");
    }

    Position [] possiblePositions = card.getPositions();
    boolean validPosition = false;

    for (Position pos : possiblePositions){
        if(pos.equals(cardMove)){
            validPosition = true;
            break;
        }
    }

    if(!validPosition){
        throw new IllegalMovementException("Posição inválida pra essa carta");
    }

    return true;
}

private Spot getTempleSpot(Color color) {
    if (color == Color.BLUE) {
        return board[0][2]; // Exemplo de posição do templo azul
    } else if (color == Color.RED) {
        return board[4][2]; // Exemplo de posição do templo vermelho
    }
    return null; // Retornar null para outras cores
}

private void playGame(){

    Scanner scanner = new Scanner(System.in);

    while(!checkVictory(currentPlayer.getPieceColor())){
        printBoard();

        Card selectedCard = selectCard();

        Position currPosition = selectPiecePosition();

        Position moveCard = selectMovePosition(selectedCard);  

        try {
            makeMove(selectedCard, moveCard, currPosition);
        }

        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    printBoard();

    Player winner = (currentPlayer == redPlayer) ? bluePlayer : redPlayer;
    System.out.println("Parabéns, " + winner.getName() + "! Você venceu o jogo!");

}

private Card selectCard(){
    Card [] cards = deck;

    for (int i = 0; i < cards.length; i++){
        System.out.println((i+1) + ": " + cards[i].getName());
    }

    Scanner scanner = new Scanner(System.in);
    int cardIndex = scanner.nextInt();

    Card selectedCard = cards[cardIndex - 1];

    return selectedCard;

}

private Position selectMovePosition(Card card) {
        System.out.println("Jogador " + currentPlayer.getName() + ", selecione uma posição de movimento:");
        System.out.println(card.getPositionsAsString());

        System.out.println("Digite a linha:");
        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();
        System.out.println("Digite a coluna:");
        int col = scanner.nextInt();

        Position pos = new Position(row, col);

        return pos;
    }

private Position selectPiecePosition() {
        System.out.println("Jogador " + currentPlayer.getName() + ", selecione a posição da peça a ser movida:");
        //System.out.println(currentPlayer.getOwnedPiecesAsString());

        System.out.println("Digite a linha:");
        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();
        System.out.println("Digite a coluna:");
        int col = scanner.nextInt();

        Position pos = new Position(row, col);

        return pos;
    }

}
