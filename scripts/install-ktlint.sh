#!/usr/bin/env bash
set -euo pipefail

KTLINT_VERSION="1.8.0"
INSTALL_DIR="${1:-/usr/local/bin}"

echo "Installing ktlint ${KTLINT_VERSION} to ${INSTALL_DIR}..."
curl -sSLO "https://github.com/pinterest/ktlint/releases/download/${KTLINT_VERSION}/ktlint"
chmod +x ktlint

if [ -w "${INSTALL_DIR}" ]; then
    mv ktlint "${INSTALL_DIR}/"
else
    sudo mv ktlint "${INSTALL_DIR}/"
fi

echo "ktlint ${KTLINT_VERSION} installed successfully."
