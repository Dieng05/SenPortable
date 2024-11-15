package com.example.senportable;

public class Coordonnees {
    private String emei;
    private String adIp;
    private double lattitude;
    private double  longitude;

    public Coordonnees(String emei, String adIp, double lattitude, double longitude) {
        this.emei = emei;
        this.adIp = adIp;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public String getEmei() {
        return emei;
    }

    public void setEmei(String emei) {
        this.emei = emei;
    }

    public String getAdIp() {
        return adIp;
    }

    public void setAdIp(String adIp) {
        this.adIp = adIp;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Coordonnees{" +
                "emei='" + emei + '\'' +
                ", adIp='" + adIp + '\'' +
                ", lattitude=" + lattitude +
                ", longitude=" + longitude +
                '}';
    }
}
