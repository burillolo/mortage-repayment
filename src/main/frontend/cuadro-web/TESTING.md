# Testing Guide for Mortgage Calculator

This document provides comprehensive testing approaches for the Mortgage Calculator application, including both automated and manual testing methods.

## 🚨 Protractor E2E Testing Issue

**Note:** The traditional Protractor e2e tests have compatibility issues with modern browsers and Angular 18. Protractor has been deprecated by the Angular team. This guide provides alternative testing approaches.

## 🧪 Testing Approaches

### 1. Manual Testing (Recommended)

#### Quick Manual Test
1. Start the application:
   ```bash
   npm start
   ```
2. Open http://localhost:4200
3. Use the test verification page:
   ```bash
   npm run test:verify
   ```

#### Test Verification Page
Open `test-verification.html` in your browser for a comprehensive manual testing checklist with:
- ✅ Step-by-step test scenarios
- 🧮 Calculation verification tool
- 📊 Interactive test checklist
- 📋 Test report generation

### 2. Automated Testing with Puppeteer

#### Prerequisites
```bash
npm install puppeteer --save-dev
```

#### Run Automated Tests
```bash
npm run test:manual
```

This will:
- Launch a browser automatically
- Navigate to the application
- Test all form elements
- Verify slider functionality
- Test financial calculations
- Generate a test report

### 3. Unit Testing (Existing)

```bash
npm test
```

## 📋 Test Scenarios

### Core Functionality Tests

#### 1. Form Elements Presence
- [ ] Amount input field is present
- [ ] Duration input field is present  
- [ ] Interest rate input field is present
- [ ] Financing percentage slider is present
- [ ] Calculate button is present

#### 2. Slider Functionality
- [ ] Default slider value is 80%
- [ ] Slider updates percentage display in real-time
- [ ] Slider works at minimum value (1%)
- [ ] Slider works at maximum value (100%)
- [ ] Slider thumb moves smoothly

#### 3. Financial Calculations
- [ ] Initial calculation is correct (80% of property amount)
- [ ] Financing amount updates when property price changes
- [ ] Financing amount updates when percentage changes
- [ ] Calculations are mathematically accurate
- [ ] Real-time updates work immediately

#### 4. Form Integration
- [ ] All form fields accept input
- [ ] Form validation works correctly
- [ ] Calculate button triggers submission
- [ ] Form resets after submission

### Specific Test Cases

#### Test Case 1: Basic Calculation
```
Property Amount: €200,000
Financing Percentage: 80%
Expected Financing Amount: €160,000
```

#### Test Case 2: Slider Movement
```
Property Amount: €300,000
Financing Percentage: 75% → 90%
Expected Financing Amount: €225,000 → €270,000
```

#### Test Case 3: Boundary Testing
```
Property Amount: €100,000
Financing Percentage: 1% → 100%
Expected Financing Amount: €1,000 → €100,000
```

#### Test Case 4: Real-time Updates
```
Property Amount: €250,000 → €500,000
Financing Percentage: 80% → 60%
Expected Financing Amount: €200,000 → €300,000
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Application Not Loading
- Ensure Angular dev server is running: `npm start`
- Check that port 4200 is available
- Verify no console errors in browser

#### 2. Slider Not Working
- Check that Material Design components are loaded
- Verify form controls are properly bound
- Ensure no JavaScript errors in console

#### 3. Calculations Not Updating
- Check that reactive properties are working
- Verify form value changes are detected
- Ensure no TypeScript compilation errors

#### 4. Puppeteer Tests Failing
- Install Puppeteer: `npm install puppeteer --save-dev`
- Ensure application is running on localhost:4200
- Check browser compatibility

### Debug Mode

#### Enable Debug Logging
Add to `dashboard-simulator.component.ts`:
```typescript
ngOnInit(): void {
  console.log('Dashboard component initialized');
  // ... existing code ...
  
  combineLatest([
    this.addressForm.get('amount')!.valueChanges,
    this.addressForm.get('financingPercentage')!.valueChanges
  ]).subscribe(([amount, percentage]) => {
    console.log('Form values changed:', { amount, percentage });
    this.financingPercentage = percentage || 80;
    this.financingAmount = ((amount || 0) * this.financingPercentage) / 100;
    console.log('Calculated financing amount:', this.financingAmount);
  });
}
```

#### Browser DevTools
1. Open Chrome DevTools (F12)
2. Go to Console tab
3. Look for error messages
4. Check Network tab for failed requests

## 📊 Test Coverage

### Current Coverage
- ✅ Form element presence
- ✅ Slider functionality
- ✅ Financial calculations
- ✅ Real-time updates
- ✅ Form validation
- ✅ User interactions

### Areas for Improvement
- 🔄 Backend integration testing
- 🔄 Error handling scenarios
- 🔄 Performance testing
- 🔄 Cross-browser compatibility

## 🚀 Continuous Integration

### GitHub Actions Example
```yaml
name: Test Mortgage Calculator
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-node@v2
        with:
          node-version: '18'
      - run: npm install
      - run: npm run build
      - run: npm start &
      - run: npm run test:manual
```

## 📝 Test Documentation

### Test Results Template
```
Test Date: [DATE]
Tester: [NAME]
Browser: [BROWSER VERSION]
Angular Version: 18.0.0

Test Results:
- Form Elements: ✅ PASS
- Slider Functionality: ✅ PASS
- Financial Calculations: ✅ PASS
- Real-time Updates: ✅ PASS
- Form Integration: ✅ PASS

Issues Found: [NONE/SPECIFY]
Recommendations: [NONE/SPECIFY]
```

## 🔗 Related Files

- `test-verification.html` - Manual testing interface
- `manual-test.js` - Automated testing script
- `e2e/` - Protractor e2e tests (deprecated)
- `src/app/dashboard-simulator/` - Main component under test

## 📞 Support

If you encounter issues with testing:
1. Check the troubleshooting section above
2. Review browser console for errors
3. Verify all dependencies are installed
4. Ensure the application builds successfully

For additional help, refer to the main project README or create an issue in the project repository.
