package CDR;

public class CallingRecord {

	private int origIpAddr;	
	private int destIpAddr;
	private String originalCalledPartyNumber;
	private String finalCalledPartyNumber;
	private int dateTimeConnect;
	private int dateTimeDisconnect;
	private int duration;
	private String origDeviceName;
	private String destDeviceName;
	private String origIpv4v6Addr;
	private String destIpv4v6Addr;
	private String callingPartyNumber; // used as the classifier

	/**
	 *
	 * @param origIpAddr	
	 * @param destIpAddr
	 * @param originalCalledPartyNumber
	 * @param finalCalledPartyNumber
	 * @param dateTimeConnect
	 * @param dateTimeDisconnect
	 * @param duration
	 * @param origDeviceName
	 * @param destDeviceName
	 * @param origIpv4v6Addr
	 * @param destIpv4v6Addr
	 * @param callingPartyNumber --- used as the classifier
     *
	 */
	public CallingRecord(int origIpAddr,						 
						 int destIpAddr,
						 String originalCalledPartyNumber,
						 String finalCalledPartyNumber,
						 int dateTimeConnect,
						 int dateTimeDisconnect,
						 int duration,
						 String origDeviceName,
						 String destDeviceName,
						 String origIpv4v6Addr,
						 String destIpv4v6Addr,
						 String callingPartyNumber){
		super();
	    this.origIpAddr = origIpAddr;
		this.destIpAddr = destIpAddr;
		this.originalCalledPartyNumber = originalCalledPartyNumber;
		this.finalCalledPartyNumber = finalCalledPartyNumber;
		this.dateTimeConnect = dateTimeConnect;
		this.dateTimeDisconnect = dateTimeDisconnect;
		this.duration = duration;
		this.origDeviceName = origDeviceName;
		this.destDeviceName = destDeviceName;
		this.origIpv4v6Addr = origIpv4v6Addr;
		this.destIpv4v6Addr = destIpv4v6Addr;
		this.callingPartyNumber = callingPartyNumber; // used as the classifier
	}

	/**
	 * @return the origIpAddr
	 * This field identifies the v4 IP address of the device that originates the call signaling
	 */
	public int getOrigIpAddr() {
		return origIpAddr;
	}

	/**
	 * @param origIpAddr the origIpAddr to set
	 * This field identifies the v4 IP address of the device that originates the call signaling
	 */
	public void setOrigIpAddr(int origIpAddr) {
		this.origIpAddr = origIpAddr;
	}

	/**
	 * @return the destIpAddr
	 * This field identifies the v4 IP address of the device that terminates the call signaling.
	 */
	public int getDestIpAddr() {
		return destIpAddr;
	}

	/**
	 * @param destIpAddr the destIpAddr to set
	 * This field identifies the v4 IP address of the device that terminates the call signaling.
	 */
	public void setDestIpAddr(int destIpAddr) {
		this.destIpAddr = destIpAddr;
	}


	/**
	 * @return the callingPartyNumber
	 * This field specifies a numeric string of up to 25 characters that indicates
	 * the calling party number if the calling party is identified with a directory number.
	 */
	public String getCallingPartyNumber() {
		return callingPartyNumber;
	}

	/**
	 * @param callingPartyNumber the callingPartyNumber to set
	 * This field specifies a numeric string of up to 25 characters that indicates
	 * the calling party number if the calling party is identified with a directory number.
	 */
	public void setCallingPartyNumber(String callingPartyNumber) {
		this.callingPartyNumber = callingPartyNumber;
	}

	/**
	 * @return the originalCalledPartyNumber
	 * This field specifies the number to which the original call was presented, prior to any call forwarding.
	 */
	public String getOriginalCalledPartyNumber() {
		return originalCalledPartyNumber;
	}

	/**
	 * @param originalCalledPartyNumber the originalCalledPartyNumber to set
	 * This field specifies the number to which the original call was presented, prior to any call forwarding.
	 */
	public void setOriginalCalledPartyNumber(String originalCalledPartyNumber) {
		this.originalCalledPartyNumber = originalCalledPartyNumber;
	}

	/**
	 * @return the finalCalledPartyNumber
	 * This field specifies the phone number to which the call finally gets presented.
	 */
	public String getFinalCalledPartyNumber() {
		return finalCalledPartyNumber;
	}

