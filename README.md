# E-Commerce Multi-Cloud Three-Tier CI/CD Application

A production-grade e-commerce web application deployed on a **three-tier architecture** across **AWS**, with CI/CD automation using **Jenkins**, **Maven**, and **Tomcat**.

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                        AWS VPC                          │
│                    (10.0.0.0/16)                        │
│                                                         │
│  ┌──────────────────────────────────────────────────┐   │
│  │           Public Subnet (10.0.1.0/24)            │   │
│  │                                                  │   │
│  │   ┌─────────────┐      ┌─────────────────────┐  │   │
│  │   │Jenkins EC2  │      │    Tomcat EC2        │  │   │
│  │   │(CI/CD)      │─────▶│  (App Server)        │  │   │
│  │   │t3.medium    │      │  t3.small            │  │   │
│  │   └─────────────┘      └─────────────────────┘  │   │
│  └──────────────────────────────────────────────────┘   │
│                                                         │
│  ┌──────────────────────────────────────────────────┐   │
│  │          Private Subnet (10.0.2.0/24)            │   │
│  │                                                  │   │
│  │              ┌──────────────────┐                │   │
│  │              │   RDS MySQL      │                │   │
│  │              │  (Database Tier) │                │   │
│  │              └──────────────────┘                │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

---

## CI/CD Pipeline

```
Developer → git push → GitHub → Webhook → Jenkins Pipeline
                                               │
                              ┌────────────────▼────────────────┐
                              │  Stage 1: Checkout               │
                              │  Stage 2: Build & Test (Maven)   │
                              │  Stage 3: Archive WAR Artifact   │
                              │  Stage 4: Deploy to Tomcat       │
                              │  Stage 5: Smoke Test             │
                              └─────────────────────────────────┘
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Web Framework | Java Servlets + JSP |
| Build Tool | Maven 3.9 |
| Application Server | Apache Tomcat 9 |
| Database | MySQL 8 |
| Payment | Stripe API |
| CI/CD | Jenkins |
| Version Control | GitHub |
| Cloud | AWS (EC2, RDS, VPC) |
| Frontend | Bootstrap 5, JSTL |

---

## Features

- User registration and login with BCrypt password hashing
- Product listing, search, and filtering by category
- Shopping cart with quantity management
- Secure checkout flow
- Stripe payment integration
- Order history and order detail view
- Admin panel for product and order management
- Responsive UI with Bootstrap 5

---

## Project Structure

```
ecommerce-multicloud-three-tier-cicd/
├── Jenkinsfile                          # CI/CD pipeline definition
├── pom.xml                              # Maven build configuration
├── README.md                            # Project documentation
├── docs/                                # Architecture diagrams
└── src/
    └── main/
        ├── java/com/ecommerce/
        │   ├── controller/              # Servlets (HTTP layer)
        │   │   ├── AuthServlet.java
        │   │   ├── CartServlet.java
        │   │   ├── CheckoutServlet.java
        │   │   ├── HomeServlet.java
        │   │   ├── OrderServlet.java
        │   │   ├── PaymentServlet.java
        │   │   └── ProductServlet.java
        │   ├── dao/                     # Database access layer
        │   │   ├── CartDAO.java
        │   │   ├── OrderDAO.java
        │   │   ├── PaymentDAO.java
        │   │   ├── ProductDAO.java
        │   │   └── UserDAO.java
        │   ├── filter/                  # Servlet filters
        │   │   ├── AuthFilter.java
        │   │   └── CharacterEncodingFilter.java
        │   ├── model/                   # Domain models
        │   │   ├── CartItem.java
        │   │   ├── Category.java
        │   │   ├── Order.java
        │   │   ├── OrderItem.java
        │   │   ├── Payment.java
        │   │   ├── Product.java
        │   │   └── User.java
        │   ├── service/                 # Business logic layer
        │   │   ├── CartService.java
        │   │   ├── OrderService.java
        │   │   ├── PaymentService.java
        │   │   ├── ProductService.java
        │   │   └── UserService.java
        │   └── util/                    # Utilities
        │       ├── AppConfig.java
        │       └── DBConnection.java
        ├── resources/
        │   ├── db.properties            # Database configuration
        │   └── schema.sql               # Database schema + seed data
        └── webapp/
            ├── WEB-INF/
            │   ├── web.xml              # Deployment descriptor
            │   └── views/              # JSP pages
            │       ├── common/         # Header, footer
            │       ├── error/          # 404, 403, 500 pages
            │       ├── index.jsp
            │       ├── products.jsp
            │       ├── product-detail.jsp
            │       ├── cart.jsp
            │       ├── login.jsp
            │       ├── register.jsp
            │       ├── checkout.jsp
            │       ├── payment.jsp
            │       ├── order-history.jsp
            │       └── order-detail.jsp
            └── css/
                └── style.css
