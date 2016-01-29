package com.example.manuelseguranavarro.festival;

/**
 * Created by manuelseguranavarro on 29/1/16.
 */
public class ClaseFestival {
    private String artistsName;
    private String locationCity;
    private String artistsLogo;
    private String VenueName;
    private String VenueLogo;
    private Double locatioLatitud;
    private Double locatioLongitud;
    private int ticketPriceMax;
    private int ticketPriceMin;

    public ClaseFestival(String artistsName, String locationCity, String artistsLogo) {
        this.artistsName = artistsName;
        this.locationCity = locationCity;
        this.artistsLogo = artistsLogo;
    }

    public ClaseFestival(String venueName, String venueLogo, Double locatioLatitud, Double locatioLongitud, int ticketPriceMax, int ticketPriceMin) {
        VenueName = venueName;
        VenueLogo = venueLogo;
        this.locatioLatitud = locatioLatitud;
        this.locatioLongitud = locatioLongitud;
        this.ticketPriceMax = ticketPriceMax;
        this.ticketPriceMin = ticketPriceMin;
    }


    public String getArtistsName() {
        return artistsName;
    }

    public void setArtistsName(String artistsName) {
        this.artistsName = artistsName;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getArtistsLogo() {
        return artistsLogo;
    }

    public void setArtistsLogo(String artistsLogo) {
        this.artistsLogo = artistsLogo;
    }

    public String getVenueName() {
        return VenueName;
    }

    public void setVenueName(String venueName) {
        VenueName = venueName;
    }

    public String getVenueLogo() {
        return VenueLogo;
    }

    public void setVenueLogo(String venueLogo) {
        VenueLogo = venueLogo;
    }

    public Double getLocatioLatitud() {
        return locatioLatitud;
    }

    public void setLocatioLatitud(Double locatioLatitud) {
        this.locatioLatitud = locatioLatitud;
    }

    public Double getLocatioLongitud() {
        return locatioLongitud;
    }

    public void setLocatioLongitud(Double locatioLongitud) {
        this.locatioLongitud = locatioLongitud;
    }

    public int getTicketPriceMax() {
        return ticketPriceMax;
    }

    public void setTicketPriceMax(int ticketPriceMax) {
        this.ticketPriceMax = ticketPriceMax;
    }

    public int getTicketPriceMin() {
        return ticketPriceMin;
    }

    public void setTicketPriceMin(int ticketPriceMin) {
        this.ticketPriceMin = ticketPriceMin;
    }
}
