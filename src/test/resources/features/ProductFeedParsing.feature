Feature: we can parse field values

 Scenario Outline: The first flag in the left-to-right array is #1
 If Flag #3 is set, this is a per-weight item
 If Flag #5 is set, the item is taxable. Tax rate is always 7.775%

    Then we can detect flags in '<Field Value>' '<Taxable>' '<Per Weight>'
    Examples:
      | Field Value | Taxable | Per Weight |
      | NNNNNNNNN   | N       | N       |
      | NNNNYNNNN   | Y       | N       |
      | YNNNNNNNN   | N       | N       |
      | NNYNNNNNN   | N       | Y       |
      | NNYNNNYYN   | N       | Y       |

  Scenario Outline: we can get string without padding
    Then we can get string '<Expected Value>' from padded '<Field Value>'
    Examples:
      | Field Value |  Expected Value |
      | "  aa      "| aa |
      |     " b  "  | b  |

  Scenario Outline: we can parse currency fields
     US dollar value, where last two digits represent cents. The leading zero will be replaced with a dash if the value is negative
    Then we can parse currency '<units_and_cents>' from '<Field Value>'
    Examples:
      | Field Value |  units_and_cents |
      | 00000567 |  5.67 |
      | 00000000 |  0.00 |
      | 00000001 |  0.01 |
      | -0000001 |  -0.01|
      | 00001000 |  10.00|
      | 00000349 |  3.49 |
      | -0000349 |  -3.49|


  Scenario Outline: we can parse integer fields
  US dollar value, where last two digits represent cents. The leading zero will be replaced with a dash if the value is negative
    Then we can parse int <Int> from '<Field Value>'
    Examples:
      | Field Value |  Int |
      | 00000567 |  567 |
      | 00000000 |  0 |
      | 00000001 |  1 |
      | 00001000 |  1000|
      | 00000349 |  349 |





  Scenario: we can get standard parser
    Given 'standard' feed format
    And 'input-sample.txt' input file
    Then we can parse it without errors



