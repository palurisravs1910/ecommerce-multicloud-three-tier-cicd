#!/bin/bash
# ============================================================
# install-jenkins.sh
# Installs Jenkins + Java 21 + Maven + Git on Ubuntu 22.04
# Run as: bash scripts/install-jenkins.sh
# ============================================================

set -e

echo "============================================"
echo " Jenkins Server Setup Script"
echo " Ubuntu 22.04 | Java 21 | Maven | Git"
echo "============================================"

# ---- Update system ----
echo "[1/6] Updating system packages..."
sudo apt update && sudo apt upgrade -y

# ---- Install Java 21 ----
echo "[2/6] Installing Java 21..."
sudo apt install openjdk-21-jdk -y
java -version

# ---- Set JAVA_HOME ----
echo "[3/6] Setting JAVA_HOME..."
echo 'JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"' | sudo tee -a /etc/environment
source /etc/environment

# ---- Install Jenkins ----
echo "[4/6] Installing Jenkins..."
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
    /usr/share/keyrings/jenkins-keyring.asc > /dev/null

echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
    https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
    /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt update
sudo apt install jenkins -y

# ---- Start Jenkins ----
echo "[5/6] Starting Jenkins service..."
sudo systemctl start jenkins
sudo systemctl enable jenkins

# ---- Install Maven and Git ----
echo "[6/6] Installing Maven and Git..."
sudo apt install maven git -y

# ---- Summary ----
echo ""
echo "============================================"
echo " Installation Complete!"
echo "============================================"
echo " Java    : $(java -version 2>&1 | head -1)"
echo " Jenkins : $(jenkins --version)"
echo " Maven   : $(mvn -version | head -1)"
echo " Git     : $(git --version)"
echo ""
echo " Jenkins UI  : http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080"
echo " Admin Pass  : sudo cat /var/lib/jenkins/secrets/initialAdminPassword"
echo "============================================"
