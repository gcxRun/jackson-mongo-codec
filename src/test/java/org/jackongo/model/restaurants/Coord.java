package org.jackongo.model.restaurants;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;


@JsonDeserialize(using = CoordJsonDeserializer.class)
@JsonSerialize(using = CoordJsonSerializer.class)
@Data
public class Coord {
  public double _lat;
  public double _long;
}
