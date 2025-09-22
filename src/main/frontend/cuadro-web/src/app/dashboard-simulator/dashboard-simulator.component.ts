import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Settings } from '../settings';

@Component({
  selector: 'app-dashboard-simulator',
  templateUrl: './dashboard-simulator.component.html',
  styleUrls: ['./dashboard-simulator.component.css']
})
export class DashboardSimulatorComponent {
  addressForm = this.fb.group({
    amount: [null, Validators.required],
    duration: [null, Validators.required],
    interest: [null, Validators.required],
    financingPercentage: [80, [Validators.required, Validators.min(1), Validators.max(100)]]
  });

  @Output() serverRequest: EventEmitter<Settings> = new EventEmitter<Settings>();

  constructor(private fb: FormBuilder) {}

  getFinancingPercentage(): number {
    return this.addressForm.get('financingPercentage')?.value || 80;
  }

  getFinancingAmount(): number {
    const amount = this.addressForm.get('amount')?.value || 0;
    const percentage = this.getFinancingPercentage();
    return (amount * percentage) / 100;
  }

  onSubmit() {
    if (this.addressForm.invalid) { 
      return; 
    }
    
    const duration = this.addressForm.get('duration')?.value ?? 0;
    const amount = this.addressForm.get('amount')?.value ?? 0;
    const interest = this.addressForm.get('interest')?.value ?? 0;
    const financingAmount = this.getFinancingAmount() ?? 0;
    
    if (duration > 0 && financingAmount > 0 && interest > 0) {
      this.serverRequest.emit({
        duration: duration,
        nominal: financingAmount, // Use calculated financing amount instead of total amount
        rate: interest
      });
    }
  }
}
