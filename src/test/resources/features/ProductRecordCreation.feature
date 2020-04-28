Feature: create normalized ProductRecord from various inputs

  Scenario Outline: create price from parts
  Prices can either be a singular price per unit (e.g. $1.00) or a split price (e.g. 2 for $0.99).
  Only one price per pricing level will exist. The other will be all 0's, which indicates there is no price.
  If a price is split pricing, the Calculator Price is Split Price / For X

    Given price parts '<Price>' '<Split Price>' and number <X>
    Then we have '<Calc Price>' and '<Display Price>'
    Examples:
      | Price | Split Price | X       | Calc Price | Display Price | comment |
      | 0.99  | 0.0         | 1       | 0.99       | $0.99         |         |
      | 0.0   | 0.99        | 13      | 0.0761     | 13 for $0.99  |   0.07615 is actual value, but we limit scale to 4      |
