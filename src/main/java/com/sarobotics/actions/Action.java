package com.sarobotics.actions;


public interface Action {
  /**
   * Sgancia sonda serve per sganciare la sonda dal drone una volta raggiunta l'altezza attuale + 100 metri.
   */
  void sganciaSonda();

  /**
   * Apre il paracadute quando la sonda raggiunge l'altezza definita ma cmq < dell'altezza attuale + 100m
   */
  void apriParacadute();
}
