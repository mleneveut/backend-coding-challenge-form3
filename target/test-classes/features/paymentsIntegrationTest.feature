Feature: Testing the Payment REST API
  Users should be able to submit GET, POST, PUT, DELETE requests to handle payments

  Scenario: list all the payments
    Given the web context is set
    Given the database is empty
    Given the data PaymentControllerITest.should_get_all_payments.sql is loaded
    When client request GET /v1/payments
    Then the response code should be 200
    Then the response content type should be UTF8
    Then the result json path $ should exists
    Then the result json path $.links[0].rel should be self
    Then the result json path $.links[0].href should be http://localhost/v1/payments
    Then the result json path $.content should contain 2 records
    Then the result json path $.content[0].id should be 4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.content[0].version should be 0
    Then the result json path $.content[0].attributes.amount should be 100.21
    Then the result json path $.content[1].id should be 216d4da9-e59a-4cc6-8df3-3da6e7580b77
    Then the result json path $.content[1].version should be 1
    Then the result json path $.content[1].attributes.amount should be 101.21
    Then clean the database

  Scenario: get a payment
    Given the web context is set
    Given the data PaymentControllerITest.should_get_all_payments.sql is loaded
    When client request GET /v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the response code should be 200
    Then the response content type should be UTF8
    Then the result json path $ should exists
    Then the result json path $.links[0].rel should be payments
    Then the result json path $.links[0].href should be http://localhost/v1/payments
    Then the result json path $.links[1].rel should be self
    Then the result json path $.links[1].href should be http://localhost/v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.links[2].rel should be update
    Then the result json path $.links[2].href should be http://localhost/v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.links[3].rel should be delete
    Then the result json path $.links[3].href should be http://localhost/v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.id should be 4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.version should be 0
    Then the result json path $.attributes.amount should be 100.21
    Then clean the database

  Scenario: create a payment
    Given the web context is set
    When client request POST /v1/payments with json data:
    """
    {"organisationId":"743d5b63-8e6f-432e-a8fa-c5d8d2ee5fc0","type":"Payment","version":1,"attributes":{"amount":"102.21","beneficiary_party":{"account_name":"W Owenss","account_number":"319268199"}}}
    """
    Then the response code should be 200
    Then the response content type should be UTF8
    Then the result json path $ should exists
    Then the result json path $.links[0].rel should be payments
    Then the result json path $.links[1].rel should be self
    Then the result json path $.links[2].rel should be update
    Then the result json path $.links[3].rel should be delete
    Then the result json path $.organisationId should be 743d5b63-8e6f-432e-a8fa-c5d8d2ee5fc0
    Then the result json path $.version should be 1
    Then the result json path $.attributes.amount should be 102.21
    Then the result json path $.attributes.beneficiary_party.account_name should be W Owenss
    Then the result json path $.attributes.beneficiary_party.account_number should be 319268199
    Given the created payment is returned
    When client request GET /v1/payments/ with created id
    Then the response code should be 200
    Then the response content type should be UTF8
    Then the result json path $ should exists
    Then the result json path $.links[0].rel should be payments
    Then the result json path $.links[0].href should be http://localhost/v1/payments
    Then the result json path $.links[1].rel should be self
    Then the result json path $.links[1].href should with created id be http://localhost/v1/payments/
    Then the result json path $.links[2].rel should be update
    Then the result json path $.links[2].href should with created id be http://localhost/v1/payments/
    Then the result json path $.links[3].rel should be delete
    Then the result json path $.links[3].href should with created id be http://localhost/v1/payments/
    Then the result json path $.id should equals created id
    Then the result json path $.organisationId should be 743d5b63-8e6f-432e-a8fa-c5d8d2ee5fc0
    Then the result json path $.version should be 1
    Then the result json path $.attributes.amount should be 102.21
    Then the result json path $.attributes.beneficiary_party.account_name should be W Owenss
    Then the result json path $.attributes.beneficiary_party.account_number should be 319268199
    Then clean the database

  Scenario: update a payment
    Given the web context is set
    Given the data PaymentControllerITest.should_get_all_payments.sql is loaded
    When client request PUT /v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43 with json data:
    """
    {"organisationId":"743d5b63-8e6f-432e-a8fa-c5d8d2ee5fc0","type":"Payment","version":1,"attributes":{"amount":"102.21","beneficiary_party":{"account_name":"W Owenss","account_number":"319268199"}}}
    """
    Then the response code should be 200
    Then the response content type should be UTF8
    Then the result json path $ should exists
    Then the result json path $.links[0].rel should be payments
    Then the result json path $.links[0].href should be http://localhost/v1/payments
    Then the result json path $.links[1].rel should be self
    Then the result json path $.links[1].href should be http://localhost/v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.links[2].rel should be update
    Then the result json path $.links[2].href should be http://localhost/v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.links[3].rel should be delete
    Then the result json path $.links[3].href should be http://localhost/v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.id should be 4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.organisationId should be 743d5b63-8e6f-432e-a8fa-c5d8d2ee5fc0
    Then the result json path $.version should be 1
    Then the result json path $.attributes.amount should be 102.21
    Then the result json path $.attributes.beneficiary_party.account_name should be W Owenss
    Then the result json path $.attributes.beneficiary_party.account_number should be 319268199
    When client request GET /v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the response code should be 200
    Then the response content type should be UTF8
    Then the result json path $ should exists
    Then the result json path $.links[0].rel should be payments
    Then the result json path $.links[0].href should be http://localhost/v1/payments
    Then the result json path $.links[1].rel should be self
    Then the result json path $.links[1].href should be http://localhost/v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.links[2].rel should be update
    Then the result json path $.links[2].href should be http://localhost/v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.links[3].rel should be delete
    Then the result json path $.links[3].href should be http://localhost/v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.id should be 4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the result json path $.organisationId should be 743d5b63-8e6f-432e-a8fa-c5d8d2ee5fc0
    Then the result json path $.version should be 1
    Then the result json path $.attributes.amount should be 102.21
    Then the result json path $.attributes.beneficiary_party.account_name should be W Owenss
    Then the result json path $.attributes.beneficiary_party.account_number should be 319268199
    Then clean the database

  Scenario: update fails for a missing payment
    Given the web context is set
    When client request PUT /v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec40 with json data:
    """
    {"organisationId":"743d5b63-8e6f-432e-a8fa-c5d8d2ee5fc0","type":"Payment","version":1,"attributes":{"amount":"102.21","beneficiary_party":{"account_name":"W Owenss","account_number":"319268199"}}}
    """
    Then the response code should be 404
    Then the response content type should be UTF8
    Then the result json path description should be The payment 4ee3a8d8-ca7b-4290-a52c-dd5b6165ec40 can not be found

  Scenario: delete a payment
    Given the web context is set
    Given the data PaymentControllerITest.should_get_all_payments.sql is loaded
    When client request DELETE /v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the response code should be 200
    When client request GET /v1/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43
    Then the response code should be 404
    Then the response content type should be UTF8
    Then the result json path description should be The payment 4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43 can not be found
    Then clean the database