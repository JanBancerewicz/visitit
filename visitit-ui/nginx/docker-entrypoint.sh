#!/usr/bin/env bash
set -euo pipefail

: "${CATEGORY_BASE_URL:=http://category-service:8080}"
: "${ELEMENTS_BASE_URL:=http://elements-service:8080}"

export CATEGORY_BASE_URL ELEMENTS_BASE_URL
envsubst '${CATEGORY_BASE_URL} ${ELEMENTS_BASE_URL}' \
  < /etc/nginx/templates/default.conf.template \
  > /etc/nginx/conf.d/default.conf

exec "$@"
