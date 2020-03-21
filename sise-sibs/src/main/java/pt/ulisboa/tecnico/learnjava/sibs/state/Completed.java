package pt.ulisboa.tecnico.learnjava.sibs.state;

public class Completed implements TransferState {
	public void process(TransferOperationState state) {
		state.setState(this);
	}
	
	public String toString() {
		return "Completed";
	}
}
