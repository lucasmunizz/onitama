import java.util.Arrays;
import java.util.Random;

import enums.Color;

/**
 * Classe que contém informações das cartas
 */
public class Card {

    private String name;
    private Color color;
    private Position[] positions;

    /**
     * Construtor que define os principais atributos de uma cara
     * @param name Nome da carta
     * @param color Cor da carta
     * @param positions Todas as posições relativas de movimento
     */
    public Card(String name, Color color, Position[] positions) {

        this.name = name;
        this.color = color;
        this.positions = positions;
    }

    /**
     * Método que devolve o nome da carta
     * @return String que contém o nome da carta
     */
    public String getName() {
        return name;
    }

    /**
     * Método que devolve a cor da carta
     * @return Enum Color que contém a cor da carta
     */
    public Color getColor() {
        return color;
    }

    /**
     * Método que devolve todas as possíveis posições relativas de movimento.
     * A posição atual da peça é o ponto de origem (0,0). Uma carta possui as possíveis posições de movimento em relação ao ponto de origem.
     * @return Um array de Position contendo todas as possíveis posições de movimento em relação ao ponto de origem
     */
    public Position[] getPositions() {
        return positions;
    }

    /**
     * Método que cria todas as cartas do jogo, embaralha-as e devolve as 5 que serão utilizadas na partida.
     * @return Vetor de cartas com todas as cartas do jogo
     */
    public static Card[] createCards() {
       Card[] allCards = {
        new Card("Tiger", Color.BLUE, new Position[]{new Position(-2, 0), new Position(1, 0)}),
        new Card("Dragon", Color.RED, new Position[]{new Position(1, 1), new Position(-1, 2), new Position(1,-1), new Position(-1, -2)}),
        new Card("Frog", Color.RED, new Position[]{new Position(0, -2), new Position(-1, -1), new Position(1, 1)}),
        new Card("Rabbit", Color.BLUE, new Position[]{new Position(1, -1), new Position(-1, 1), new Position(0, 2)}),
        new Card("Crab", Color.BLUE, new Position[]{new Position(0, -2), new Position(0, 2), new Position(-1, 0)}),
        new Card("Elephant", Color.RED, new Position[]{new Position(0, -1), new Position(0, 1), new Position(-1, -1), new Position(-1, 1)}),
        new Card("Goose", Color.BLUE, new Position[]{new Position(-1, -1), new Position(0, -1), new Position(0, 1), new Position(1, 1)}),
        new Card("Rooster", Color.RED, new Position[]{new Position(0, -1), new Position(1, -1), new Position(0, 1), new Position(-1, 1)})
    };

    //embaralhar

    Random random = new Random();
    for (int i = allCards.length - 1; i > 0; i--) {
        int j = random.nextInt(i + 1);
        Card temp = allCards[i];
        allCards[i] = allCards[j];
        allCards[j] = temp;
    }

    Card[] selectedCards = new Card[5];
    System.arraycopy(allCards, 0, selectedCards, 0, 5);

    return selectedCards;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + Arrays.hashCode(positions);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Card other = (Card) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (color != other.color)
            return false;
        if (!Arrays.equals(positions, other.positions))
            return false;
        return true;
    }

    public void positionsAsString(Card card){
        Position [] allowedPositions = getPositions();
        for (Position pos : allowedPositions){
            System.out.print(pos.toString() +", ");
        }
        System.out.println(); 
    }

    public static Card [] pickCustomDeck(Card [] customDeck){
        Random random = new Random();
        for (int i = customDeck.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Card temp = customDeck[i];
            customDeck[i] = customDeck[j];
            customDeck[j] = temp;
        }

        Card[] selectedCards = new Card[5];
        System.arraycopy(customDeck, 0, selectedCards, 0, 5);

        return selectedCards;
    }

    

}
