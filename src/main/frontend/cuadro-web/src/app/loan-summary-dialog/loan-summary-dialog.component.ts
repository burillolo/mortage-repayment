import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { LoanSummary } from '../loan-summary';

@Component({
  selector: 'app-loan-summary-dialog',
  templateUrl: './loan-summary-dialog.component.html',
  styleUrls: ['./loan-summary-dialog.component.css']
})
export class LoanSummaryDialogComponent implements OnInit {
  @Input() loanSummary: LoanSummary | null = null;
  @Input() showDialog: boolean = false;
  @Output() closeDialog = new EventEmitter<void>();

  constructor() { }

  ngOnInit(): void {
  }

  onCloseDialog(): void {
    this.closeDialog.emit();
  }
}
