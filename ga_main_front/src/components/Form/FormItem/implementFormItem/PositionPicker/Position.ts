export class Position {
    constructor(longitude, latitude, address, label) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.label = label;
    }

    static empty() {
        return new Position(0, 0, "", "");
    }
}