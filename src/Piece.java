import enums.Color;

/**
 * Classe que contém informações das peças de jogo
 */
public class Piece {

    private Color color;
    private boolean IsMaster;
    private boolean alive;

    /**
     * Construtor que define a cor e o tipo da peça
     * @param color Cor da peça
     * @param isMaster Se o tipo da peça é mestre ou não
     */
    public Piece(Color color, boolean isMaster) {
        this.color = color;
        this.IsMaster = isMaster;
        this.alive = true;
    }

    /**
     * Método que devolve a cor da peça
     * @return Enum Color com a cor da peça
     */
    public Color getColor() {
        return color;
    }

    /**
     * Método que devolve se é um mestre ou não
     * @return Booleano true para caso seja um mestre e false caso contrário
     */
    public boolean isMaster() {
        return IsMaster;
    }

    /**
     * Método que devolve se a peça ainda está em jogo ou não
     * @return Booleano true para caso esteja em jogo e false caso contrário
     */
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
	public String toString() {
        if (isMaster()){
            return "M";
        }
		
        return "A";
	}
}
