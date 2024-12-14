CREATE TABLE payments (id BIGINT PRIMARY KEY, status varchar(255), type varchar(255), session_url varchar(512), session_id varchar(512), amount_to_pay BIGINT, is_deleted BOOLEAN);

insert into payments (id, status, type, session_url, session_id, amount_to_pay, is_deleted)
values (1, 'PENDING', 'PAYMENT', 'https://checkout.stripe.com/c/pay/cs_test_a14MDwUGkZTN45bSwYJxpeg47gs2lIpGpHOUg3Tjj6nXBzYcearaHGn0n3#fidkdWxOYHwnPyd1blpxYHZxWjA0VE5iTHJXcVZCd20wfVd0SH9EQ3NcYDZEYXdWMX1EPV1GcHx0Tjd9T05Ka3x1b2RMdzNMNX99bXJ8Z2dWaGF9Q2dRNWBLMT1SMTFSQEhIaEJNMmp9dEBdNTVqbFY2QjFAfCcpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl', 'cs_test_a14MDwUGkZTN45bSwYJxpeg47gs2lIpGpHOUg3Tjj6nXBzYcearaHGn0n3', 7200, 0);
insert into payments (id, status, type, session_url, session_id, amount_to_pay, is_deleted)
values (2, 'PAID', 'FINE', 'https://checkout.stripe.com/c/pay/cs_test_a14MDwUGkZTN45bSwYJxpeg47gs2lIpGpHOUg3Tjj6nXBzYcearaHGn0n3#fidkdWxOYHwnPyd1blpxYHZxWjA0VE5iTHJXcVZCd20wfVd0SH9EQ3NcYDZEYXdWMX1EPV1GcHx0Tjd9T05Ka3x1b2RMdzNMNX99bXJ8Z2dWaGF9Q2dRNWBLMT1SMTFSQEhIaEJNMmp9dEBdNTVqbFY2QjFAfCcpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl', 'cs_test_a14MDwUGkZTN45bSwYJxpeg47gs2lIpGpHOUg3Tjj6nXBzYcearaHGn0n3', 7560, 0);