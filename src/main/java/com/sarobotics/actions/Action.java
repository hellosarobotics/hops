package com.sarobotics.actions;


import com.sarobotics.utils.InvalidOpenParachuteAltitude;

public abstract class Action {

  private int detachAltitude;
  private int openParachuteAltitude;
  private boolean deveAncoraScoppiare;
  private boolean ilParacaduteSiDeveAncoraAprire;

  public Action(int _detachAltitude, int _openParachuteAltitude) throws InvalidOpenParachuteAltitude {
    /*
     * _detachAltitude > _openParachuteAltitude
     */
    if( _detachAltitude > _openParachuteAltitude ) {
      this.detachAltitude = _detachAltitude;
      this.openParachuteAltitude = _openParachuteAltitude;
      this.deveAncoraScoppiare = true;
      this.ilParacaduteSiDeveAncoraAprire = true;
    }else {
      //Throws exceptions
      throw new InvalidOpenParachuteAltitude("Detach Altitude: "+_detachAltitude +" < Open parachute altitude: " + _openParachuteAltitude);
    }
  }

  public boolean canDetach(int actualAltitude ){
    return this.detachAltitude < actualAltitude;
  }

  public boolean canOpenParachute(int actualAltitude) {
    return this.openParachuteAltitude > actualAltitude &&
            sondaDetached() &&
            this.ilParacaduteSiDeveAncoraAprire;
  }

  private boolean sondaDetached() {
    return !this.deveAncoraScoppiare;
  }

  public boolean getDeveAncoraScoppiare() {
    return this.deveAncoraScoppiare;
  }

//  public boolean getIlParacaduteSiDeveAncoraAprire() {
//    return this.ilParacaduteSiDeveAncoraAprire;
//  }

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
