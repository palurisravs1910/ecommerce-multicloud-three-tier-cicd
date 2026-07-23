# Security Policy

## Supported Versions

| Version | Supported |
|---|---|
| 1.x | Yes |

---

## Reporting a Vulnerability

If you discover a security vulnerability, please **do not open a public GitHub issue**.

Instead, contact the maintainer directly:
- GitHub: [@palurisravs1910](https://github.com/palurisravs1910)

Please include:
- Description of the vulnerability
- Steps to reproduce
- Potential impact
- Suggested fix (if any)

You will receive a response within 48 hours.

---

## Security Practices in This Project

| Practice | Implementation |
|---|---|
| Password hashing | BCrypt with salt rounds |
| SQL injection prevention | Parameterized queries throughout all DAOs |
| Session management | HttpOnly cookies, 30-minute timeout |
| Auth protection | AuthFilter on all secured routes |
| DB isolation | RDS in private subnet — no public access |
| Tomcat access | Only accessible via ALB — not direct internet |
| Credentials | Never committed — `db.properties` in `.gitignore` |
| Payment | Stripe handles card data — no raw card numbers stored |

---

## Known Limitations

- Application runs on HTTP (not HTTPS) — HTTPS via ACM is planned
- No rate limiting on login endpoint
- No CSRF protection on API endpoints (planned)
