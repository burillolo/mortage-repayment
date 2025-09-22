# E2E Tests for Mortgage Calculator

This directory contains end-to-end (E2E) tests for the Mortgage Calculator application using Protractor.

## Test Files

### `app.e2e-spec.ts`
Basic application tests that verify:
- Application loads correctly
- All required form fields are present
- Dashboard component is displayed

### `dashboard-simulator.e2e-spec.ts`
Comprehensive tests for the dashboard simulator functionality:
- **Slider Reactivity**: Tests that the financing percentage slider updates correctly
- **Financial Calculations**: Verifies that financing amount calculations are accurate
- **Form Validation**: Ensures form validation works properly
- **Boundary Testing**: Tests minimum (1%) and maximum (100%) percentage values
- **Real-time Updates**: Verifies that changes to inputs update calculations immediately

### `dashboard-simulator.po.ts`
Page Object Model for the dashboard simulator, providing:
- Element selectors for all form fields and displays
- Helper methods for interacting with the slider
- Methods for verifying calculations and displays
- Utility functions for form interaction

## Test Scenarios

### 1. Slider Functionality
- ✅ Default percentage display (80%)
- ✅ Slider value updates when moved
- ✅ Percentage display updates in real-time
- ✅ Boundary value testing (1% to 100%)

### 2. Financial Calculations
- ✅ Financing amount updates when property price changes
- ✅ Financing amount updates when percentage changes
- ✅ Calculation accuracy with various input combinations
- ✅ Decimal amount handling

### 3. Form Integration
- ✅ All form fields are present and functional
- ✅ Form validation works correctly
- ✅ Calculate button triggers submission
- ✅ End-to-end calculation flow

## Running the Tests

### Prerequisites
1. Install dependencies: `npm install`
2. Start the Angular dev server: `npm start`
3. Ensure the application is accessible at `http://localhost:4200`

### Running Tests

#### Option 1: Using the test runner script
```bash
./run-e2e-tests.sh
```

#### Option 2: Using npm script
```bash
npm run e2e
```

#### Option 3: Running specific test files
```bash
# Run only basic app tests
ng e2e --specs=./e2e/src/app.e2e-spec.ts

# Run only dashboard simulator tests
ng e2e --specs=./e2e/src/dashboard-simulator.e2e-spec.ts
```

## Test Configuration

### `protractor.conf.js`
- **Browser**: Chrome (headless mode available)
- **Base URL**: `http://localhost:4200/`
- **Timeout**: 30 seconds
- **Framework**: Jasmine with SpecReporter

### `tsconfig.json`
- **Target**: ES2018
- **Module**: CommonJS
- **Types**: Jasmine, Node, Protractor

## Test Data

The tests use various test cases to ensure comprehensive coverage:

```typescript
// Property amounts and expected financing calculations
const testCases = [
  { amount: 200000, percentage: 80, expectedFinancing: 160000 },
  { amount: 300000, percentage: 60, expectedFinancing: 180000 },
  { amount: 150000, percentage: 90, expectedFinancing: 135000 },
  { amount: 500000, percentage: 70, expectedFinancing: 350000 }
];
```

## Troubleshooting

### Common Issues

1. **"Angular dev server is not running"**
   - Start the dev server: `npm start`
   - Ensure it's running on port 4200

2. **"Cannot find module 'protractor'"**
   - Install dependencies: `npm install`
   - Check that protractor is in devDependencies

3. **Tests fail with timeout errors**
   - Increase timeout in `protractor.conf.js`
   - Check that the application loads correctly in browser

4. **Slider tests fail**
   - Ensure the slider component is properly rendered
   - Check that form controls are correctly bound

### Debug Mode

To run tests in debug mode:
```bash
ng e2e --debug
```

This will:
- Open Chrome in non-headless mode
- Pause execution at breakpoints
- Allow step-by-step debugging

## Test Coverage

The E2E tests provide comprehensive coverage of:

- ✅ **UI Components**: All form fields, buttons, and displays
- ✅ **User Interactions**: Slider movement, form input, button clicks
- ✅ **Business Logic**: Financial calculations and validations
- ✅ **Data Flow**: Real-time updates and form submission
- ✅ **Edge Cases**: Boundary values and error conditions

## Continuous Integration

For CI/CD pipelines, use:
```bash
npm run e2e:ci
```

This runs tests in headless mode suitable for automated environments.

## Maintenance

### Adding New Tests
1. Create new test files following the naming convention: `*.e2e-spec.ts`
2. Update the Page Object Model if new elements are added
3. Add test cases to existing spec files for new functionality
4. Update this README with new test scenarios

### Updating Selectors
If UI elements change, update the selectors in `dashboard-simulator.po.ts`:
- Use stable selectors (formControlName, data attributes)
- Avoid brittle selectors (CSS classes that might change)
- Test selectors in browser dev tools before updating tests
