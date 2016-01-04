package org.jackongo.model.restaurants;

import org.joda.time.DateTime;

import lombok.Data;

/*
{
            "date" : ISODate("2014-05-22T00:00:00.000+0000"),
            "grade" : "A",
            "score" : NumberInt(10)
        }
 */

@Data
public class Grade {

  public DateTime date;
  public String grade;
  public int score;

}
