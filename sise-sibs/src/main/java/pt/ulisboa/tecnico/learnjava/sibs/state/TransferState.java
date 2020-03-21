package pt.ulisboa.tecnico.learnjava.sibs.state;

public interface TransferState {
	void process(TransferOperationState state);
}