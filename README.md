# ShopEasy — E-Commerce Three-Tier CI/CD on AWS

A production-grade e-commerce web application built with **Java Servlets + JSP**, deployed on a **three-tier architecture** on **AWS**, with a fully automated **Jenkins CI/CD pipeline**.

---

## Screenshots

### Application UI
![Home Page](screenshots/app-ui/home.png)
![Products Page](screenshots/app-ui/products.png)
![Cart Page](screenshots/app-ui/cart.png)
![Payment Page](screenshots/app-ui/payment.png)

### CI/CD Pipeline
![Jenkins Pipeline](screenshots/jenkins-pipeline/pipeline-success.png)
![Jenkins Stages](screenshots/jenkins-pipeline/pipeline-stages.png)

### AWS Infrastructure
![EC2 Instances](screenshots/aws-infrastructure/ec2-instances.png)
![VPC Setup](screenshots/aws-infrastructure/vpc.png)
![ALB Setup](screenshots/aws-infrastructure/alb.png)
![Security Groups](screenshots/aws-infrastructure/security-groups.png)

### Kiro AI-Assisted Development
![Kiro Session](screenshots/kiro-ai/kiro-session.png)

> This project was built using **Kiro AI** — an agentic AI software engineer inside the Kiro IDE.
> All application code, CI/CD pipeline, and infrastructure documentation were generated and refined
> through AI-assisted prompting, demonstrating the power of combining DevOps expertise with AI tooling.

---

## Architecture Overview

```
  Internet
      │
      ▼  HTTP :80
┌─────────────────────────────────┐
│   Application Load Balancer     │
│   (ecommerce-alb)               │
│   ALB DNS → ecommerce-app       │
└─────────────────────────────────┘
      │
      ▼  HTTP :8080 (internal)
┌─────────────────────────────────────────────────────────┐
│                     AWS VPC (10.0.0.0/16)               │
│                                                         │
│  ┌───────────────────────────────────────────────────┐  │
│  │          Public Subnet (10.0.1.0/24)              │  │
│  │                                                   │  │
│  │  ┌──────────────┐        ┌────────────────────┐   │  │
│  │  │ Jenkins EC2  │        │   Tomcat EC2        │   │  │
│  │  │ t3.medium    │───────▶│   t3.small          │   │  │
│  │  │ (CI/CD)      │        │   (App Server)      │   │  │
│  │  └──────────────┘        └────────────────────┘   │  │
│  └───────────────────────────────────────────────────┘  │
│                                                         │
│  ┌───────────────────────────────────────────────────┐  │
│  │          Private Subnet (10.0.2.0/24)             │  │
│  │                                                   │  │
│  │              ┌─────────────────────┐              │  │
│  │              │    AWS RDS MySQL     │              │  │
│  │              │    (Database Tier)   │              │  │
│  │              └─────────────────────┘              │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

---

## CI/CD Pipeline

```
Developer
    │
    │  git push
    ▼
GitHub Repository
    │
    │  Webhook trigger
    ▼
Jenkins Pipeline
    │
    ├── Stage 1: Checkout        → Pull latest code from GitHub
    ├── Stage 2: Build & Test    → mvn clean test package
    ├── Stage 3: Archive WAR     → Save artifact in Jenkins
    ├── Stage 4: Deploy to AWS   → Push WAR to Tomcat Manager API
    └── Stage 5: Smoke Test      → Verify HTTP 200 via ALB DNS
```

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 21 |
| Web Framework | Java Servlets + JSP | - |
| Build Tool | Apache Maven | 3.9 |
| Application Server | Apache Tomcat | 9.0.120 |
| Database | MySQL | 8.0 |
| Payment Gateway | Stripe API | 23.3.0 |
| CI/CD Server | Jenkins | 2.x |
| Load Balancer | AWS ALB | - |
| Cloud | AWS EC2, RDS, VPC, ALB | - |
| Frontend | Bootstrap 5 + JSTL | 5.3 |
| Security | BCrypt password hashing | - |
| Logging | SLF4J + Logback | - |

---

## Features

- User registration and login with BCrypt password hashing
- Product listing, search, and category filtering
- Shopping cart with real-time quantity management
- Secure checkout with shipping address
- Stripe payment integration with real-time card validation
- Order history and detailed order view
- Admin panel for product and order management
- Auth filter protecting all secured routes
- Responsive UI with Bootstrap 5
- Custom 404, 403, 500 error pages
- Character encoding filter for UTF-8 support

---

## Project Structure

```
ecommerce-multicloud-three-tier-cicd/
│
├── Jenkinsfile                        # CI/CD pipeline definition
├── pom.xml                            # Maven build configuration
├── README.md                          # Project documentation
├── CHANGELOG.md                       # Version history
├── LICENSE                            # MIT License
│
├── scripts/                           # Server setup scripts
│   ├── install-jenkins.sh             # Jenkins installation
│   ├── install-tomcat.sh              # Tomcat installation
│   └── db-setup.sh                    # Database initialization
│
├── docs/                              # Detailed documentation
│   ├── setup-guide.md                 # Step-by-step setup guide
│   ├── ci-cd-pipeline.md              # Pipeline documentation
│   ├── alb-setup.md                   # ALB + Load Balancer setup
│   └── infrastructure.md              # AWS infrastructure details
│
├── screenshots/                       # Project screenshots
│   ├── app-ui/                        # Application UI screenshots
│   ├── jenkins-pipeline/              # Jenkins pipeline screenshots
│   ├── aws-infrastructure/            # AWS console screenshots
│   └── kiro-ai/                       # Kiro AI session screenshots
│
└── src/
    └── main/
        ├── java/com/ecommerce/
        │   ├── controller/            # Servlets (HTTP layer)
        │   ├── dao/                   # Database access layer
        │   ├── filter/                # Auth + encoding filters
        │   ├── model/                 # Domain models
        │   ├── service/               # Business logic layer
        │   └── util/                  # DB connection + config
        ├── resources/
        │   ├── db.properties.example  # Config template (safe to commit)
        │   └── schema.sql             # MySQL schema + seed data
        └── webapp/
            ├── WEB-INF/
            │   ├── web.xml
            │   └── views/             # JSP pages
            └── css/
                └── style.css
