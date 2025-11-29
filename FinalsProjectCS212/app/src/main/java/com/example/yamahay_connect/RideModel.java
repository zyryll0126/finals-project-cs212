package com.example.yamahay_connect;

public class RideModel {
    private String date;
    private String time;
    private String route;
    private String distance;
    private String duration;

    // This is the constructor required for the "new RideModel(...)" lines to work
    public RideModel(String date, String time, String route, String distance, String duration) {
        this.date = date;
        this.time = time;
        this.route = route;
        this.distance = distance;
        this.duration = duration;
    }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getRoute() { return route; }
    public String getDistance() { return distance; }
    public String getDuration() { return duration; }
}