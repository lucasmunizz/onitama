
import java.util.Arrays;
import java.util.InputMismatchException;
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
    private Card tableCard;
    private Card[] deck;
    
    public GameImpl(){
        deck = Card.createCards();
        tableCard = deck[0];
        Color startingColor = tableCard.getColor();
        Card [] redPlayerCards = {deck[1], deck[2]};
        Card [] bluePlayerCards = {deck[3], deck[4]};
        this.redPlayer = new Player("Red Player", Color.RED, redPlayerCards);
        this.bluePlayer = new Player("Blue Pla", Color.BLUE, bluePlayerCards);
        currentPlayer = (startingColor == Color.RED) ? redPlayer : bluePlayer;
        initializeBoard();
        playGame();
    }

 

    public GameImpl(String nameRedPlayer, String nameBluePlayer){
        deck = Card.createCards();
        tableCard = deck[0];
        Color startingColor = tableCard.getColor();
        Card [] redPlayerCards = {deck[1], deck[2]};
        Card [] bluePlayerCards = {deck[3], deck[4]};
        this.redPlayer = new Player(nameRedPlayer, Color.RED, redPlayerCards);
        this.bluePlayer = new Player(nameBluePlayer, Color.BLUE, bluePlayerCards);
        currentPlayer = (startingColor == Color.RED) ? redPlayer : bluePlayer;
        initializeBoard();
        playGame();
    }

    public GameImpl(String nameRedPlayer, String nameBluePlayer, Card [] customDeck){
        deck = Card.pickCustomDeck(customDeck);
        tableCard = deck[0];
        Color startingColor = tableCard.getColor();
        Card [] redPlayerCards = {deck[1], deck[2]};
        Card [] bluePlayerCards = {deck[3], deck[4]};
        this.redPlayer = new Player(nameRedPlayer, Color.RED, redPlayerCards);
        this.bluePlayer = new Player(nameBluePlayer, Color.BLUE, bluePlayerCards);
        currentPlayer = (startingColor == Color.RED) ? redPlayer : bluePlayer;
        initializeBoard();
        playGame();
    }

    /**
     * Método que devolve a cor da posição do tabuleiro. Se possui uma cor, significa que é um templo. Caso contrário, é um espaço normal
     * @param position Posição do tabuleiro
     * @return O enum Color que representa a cor da posição
     */
    @Override
    public Color getSpotColor(Position position){
        return board[position.getRow()][position.getCol()].getColor();
    }

    /**
     * Método que devolve a peça que está na posição do tabuleiro
     * @param position Posição do tabuleiro
     * @return Um objeto Piece que representa a peça na posição indicada. Se não tiver peça, devolve null
     */
    @Override
    public Piece getPiece(Position position){
        return board[position.getRow()][position.getCol()].getPiece();
    }

    /**
     * Método que devolve a carta que está na mesa, que será substituída após a próxima jogada
     * @return Um objeto Card que representa a carta na mesa
     */
    @Override
    public Card getTableCard(){
        return tableCard;
    }

    /**
     * Método que devolve as informações sobre o jogador com as peças vermelhas
     * @return Um objeto Player que representa o jogador vermelho
     */
    @Override
    public Player getRedPlayer(){
        return redPlayer;
    }

    /**
     * Método que devolve as informações sobre o jogador com as peças azuis
     * @return Um objeto Player que representa o jogador azul
     */
    @Override
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
    @Override
    public void makeMove(Card card, Position cardMove, Position currentPos) throws IncorrectTurnOrderException, IllegalMovementException, InvalidCardException, InvalidPieceException {

        if (!board[currentPos.getRow()][currentPos.getCol()].isOccupied()){
            throw new InvalidPieceException("Não há nenhuma peça na posição");
        }

        if (!board[cardMove.getRow() + currentPos.getRow()][cardMove.getCol() + currentPos.getCol()].isValid()){
            throw new IllegalMovementException("Movimento fora do tabuleiro"); //index out of bonds
        }

        if (!validMove(card, cardMove, currentPos)){
            throw new IllegalMovementException("Movimento inválido para a carta");
        }

        if (!currentPlayer.hasCard(card)){
            throw new InvalidCardException("O jogador não possui a carta");
        }
       
        Spot currentSpot = board[currentPos.getRow()][currentPos.getCol()];

        Spot destinationSpot = board[cardMove.getRow() + currentPos.getRow()][cardMove.getCol() + currentPos.getCol()];

        Piece piece = currentSpot.getPiece();
        

        if (currentPlayer.getPieceColor() != piece.getColor()){
             throw new IncorrectTurnOrderException("Não é a vez do jogador fazer um movimento.");
        }


        if (destinationSpot.isOccupied() && destinationSpot.getPiece().getColor() == currentSpot.getPiece().getColor()){
             throw new IllegalMovementException("Posição já ocupada por peça de mesma cor");
         }

        destinationSpot.occupySpot(currentSpot.getPiece());
        currentSpot.releaseSpot();


        currentPlayer.swapCard(card, tableCard);

        this.tableCard = card;

    }

    /**
     * Método que confere se um jogador de uma determinada cor venceu o jogo. Critérios de vitória:
     * — Derrotou a peça de mestre adversária
     * — Posicionou o seu mestre na posição da base adversária
     * @param color Cor das peças do jogador que confere a condição de vitória
     * @return Um booleano true para caso esteja em condições de vencer e false caso contrário
     */
    @Override
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
    @Override
    public void printBoard(){

      System.out.println("Tabuleiro:");

        for (int row = 0; row < 5; row++) {

            System.out.print(row + "   ");

            for (int col = 0; col < 5; col++) {
                Spot spot = board[row][col];
                Piece piece = spot.getPiece();


                if (piece == null){
                    System.out.print("-- ");
                } else {
                    if (piece.getColor() == Color.RED){
                        System.out.print("R" + piece.toString() + " ");
                    } else {
                        System.out.print("B" + piece.toString() + " ");
                    }
                }
                
            }
            System.out.println();
        }
        
        System.out.println("    0   1  2  3  4");
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


    public boolean validMove(Card card, Position cardMove, Position currPosition){

        Position[] validPositions = card.getPositions();

        // Verifica se a posição de destino está nas posições válidas
        Position destination = new Position(cardMove.getRow(),cardMove.getCol());
        for (Position validPos : validPositions) {
            if (validPos.equals(destination)) {
            // A posição de destino está nas posições válidas
                return true;
        }
    }

    // A posição de destino não está nas posições válidas
        return false;

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

        boolean gameOver = false;

        while(!gameOver){
            System.out.println();
            System.out.println("Carta da mesa: " + tableCard.getName());
            System.out.println();
            System.out.println("Jogador atual: " + currentPlayer.getName() + "(" + currentPlayer.getPieceColor() + ")");
            System.out.println();
            printBoard();

            Card selectedCard = selectCard(currentPlayer);

            Position currPosition = selectPiecePosition();

            Position moveCard = selectMovePosition(selectedCard);  


        try {
            makeMove(selectedCard, moveCard, currPosition);
            gameOver = checkVictory(currentPlayer.getPieceColor()) ? true : false;
            switchTurn();
        }

        catch(Exception e){
            System.out.println("Movimento fora do tabuleiro");
        }
    }

        printBoard();

        Player winner = (currentPlayer == redPlayer) ? bluePlayer : redPlayer;
        System.out.println("Parabéns, " + winner.getName() + "! Você venceu o jogo!");

}

    private Card selectCard(Player currentPlayer){

        System.out.println("Jogador " + currentPlayer.getName() + ", selecione a carta:");
        Card [] cards = currentPlayer.getCards();

        for (int i = 0; i < cards.length; i++){
         System.out.println((i+1) + ": " + cards[i].getName());
        }

        Card selectedCard = null;
        boolean validInput = false;


        while (!validInput) {
        try {
            Scanner scanner = new Scanner(System.in);
            int cardIndex = scanner.nextInt();
            if (cardIndex >= 1 && cardIndex <= cards.length) {
                selectedCard = cards[cardIndex - 1];
                validInput = true;
            } else {
                System.out.println("Selecione uma carta válida");
            }
        } catch (InputMismatchException e) {
            System.out.println("Selecione uma carta válida");
        }
    }

    return selectedCard;

}

    private Position selectMovePosition(Card card) {
        System.out.println("Jogador " + currentPlayer.getName() + ", selecione a posição de movimento da peça:");
        System.out.println("Movimentos possíveis para a carta:");
        card.positionsAsString(card);

        boolean validInput = false;
        int row = 0;
        int col = 0;

        while (!validInput) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Digite a linha:");
                row = scanner.nextInt();
                System.out.println("Digite a coluna:");
                col = scanner.nextInt();

                validInput = true;  // Definir como true para permitir números positivos e negativos

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Tente novamente.");
            }
    }

    Position pos = new Position(row, col);
    return pos;
    }

    private Position selectPiecePosition() {
        System.out.println("Jogador " + currentPlayer.getName() + ", selecione a posição da peça a ser movida:");

    
        int row = -1;
        int col = -1;

        while (row < 0 || col < 0) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Digite a linha:");
                row = scanner.nextInt();
                System.out.println("Digite a coluna:");
                col = scanner.nextInt();

                if (row < 0 || col < 0){
                    System.out.println("Peça fora do tabuleiro. Digite apenas números positivos");
                } 

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Tente novamente.");
            }
    }
        Position pos = new Position(row, col);
        return pos;
        
    }

    private void switchTurn(){
        currentPlayer = (currentPlayer == bluePlayer) ? redPlayer : bluePlayer;
    }

}
