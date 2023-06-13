
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

    }

    public GameImpl(String nameRedPlayer, String nameBluePlayer){
        this.redPlayer = new Player(nameRedPlayer, Color.RED, deck);
        this.bluePlayer = new Player(nameBluePlayer, Color.BLUE, deck);
        
    }

    public GameImpl(String nameRedPlayer, String nameBluePlayer, Piece[] pieces){

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
    public void makeMove(Piece piece, Card card, Position position) throws IncorrectTurnOrderException, IllegalMovementException, InvalidCardException, InvalidPieceException {
        if (currentPlayer.getPieceColor() != piece.getColor()){
              throw new IncorrectTurnOrderException("Não é a vez do jogador fazer um movimento.");
        }


    }

    /**
     * Método que confere se um jogador de uma determinada cor venceu o jogo. Critérios de vitória:
     * — Derrotou a peça de mestre adversária
     * — Posicionou o seu mestre na posição da base adversária
     * @param color Cor das peças do jogador que confere a condição de vitória
     * @return Um booleano true para caso esteja em condições de vencer e false caso contrário
     */
    public boolean checkVictory(Color color){
        return false;
    }

    /**
     * Método que imprime o tabuleiro no seu estado atual
     * OBS: Esse método é opcional não será utilizado na correção, mas serve para acompanhar os resultados parciais do jogo
     */
    public void printBoard(){

    }

    private void initializeBoard(){
        board = new Spot[5][5];
        
        board[0][2] = new Spot(new Piece(Color.BLUE, true), new Position(0, 2));
        board[4][2] = new Spot(new Piece(Color.RED, true), new Position(4, 2));

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (row == 0) {
                    board[row][col] = new Spot(new Piece(Color.BLUE, false), new Position(row, col));
                } else if (row == 4) {
                    board[row][col] = new Spot(new Piece(Color.RED, false), new Position(row, col));
                }
            }
        }
    }


}
