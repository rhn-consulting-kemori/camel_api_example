# check_example
## Build & Run
mvn clean spring-boot:run

## Build Container Image
mvn package
podman build -t <your image tag> .

## Run COntainer
podman run -d --name <container name> -p 8183:8183 <your image tag>

## deposit-entry-check
http://localhost:8183/camel/demo/deposit-entry-check

POST
{"request_id": "A-001", "card_number": "3540000100010001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_amount": 10000, "excess_money_handling_category": "9"}
{"request_id": "", "card_number": "3540000100010001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_amount": 10000, "excess_money_handling_category": "9"}
{"request_id": "A-003", "card_number": "35400001000100019", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_amount": 10000, "excess_money_handling_category": "9"}
{"request_id": "A-004", "card_number": "354000010001001X", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_amount": 10000, "excess_money_handling_category": "9"}
{"request_id": "A-005", "card_number": "3540000100010001", "customer_contract_number": "00000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_amount": 10000, "excess_money_handling_category": "9"}
{"request_id": "A-006", "card_number": "3540000100010001", "customer_contract_number": "000000001A", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_amount": 10000, "excess_money_handling_category": "9"}
{"request_id": "A-007", "card_number": "3540000100010001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240431", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_amount": 10000, "excess_money_handling_category": "9"}
{"request_id": "A-008", "card_number": "3540000100010001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240631", "deposit_date": "20240611", "deposit_amount": 10000, "excess_money_handling_category": "9"}
{"request_id": "A-009", "card_number": "3540000100010001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240631", "deposit_amount": 10000, "excess_money_handling_category": "9"}
{"request_id": "A-010", "card_number": "3540000100010001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_amount": 0, "excess_money_handling_category": "9"}
{"request_id": "A-011", "card_number": "3540000100010001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_amount": 10000, "excess_money_handling_category": "10"}

--------------------------------
## deposit-category
http://localhost:8183/camel/demo/deposit-category

{"request_id": "A-001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611"}

## check-available-deposit-amount
http://localhost:8183/camel/demo/check-available-deposit-amount

{"request_id": "A-001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611", "deposit_category_code": "9"}

## deposit-allocation
http://localhost:8183/camel/demo/deposit-allocation

{
    "request_id": "A-001", 
    "customer_contract_number": "0000000001", 
    "customer_billing_due_date": "20240515", 
    "contract_settlement_date":"20240610", 
    "deposit_date": "20240611", 
    "deposit_category_code": "9", 
    "deposit_amount": 80616, 
    "excess_money_handling_category": "9",
    "deposit_available_amount_data": {
        "estimated_billing_amount": {
            "total_billing": {
                "billing_principal_amount": 80000,
                "billing_interest_amount": 369,
                "deposit_principal_amount": 0,
                "deposit_interest_amount": 0
            },
            "products_billing_map": {
                "sprv": {
                    "billing_principal_amount": 30000,
                    "billing_interest_amount": 369,
                    "deposit_principal_amount": 0,
                    "deposit_interest_amount": 0
                },
                "sp1": {
                    "billing_principal_amount": 50000,
                    "billing_interest_amount": 0,
                    "deposit_principal_amount": 0,
                    "deposit_interest_amount": 0
                }
            }
        },
        "deposit_available_amount": {
            "total_amout": {
                "principal_amount": 80000,
                "interest_amount": 369
            },
            "products_amount_map": {
                "sprv": {
                    "principal_amount": 30000,
                    "interest_amount": 369
                },
                "sp1": {
                    "principal_amount": 50000,
                    "interest_amount": 0
                }
            }
        }
    }
}

## deposit
http://localhost:8183/camel/demo/deposit

{
    "request_id": "A-001", 
    "customer_contract_number": "0000000001", 
    "customer_billing_due_date": "20240515", 
    "contract_settlement_date":"20240610", 
    "deposit_date": "20240611", 
    "deposit_category_code": "9", 
    "deposit_amount": 10000, 
    "excess_money_handling_category": "9",
    "deposit_allocation_data": {
        "deposit_allocation_amount": {
            "total_amout": {
                "principal_amount": 10000,
                "interest_amount": 0
            },
            "products_amount_map": {
                "sprv": {
                    "principal_amount": 10000,
                    "interest_amount": 0
                },
                "sp1": {
                    "principal_amount": 0,
                    "interest_amount": 0
                }
            }
        },
        "estimated_billing_amount": {
            "total_billing": {
                "billing_principal_amount": 70000,
                "billing_interest_amount": 369,
                "deposit_principal_amount": 10000,
                "deposit_interest_amount": 0
            },
            "products_billing_map": {
                "sprv": {
                    "billing_principal_amount": 20000,
                    "billing_interest_amount": 369,
                    "deposit_principal_amount": 10000,
                    "deposit_interest_amount": 0
                },
                "sp1": {
                    "billing_principal_amount": 50000,
                    "billing_interest_amount": 0,
                    "deposit_principal_amount": 0,
                    "deposit_interest_amount": 0
                }
            }
        },
        "excess_money": 0
    }
}