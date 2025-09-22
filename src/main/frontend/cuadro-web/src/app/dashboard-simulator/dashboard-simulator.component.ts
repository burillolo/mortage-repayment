import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { FormsModule, ReactiveFormsModule, Validators, FormControl, FormBuilder, FormGroup } from '@angular/forms';
import { MatSliderModule } from '@angular/material/slider';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Settings } from '../settings';

@Component({
  selector: 'app-dashboard-simulator',
  templateUrl: './dashboard-simulator.component.html',
  styleUrls: ['./dashboard-simulator.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatSliderModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatRadioModule
  ]
})
export class DashboardSimulatorComponent implements OnInit {

  // Slider properties using Material API
  sliderPercentageValue: number = 80;
  financingAmountValue: number = 0;
  sliderControl: FormControl = new FormControl(this.sliderPercentageValue);

  // Duration slider properties
  durationValue: number = 25; // Default to 25 years
  durationControl: FormControl = new FormControl(this.durationValue);

  @ViewChild('slider', { static: false }) slider!: ElementRef;

  addressForm: FormGroup = this.fb.group({
    amount: [100000, [Validators.required, Validators.min(1)]],
    duration: [this.durationControl, [Validators.required, Validators.min(10), Validators.max(40)]],
    interest: [null, [Validators.required, Validators.min(0.01)]],
    financingPercentage: [this.sliderControl, [Validators.required, Validators.min(1), Validators.max(100)]],
  });

  constructor(private fb: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    // Initialize form with default values - set the form control value directly
    this.addressForm.get('financingPercentage')?.setValue(this.sliderPercentageValue);
    this.addressForm.get('duration')?.setValue(this.durationValue);

    // Subscribe to form changes to update financing amount
    this.addressForm.get('amount')?.valueChanges.subscribe(amount => {
      if (amount && amount > 0) {
        this.financingAmountValue = (amount * this.sliderControl.value) / 100;
      }
    });

    // Subscribe to financing percentage changes
    this.sliderControl.valueChanges.subscribe(percentage => {
      const amount = this.addressForm.get('amount')?.value;
      if (amount && amount > 0) {
        this.financingAmountValue = (amount * percentage) / 100;
      }
    });

    // Subscribe to duration changes
    this.durationControl.valueChanges.subscribe(duration => {
      this.durationValue = duration;
      this.addressForm.get('duration')?.setValue(duration);
    });

    // Initial calculation
    this.financingAmountValue = (this.addressForm.get('amount')?.value * this.sliderPercentageValue) / 100;
  }

  // Method to prevent negative values on input
  onAmountInput(event: any): void {
    const value = event.target.value;
    if (value < 0) {
      event.target.value = 0;
      this.addressForm.get('amount')?.setValue(0);
    }
  }


  onInterestInput(event: any): void {
    const value = event.target.value;
    if (value < 0) {
      event.target.value = 0;
      this.addressForm.get('interest')?.setValue(0);
    }
  }

  getFinancingPercentage(): number {
    return this.sliderControl.value;
  }

  getFinancingAmount(): number {
    return this.financingAmountValue;
  }

  onSubmit() {
    if (this.addressForm.invalid) {
      return;
    }

    const duration = this.addressForm.get('duration')?.value ?? 0;
    const amount = this.addressForm.get('amount')?.value ?? 0;
    const financingPercentage = this.addressForm.get('financingPercentage')?.value ?? 0;
    const interest = this.addressForm.get('interest')?.value ?? 0;

    if (duration > 0 && amount > 0 && financingPercentage > 0 && interest > 0) {
      const settings: Settings = {
        duration: duration,
        nominal: amount * financingPercentage / 100,
        rate: interest
      };

      // Navigate to table with query parameters
      this.router.navigate(['/cuotas'], {
        queryParams: {
          nominal: settings.nominal,
          rate: settings.rate,
          duration: settings.duration
        }
      });
    }
  }
}
