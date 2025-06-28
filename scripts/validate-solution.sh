#!/bin/bash

BRANCH_NAME=$1

echo "🔍 Starting validation for branch: $BRANCH_NAME"

# Extract ticket ID from branch name
TICKET_ID=$(echo "$BRANCH_NAME" | grep -o 'ticket-[0-9]*' | grep -o '[0-9]*')

if [ -z "$TICKET_ID" ]; then
    echo "❌ Could not extract ticket ID from branch name: $BRANCH_NAME"
    exit 1
fi

echo "📋 Processing ticket ID: $TICKET_ID"

# Check if solution file exists
SOLUTION_FILE="src/main/java/solutions/Ticket${TICKET_ID}Solution.java"

if [ ! -f "$SOLUTION_FILE" ]; then
    echo "❌ Solution file not found: $SOLUTION_FILE"
    exit 1
fi

echo "📁 Found solution file: $SOLUTION_FILE"

# Validate solution file content
echo "🔎 Validating solution content..."

# Check if file is not empty
if [ ! -s "$SOLUTION_FILE" ]; then
    echo "❌ Solution file is empty"
    exit 1
fi

# Check for basic Java structure
if ! grep -q "public class Ticket${TICKET_ID}Solution" "$SOLUTION_FILE"; then
    echo "❌ Solution file doesn't contain proper class declaration"
    exit 1
fi

# Check for package declaration
if ! grep -q "package solutions;" "$SOLUTION_FILE"; then
    echo "❌ Solution file missing package declaration"
    exit 1
fi

# Validate file size (must be reasonable)
FILE_SIZE=$(stat -c%s "$SOLUTION_FILE")
if [ "$FILE_SIZE" -lt 50 ]; then
    echo "❌ Solution file too small (less than 50 bytes)"
    exit 1
fi

if [ "$FILE_SIZE" -gt 100000 ]; then
    echo "❌ Solution file too large (more than 100KB)"
    exit 1
fi

echo "✅ All validations passed for ticket $TICKET_ID"
echo "📊 Solution file size: $FILE_SIZE bytes" 