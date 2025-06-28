#!/bin/bash

BRANCH_NAME=$1
STATUS=$2

echo "📡 Updating pipeline status: $STATUS for branch: $BRANCH_NAME"

if [ -z "$SPRING_APP_URL" ]; then
    echo "⚠️ SPRING_APP_URL not set, skipping status update"
    exit 0
fi

# Send status update to Spring Boot application
curl -X POST "$SPRING_APP_URL/api/tickets/pipeline-status" \
  -H "Content-Type: application/json" \
  -d "{\"branch_name\":\"$BRANCH_NAME\",\"status\":\"$STATUS\"}" \
  --silent --show-error

if [ $? -eq 0 ]; then
    echo "✅ Status updated successfully"
else
    echo "❌ Failed to update status"
    exit 1
fi 