```

---

## Infrastructure Setup

### Prerequisites
- AWS Account
- GitHub Account
- Java 21
- Maven 3.9

### AWS Infrastructure

#### VPC Setup
| Resource | Value |
|---|---|
| VPC CIDR | 10.0.0.0/16 |
| Public Subnet | 10.0.1.0/24 |
| Private Subnet | 10.0.2.0/24 |
| Internet Gateway | Attached to public subnet |

#### EC2 Instances
| Server | Instance Type | Subnet | Purpose |
|---|---|---|---|
| jenkins-server | t3.medium | Public | CI/CD Server |
| tomcat-server | t3.small | Public | App Server |

#### Security Groups
| SG Name | Port | Source | Purpose |
|---|---|---|---|
| jenkins-sg | 22 | My IP | SSH |
| jenkins-sg | 8080 | Anywhere | Jenkins UI |
| tomcat-sg | 22 | My IP | SSH |
| tomcat-sg | 8080 | Anywhere | App Access |
| rds-sg | 3306 | tomcat-sg | DB Access |

### Jenkins Setup

```bash
# Install Java 21
sudo apt install openjdk-21-jdk -y

# Install Jenkins
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update && sudo apt install jenkins -y
sudo systemctl start jenkins && sudo systemctl enable jenkins

# Install Maven and Git
sudo apt install maven git -y
```

### Tomcat Setup

```bash
# Install Java 21
sudo apt install openjdk-21-jdk -y

# Download Tomcat 9
cd /home/ubuntu
wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.120/bin/apache-tomcat-9.0.120.tar.gz
tar -xzvf apache-tomcat-9.0.120.tar.gz
chmod +x apache-tomcat-9.0.120/bin/*.sh

# Start Tomcat
./apache-tomcat-9.0.120/bin/startup.sh
```

---

## Database Setup

Run the schema file on your MySQL instance:

```bash
mysql -u root -p < src/main/resources/schema.sql
```

Update `src/main/resources/db.properties` with your RDS endpoint:

```properties
db.url=jdbc:mysql://<rds-endpoint>:3306/ecommerce_db
db.username=your_username
db.password=your_password
```

---

## CI/CD Pipeline Stages

| Stage | Description |
|---|---|
| Checkout | Pulls latest code from GitHub |
| Build & Test | Runs `mvn clean test package` |
| Archive Artifact | Saves WAR file as build artifact |
| Deploy to AWS | Deploys WAR to Tomcat via Manager API |
| Smoke Test | Verifies app returns HTTP 200 |

---

## Roadmap

- [x] Three-tier architecture on AWS
- [x] Jenkins CI/CD pipeline
- [x] GitHub webhook auto-trigger
- [x] Maven build + WAR deployment
- [x] Stripe payment integration
- [ ] AWS RDS MySQL (managed database)
- [ ] Azure VM (failover/DR)
- [ ] Docker containerization
- [ ] AWS EKS deployment
- [ ] Terraform infrastructure as code
- [ ] SonarQube code quality analysis
- [ ] Nginx reverse proxy

---

## Author

**Sravani Paluri**  
DevOps | Cloud | Java  
[GitHub](https://github.com/palurisravs1910)
