# LedgerLine

LedgerLine is a money-movement demo that keeps balances and a double-entry ledger consistent, rejects duplicate requests, and records an append-only audit trail.

## Local development

1. Copy `.env.example` to `.env` and set a strong PostgreSQL password and `JWT_SECRET`.
2. Create `ledgerline_db` and the configured database user.
3. Start the API:

   ```powershell
   cd backend\ledgerline
   .\mvnw.cmd spring-boot:run
   ```

4. Start the web app in a second terminal:

   ```powershell
   cd frontend
   npm install
   npm run dev
   ```

The API is available at `http://localhost:8080/api`; the Vite app is available at `http://localhost:5173`.

## Security notes

- Secrets are read from environment variables and `.env` is ignored by Git.
- Money movement runs in a database transaction and locks both accounts in deterministic order.
- An idempotency key is required for every write operation.
- Account owners can operate only their own accounts; auditors can read the audit trail; administrators can manage users.
- See [THREATS.md](THREATS.md) for the threat model and [PROMPTS.md](PROMPTS.md) for the AI-assisted development record.

This repository describes only functionality that is implemented and tested. Deployment-specific environment variables must be configured in the hosting provider; never upload the local `.env`.

