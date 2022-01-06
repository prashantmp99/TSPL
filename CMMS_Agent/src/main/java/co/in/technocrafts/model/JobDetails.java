package co.in.technocrafts.model;

public class JobDetails {
	
	String terminalID;

	public String getTerminalID() {
		return terminalID;
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}

	@Override
	public String toString() {
		return "JobDetails [terminalID=" + terminalID + "]";
	}

}
