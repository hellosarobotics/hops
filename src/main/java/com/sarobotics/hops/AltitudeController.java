package com.sarobotics.hops;

public class AltitudeController {

  private int actualAltitude;
  private int previousAltitude;
  private int firstAltitudeAtPowerOn;

  public AltitudeController( int _actualAltitude ) {
    this.actualAltitude = _actualAltitude;
    this.previousAltitude = 0;
    this.firstAltitudeAtPowerOn = 0;
  }

  public int getActualAltitude() {
    return actualAltitude;
  }

  public void setActualAltitude(int actualAltitude) {
    this.actualAltitude = actualAltitude;
  }

  public int getPreviousAltitude() {
    return previousAltitude;
  }

  public void setPreviousAltitude(int previousAltitude) {
    this.previousAltitude = previousAltitude;
  }


  public void settaAltitudineAttuale(int v) {
    this.previousAltitude = this.actualAltitude;
    this.actualAltitude = v;
  }

  public Boolean isGoingUp() {
    return this.actualAltitude > this.previousAltitude;
  }

  public Boolean isGoingDown() {
    return this.actualAltitude < this.previousAltitude;
  }


  public String print() {
    return "previous altitude: " + this.previousAltitude + " actual altitude:" +this.actualAltitude;
  }
}
