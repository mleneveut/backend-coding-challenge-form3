Feature: Testing the Payment mapping
  A Payment DTO should be mapped to a Payment DAO and reverse

  Scenario: map a DTO from a DAO
    Given the RequestContextHolder is set
    Given the dao is setup
    When the dto is mapped from the dao
    Then the dto and dao have the same values

  Scenario: map a DAO from a DTO
    Given the RequestContextHolder is set
    Given the dto is setup
    When the dao is mapped from the dto
    Then the dto and dao have the same values