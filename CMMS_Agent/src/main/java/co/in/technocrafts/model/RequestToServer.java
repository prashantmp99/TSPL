package co.in.technocrafts.model;

public class RequestToServer {
	String TerminalID;
	String JobID;
	String DetailId;
	public String getTerminalID() {
		return TerminalID;
	}
	public void setTerminalID(String terminalID) {
		TerminalID = terminalID;
	}
	public String getJobID() {
		return JobID;
	}
	public void setJobID(String jobID) {
		JobID = jobID;
	}
	public String getDetailId() {
		return DetailId;
	}
	public void setDetailId(String detailId) {
		DetailId = detailId;
	}
	@Override
	public String toString() {
		return "RequestToServer [TerminalID=" + TerminalID + ", JobID=" + JobID + ", DetailId=" + DetailId + "]";
	}
	
	
	

}
