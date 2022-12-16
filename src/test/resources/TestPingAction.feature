#Author:      dirk.schweier
Feature: Ping Action
  Diese Klasse testet die Angaben aus der Ping Action

  Scenario: Ping Creation Version 1
    When Create "Ping Action"
    Then Type should be "QF-Test - ping daemon"

  Scenario: Ping Creation Version 2
    When Create "Ping" Action
    Then Type should be "QF-Test - ping daemon"

#  @tag2
#  Scenario Outline: Title of your scenario outline
#    Given I want to write a step with <name>
#    When I check for the <value> in step
#    Then I verify the <status> in step
#
#    Examples: 
#      | name  | value | status  |
#      | name1 |     5 | success |
#      | name2 |     7 | Fail    |
