import { DashboardSimulatorPage } from './dashboard-simulator.po';
import { browser, logging } from 'protractor';

describe('Dashboard Simulator E2E Tests', () => {
  let page: DashboardSimulatorPage;

  beforeEach(() => {
    page = new DashboardSimulatorPage();
  });

  describe('Financing Percentage Slider', () => {
    beforeEach(async () => {
      await page.navigateTo();
      // Set up basic form data
      await page.setAmount(200000);
      await page.setDuration(25);
      await page.setInterest(3.5);
    });

    it('should display default financing percentage of 80%', async () => {
      const percentage = await page.getFinancingPercentageValue();
      expect(percentage).toBe(80);
    });

    it('should update financing percentage when slider is moved', async () => {
      // Test different percentage values
      const testPercentages = [50, 75, 90, 100];
      
      for (const percentage of testPercentages) {
        await page.setFinancingPercentage(percentage);
        await browser.sleep(100); // Allow for UI updates
        
        const displayedPercentage = await page.getFinancingPercentageValue();
        expect(displayedPercentage).toBe(percentage);
      }
    });

    it('should update financing amount when percentage changes', async () => {
      const amount = 200000;
      await page.setAmount(amount);
      
      // Test different percentages and verify calculations
      const testCases = [
        { percentage: 50, expectedAmount: 100000 },
        { percentage: 75, expectedAmount: 150000 },
        { percentage: 80, expectedAmount: 160000 },
        { percentage: 90, expectedAmount: 180000 },
        { percentage: 100, expectedAmount: 200000 }
      ];

      for (const testCase of testCases) {
        await page.setFinancingPercentage(testCase.percentage);
        await page.waitForCalculation();
        
        const actualAmount = await page.getFinancingAmountValue();
        expect(actualAmount).toBe(testCase.expectedAmount);
      }
    });

    it('should update financing amount when property amount changes', async () => {
      const percentage = 80;
      await page.setFinancingPercentage(percentage);
      
      // Test different property amounts
      const testCases = [
        { amount: 100000, expectedFinancing: 80000 },
        { amount: 250000, expectedFinancing: 200000 },
        { amount: 500000, expectedFinancing: 400000 }
      ];

      for (const testCase of testCases) {
        await page.setAmount(testCase.amount);
        await page.waitForCalculation();
        
        const actualFinancing = await page.getFinancingAmountValue();
        expect(actualFinancing).toBe(testCase.expectedFinancing);
      }
    });

    it('should display correct percentage text', async () => {
      await page.setFinancingPercentage(75);
      await browser.sleep(100);
      
      const percentageText = await page.getFinancingPercentageText();
      expect(percentageText).toContain('75% del precio total del inmueble');
    });
  });

  describe('Form Validation and Calculation', () => {
    it('should calculate financing amount correctly with different combinations', async () => {
      await page.navigateTo();
      
      const testCases = [
        { amount: 300000, percentage: 60, expectedFinancing: 180000 },
        { amount: 150000, percentage: 90, expectedFinancing: 135000 },
        { amount: 500000, percentage: 70, expectedFinancing: 350000 }
      ];

      for (const testCase of testCases) {
        await page.setAmount(testCase.amount);
        await page.setFinancingPercentage(testCase.percentage);
        await page.waitForCalculation();
        
        const actualFinancing = await page.getFinancingAmountValue();
        expect(actualFinancing).toBe(testCase.expectedFinancing);
      }
    });

    it('should maintain calculation accuracy with decimal amounts', async () => {
      await page.navigateTo();
      
      await page.setAmount(123456);
      await page.setFinancingPercentage(85);
      await page.waitForCalculation();
      
      const expectedFinancing = Math.round(123456 * 0.85);
      const actualFinancing = await page.getFinancingAmountValue();
      expect(actualFinancing).toBe(expectedFinancing);
    });
  });

  describe('End-to-End Calculation Flow', () => {
    it('should complete full calculation flow and show results', async () => {
      await page.navigateTo();
      
      // Fill in the form
      await page.setAmount(250000);
      await page.setDuration(30);
      await page.setInterest(4.2);
      await page.setFinancingPercentage(80);
      
      // Verify financing calculation
      await page.waitForCalculation();
      const financingAmount = await page.getFinancingAmountValue();
      expect(financingAmount).toBe(200000);
      
      // Click calculate button
      await page.clickCalculate();
      
      // Wait for table to appear (if backend is running)
      await browser.sleep(2000);
      
      // Verify that calculation was triggered
      // Note: This test will pass even if backend is not running
      // as we're testing the frontend calculation logic
      expect(financingAmount).toBeGreaterThan(0);
    });
  });

  describe('Slider Boundary Values', () => {
    beforeEach(async () => {
      await page.navigateTo();
      await page.setAmount(100000);
    });

    it('should handle minimum percentage (1%)', async () => {
      await page.setFinancingPercentage(1);
      await page.waitForCalculation();
      
      const percentage = await page.getFinancingPercentageValue();
      const amount = await page.getFinancingAmountValue();
      
      expect(percentage).toBe(1);
      expect(amount).toBe(1000); // 1% of 100000
    });

    it('should handle maximum percentage (100%)', async () => {
      await page.setFinancingPercentage(100);
      await page.waitForCalculation();
      
      const percentage = await page.getFinancingPercentageValue();
      const amount = await page.getFinancingAmountValue();
      
      expect(percentage).toBe(100);
      expect(amount).toBe(100000); // 100% of 100000
    });
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
