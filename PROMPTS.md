# AI development record

This file is maintained during development. Entries record what was requested, what was produced, what was wrong or incomplete, and what was changed instead.

## 2026-11-12 — project inspection and scope

- **Asked:** Inspect the existing LedgerLine project and help complete it.
- **Produced:** The supplied GitHub repository and local workspace were empty, so no existing implementation could be safely extended.
- **What was wrong:** The original setup described by the brief was not present in the selected folder.
- **Did instead:** Initialized a new, explainable MVP in the confirmed workspace. Commit: `pending`

## 2026-11-12 — authorization design

- **Asked:** Build role-based authorization for account holders, auditors, and administrators.
- **Produced:** A JWT-based Spring Security boundary with role checks and ownership checks in the service layer.
- **What was wrong:** A role check alone would allow one account holder to access another holder's account.
- **Did instead:** Added explicit owner checks for account reads and writes, while auditors receive read-only audit access. Commit: `pending`

## 2026-11-12 — access-control review

- **Asked:** Protect money movement and account data from unauthorized access.
- **Produced:** Controller annotations and service authorization.
- **What was wrong:** Controller-only checks are easy to bypass when a service is reused by another endpoint.
- **Did instead:** Kept authorization in both the HTTP boundary and business service, and added tests for cross-owner access rejection. Commit: `pending`

## 2026-11-12 — audit trail

- **Asked:** Add a traceable, append-only audit trail for money movement.
- **Produced:** Audit rows written in the same transaction as the ledger entries.
- **What was wrong:** A mutable audit entity would not meet the append-only requirement.
- **Did instead:** Exposed only a read repository for audit records and never update or delete existing audit rows. Commit: `pending`

## 2026-11-12 — concurrency and idempotency

- **Asked:** Prevent duplicate payments and negative balances under simultaneous requests.
- **Produced:** An idempotency table and pessimistic account row locks.
- **What was wrong:** Locking only the sender allows races when the receiver is also being modified.
- **Did instead:** Lock both accounts in sorted ID order, validate the sender balance after locking, and persist the idempotency result atomically. Commit: `pending`

