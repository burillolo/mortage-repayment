import { AppPage } from './app.po';
import { browser, by, logging } from 'protractor';

describe('Mortgage Repayment Calculator App', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('should display the mortgage calculator application', async () => {
    await page.navigateTo();
    
    // Check if the main dashboard component is present
    const dashboard = browser.element(by.css('app-dashboard-simulator'));
    expect(await dashboard.isPresent()).toBe(true);
  });

  it('should have all required form fields', async () => {
    await page.navigateTo();
    
    // Check for amount input
    const amountInput = browser.element(by.css('input[formControlName="amount"]'));
    expect(await amountInput.isPresent()).toBe(true);
    
    // Check for duration input
    const durationInput = browser.element(by.css('input[formControlName="duration"]'));
    expect(await durationInput.isPresent()).toBe(true);
    
    // Check for interest input
    const interestInput = browser.element(by.css('input[formControlName="interest"]'));
    expect(await interestInput.isPresent()).toBe(true);
    
    // Check for financing slider
    const financingSlider = browser.element(by.css('mat-slider[formControlName="financingPercentage"]'));
    expect(await financingSlider.isPresent()).toBe(true);
    
    // Check for calculate button
    const calculateButton = browser.element(by.css('button[type="submit"]'));
    expect(await calculateButton.isPresent()).toBe(true);
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
