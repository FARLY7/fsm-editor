package StateMachine;

public class Transition {
	
	private StateFigure state;
	private String input;

	public Transition(StateFigure state, String input) {
		this.state = state;
		this.input = input;
	}

	public StateFigure getState() {
		return state;
	}
	
	public String getInput() {
		return input;
	}
}
