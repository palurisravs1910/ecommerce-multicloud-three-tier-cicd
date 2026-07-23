# Contributing

Thank you for your interest in contributing to ShopEasy.

---

## How to Contribute

1. Fork the repository
2. Create a feature branch
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Make your changes
4. Test your changes locally with Maven
   ```bash
   mvn clean test package
   ```
5. Commit with a clear message
   ```bash
   git commit -m "Add: brief description of change"
   ```
6. Push to your fork and open a Pull Request

---

## Commit Message Format

| Prefix | When to use |
|---|---|
| `Add:` | New feature or file |
| `Fix:` | Bug fix |
| `Update:` | Modification to existing feature |
| `Remove:` | Deleting something |
| `Docs:` | Documentation only |
| `Refactor:` | Code restructure, no feature change |

---

## Code Standards

- Java 21 compatible code only
- Follow existing package structure (`controller`, `dao`, `service`, `model`)
- Use parameterized queries in all DAO methods — no string concatenation in SQL
- All passwords must be BCrypt hashed
- No credentials or secrets in source code — use `db.properties` (gitignored)

---

## Reporting Issues

Open a GitHub Issue with:
- Clear title
- Steps to reproduce
- Expected vs actual behavior
- Environment details (OS, Java version, browser)
