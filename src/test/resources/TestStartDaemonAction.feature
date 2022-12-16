#Author:      dirk.schweier
Feature: Start Daemon Action
  Diese Klasse testet die Angaben aus der Start Action

  Scenario: Start Daemon Action has type
    Given Action is "Start Action"
    When Start Action was created
    Then Type should be "QF-Test - start daemon"
