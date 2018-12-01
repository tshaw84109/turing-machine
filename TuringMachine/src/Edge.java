//This class represents a single edge
public class Edge {
	String from, toState;
	char letter, newLetter, direction;

	public Edge(String from, char letter, char newLetter, char direction, String toState) {
		super();
		this.from = from;
		this.toState = toState;
		this.letter = letter;
		this.newLetter = newLetter;
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "edge [from=" + from + ", toState=" + toState + ", letter=" + letter + ", newLetter=" + newLetter
				+ ", direction=" + direction + "]";
	}

	public String getFrom() {
		return from;
	}

	public String getToState() {
		return toState;
	}

	public char getLetter() {
		return letter;
	}

	public char getNewLetter() {
		return newLetter;
	}

	public char getDirection() {
		return direction;
	}
}
