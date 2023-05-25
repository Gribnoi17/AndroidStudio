package ru.mirea.gribanovdv.httpurlconnection;

public class Data {
    private String ip;
    private String country;
    private String region;
    private String city;

    public String getIp() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public Data(String ip, String country, String region, String city) {
        this.ip = ip;
        this.country = country;
        this.region = region;
        this.city = city;
    }


}
