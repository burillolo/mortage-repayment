import { Component } from '@angular/core';
import { CuotasServiceService } from './cuotas-service.service';
import { Settings } from './settings';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'cuadro-web';
  constructor(private serverService: CuotasServiceService) {}

  callServer(settingRequest: Settings) {
    this.serverService.isLoading.next(true);
    this.serverService.obtenerPeriodosCuotas(settingRequest.nominal, settingRequest.rate, settingRequest.duration);
  }

}
