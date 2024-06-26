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
## deposit-entry-check
http://localhost:8183/camel/demo/deposit-category

{"request_id": "A-001", "customer_contract_number": "0000000001", "customer_billing_due_date": "20240515", "contract_settlement_date":"20240610", "deposit_date": "20240611"}