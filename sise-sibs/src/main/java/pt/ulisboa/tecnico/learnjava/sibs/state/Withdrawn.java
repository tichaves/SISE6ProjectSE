package pt.ulisboa.tecnico.learnjava.sibs.state;

public class Withdrawn implements TransferState {
	public void process(TransferOperationState state) {
		state.setState(this);
	}
	
	public String toString() {
		return "Withdrawn";
	}
}
