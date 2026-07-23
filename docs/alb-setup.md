# Application Load Balancer (ALB) + RDS MySQL Setup

---

## Why ALB?

Instead of exposing Tomcat directly via public IP, the ALB sits in front and provides:
- Single stable DNS entry point (no raw IP)
- Health checks on Tomcat
- Easy HTTPS/SSL termination (future)
- Foundation for auto-scaling

```
Internet → ALB (port 80) → Target Group → Tomcat EC2 (port 8080)
```

---

## ALB Setup

### Step 1 — Create ALB Security Group

EC2 → Security Groups → Create Security Group

| Field | Value |
|---|---|
| Name | `alb-sg` |
| VPC | `ecommerce-vpc` |
| Inbound: Port 80 | Source: 0.0.0.0/0 |

### Step 2 — Update Tomcat Security Group

EC2 → Security Groups → `tomcat-sg` → Edit Inbound Rules

Change port 8080 source from `Anywhere` to `alb-sg`:

| Port | Source |
|---|---|
| 8080 | alb-sg |

This ensures Tomcat is only accessible through the ALB — not directly from the internet.

### Step 3 — Create Target Group

EC2 → Target Groups → Create Target Group

| Field | Value |
|---|---|
| Target type | Instances |
| Name | `ecommerce-tg` |
| Protocol | HTTP |
| Port | 8080 |
| VPC | `ecommerce-vpc` |
| Health check path | `/ecommerce-app/` |
| Healthy threshold | 2 |
| Unhealthy threshold | 3 |
| Interval | 30 seconds |

Click Next → Select `tomcat-server` → Include as pending → Create

### Step 4 — Create Application Load Balancer

EC2 → Load Balancers → Create → Application Load Balancer

| Field | Value |
|---|---|
| Name | `ecommerce-alb` |
| Scheme | Internet-facing |
| IP type | IPv4 |
| VPC | `ecommerce-vpc` |
| Subnets | `ecommerce-public-subnet` + `ecommerce-public-subnet-2` |
| Security Group | `alb-sg` |

Listener:
| Protocol | Port | Action |
|---|---|---|
| HTTP | 80 | Forward to `ecommerce-tg` |

Click Create Load Balancer.

### Step 5 — Get ALB DNS Name

After creation, copy the DNS name from the ALB details page:
```
ecommerce-alb-xxxxxxxx.ap-south-2.elb.amazonaws.com
```

Your app is now accessible at:
```
http://ecommerce-alb-xxxxxxxx.ap-south-2.elb.amazonaws.com/ecommerce-app/
```

### Step 6 — Update Jenkinsfile

Update the `ALB_DNS` value in Jenkinsfile with your ALB DNS name for smoke tests.

---

## Health Check

The ALB continuously checks Tomcat health every 30 seconds at `/ecommerce-app/`.

- **Healthy:** HTTP 200 → ALB routes traffic to instance
- **Unhealthy:** No response → ALB stops routing to instance

You can view health status at:
EC2 → Target Groups → `ecommerce-tg` → Targets tab

---

## RDS MySQL Setup

### Step 1 — Create DB Subnet Group

RDS → Subnet Groups → Create DB Subnet Group

| Field | Value |
|---|---|
| Name | `ecommerce-db-subnet-group` |
| VPC | `ecommerce-vpc` |
| Subnets | `ecommerce-private-subnet` |

### Step 2 — Create RDS Instance

RDS → Databases → Create Database

| Field | Value |
|---|---|
| Engine | MySQL 8.0 |
| Template | Free tier |
| DB identifier | `ecommerce-db` |
| Master username | `admin` |
| Master password | (your secure password) |
| Instance class | `db.t3.micro` |
| Storage | 20 GB gp2 |
| VPC | `ecommerce-vpc` |
| Subnet group | `ecommerce-db-subnet-group` |
| Public access | No |
| Security group | `rds-sg` |
| Initial DB name | `ecommerce_db` |

Click Create Database — takes 5-10 minutes to provision.

### Step 3 — Get RDS Endpoint

RDS → Databases → `ecommerce-db` → Connectivity tab → copy Endpoint:
```
ecommerce-db.xxxxxxxxxx.ap-south-2.rds.amazonaws.com
```

### Step 4 — Connect App to RDS

SSH into Tomcat server and update db.properties:
```bash
nano /home/ubuntu/apache-tomcat-9.0.120/webapps/ecommerce-app/WEB-INF/classes/db.properties
```

Update:
```properties
db.url=jdbc:mysql://ecommerce-db.xxxxxxxxxx.ap-south-2.rds.amazonaws.com:3306/ecommerce_db?useSSL=true&serverTimezone=UTC
db.username=admin
db.password=your_password
```

### Step 5 — Initialize Database Schema

From any machine that can reach RDS (or from Tomcat EC2):
```bash
bash scripts/db-setup.sh ecommerce-db.xxxxxxxxxx.ap-south-2.rds.amazonaws.com admin
```

### Step 6 — Verify Connection

From Tomcat server SSH:
```bash
mysql -h ecommerce-db.xxxxxxxxxx.ap-south-2.rds.amazonaws.com -u admin -p ecommerce_db
show tables;
```

Should show:
```
cart, categories, order_items, orders, payments, products, users
```

---

## Architecture After ALB + RDS

```
Internet
    │
    ▼ HTTP :80
┌─────────────────────┐
│       ALB           │  ← ecommerce-alb (DNS entry point)
│   Health checks     │
└─────────────────────┘
    │
    ▼ HTTP :8080 (internal, alb-sg only)
┌─────────────────────┐
│    Tomcat EC2       │  ← App Server (tomcat-sg)
│  ecommerce-app.war  │
└─────────────────────┘
    │
    ▼ MySQL :3306 (internal, tomcat-sg only)
┌─────────────────────┐
│    RDS MySQL        │  ← Private subnet (rds-sg)
│   ecommerce_db      │
└─────────────────────┘
```
