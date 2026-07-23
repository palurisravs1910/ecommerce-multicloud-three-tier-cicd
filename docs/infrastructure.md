# Infrastructure Details

## AWS VPC Configuration

| Resource | Name | Value |
|---|---|---|
| VPC | ecommerce-vpc | 10.0.0.0/16 |
| Public Subnet 1 | ecommerce-public-subnet | 10.0.1.0/24 — ap-south-2a |
| Public Subnet 2 | ecommerce-public-subnet-2 | 10.0.3.0/24 — ap-south-2b |
| Private Subnet | ecommerce-private-subnet | 10.0.2.0/24 — ap-south-2a |
| Internet Gateway | ecommerce-igw | Attached to VPC |
| Route Table | ecommerce-public-rt | 0.0.0.0/0 → IGW |

## Security Group Rules

### alb-sg
| Port | Protocol | Source | Purpose |
|---|---|---|---|
| 80 | TCP | 0.0.0.0/0 | Public HTTP access |

### jenkins-sg
| Port | Protocol | Source | Purpose |
|---|---|---|---|
| 22 | TCP | My IP | SSH only |
| 8080 | TCP | 0.0.0.0/0 | Jenkins UI |

### tomcat-sg
| Port | Protocol | Source | Purpose |
|---|---|---|---|
| 22 | TCP | My IP | SSH only |
| 8080 | TCP | alb-sg | App traffic via ALB only |

### rds-sg
| Port | Protocol | Source | Purpose |
|---|---|---|---|
| 3306 | TCP | tomcat-sg | MySQL from app only |

## Load Balancer
- Name: ecommerce-alb
- Type: Application Load Balancer (Internet-facing)
- Listener: HTTP port 80 → forwards to ecommerce-tg
- Target Group: ecommerce-tg (port 8080, health check: /ecommerce-app/)
- Subnets: ecommerce-public-subnet + ecommerce-public-subnet-2

## Jenkins Configuration
- Elastic IP: 16.113.62.231
- URL: http://16.113.62.231:8080
- Tools: JDK 21 (jdk-21), Maven 3.9 (maven)
- Plugins: Git, GitHub Integration, Pipeline, Deploy to Container
- Credentials: tomcat-aws-credentials (deployer / Deploy@123)

## Tomcat Configuration
- Elastic IP: 16.112.90.159
- Version: 9.0.120
- Install path: /home/ubuntu/apache-tomcat-9.0.120
- Deploy path: /ecommerce-app
- Manager user: deployer
