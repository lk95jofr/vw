package se.knowit.android.vw.servicecenter;

public class ServiceCenter {
    private String dName = "";
    private String dAddr = "";
    private String dCity = "";
    private String dPhn = "";
    private String dUrl = "";
    private String dlat = "";
    private String dlng = "";
    private String dist = "";

    public ServiceCenter() {
    }
    
	public String getDName() {
		return dName;
	}

	public void setDName(String dName) {
		this.dName = dName;
	}

	public String getDAddr() {
		return dAddr;
	}

	public void setDAddr(String dAddr) {
		this.dAddr = dAddr;
	}

	public String getDCity() {
		return dCity;
	}

	public void setDCity(String dCity) {
		this.dCity = dCity;
	}

	public String getDPhn() {
		return dPhn;
	}

	public void setDPhn(String dPhn) {
		this.dPhn = dPhn;
	}

	public String getDUrl() {
		return dUrl;
	}

	public void setDUrl(String dUrl) {
		this.dUrl = dUrl;
	}

	public String getDlat() {
		return dlat;
	}

	public void setDlat(String dlat) {
		this.dlat = dlat;
	}

	public String getDlng() {
		return dlng;
	}

	public void setDlng(String dlng) {
		this.dlng = dlng;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		int x = dist.indexOf(".");
		if (x > 0) {
			x += 3;
		} else if (x < 0) {
			x = 0;
		} else {
			x = 1;
		}
		this.dist = dist.substring(0, x);
	}
}
