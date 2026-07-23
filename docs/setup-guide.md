# Complete Setup Guide

This guide walks through the complete setup of the ShopEasy e-commerce application on AWS from scratch.

---

## Prerequisites

- AWS Account
- GitHub Account with the repository cloned
- Windows/Linux/Mac machine with SSH client
- Key pair `.pem` file downloaded

---

## Step 1 — AWS VPC Setup

### Create VPC
- Go to AWS Console → VPC → Your VPCs → Create VPC
- Name: `ecommerce-vpc`
- IPv4 CIDR: `10.0.0.0/16`

### Create Subnets

**Public Subnet 1:**
- Name: `ecommerce-public-subnet`
- AZ: `ap-south-2a`
- CIDR: `10.0.1.0/24`

**Public Subnet 2 (required for ALB):**
- Name: `ecommerce-public-subnet-2`
- AZ: `ap-south-2b`
- CIDR: `10.0.3.0/24`

**Private Subnet:**
- Name: `ecommerce-private-subnet`
- AZ: `ap-south-2a`
- CIDR: `10.0.2.0/24`

### Create Internet Gateway
- Name: `ecommerce-igw`
- Actions → Attach to VPC → `ecommerce-vpc`

### Create Route Table
- Name: `ecommerce-public-rt`
- VPC: `ecommerce-vpc`
- Edit Routes → Add: `0.0.0.0/0` → `ecommerce-igw`
- Associate both public subnets

### Enable Auto-assign Public IP
- For both public subnets → Edit subnet settings → Enable auto-assign public IPv4

---

## Step 2 — Security Groups

### alb-sg
| Port | Source | Purpose |
|---|---|---|
| 80 | 0.0.0.0/0 | ALB public HTTP |

### jenkins-sg
| Port | Source | Purpose |
|---|---|---|
| 22 | My IP | SSH |
| 8080 | 0.0.0.0/0 | Jenkins UI |

### tomcat-sg
| Port | Source | Purpose |
|---|---|---|
| 22 | My IP | SSH |
| 8080 | alb-sg | App via ALB only |

### rds-sg
| Port | Source | Purpose |
|---|---|---|
| 3306 | tomcat-sg | MySQL from app only |

---

## Step 3 — EC2 Instances

### Jenkins Server
- AMI: Ubuntu Server 22.04 LTS
- Instance type: `t3.medium`
- Subnet: `ecommerce-public-subnet`
- Security group: `jenkins-sg`
- Storage: 20 GB

### Tomcat Server
- AMI: Ubuntu Server 22.04 LTS
- Instance type: `t3.small`
- Subnet: `ecommerce-public-subnet`
- Security group: `tomcat-sg`
- Storage: 15 GB

### Elastic IPs
Allocate and associate Elastic IPs to both instances so IPs don't change on restart.

---

## Step 4 — Jenkins Installation

SSH into Jenkins server:
```bash
ssh -i "yourkey.pem" ubuntu@<jenkins-elastic-ip>
```

Run the setup script:
```bash
bash scripts/install-jenkins.sh
```

Open Jenkins UI: `http://<jenkins-elastic-ip>:8080`

Get initial password:
```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

### Jenkins Configuration
1. Install suggested plugins
2. Create admin user
3. Go to Manage Jenkins → Tools:
   - JDK: name `jdk-21`, path `/usr/lib/jvm/java-21-openjdk-amd64`
   - Maven: name `maven`, path `/usr/share/maven`
4. Go to Manage Jenkins → Credentials → Add:
   - ID: `tomcat-aws-credentials`
   - Username: `deployer`
   - Password: `Deploy@123`

---

## Step 5 — Tomcat Installation

SSH into Tomcat server:
```bash
ssh -i "yourkey.pem" ubuntu@<tomcat-elastic-ip>
```

Run the setup script:
```bash
bash scripts/install-tomcat.sh
```

Verify: `http://<tomcat-elastic-ip>:8080`

---

## Step 6 — RDS MySQL Setup

See [RDS Setup section in alb-setup.md](alb-setup.md#rds-mysql-setup) for complete steps.

---

## Step 7 — Application Load Balancer

See [ALB Setup Guide](alb-setup.md) for complete steps.

---

## Step 8 — Jenkins Pipeline

1. Jenkins → New Item → `ecommerce-pipeline` → Pipeline
2. Pipeline → Definition: Pipeline script from SCM
3. SCM: Git
4. Repository URL: `https://github.com/palurisravs1910/ecommerce-multicloud-three-tier-cicd.git`
5. Branch: `*/main`
6. Script Path: `Jenkinsfile`
7. Save → Build Now

### GitHub Webhook
1. GitHub repo → Settings → Webhooks → Add webhook
2. Payload URL: `http://<jenkins-elastic-ip>:8080/github-webhook/`
3. Content type: `application/json`
4. Event: Push only
5. Jenkins job → Configure → Build Triggers → GitHub hook trigger for GITScm polling

---

## Step 9 — Configure Database Connection

Copy and edit db.properties:
```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
nano src/main/resources/db.properties
```

Update with your RDS endpoint:
```properties
db.url=jdbc:mysql://<RDS-ENDPOINT>:3306/ecommerce_db
db.username=admin
db.password=your_password
```

Initialize the database:
```bash
bash scripts/db-setup.sh <RDS-ENDPOINT> admin
```