```

---

## Quick Start

### Prerequisites
- AWS Account with EC2 and RDS access
- GitHub Account
- Java 21
- Maven 3.9

### 1. Clone the Repository
```bash
git clone https://github.com/palurisravs1910/ecommerce-multicloud-three-tier-cicd.git
cd ecommerce-multicloud-three-tier-cicd
```

### 2. Configure Database
```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
# Edit db.properties with your RDS endpoint and credentials
```

### 3. Initialize Database
```bash
mysql -h <RDS-ENDPOINT> -u admin -p < src/main/resources/schema.sql
```

### 4. Setup Jenkins Server
```bash
bash scripts/install-jenkins.sh
```

### 5. Setup Tomcat Server
```bash
bash scripts/install-tomcat.sh
```

### 6. Build Manually (optional)
```bash
mvn clean package -DskipTests
```

---

## AWS Infrastructure

| Resource | Name | Value |
|---|---|---|
| VPC | ecommerce-vpc | 10.0.0.0/16 |
| Public Subnet 1 | ecommerce-public-subnet | 10.0.1.0/24 |
| Public Subnet 2 | ecommerce-public-subnet-2 | 10.0.3.0/24 |
| Private Subnet | ecommerce-private-subnet | 10.0.2.0/24 |
| Jenkins EC2 | jenkins-server | t3.medium |
| Tomcat EC2 | tomcat-server | t3.small |
| Load Balancer | ecommerce-alb | Internet-facing |
| Target Group | ecommerce-tg | Port 8080 |
| Database | ecommerce-db | RDS MySQL 8.0 |

---

## Security Groups

| SG Name | Port | Source | Purpose |
|---|---|---|---|
| alb-sg | 80 | 0.0.0.0/0 | ALB public access |
| jenkins-sg | 8080 | 0.0.0.0/0 | Jenkins UI |
| jenkins-sg | 22 | My IP | SSH |
| tomcat-sg | 8080 | alb-sg | App via ALB only |
| tomcat-sg | 22 | My IP | SSH |
| rds-sg | 3306 | tomcat-sg | DB from app only |

---

## Documentation

| Document | Description |
|---|---|
| [Setup Guide](docs/setup-guide.md) | Complete step-by-step setup |
| [CI/CD Pipeline](docs/ci-cd-pipeline.md) | Pipeline stages explained |
| [ALB Setup](docs/alb-setup.md) | Load balancer configuration |
| [Infrastructure](docs/infrastructure.md) | AWS resource details |

---

## Roadmap

- [x] Three-tier VPC architecture on AWS
- [x] Jenkins CI/CD pipeline with GitHub webhook
- [x] Maven build + WAR deployment to Tomcat
- [x] Application Load Balancer (ALB)
- [x] Elastic IPs for stable server addresses
- [x] Stripe payment integration
- [x] BCrypt password security
- [ ] AWS RDS MySQL (managed database)
- [ ] HTTPS with AWS Certificate Manager (ACM)
- [ ] Custom domain with Route 53

---

## Built With AI Assistance

This project was developed using **[Kiro](https://kiro.dev)** — an agentic AI software engineer
integrated into the IDE. The entire application code, CI/CD pipeline, and infrastructure
documentation were built through structured AI-assisted prompting, showcasing how modern
DevOps workflows can be accelerated with AI tooling.

---

## Author

**Sravani Paluri**
DevOps | Cloud | Java

[![GitHub](https://img.shields.io/badge/GitHub-palurisravs1910-black?logo=github)](https://github.com/palurisravs1910)
