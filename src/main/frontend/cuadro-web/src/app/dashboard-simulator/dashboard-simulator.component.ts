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
    interest: [null, Validators.required]
  });

  @Output() serverRequest: EventEmitter<Settings> = new EventEmitter<Settings>();

  constructor(private fb: FormBuilder) {}

  onSubmit() {
    if (this.addressForm.errors) { return; }
    this.serverRequest.emit({duration: this.addressForm.get('duration').value,
    nominal: this.addressForm.get('amount').value,
    rate: this.addressForm.get('interest').value});
  }
}
