import { Component } from '@angular/core';
import { Settings } from './settings';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'cuadro-web';
  showTable = false; // Control table visibility
  currentLoanData: Settings | null = null; // Store loan data
  
  constructor() {}

  callServer(settingRequest: Settings) {
    this.currentLoanData = settingRequest; // Store the loan data
    this.showTable = true; // Show table after calculation
  }

  onBackToSimulator(): void {
    this.showTable = false; // Hide table and show dashboard
  }
}
