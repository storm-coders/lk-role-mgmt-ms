Feature: Example controller integration testing

Scenario: Fetch Greetings
    Given url baseUrl + '/greeting'
    When method GET
    Then status 200
    And match response == { greeting : 'Hi, from secured endpoint'}