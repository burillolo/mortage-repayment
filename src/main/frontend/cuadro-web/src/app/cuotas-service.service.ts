import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable, Subscriber, Observer, BehaviorSubject } from 'rxjs';
import { Period } from './period';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CuotasServiceService {

  periodSubscriber: Subscriber<Period[]>;
  periodos$: Observable<Period[]>;
  isLoading = new BehaviorSubject(false);

  constructor(private httpClient: HttpClient) {
    this.periodSubscriber = new Subscriber();
    this.periodos$ = new Observable(e => {
      this.periodSubscriber = e;
    });
  }

  obtenerPeriodosCuotas(nominal: number, interest: number, months: number): void {
    (this.httpClient.get<Period[]>('/api', {
        params: new HttpParams()
            .set('principal', nominal.toString())
            .set('interest', interest.toString())
            .set('duration', months.toString())
    })).pipe(
      map(periodo => {periodo.forEach(a => a.cuota = a.principal + a.interest); return periodo; })
    ).subscribe((a) => {
      this.periodSubscriber.next(a);
      this.isLoading.next(false);
    });
  }

  getPeriodsDataSource(): Observable<Period[]> {
    return this.periodos$;
  }

  getIsLoadingResult(): Observable<boolean> {
    return this.isLoading;
  }

}
