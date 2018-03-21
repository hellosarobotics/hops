package com.sarobotics.actions;


import com.sarobotics.utils.InvalidOpenParachuteAltitude;

public abstract class Action {

  private int burstAltitude;
  private int openParachuteAltitude;
  private boolean deveAncoraScoppiare;
  private boolean ilParacaduteSiDeveAncoraAprire;

  public Action(int _burstAltitude, int _openParachuteAltitude) throws InvalidOpenParachuteAltitude {
    /**
     * _burstAltitude > _openParachuteAltitude
     */
    if(_burstAltitude > _openParachuteAltitude) {
      this.burstAltitude = _burstAltitude;
      this.openParachuteAltitude = _openParachuteAltitude;
      this.deveAncoraScoppiare = true;
      this.ilParacaduteSiDeveAncoraAprire = true;
    }else {
      //Throws exceptions
      throw new InvalidOpenParachuteAltitude("Burst Altitude: "+_burstAltitude +" < Open parachute altitude: " + _openParachuteAltitude);
    }
  }

  public boolean canBurst(int actualAltitude ){
    return this.burstAltitude<actualAltitude;
  }

  public boolean canOpenParachute(int actualAltitude) {
    return this.openParachuteAltitude > actualAltitude &&
            ilPalloneEScoppiato() &&
            this.ilParacaduteSiDeveAncoraAprire;
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

  /**
   * Sgancia sonda serve per sganciare la sonda dal drone una volta raggiunta l'altezza attuale + 100 metri.
   */
  public abstract void sganciaSonda();

  /**
   * Apre il paracadute quando la sonda raggiunge l'altezza definita ma cmq < dell'altezza attuale + 100m
   */
  public abstract void apriParacadute();
}
