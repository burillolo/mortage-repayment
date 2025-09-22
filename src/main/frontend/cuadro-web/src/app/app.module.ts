import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TablaCuotasComponent } from './tabla-cuotas/tabla-cuotas.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { HttpClientModule } from '@angular/common/http';
import { CuotasServiceService } from './cuotas-service.service';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSliderModule } from '@angular/material/slider';
import { ReactiveFormsModule } from '@angular/forms';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoanSummaryPanelComponent } from './loan-summary-panel/loan-summary-panel.component';
import { LoanSummaryDialogComponent } from './loan-summary-dialog/loan-summary-dialog.component';
import { DashboardSimulatorComponent } from './dashboard-simulator/dashboard-simulator.component';

@NgModule({
  declarations: [
    AppComponent,
    TablaCuotasComponent,
    LoanSummaryPanelComponent,
    LoanSummaryDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    HttpClientModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    MatIconModule,
    MatSliderModule,
    ReactiveFormsModule,
    DashboardSimulatorComponent
  ],
  providers: [CuotasServiceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
