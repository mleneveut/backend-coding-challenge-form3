Feature: Testing the Payment repository
  A Payment should be read and written to the database

  Scenario: get a payment
    Given the mocks are injected
    Given the mockDao is setup
    Given the findOne is mocked to return the mockDao
    When we find the payment in database
    Then the dao and mockDao are equals

  Scenario: get all payments
    Given the mocks are injected
    Given the mockDao is setup
    Given the findAll is mocked to return a list of the mockDao
    When we find all the payments in database
    Then the dao and mockDao are equals

  Scenario: create a payment
    Given the mocks are injected
    Given the mockDao for creation is setup
    Given the save is mocked to return a mockDao with new id
    When we save a payment in database
    Then the dao and mockDao are equals

  Scenario: update a payment
    Given the mocks are injected
    Given the mockDao is setup
    Given the findOne is mocked to return the mockDao
    Given the save is mocked to return the mockDao
    When we save a payment in database
    Then the dao and mockDao are equals

  Scenario: delete a payment
    Given the mocks are injected
    When we delete a payment in database
    Then the delete has been called once