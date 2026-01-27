#!/bin/sh
set -e

echo "Frontend entrypoint starting..."

# wymagamy BACKEND_API_URL
if [ -z "$BACKEND_API_URL" ]; then
  echo "ERROR: BACKEND_API_URL is not set"
  exit 1
fi

echo "Generating env.js with BACKEND_API_URL=$BACKEND_API_URL"

cat <<EOF > /app/dist/env.js
window.__env = {
  BACKEND_API_URL: "$BACKEND_API_URL"
};
EOF

echo "env.js generated:"
cat /app/dist/env.js

echo "Starting http-server..."
http-server /app/dist -p 8080
