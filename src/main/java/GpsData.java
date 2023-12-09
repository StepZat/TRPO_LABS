public class GpsData extends DataPrototype {
    private double latitude;
    private double longitude;

    public GpsData() {
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public GpsData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public GpsData clone() {
        return new GpsData(this.latitude, this.longitude);
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }

    @Override
    public void parseFromString(String data) {
        try {
            String[] parts = data.split(",");
            this.latitude = Double.parseDouble(parts[0]);
            this.longitude = Double.parseDouble(parts[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return;
        }
    }
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Override
    public int compareTo(DataPrototype other) {
        if (!(other instanceof GpsData otherGps)) {
            throw new IllegalArgumentException("Can only compare with GpsData");
        }

        double thisDistance = calculateDistance(this.latitude, this.longitude, 0.0, 0.0);
        double otherDistance = calculateDistance(otherGps.latitude, otherGps.longitude, 0.0, 0.0);

        return Double.compare(thisDistance, otherDistance);
    }

    public GpsData add(GpsData other) {
        double thisDistance = calculateDistance(this.latitude, this.longitude, 0.0, 0.0);
        double otherDistance = calculateDistance(other.latitude, other.longitude, 0.0, 0.0);

        return thisDistance >= otherDistance ? this.clone() : other.clone();
    }

}