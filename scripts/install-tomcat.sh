#!/bin/bash
# ============================================================
# install-tomcat.sh
# Installs Java 21 + Apache Tomcat 9.0.120 on Ubuntu 22.04
# Run as: bash scripts/install-tomcat.sh
# ============================================================

set -e

TOMCAT_VERSION="9.0.120"
TOMCAT_URL="https://dlcdn.apache.org/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz"
INSTALL_DIR="/home/ubuntu"
DEPLOYER_USER="deployer"
DEPLOYER_PASS="Deploy@123"

echo "============================================"
echo " Tomcat Server Setup Script"
echo " Ubuntu 22.04 | Java 21 | Tomcat ${TOMCAT_VERSION}"
echo "============================================"

# ---- Update system ----
echo "[1/6] Updating system packages..."
sudo apt update && sudo apt upgrade -y

# ---- Install Java 21 ----
echo "[2/6] Installing Java 21..."
sudo apt install openjdk-21-jdk -y
java -version

# ---- Download Tomcat ----
echo "[3/6] Downloading Apache Tomcat ${TOMCAT_VERSION}..."
cd ${INSTALL_DIR}
wget -q ${TOMCAT_URL}
tar -xzvf apache-tomcat-${TOMCAT_VERSION}.tar.gz
rm apache-tomcat-${TOMCAT_VERSION}.tar.gz
chmod +x apache-tomcat-${TOMCAT_VERSION}/bin/*.sh

# ---- Configure Tomcat Users ----
echo "[4/6] Configuring Tomcat manager users..."
sudo tee ${INSTALL_DIR}/apache-tomcat-${TOMCAT_VERSION}/conf/tomcat-users.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<tomcat-users xmlns="http://tomcat.apache.org/xml"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://tomcat.apache.org/xml tomcat-users.xsd"
              version="1.0">
  <role rolename="manager-gui"/>
  <role rolename="manager-script"/>
  <user username="${DEPLOYER_USER}" password="${DEPLOYER_PASS}" roles="manager-gui,manager-script"/>
</tomcat-users>
EOF

# ---- First startup to extract webapps ----
echo "[5/6] Starting Tomcat to extract webapps..."
${INSTALL_DIR}/apache-tomcat-${TOMCAT_VERSION}/bin/startup.sh
echo "Waiting 15 seconds for Tomcat to fully start and extract webapps..."
sleep 15

# ---- Allow Remote Manager Access ----
# Must run AFTER first startup so webapps/manager directory exists
echo "[6/6] Allowing remote access to Tomcat manager..."
sudo tee ${INSTALL_DIR}/apache-tomcat-${TOMCAT_VERSION}/webapps/manager/META-INF/context.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<Context antiResourceLocking="false" privileged="true">
  <CookieProcessor className="org.apache.tomcat.util.http.Rfc6265CookieProcessor"
                   sameSiteCookies="strict" />
  <Valve className="org.apache.catalina.valves.RemoteCIDRValve"
         allow="0.0.0.0/0,::/0" />
  <Manager sessionAttributeValueClassNameFilter="java\.lang\.(?:Boolean|Integer|Long|Number|String)|org\.apache\.catalina\.filters\.CsrfPreventionFilter\$LruCache(?:\$1)?|java\.util\.(?:Linked)?HashMap"/>
</Context>
EOF

# ---- Restart Tomcat to apply changes ----
${INSTALL_DIR}/apache-tomcat-${TOMCAT_VERSION}/bin/shutdown.sh
sleep 5
${INSTALL_DIR}/apache-tomcat-${TOMCAT_VERSION}/bin/startup.sh

# ---- Summary ----
echo ""
echo "============================================"
echo " Installation Complete!"
echo "============================================"
echo " Java    : $(java -version 2>&1 | head -1)"
echo " Tomcat  : ${TOMCAT_VERSION}"
echo ""
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)
echo " Tomcat URL : http://${PUBLIC_IP}:8080"
echo " Manager    : http://${PUBLIC_IP}:8080/manager"
echo " Deployer   : ${DEPLOYER_USER} / ${DEPLOYER_PASS}"
echo "============================================"
