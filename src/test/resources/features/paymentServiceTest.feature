Feature: Testing the Payment service
  A Payment should be handled by a service

  Scenario: get a payment
    Given the mocks are injected
    Given the mockDao is setup
    Given the findOne is mocked to return the mockDao
    When we get the payment by its id
    Then the dao and mockDao are equals

  Scenario: get all payments
    Given the mocks are injected
    Given the mockDao is setup
    Given the findAll is mocked to return a list of the mockDao
    When we get the payments
    Then the dao and mockDao are equals

  Scenario: create a payment
    Given the mocks are injected
    Given the mockDao for creation is setup
    Given the save is mocked to return a mockDao with new id
    When we save a payment
    Then the dao and mockDao are equals

  Scenario: update a payment
    Given the mocks are injected
    Given the mockDao is setup
    Given the mockDto is setup
    Given the findOne is mocked to return the mockDao
    Given the save is mocked to return the mockDao
    When we update a payment
    Then the dao and mockDao are equals

  Scenario: update a not existing payment
    Given the mocks are injected
    Given the mockDao is setup
    Given the mockDto is setup
    Given the findOne is mocked to return null
    When we update a payment
    Then the returned dao is null

  Scenario: delete a payment
    Given the mocks are injected
    When we delete a payment
    Then the delete has been called once