# Infrastructure Details

## AWS VPC Configuration

| Resource | Name | Value |
|---|---|---|
| VPC | ecommerce-vpc | 10.0.0.0/16 |
| Public Subnet | ecommerce-public-subnet | 10.0.1.0/24 |
| Private Subnet | ecommerce-private-subnet | 10.0.2.0/24 |
| Internet Gateway | ecommerce-igw | Attached to VPC |
| Route Table | ecommerce-public-rt | 0.0.0.0/0 → IGW |

## Security Group Rules

### jenkins-sg
| Port | Protocol | Source |
|---|---|---|
| 22 | TCP | My IP |
| 8080 | TCP | 0.0.0.0/0 |

### tomcat-sg
| Port | Protocol | Source |
|---|---|---|
| 22 | TCP | My IP |
| 8080 | TCP | 0.0.0.0/0 |

### rds-sg
| Port | Protocol | Source |
|---|---|---|
| 3306 | TCP | tomcat-sg |

## Jenkins Configuration
- URL: http://16.113.19.153:8080
- Tools: JDK 21, Maven 3.9
- Plugins: Git, GitHub Integration, Pipeline, Deploy to Container
- Credentials: tomcat-aws-credentials

## Tomcat Configuration
- URL: http://16.112.58.237:8080
- Version: 9.0.120
- Deploy path: /ecommerce-app
- Manager user: deployer
