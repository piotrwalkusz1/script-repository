Feature: Management of collections

Scenario: Get info about a collection
    Given I am on the home page
    And I am logged
    And I hava a private collection with name "MyCollection"
    When I go to the repository page
    And I choose a collection with name "MyCollection"
    Then I should see a public collection with name "MyCollection"
