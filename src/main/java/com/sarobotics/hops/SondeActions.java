package com.sarobotics.hops;

/**
 * Questa classe raccoglie le azioni che la sonda può effettuare,
 * Sganciarsi,
 * Aprire il paracadute.
 */
public class SondeActions {

  private int burstAltitude;
  private int openParachuteAltitude;
  private boolean deveAncoraScoppiare;
  private boolean ilParacaduteSiDeveAncoraAprire;


  public SondeActions(){  }

  public SondeActions(int _burstAltitude, int _openParachuteAltitude) {
    this.burstAltitude = _burstAltitude;
    this.openParachuteAltitude = _openParachuteAltitude;
    this.deveAncoraScoppiare=true;
    this.ilParacaduteSiDeveAncoraAprire=true;
  }

  public boolean canBurst(int actualAltitude ){
    return this.burstAltitude<actualAltitude;
  }


  public boolean canOpenParachute(int actualAltitude) {
    return this.openParachuteAltitude > actualAltitude && ilPalloneEScoppiato() && this.ilParacaduteSiDeveAncoraAprire;
  }

  private boolean ilPalloneEScoppiato() {
    return !this.deveAncoraScoppiare;
  }

  public boolean getDeveAncoraScoppiare() {
    return this.deveAncoraScoppiare;
  }

  public boolean getIlParacaduteSiDeveAncoraAprire() {
    return this.ilParacaduteSiDeveAncoraAprire;
  }

  public void setDeveAncoraScoppiare(boolean deveAncoraScoppiare) {
    this.deveAncoraScoppiare = deveAncoraScoppiare;
  }

  public void setIlParacaduteSiDeveAncoraAprire(boolean ilParacaduteSiDeveAncoraAprire) {
    this.ilParacaduteSiDeveAncoraAprire = ilParacaduteSiDeveAncoraAprire;
  }
}
