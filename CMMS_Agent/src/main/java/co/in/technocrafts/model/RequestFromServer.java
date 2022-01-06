package co.in.technocrafts.model;

public class RequestFromServer {
	
int jobID;
int detailId;
int activityID;
String activity;
String terminalID;
String status;


String fileDate;
String filePath;


public String getFilePath() {
	return filePath;
}
public void setFilePath(String filePath) {
	this.filePath = filePath;
}
public String getFileDate() {
	return fileDate;
}
public void setFileDate(String fileDate) {
	this.fileDate = fileDate;
}
public int getJobID() {
	return jobID;
}
public void setJobID(int jobID) {
	this.jobID = jobID;
}
public int getDetailId() {
	return detailId;
}
public void setDetailId(int detailId) {
	this.detailId = detailId;
}
public int getActivityID() {
	return activityID;
}
public void setActivityID(int activityID) {
	this.activityID = activityID;
}
public String getActivity() {
	return activity;
}
public void setActivity(String activity) {
	this.activity = activity;
}
public String getTerminalID() {
	return terminalID;
}
public void setTerminalID(String terminalID) {
	this.terminalID = terminalID;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
@Override
public String toString() {
	return "RequestFromServer [jobID=" + jobID + ", detailId=" + detailId + ", activityID=" + activityID + ", activity="
			+ activity + ", terminalID=" + terminalID + ", status=" + status + ", fileDate=" + fileDate + ", filePath="
			+ filePath + "]";
}


}
