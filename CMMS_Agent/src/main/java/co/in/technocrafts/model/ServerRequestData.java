package co.in.technocrafts.model;

import java.util.Arrays;

public class ServerRequestData {
	
	public String commandFromServer;
	public String filename;
	public byte[] bytesDataToServer;
	
	public String getCommandFromServer() {
		return commandFromServer;
	}
	public void setCommandFromServer(String commandFromServer) {
		this.commandFromServer = commandFromServer;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public byte[] getBytesDataToServer() {
		return bytesDataToServer;
	}
	public void setBytesDataToServer(byte[] bytesDataToServer) {
		this.bytesDataToServer = bytesDataToServer;
	}
	@Override
	public String toString() {
		return "ServerRequestData [commandFromServer=" + commandFromServer + ", filename=" + filename
				+ ", bytesDataToServer=" + Arrays.toString(bytesDataToServer) + "]";
	}
	
	

}
