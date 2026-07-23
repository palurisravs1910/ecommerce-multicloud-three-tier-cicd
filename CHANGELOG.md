# Changelog

All notable changes to this project are documented here.

---

## [1.3.0] - 2026-07-22

### Added
- AWS RDS MySQL 8.4.9 connected as managed database tier
- Database running in private subnet — no public access
- Schema initialized with all 7 tables and seed data
- App fully connected to RDS — three-tier architecture complete

---

## [1.2.0] - 2026-07-22

### Added
- Application Load Balancer (ALB) in front of Tomcat
- Second public subnet (`ecommerce-public-subnet-2`) for ALB high availability
- ALB security group (`alb-sg`) for controlled public access
- ALB target group (`ecommerce-tg`) with health checks on `/ecommerce-app/`
- Elastic IPs for Jenkins and Tomcat servers (stable IPs on restart)
- `scripts/` folder with reusable shell scripts
- `docs/alb-setup.md` with complete ALB + RDS setup guide
- `docs/setup-guide.md` with end-to-end setup instructions
- `docs/ci-cd-pipeline.md` with pipeline documentation
- `screenshots/` folder structure for project screenshots
- `db.properties.example` template file
- `CHANGELOG.md` and `LICENSE`
- Improved `.gitignore` — excludes `db.properties` and build artifacts

### Changed
- Tomcat security group updated — port 8080 now only accessible from `alb-sg`
- Jenkinsfile smoke test now uses ALB DNS instead of direct Tomcat IP
- Jenkinsfile cleaned up — no hardcoded IPs, uses environment variables
- README completely rewritten — professional open-source format

### Security
- `db.properties` removed from Git tracking (contains credentials)
- Tomcat no longer directly exposed to internet (behind ALB)

---

## [1.1.0] - 2026-07-22

### Added
- GitHub webhook for automatic Jenkins pipeline trigger on push
- Elastic IP allocation for Jenkins and Tomcat servers
- `docs/infrastructure.md` with AWS resource details

### Fixed
- `getLoggedUser` method missing from `OrderServlet`
- Jenkins tool names corrected (`maven`, `jdk-21`)
- `JAVA_HOME` environment variable set on Jenkins server
- Tomcat Manager remote access enabled (`RemoteCIDRValve allow 0.0.0.0/0`)

---

## [1.0.0] - 2026-07-22

### Added
- Full e-commerce Java web application (Servlets + JSP)
- Three-tier AWS architecture: VPC + public/private subnets
- Jenkins CI/CD pipeline with 5 stages
- Maven build producing deployable WAR artifact
- Apache Tomcat 9 application server
- MySQL database schema with seed data
- User authentication with BCrypt password hashing
- Product listing, search, and category filtering
- Shopping cart with quantity management
- Stripe payment integration
- Order management and history
- Admin panel for products and orders
- Bootstrap 5 responsive UI
- Auth and encoding servlet filters
- Custom error pages (404, 403, 500)
- `schema.sql` for database initialization
- `README.md` with architecture diagram