	/**
	 * @param finalCalledPartyNumber the finalCalledPartyNumber to set
	 * This field specifies the phone number to which the call finally gets presented.
	 */
	public void setFinalCalledPartyNumber(String finalCalledPartyNumber) {
		this.finalCalledPartyNumber = finalCalledPartyNumber;
	}

	/**
	 * @return the dateTimeConnect
	 * This field identifies the date and time that the call connects.
	 */
	public int getDateTimeConnect() {
		return dateTimeConnect;
	}

	/**
	 * @param dateTimeConnect the dateTimeConnect to set
	 * This field identifies the date and time that the call connects.
	 */
	public void setDateTimeConnect(int dateTimeConnect) {
		this.dateTimeConnect = dateTimeConnect;
	}

	/**
	 * @return the dateTimeDisconnect
	 * This field identifies the date and time that the call is cleared.
	 */
	public int getDateTimeDisconnect() {
		return dateTimeDisconnect;
	}

	/**
	 * @param dateTimeDisconnect the dateTimeDisconnect to set
	 * This field identifies the date and time that the call is cleared.
	 */
	public void setDateTimeDisconnect(int dateTimeDisconnect) {
		this.dateTimeDisconnect = dateTimeDisconnect;
	}

	/**
	 * @return the duration
	 * This field identifies the difference between the Connect Time and Disconnect Time.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 * This field identifies the difference between the Connect Time and Disconnect Time.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the origDeviceName
	 * This field specifies the text string that identifies the name of the originating device.
	 */
	public String getOrigDeviceName() {
		return origDeviceName;
	}

	/**
	 * @param origDeviceName the origDeviceName to set
	 * This field specifies the text string that identifies the name of the originating device.
	 */
	public void setOrigDeviceName(String origDeviceName) {
		this.origDeviceName = origDeviceName;
	}

	/**
	 * @return the destDeviceName
	 * This field specifies the text string that identifies the name of the destination device.
	 */
	public String getDestDeviceName() {
		return destDeviceName;
	}

	/**
	 * @param destDeviceName the destDeviceName to set
	 * This field specifies the text string that identifies the name of the destination device.
	 */
	public void setDestDeviceName(String destDeviceName) {
		this.destDeviceName = destDeviceName;
	}

	/**
	 * @return the origIpv4v6Addr
	 * This field identifies the IP address of the device that originates the call signaling.
	 * The field can be either IPv4 or IPv6 format depending on the type of IP address that gets used for the call.
	 */
	public String getOrigIpv4v6Addr() {
		return origIpv4v6Addr;
	}

	/**
	 * @param origIpv4v6Addr the origIpv4v6Addr to set
	 * This field identifies the IP address of the device that originates the call signaling.
	 * The field can be either IPv4 or IPv6 format depending on the type of IP address that gets used for the call.
	 */
	public void setOrigIpv4v6Addr(String origIpv4v6Addr) {
		this.origIpv4v6Addr = origIpv4v6Addr;
	}

	/**
	 * @return the destIpv4v6Addr
	 * This field identifies the IP address of the device that terminates the call signaling.
	 * The field can be either IPv4 or IPv6 format depending on the type of IP address that gets used for the call.
	 */
	public String getDestIpv4v6Addr() {
		return destIpv4v6Addr;
	}

	/**
	 * @param destIpv4v6Addr the destIpv4v6Addr to set
	 * This field identifies the IP address of the device that terminates the call signaling.
	 * The field can be either IPv4 or IPv6 format depending on the type of IP address that gets used for the call.
	 */
	public void setDestIpv4v6Addr(String destIpv4v6Addr) {
		this.destIpv4v6Addr = destIpv4v6Addr;
	}

	@Override
	public String toString() {
		return "CallingRecord [origIpAddr = " + origIpAddr				               
				               + ", destIpAddr = " + destIpAddr
				               + ", originalCalledPartyNumber = " + originalCalledPartyNumber
				               + ", finalCalledPartyNumber = " + finalCalledPartyNumber
				               + ", dateTimeConnect = " + dateTimeConnect
				               + ", dateTimeDisconnect = " + dateTimeDisconnect
				               + ", duration = " + duration
				               + ", origDeviceName = " + origDeviceName
				               + ", destDeviceName = " + destDeviceName
				               + ", origIpv4v6Addr = " + origIpv4v6Addr
				               + ", destIpv4v6Addr = " + destIpv4v6Addr 
				               + ", callingPartyNumber = " + callingPartyNumber + "]";
	}

}
