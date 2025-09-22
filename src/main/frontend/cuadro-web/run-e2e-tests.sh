#!/bin/bash

# E2E Test Runner Script for Mortgage Calculator
# This script demonstrates how to run the Protractor e2e tests

echo "🏠 Mortgage Calculator E2E Test Runner"
echo "======================================"

# Check if Angular dev server is running
if ! curl -s http://localhost:4200 > /dev/null; then
    echo "❌ Angular dev server is not running on localhost:4200"
    echo "Please start the dev server first:"
    echo "  npm start"
    echo ""
    echo "Then run this script again."
    exit 1
fi

echo "✅ Angular dev server is running"
echo ""

# Run the e2e tests
echo "🧪 Running E2E tests..."
echo "This will test:"
echo "  - Slider percentage updates"
echo "  - Financial amount calculations"
echo "  - Form validation"
echo "  - End-to-end calculation flow"
echo ""

npm run e2e

echo ""
echo "✅ E2E tests completed!"
echo ""
echo "Test Coverage:"
echo "  📊 Slider reactivity and percentage display"
echo "  💰 Financial amount calculations with different inputs"
echo "  🔢 Boundary value testing (1% to 100%)"
echo "  📝 Form validation and submission"
echo "  🔄 Real-time updates when values change"
