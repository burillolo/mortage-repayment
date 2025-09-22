import { Component, Input, OnInit } from '@angular/core';
import { LoanSummary } from '../loan-summary';

@Component({
  selector: 'app-loan-summary-panel',
  templateUrl: './loan-summary-panel.component.html',
  styleUrls: ['./loan-summary-panel.component.css']
})
export class LoanSummaryPanelComponent implements OnInit {
  @Input() loanSummary: LoanSummary | null = null;

  constructor() { }

  ngOnInit(): void {
  }
}