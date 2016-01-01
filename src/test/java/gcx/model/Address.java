package gcx.model;

import lombok.Data;

/*
"address" : {
        "building" : "2780",
        "coord" : [
        -73.98241999999999,
        40.579505
        ],
        "street" : "Stillwell Avenue",
        "zipcode" : "11224"
        }
*/
@Data
public class Address {
    public String building;

    public String street;
    public String zipcode;

    public Coord coord;
}
