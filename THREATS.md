# Threat model

## Assets

- Account balances and the double-entry ledger
- Authentication credentials and JWT signing secret
- Idempotency records
- Audit history and personal account data

## Threats and mitigations

| Threat | Mitigation | Test/verification |
|---|---|---|
| Duplicate transfer request | Required idempotency key with unique database constraint; replay returns the original result | `MoneyMovementServiceTest` |
| Concurrent withdrawals | PostgreSQL pessimistic row locks in deterministic order; balance check occurs after locking | `MoneyMovementServiceTest` and database integration tests |
| Negative balance | Server-side balance validation inside the transaction | withdrawal failure test |
| Cross-account access | Service-level owner checks and role restrictions | authorization tests |
| Forged or expired token | Signed JWT validation with configured secret and expiry | Spring Security configuration |
| Audit tampering | Audit records are inserted with the business transaction and have no update/delete API | audit endpoint review |
| SQL injection | Spring Data parameterized queries; no string-built SQL | code review |
| Secret leakage | `.env` ignored; `.env.example` contains placeholders only | Git ignore check |

## Residual risks

Production deployment still needs TLS, managed secret storage, rate limiting, refresh-token/session revocation, database backups, monitoring, and an external penetration test.

