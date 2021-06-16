@pet
Feature: Access pet api and perform CRUD

#  1. Environment and specs: https://petstore.swagger.io/
#  2. Required scripting language: Java
#  3. Required testing tool: Rest-assured
#  4. CI platform: git, jenkins

  Background:
    Given I use "https://petstore.swagger.io/v2" as base URI

  @pet1
  Scenario Outline: Verify findByStatus functionality of PET API
    When I make "GET" call to "/pet/findByStatus?status=<STATUS>"
    Then I get response code "200"
    And I verify at least "1" pet details is available in the response
    Examples:
      | STATUS    |
      | available |
      | pending   |
      | sold      |

  @pet2
  Scenario: Verify add new pet functionality of PET API
    Given I make REST service headers with the below fields
      | Content-Type     | Accept           |
      | application/json | application/json |
    Given I read request body from "addNewPetRequest"
    When I make "POST" call to "/pet"
    Then I get response code "200"
