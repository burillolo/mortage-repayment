import { browser, by, element, ElementFinder, ElementArrayFinder } from 'protractor';

export class DashboardSimulatorPage {
  navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl) as Promise<unknown>;
  }

  // Form elements
  getAmountInput(): ElementFinder {
    return element(by.css('input[formControlName="amount"]'));
  }

  getDurationInput(): ElementFinder {
    return element(by.css('input[formControlName="duration"]'));
  }

  getInterestInput(): ElementFinder {
    return element(by.css('input[formControlName="interest"]'));
  }

  getFinancingSlider(): ElementFinder {
    return element(by.css('mat-slider[formControlName="financingPercentage"]'));
  }

  getSliderThumb(): ElementFinder {
    return element(by.css('mat-slider input[matSliderThumb]'));
  }

  getCalculateButton(): ElementFinder {
    return element(by.css('button[type="submit"]'));
  }

  // Display elements
  getFinancingPercentageDisplay(): ElementFinder {
    return element(by.css('.slider-current'));
  }

  getFinancingAmountDisplay(): ElementFinder {
    return element(by.css('.financing-amount'));
  }

  getFinancingPercentageTextDisplay(): ElementFinder {
    return element(by.css('.financing-percentage'));
  }

  // Table elements (for verification after calculation)
  getTable(): ElementFinder {
    return element(by.css('app-tabla-cuotas table'));
  }

  getTableRows(): ElementArrayFinder {
    return element.all(by.css('app-tabla-cuotas table tbody tr'));
  }

  // Methods for interaction
  async setAmount(amount: number): Promise<void> {
    const input = this.getAmountInput();
    await input.clear();
    await input.sendKeys(amount.toString());
  }

  async setDuration(duration: number): Promise<void> {
    const input = this.getDurationInput();
    await input.clear();
    await input.sendKeys(duration.toString());
  }

  async setInterest(interest: number): Promise<void> {
    const input = this.getInterestInput();
    await input.clear();
    await input.sendKeys(interest.toString());
  }

  async setFinancingPercentage(percentage: number): Promise<void> {
    const slider = this.getSliderThumb();
    await browser.executeScript(`
      const slider = arguments[0];
      const percentage = arguments[1];
      const min = 1;
      const max = 100;
      const value = (percentage - min) / (max - min);
      slider.value = value;
      slider.dispatchEvent(new Event('input', { bubbles: true }));
      slider.dispatchEvent(new Event('change', { bubbles: true }));
    `, slider, percentage);
  }

  async clickCalculate(): Promise<void> {
    await this.getCalculateButton().click();
  }

  // Methods for verification
  async getFinancingPercentageValue(): Promise<number> {
    const text = await this.getFinancingPercentageDisplay().getText();
    return parseInt(text.replace('%', ''));
  }

  async getFinancingAmountValue(): Promise<number> {
    const text = await this.getFinancingAmountDisplay().getText();
    // Remove currency symbols and parse
    const cleanText = text.replace(/[€,\s]/g, '');
    return parseFloat(cleanText);
  }

  async getFinancingPercentageText(): Promise<string> {
    return this.getFinancingPercentageTextDisplay().getText();
  }

  async isTableVisible(): Promise<boolean> {
    try {
      return await this.getTable().isDisplayed();
    } catch (error) {
      return false;
    }
  }

  async getTableRowCount(): Promise<number> {
    return this.getTableRows().count();
  }

  // Wait for calculations to complete
  async waitForCalculation(): Promise<void> {
    await browser.wait(async () => {
      const amount = await this.getFinancingAmountValue();
      return amount > 0;
    }, 5000, 'Financing amount calculation did not complete');
  }
}
