import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Period } from './period';
import { PageResponse } from './page-response';
import { LoanSummary } from './loan-summary';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CuotasServiceService {

  private periodsSubject = new BehaviorSubject<Period[]>([]);
  private pageInfoSubject = new BehaviorSubject<{totalElements: number, totalPages: number, pageNumber: number, pageSize: number}>({
    totalElements: 0, 
    totalPages: 0, 
    pageNumber: 0, 
    pageSize: 20
  });
  periodos$: Observable<Period[]> = this.periodsSubject.asObservable();
  pageInfo$: Observable<{totalElements: number, totalPages: number, pageNumber: number, pageSize: number}> = this.pageInfoSubject.asObservable();
  isLoading = new BehaviorSubject<boolean>(false);

  constructor(private httpClient: HttpClient) {}

  obtenerPeriodosCuotas(nominal: number, interest: number, months: number, page: number = 0, size: number = 20): Observable<PageResponse<Period>> {
    this.isLoading.next(true);
    return this.httpClient.get<PageResponse<Period>>('/api', {
        params: new HttpParams()
            .set('principal', nominal.toString())
            .set('interest', interest.toString())
            .set('duration', months.toString())
            .set('page', page.toString())
            .set('size', size.toString())
    }).pipe(
      map(pageResponse => {
        // Add calculated cuota field to each period
        const pageContent = pageResponse.content.map((period: Period, index: number) => ({
          ...period,
          cuota: period.principal + period.interest,
          mes: (page * size) + index + 1
        }));
        
        // Update subjects with new data
        this.periodsSubject.next(pageContent);
        this.pageInfoSubject.next({
          totalElements: pageResponse.totalElements,
          totalPages: pageResponse.totalPages,
          pageNumber: pageResponse.number,
          pageSize: pageResponse.size
        });
        this.isLoading.next(false);
        
        return {
          ...pageResponse,
          content: pageContent
        };
      })
    );
  }

  getPeriodsDataSource(): Observable<Period[]> {
    return this.periodos$;
  }

  getPageInfo(): Observable<{totalElements: number, totalPages: number, pageNumber: number, pageSize: number}> {
    return this.pageInfo$;
  }

  getIsLoadingResult(): Observable<boolean> {
    return this.isLoading;
  }

  getLoanSummary(nominal: number, interest: number, months: number): Observable<LoanSummary> {
    return this.httpClient.get<LoanSummary>('/api/summary', {
      params: new HttpParams()
        .set('principal', nominal.toString())
        .set('interest', interest.toString())
        .set('duration', Math.floor(months / 12).toString())
    });
  }

}
