#!/bin/bash
# ============================================================
# db-setup.sh
# Initializes the ecommerce_db MySQL database
# Run as: bash scripts/db-setup.sh <RDS-ENDPOINT> <USERNAME>
# Example: bash scripts/db-setup.sh mydb.xxxx.rds.amazonaws.com admin
# ============================================================

set -e

RDS_ENDPOINT=${1:-"localhost"}
DB_USER=${2:-"admin"}
SCHEMA_FILE="src/main/resources/schema.sql"

echo "============================================"
echo " Database Setup Script"
echo " Host : ${RDS_ENDPOINT}"
echo " User : ${DB_USER}"
echo "============================================"

# Check schema file exists
if [ ! -f "${SCHEMA_FILE}" ]; then
    echo "ERROR: Schema file not found at ${SCHEMA_FILE}"
    exit 1
fi

# Run schema
echo "Running schema.sql on ${RDS_ENDPOINT}..."
mysql -h "${RDS_ENDPOINT}" -u "${DB_USER}" -p < "${SCHEMA_FILE}"

echo ""
echo "============================================"
echo " Database setup complete!"
echo " Database : ecommerce_db"
echo " Tables   : users, categories, products,"
echo "            cart, orders, order_items, payments"
echo " Seed data: 5 categories, 7 products, 1 admin"
echo "============================================"
echo ""
echo " Admin login:"
echo "   Email    : admin@ecommerce.com"
echo "   Password : Admin@123"
echo "============================================"
